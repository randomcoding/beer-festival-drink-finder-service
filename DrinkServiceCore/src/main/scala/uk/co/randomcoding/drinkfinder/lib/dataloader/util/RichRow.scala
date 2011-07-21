/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader.util

import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import org.apache.poi.ss.usermodel.{ Row, Cell }

/**
 * This augments the default [[org.apache.poi.ss.usermodel.Row]] class with helper functions to get cells and their values
 * @author RandomCoder
 *
 */
class RichRow(row : Row) {
	import RichRow._

	/**
	 * Get the cell at the given index
	 */
	def apply(cellIndex : Int) : Cell = row.getCell(cellIndex)

	/**
	 * Gets the numeric value of a cell.
	 *
	 * @return '''Some(value)''' if the cell contains a numeric value or '''None''' otherwise
	 */
	def getNumericCellValue(cellIndex : Int) : Option[Double] = {
		if (correctCellType(row, cellIndex, Cell.CELL_TYPE_NUMERIC)) Some(row(cellIndex).getNumericCellValue) else None
	}

	/**
	 * Gets the string value of a cell.
	 *
	 * @return '''Some(value)''' if the cell contains a string value or '''None''' otherwise
	 */
	def getStringCellValue(cellIndex : Int) : Option[String] = {
		if (correctCellType(row, cellIndex, Cell.CELL_TYPE_STRING)) Some(row(cellIndex).getStringCellValue) else None
	}

	/**
	 * Checks is a row is a drink data row
	 *
	 * Currently checks the name is a string and the price cell is a numeric value
	 */
	def isDataRow(dataTemplate : DrinkDataTemplate) : Boolean = {
		null == row match {
			case true => println("Row is null"); false
			case _ => {
				val abvColumn = dataTemplate.drinkAbvColumn
				correctCellType(row, dataTemplate.drinkNameColumn, Cell.CELL_TYPE_STRING) &&
					correctCellType(row, dataTemplate.drinkAbvColumn, Cell.CELL_TYPE_NUMERIC)
			}
		}
	}

	private def correctCellType(row : Row, cellIndex : Int, expectedCellType : Int) : Boolean = {
		row(cellIndex).getCellType equals expectedCellType
	}
}

/**
 * Contains an implicit conversion from a Row to a [[RichRow]]
 */
object RichRow {
	implicit def rowToRichRow(row : Row) : RichRow = new RichRow(row)
}