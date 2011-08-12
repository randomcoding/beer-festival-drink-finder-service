/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import java.io.InputStream
import net.liftweb.common.Logger
import org.apache.poi.ss.usermodel.{WorkbookFactory, Row}
import scala.collection.JavaConverters.asScalaIteratorConverter
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.lib.dataloader.util.RichRow
import uk.co.randomcoding.drinkfinder.lib.dataloader.util.RichRow._
import uk.co.randomcoding.drinkfinder.model.brewer.{NoBrewer, Brewer}
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.drink.{NoDrink, DrinkFeature}
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory.{perry, cider, beer}

/**
 * @author RandomCoder
 *
 */
class SpreadsheetDataLoader extends Logger {
	private val featureLoader = new DrinkFeatureLoader()
	/**
	 * Read the data from an Excel spreadsheet input stream according to the provided data template and store it in the [[uk.co.randomcoding.drinkfinder.model.data.FestivalData]] object
	 */
	def loadData(excelDataFile : InputStream, dataTemplate : DrinkDataTemplate) = {

		val wb = WorkbookFactory.create(excelDataFile)
		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK)
		val dataSheet = wb.getSheetAt(0);

		val festivalData = FestivalData(dataTemplate.festivalName)

		val physicalRows = dataSheet.rowIterator.asScala
		physicalRows.foreach(row => if (row.isDataRow(dataTemplate)) addRowToData(row, festivalData, dataTemplate))
	}

	private def addRowToData(row : Row, festivalData : FestivalData, dataTemplate : DrinkDataTemplate) {
		val drink = getDrinkType(row, dataTemplate) match {
			case Some(t) => t.toLowerCase match {
				case "beer" => beer(getDrinkName(row, dataTemplate), getDrinkDescription(row, dataTemplate), getDrinkAbv(row, dataTemplate), getDrinkPrice(row, dataTemplate), getDrinkFeatures(row, dataTemplate))
				case "cider" => cider(getDrinkName(row, dataTemplate), getDrinkDescription(row, dataTemplate), getDrinkAbv(row, dataTemplate), getDrinkPrice(row, dataTemplate), getDrinkFeatures(row, dataTemplate))
				case "perry" => perry(getDrinkName(row, dataTemplate), getDrinkDescription(row, dataTemplate), getDrinkAbv(row, dataTemplate), getDrinkPrice(row, dataTemplate), getDrinkFeatures(row, dataTemplate))
			}
			case None => error("No drink type for data found"); NoDrink
		}

		val brewer = getBrewer(row, dataTemplate)
		festivalData.addBrewer(brewer)
		drink.brewer = brewer
		drink.quantityRemaining = getQuantityRemaining(row, dataTemplate)

		info("Adding drink %s to Festival Data".format(drink))
		festivalData.addDrink(drink)
	}

	private def getBrewer(row : Row, dataTemplate : DrinkDataTemplate) : Brewer = {
		row.getStringCellValue(dataTemplate.brewerNameColumn) match {
			case Some(x) => Brewer(x)
			case _ => NoBrewer
		}
	}

	private def getDrinkName(row : Row, dataTemplate : DrinkDataTemplate) : String = {
		row.getStringCellValue(dataTemplate.drinkNameColumn).getOrElse("Unknown Drink")
	}

	private def getDrinkDescription(row : Row, dataTemplate : DrinkDataTemplate) : String = {
		dataTemplate.drinkDescriptionColumn match {
			case Some(col) => row.getStringCellValue(col).getOrElse("")
			case None => ""
		}
	}

	private def getDrinkPrice(row : Row, dataTemplate : DrinkDataTemplate) : Double = {
		convertDrinkPriceValue(row.getNumericCellValue(dataTemplate.drinkPriceColumn).getOrElse(0.0))
	}

	private def getDrinkAbv(row : Row, dataTemplate : DrinkDataTemplate) : Double = {
		convertDrinkAbvValue(row.getNumericCellValue(dataTemplate.drinkAbvColumn).getOrElse(0.0))
	}

	private def getDrinkType(row : Row, dataTemplate : DrinkDataTemplate) : Option[String] = {
		dataTemplate.drinkType match {
			case Some(t) => Some(t)
			case None => row.getStringCellValue(dataTemplate.drinkTypeColumn)
		}
	}

	private def getDrinkFeatures(row : Row, dataTemplate : DrinkDataTemplate) : List[DrinkFeature] = {
			featureLoader.drinkFeatures(row, dataTemplate)
	}
	
	private def getQuantityRemaining(row: Row, dataTemplate: DrinkDataTemplate) : String = {
		dataTemplate.quantityRemainingColumn match {
			case Some(col) => {
				val quantity = row(col).getNumericCellValue
				quantity match {
					case num if num > 1.0 => "Not Yet Ready"
					case num if num >= 0.5 => "Plenty"
					case num if num >= 0.25 => "Being Drunk"
					case num if num >= 0.01 => "Nearly Gone"
					case _ => "All Gone"
				}
			}
			case None => "Not  Measured"
		}
	}

	// If this is needed to be the base class of different loaders then  override these methods.
	/**
	 * Convert the raw value of the drink ABV to a double value that is displayed as (e.g.) 4.1%.
	 *
	 * Often spreadsheets return percentages as doubles in the range 0 -> 1. So 4.1% is returned as 0.041.
	 * Therefore this needs to be multiplied by 100 to display correctly.
	 */
	protected def convertDrinkAbvValue(drinkAbvValue : Double) : Double = drinkAbvValue * 100.0D

	/**
	 * Convert the raw value of the the drink price value to the value per pint that is required by the system.
	 *
	 * Currently this is geared for the WBF format which is initially an int in pence, i.e. £2.60 per pint is stored as 260.
	 */
	protected def convertDrinkPriceValue(drinkPriceValue : Double) : Double = drinkPriceValue / 100.0D

}