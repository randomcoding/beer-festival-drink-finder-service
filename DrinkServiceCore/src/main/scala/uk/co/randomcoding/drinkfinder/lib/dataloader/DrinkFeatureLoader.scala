/**
 * 
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import org.apache.poi.ss.usermodel.Row
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature

/**
 * A Trait for all drink feature loaders to inherit from providing the basic glue to the resto of the system.
 * 
 * @author RandomCoder
 *
 */
trait DrinkFeatureLoader {

	def loadFeatures(row: Row, dataTemplate: DrinkDataTemplate) : List[DrinkFeature] = {
		val featureNames = featuresForDrink(row, dataTemplate)
		
		for {
			featureName <- featureNames
		} yield {
			DrinkFeature(featureName)
		}
		
		
		// convert to features by case classes
	}
	
	/**
	 * Function to read the features for a drink.
	 * 
	 * This needs to be implemented for each type of feature loader as the means of reading the data will be different.
	 * 
	 * This should read the drink features form the data source and return a list of strings where each element is the name of a single feature.
	 */
	def featuresForDrink(row: Row, dataTemplate: DrinkDataTemplate) : List[String]
	
	/**
	 * Function to register a feature with the service's features manager. 
	 * 
	 * This makes the feature available to the rest of the service
	 */
	private def registerFeature(featureString: String) = {
		// TODO
	}
}