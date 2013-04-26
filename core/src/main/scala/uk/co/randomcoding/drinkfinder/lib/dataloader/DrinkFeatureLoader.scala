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
 *    RandomCoder - initial API and implementation and/or initial documentation
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import org.apache.poi.ss.usermodel.Row
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature
import util.RichRow._

/**
 * Loads drink features from a spreadsheet row for a single drink.
 *
 * @author RandomCoder
 *
 */
class DrinkFeatureLoader {

	private def firstLetterCaps(inString : String) : String = inString.split(" ").map(elem => elem.charAt(0).toUpper + elem.substring(1).toLowerCase).mkString("", " ", "")

	/**
	 * Reads the [[DrinkFeature]]s from the given '''Row'''
	 */
	def drinkFeatures(row : Row, dataTemplate : DrinkDataTemplate) : List[DrinkFeature] = {
		for {
			featureName <- featuresForDrink(row, dataTemplate).map(firstLetterCaps(_)).distinct
		} yield {
			DrinkFeature(featureName)
		}
	}

	/**
	 * Function to read the features for a drink and return the names as a list of strings
	 */
	private def featuresForDrink(row : Row, dataTemplate : DrinkDataTemplate) : List[String] = {
		dataTemplate.drinkFeatureFormat.getOrElse("unknown").toLowerCase match {
			case "separatecolumns" => readFeaturesFromSeparateColumns(row, dataTemplate)
			case "singlecolumn" => readFeaturesFromSingleColumn(row, dataTemplate)
			case "mixed" => readFeaturesFromSingleColumn(row, dataTemplate) ::: readFeaturesFromSeparateColumns(row, dataTemplate)
			case _ => Nil
		}
	}

	private def readFeaturesFromSingleColumn(row : Row, dataTemplate : DrinkDataTemplate) = {
		val prefix = dataTemplate.singleDrinkFeaturePrefix match {
			case s if s.nonEmpty => s + " "
			case _ => ""
		}
		dataTemplate.drinkFeatureColumn match {
			case Some(col) => row.getStringCellValue(col) match {
				case Some(feature) => List("%s%s".format(prefix, feature))
				case _ => Nil
			}
			case _ => Nil
		}
	}

	private def readFeaturesFromSeparateColumns(row : Row, dataTemplate : DrinkDataTemplate) = {
		(for {
			(name, column) <- dataTemplate.drinkFeatureColumns
			if row.isNotBlank(column)
		} yield {
			name
		}).toList
	}
}
