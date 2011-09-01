/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader.util

import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import org.apache.poi.ss.usermodel.{ Row, Cell }
import org.apache.poi.ss.usermodel.FormulaEvaluator
import scala.MatchError

/**
 * This augments the default [[org.apache.poi.ss.usermodel.Row]] class with helper functions to get cells and their values
 * @author RandomCoder
 *
 */
class RichRow(row : Row) {
	import RichRow._

	private val evaluator = row.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()
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
		row(cellIndex).getCellType() match {
			case Cell.CELL_TYPE_NUMERIC => Some(row(cellIndex).getNumericCellValue)
			case Cell.CELL_TYPE_FORMULA if evaluator.evaluateFormulaCell(row(cellIndex)) == Cell.CELL_TYPE_NUMERIC => Some(row(cellIndex).getNumericCellValue)
			case Cell.CELL_TYPE_STRING => {
				val numberRegex = """\s*(\d+(?:\.\d+))%*""".r
				try {
					val numberRegex(number) = row(cellIndex).getStringCellValue().trim
					Some(number.toDouble)
				} catch {
					case e : MatchError => None
				}
			}
			case _ => None
		}
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
	 * Checks if a cell is not blank or null
	 */
	def isNotBlank(cellIndex : Int) : Boolean = {
		row(cellIndex) != null && row(cellIndex).getCellType != Cell.CELL_TYPE_BLANK
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
				getNumericCellValue(dataTemplate.drinkAbvColumn).isDefined &&
					correctCellType(row, dataTemplate.drinkNameColumn, Cell.CELL_TYPE_STRING)
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