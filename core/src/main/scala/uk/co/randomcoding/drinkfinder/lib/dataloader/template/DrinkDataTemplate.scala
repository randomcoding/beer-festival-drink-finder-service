/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader.template

import scala.io.Source
import net.liftweb.common.Logger

/**
 * Representation of a template to load
 * @author RandomCoder
 *
 */
class DrinkDataTemplate(templateSource : Source) extends Logger {
	private val LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toArray
	
	private val lines = templateSource.getLines.toList

	private def getValueFor(key : String) : Option[String] = {
		lines.find(_.startsWith(key)) match {
			case Some(line) if line.matches(""".+=.+""") => line.split("=")(1).trim match {
				case "-1" => None
				case value => Some(value)
			}
			case _ => None
		}
	}
	
	lazy val quantityRemainingColumn = getValueFor("drink.quantity.remaining") match {
		case Some(index) => Some(columnIndex(index))
		case _ => None 
	}

	lazy val singleDrinkFeaturePrefix = getValueFor("drink.feature.prefix").getOrElse("")/* match {
		case Some(s) : String => s + " "
		case "NOT_FOUND" => ""
	}*/
	
	lazy val festivalName = getValueFor("festival.name").get

	lazy val dataStartLine = getValueFor("data.start.line").getOrElse("0").toInt

	lazy val drinkNameColumn = columnIndex(getValueFor("drink.name.column").get)

	lazy val drinkDescriptionColumn = getValueFor("drink.description.column") match {
		case Some(col) => Some(columnIndex(col))
		case None => None
	}

	lazy val drinkPriceColumn = getValueFor("drink.price.column") match {
		case Some(col) => Some(columnIndex(col))
		case None => None
	}

	lazy val drinkAbvColumn = columnIndex(getValueFor("drink.abv.column").get)
	/*match {
		case Some(col) => Some(columnIndex(col))
		case None => None
	}*/

	lazy val brewerNameColumn = getValueFor("brewer.name.column") match {
		case Some(col) => Some(columnIndex(col))
		case None => None
	}

	lazy val drinkFeatureColumn = getValueFor("drink.feature.column") match {
		case Some(col) => Some(columnIndex(col))
		case None => None
	}

	lazy val drinkTypeColumn = getValueFor("drink.type.column") match {
		case None => -1
		case Some(col) => columnIndex(col)
	}

	lazy val drinkType = getValueFor("drink.type") /*match {
		case "NOT_FOUND" => None
		case t => Some(t)
	}*/

	lazy val drinkRemainingColumn = getValueFor("drink.quantity.remaining.column") match {
		case None => -1
		case Some(col) => columnIndex(col)
	}

	lazy val drinkFeatureFormat = getValueFor("drink.feature.format")

	private val featureNameRegexString = """drink\.feature\.([a-zA-Z/_]+)\.column\s?=\s?([A-Z]{2})"""
	private val featureNameRegex = featureNameRegexString.r
	
	/**
	 * Get the map of drink feature names to the column that is used to identify the feature for the drink.
	 * 
	 *  This is only relevant if the [[#drinkFeatureFormat]] is '''SeparateColumns'''. If it is '''SingleColumn''' an empty map will be returned.
	 */
	lazy val drinkFeatureColumns : Map[String, Int]= {

		val featureTuples = for {
			line <- lines
			if line.matches(featureNameRegexString)
			val featureNameRegex(name, col) = line
		} yield {
			(name.replaceAll("_", " "), columnIndex(col))
		}

		featureTuples.toMap
	}
	
	private def columnIndex(columnName: String) : Int = {
		columnName.toUpperCase match {
			case numberString if numberString.matches("""-?\d+""") => numberString.toInt
			case charString if charString.matches("""[A-Z]""") => LETTERS.indexOf(charString(0))
			case letterString if letterString.matches("""[A-Z]{2}""") => {
				((letterIndex(letterString(0)) + 1) * 26) + letterIndex(letterString(1))
			}
			case _ => {
				error("Invalid Column name %s".format(columnName))
				-1
			}
		}
	}
	
	private def letterIndex(letter: Char) : Int = LETTERS.indexOf(letter)
}
