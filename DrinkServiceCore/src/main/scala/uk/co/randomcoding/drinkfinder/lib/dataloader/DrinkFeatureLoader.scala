/**
 * 
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import org.apache.poi.ss.usermodel.Row
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature
import util.RichRow
import util.RichRow._

/**
 * A Trait for all drink feature loaders to inherit from providing the basic glue to the resto of the system.
 * 
 * @author RandomCoder
 *
 */
class DrinkFeatureLoader {

	/**
	 * Reads the [[DrinkFeature]]s from the given '''Row'''
	 */
	def drinkFeatures(row: Row, dataTemplate: DrinkDataTemplate) : List[DrinkFeature] = {
		for {
			featureName <- featuresForDrink(row, dataTemplate)
		} yield {
			DrinkFeature(featureName)
		}
	}
	
	/**
	 * Function to read the features for a drink.
	 * 
	 * This needs to be implemented for each type of feature loader as the means of reading the data will be different.
	 * 
	 * This should read the drink features form the data source and return a list of strings where each element is the name of a single feature.
	 */
	private def featuresForDrink(row: Row, dataTemplate: DrinkDataTemplate) : List[String] = {
		dataTemplate.drinkFeatureFormat match {
			case "SeparateColumns" => {
				(for {
					(name, column) <- dataTemplate.drinkFeatureColumns
					if row.isNotBlank(column)
				} yield {
					name
				}).toList
			}
			case "SingleColumn" => List.empty
		}
	}
}