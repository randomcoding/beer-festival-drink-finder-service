/**
 * Copyright (C) 2012 RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 * RandomCoder - initial API and implementation and/or initial documentation
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import java.io.InputStream
import net.liftweb.common.Logger
import org.apache.poi.ss.usermodel.{WorkbookFactory, Row}
import scala.collection.JavaConversions._
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.lib.dataloader.util.RichRow._
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory.{perry, cider, beer}
import uk.co.randomcoding.drinkfinder.model.drink.{DrinkRemainingStatus, DrinkFeature}
import uk.co.randomcoding.drinkfinder.model.record.BrewerRecord

/**
 * @author RandomCoder
 *
 */
class SpreadsheetDataLoader extends Logger {
  private val featureLoader = new DrinkFeatureLoader()

  /**
   * Read the data from an Excel spreadsheet input stream according to the provided data template and store it in the [[uk.co.randomcoding.drinkfinder.model.data.FestivalData]] object
   */
  def loadData(excelDataFile: InputStream, dataTemplate: DrinkDataTemplate) {

    val wb = WorkbookFactory.create(excelDataFile)
    wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK)
    val dataSheet = wb.getSheetAt(0)

    val festivalId = dataTemplate.festivalId
    val festivalData = FestivalData(festivalId, dataTemplate.festivalName)

    val physicalRows = dataSheet.rowIterator.toList

    val dataRows = physicalRows.filter(_.isDataRow(dataTemplate))

    dataRows.foreach(addRowToData(_, festivalData, dataTemplate, festivalId))
  }

  private def addRowToData(row: Row, festivalData: FestivalData, dataTemplate: DrinkDataTemplate, festivalId: String) {
    val brewer = getBrewer(row, dataTemplate)

    if (brewer.isDefined) {
      val drink = getDrinkType(row, dataTemplate) match {
        case Some(t) => {
          val drinkName = getDrinkName(row, dataTemplate)
          val description = getDrinkDescription(row, dataTemplate)
          val abv = getDrinkAbv(row, dataTemplate)
          val price = getDrinkPrice(row, dataTemplate)
          val features = getDrinkFeatures(row, dataTemplate)
          t.toLowerCase match {
            case "beer" => Some(beer(drinkName, description, abv, price, brewer.get, festivalId, features))
            case "cider" => Some(cider(drinkName, description, abv, price, brewer.get, festivalId, features))
            case "perry" => Some(perry(drinkName, description, abv, price, brewer.get, festivalId, features))
            case _ => None
          }
        }
        case None => {
          error("No drink type for data found")
          None
        }
      }

      drink match {
        case Some(d) => {
          val remaining = getQuantityRemaining(row, dataTemplate)
          d.quantityRemaining(remaining)

          debug("Adding drink %s to Festival Data".format(drink))
          festivalData.addDrink(d)
        }
        case _ => warn(s"No DrinkRecord generated for Row ${row.getRowNum}")
      }
    }
  }

  private def getBrewer(row: Row, dataTemplate: DrinkDataTemplate): Option[BrewerRecord] = {
    dataTemplate.brewerNameColumn match {
      case Some(col) => row.getStringCellValue(col) match {
        case Some(x) => Some(BrewerRecord(x))
        case _ => None
      }
      case _ => None
    }
  }

  private def getDrinkName(row: Row, dataTemplate: DrinkDataTemplate): String = {
    row.getStringCellValue(dataTemplate.drinkNameColumn).getOrElse("Unknown Drink")
  }

  private def getDrinkDescription(row: Row, dataTemplate: DrinkDataTemplate): String = {
    dataTemplate.drinkDescriptionColumn match {
      case Some(col) => row.getStringCellValue(col).getOrElse("").trim
      case None => ""
    }
  }

  private def getDrinkPrice(row: Row, dataTemplate: DrinkDataTemplate): Double = {
    dataTemplate.drinkPriceColumn match {
      case Some(col) => convertDrinkPriceValue(row.getNumericCellValue(col).getOrElse(0.0))
      case None => 0.0
    }
  }

  private def getDrinkAbv(row: Row, dataTemplate: DrinkDataTemplate): Double = {
    convertDrinkAbvValue(row.getNumericCellValue(dataTemplate.drinkAbvColumn).getOrElse(0.0))
  }

  private def getDrinkType(row: Row, dataTemplate: DrinkDataTemplate): Option[String] = {
    dataTemplate.drinkType match {
      case Some(t) => Some(t)
      case None => row.getStringCellValue(dataTemplate.drinkTypeColumn)
    }
  }

  private def getDrinkFeatures(row: Row, dataTemplate: DrinkDataTemplate): List[DrinkFeature] = {
    featureLoader.drinkFeatures(row, dataTemplate)
  }

  private def getQuantityRemaining(row: Row, dataTemplate: DrinkDataTemplate): DrinkRemainingStatus.status = {
    dataTemplate.quantityRemainingColumn match {
      case Some(col) => {
        val quantity = row(col).getNumericCellValue
        debug("Quantity from cell: %.2f".format(quantity))
        quantity match {
          case num if num > 1.0 => DrinkRemainingStatus.NOT_AVAILABLE
          case num if num >= 0.5 => DrinkRemainingStatus.PLENTY
          case num if num >= 0.25 => DrinkRemainingStatus.LESS_THAN_HALF
          case num if num >= 0.1 => DrinkRemainingStatus.RUNNING_OUT
          case num if num <= 0.01 => DrinkRemainingStatus.FINISHED
          case _ => DrinkRemainingStatus.BEING_PREPARED
        }
      }
      case None => DrinkRemainingStatus.BEING_PREPARED
    }
  }

  // If this is needed to be the base class of different loaders then  override these methods.
  /**
   * Convert the raw value of the drink ABV to a double value that is displayed as (e.g.) 4.1%.
   *
   * Often spreadsheets return percentages as doubles in the range 0 -> 1. So 4.1% is returned as 0.041.
   * Therefore this needs to be multiplied by 100 to display correctly.
   */
  protected def convertDrinkAbvValue(drinkAbvValue: Double): Double = drinkAbvValue < 0.5 match {
    case true => drinkAbvValue * 100.0D
    case false => drinkAbvValue
  }

  /**
   * Convert the raw value of the the drink price value to the value per pint that is required by the system.
   *
   * Currently this is geared for the WBF format which is initially an int in pence, i.e. £2.60 per pint is stored as 260.
   */
  protected def convertDrinkPriceValue(drinkPriceValue: Double): Double = drinkPriceValue / 100.0D

}
