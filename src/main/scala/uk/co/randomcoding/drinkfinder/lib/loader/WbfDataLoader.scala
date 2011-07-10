/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.loader

import scala.collection.JavaConverters._
import java.io.InputStream
import org.apache.poi.ss.usermodel.{ WorkbookFactory, Row, Cell }
import util.RichRow
import util.RichRow._
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkData
import uk.co.randomcoding.drinkfinder.model.brewer.{Brewer, NoBrewer}

/**
 * @author RandomCoder
 *
 */
class WbfDataLoader {
	private val MAX_RECORDS_READ = 500

	/**
	 * Read the data from an Excel spreadsheet input stream according to the provided data template
	 */
	def loadData(excelDataFile : InputStream, dataTemplate : DrinkDataTemplate) = {

		val wb = WorkbookFactory.create(excelDataFile)
		wb.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL)
		val dataSheet = wb.getSheetAt(0);

		val physicalRows = dataSheet.rowIterator.asScala

		val drinkData = new WbfDrinkData()

		physicalRows.foreach(row => if (row.isDataRow(dataTemplate)) addRowToData(row, drinkData, dataTemplate))
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
		
		// read drink features
		
		// read/determine drink type
		drinkData.addBrewer(brewer)
	}
}