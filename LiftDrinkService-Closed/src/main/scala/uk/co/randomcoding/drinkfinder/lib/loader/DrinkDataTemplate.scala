/**
 * 
 */
package uk.co.randomcoding.drinkfinder.lib.loader

import scala.io.Source

/**
 * Representation of a template to load 
 * @author RandomCoder
 *
 */
class DrinkDataTemplate(templateSource: Source) {
	private val lines = templateSource.getLines.toList
	
	private def getValueFor(key: String) : String = {		
		lines.find(_.startsWith(key)) match {
			case Some(line) => line.split("=")(1).trim
			case _ => "NOT_FOUND"
		}
	}
	
	lazy val dataStartLine = getValueFor("data.start.line").toInt
	
	lazy val drinkNameColumn = getValueFor("drink.name.column").toInt

	lazy val drinkDescriptionColumn = getValueFor("drink.description.column").toInt
	
	lazy val drinkPriceColumn = getValueFor("drink.price.column").toInt
	
	lazy val drinkAbvColumn = getValueFor("drink.abv.column").toInt
	
	lazy val brewerNameColumn = getValueFor("brewer.name.column").toInt
	
	lazy val drinkFeatureColumn = getValueFor("drink.feature.column").toInt
}

//class DrinkTemplateData(dataStartRow: Int)