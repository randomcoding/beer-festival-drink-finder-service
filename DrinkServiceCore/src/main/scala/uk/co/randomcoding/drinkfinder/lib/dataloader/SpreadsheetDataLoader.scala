/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import scala.collection.JavaConverters._
import java.io.InputStream
import org.apache.poi.ss.usermodel.{ WorkbookFactory, Row, Cell }
import util.RichRow
import util.RichRow._
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkData
import uk.co.randomcoding.drinkfinder.model.brewer.{ Brewer, NoBrewer }
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory._
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate

/**
 * @author RandomCoder
 *
 */
class SpreadsheetDataLoader {

	/**
	 * Read the data from an Excel spreadsheet input stream according to the provided data template
	 */
	def loadData(excelDataFile : InputStream, dataTemplate : DrinkDataTemplate) : WbfDrinkData = {

		val wb = WorkbookFactory.create(excelDataFile)
		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK)
		val dataSheet = wb.getSheetAt(0);

		val drinkData = new WbfDrinkData()

		val physicalRows = dataSheet.rowIterator.asScala
		physicalRows.foreach(row => if (row.isDataRow(dataTemplate)) addRowToData(row, drinkData, dataTemplate))

		drinkData
	}

	private def addRowToData(row : Row, drinkData : WbfDrinkData, dataTemplate : DrinkDataTemplate) {
		val drink = getDrinkType(row, dataTemplate) match {
			case Some(t) => t.toLowerCase match {
				case "beer" => beer(getDrinkName(row, dataTemplate), getDrinkDescription(row, dataTemplate), getDrinkAbv(row, dataTemplate), getDrinkPrice(row, dataTemplate), getDrinkFeatures(row, dataTemplate))
				case "cider" => cider(getDrinkName(row, dataTemplate), getDrinkDescription(row, dataTemplate), getDrinkAbv(row, dataTemplate), getDrinkPrice(row, dataTemplate), getDrinkFeatures(row, dataTemplate))
				case "perry" => perry(getDrinkName(row, dataTemplate), getDrinkDescription(row, dataTemplate), getDrinkAbv(row, dataTemplate), getDrinkPrice(row, dataTemplate), getDrinkFeatures(row, dataTemplate))
			}
			case None => error("No drink type for data found")
		}

		val brewer = getBrewer(row, dataTemplate)
		drinkData.addBrewer(brewer)
		drink.brewer = brewer

		drinkData.addDrink(drink)
	}

	private def getBrewer(row : Row, dataTemplate : DrinkDataTemplate) : Brewer = {
		row.getStringCellValue(dataTemplate.brewerNameColumn) match {
			case Some(x) => Brewer(x)
			case _ => NoBrewer
		}
	}

	private def getDrinkName(row : Row, dataTemplate : DrinkDataTemplate) : String = {
		row.getStringCellValue(dataTemplate.drinkNameColumn).get
	}

	private def getDrinkDescription(row : Row, dataTemplate : DrinkDataTemplate) : String = {
		row.getStringCellValue(dataTemplate.drinkDescriptionColumn).get
	}

	private def getDrinkPrice(row : Row, dataTemplate : DrinkDataTemplate) : Double = {
		convertDrinkPriceValue(row.getNumericCellValue(dataTemplate.drinkPriceColumn).get)
	}

	private def getDrinkAbv(row : Row, dataTemplate : DrinkDataTemplate) : Double = {
		convertDrinkAbvValue(row.getNumericCellValue(dataTemplate.drinkAbvColumn).get)
	}

	private def getDrinkType(row : Row, dataTemplate : DrinkDataTemplate) : Option[String] = {
		dataTemplate.drinkType match {
			case Some(t) => Some(t)
			case None => row.getStringCellValue(dataTemplate.drinkTypeColumn)
		}
	}

	// TODO: If this is needed to be the base class of different loaders then  implement these methods.
	/**
	 * Load the features from a specific spreadsheet. This is intrinsically
	 */
	protected def getDrinkFeatures(row : Row, dataTemplate : DrinkDataTemplate) : List[DrinkFeature] = {
		List.empty
	}

	/**
	 * Convert the raw value of the drink ABV to a double value that is displayed as (e.g.) 4.1%.
	 *
	 * Often spreadsheets return percentages as doubles in the range 0 -> 1. So 4.1% is returned as 0.041.
	 * Therefore this needs to be multiplied by 100 to display correctly.
	 */
	protected def convertDrinkAbvValue(drinkAbvValue : Double) : Double = drinkAbvValue * 100.0D

	/**
	 * Convert the raw value of the the drink price value to the value per 1/2 pint that is required by the system.
	 *
	 * Currently this is geared for the WBF format which is initially an int in pence, i.e. £2.60 per pint is stored as 260.
	 */
	protected def convertDrinkPriceValue(drinkPriceValue : Double) : Double = drinkPriceValue / 200.0D

}