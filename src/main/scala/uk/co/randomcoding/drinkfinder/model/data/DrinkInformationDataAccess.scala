package uk.co.randomcoding.drinkfinder.model.data

/**
 * Defines the means of accessing drink information data 
 */
trait DrinkInformationDataAccess {
	protected [this] val drinkData: DrinkData
}