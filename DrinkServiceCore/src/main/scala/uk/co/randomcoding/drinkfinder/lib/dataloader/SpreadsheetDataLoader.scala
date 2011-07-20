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
import uk.co.randomcoding.drinkfinder.model.brewer.{Brewer, NoBrewer}
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

	// These can be implemented in a parent class if there are more than one festival datasource to load
	private def addRowToData(row : Row, drinkData : WbfDrinkData, dataTemplate : DrinkDataTemplate) {
		// get brewer
		val brewer : Brewer = row.getStringCellValue(dataTemplate.brewerNameColumn) match {
			case Some(x) => Brewer(x)
			case _ => NoBrewer
		}
		
		// read basic drink data
		val drinkName = row.getStringCellValue(dataTemplate.drinkNameColumn)
		val drinkDescription = row.getStringCellValue(dataTemplate.drinkDescriptionColumn)
		val drinkPrice = row.getNumericCellValue(dataTemplate.drinkPriceColumn)
		val drinkAbv = row.getNumericCellValue(dataTemplate.drinkAbvColumn)
		
		println("Retrieved Name: %s, Desc: %s, Price: %f, ABV: %f".format(drinkName.get, drinkDescription.get, drinkPrice.get, drinkAbv.get))
		// read drink features
		
		// read/determine drink type
		drinkData.addBrewer(brewer)
	}
}