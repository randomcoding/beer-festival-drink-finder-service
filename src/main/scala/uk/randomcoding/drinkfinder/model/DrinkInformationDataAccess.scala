package uk.randomcoding.drinkfinder.model

/**
 * Defines the means of accessing drink information data 
 */
trait DrinkInformationDataAccess {
	protected [this] val drinkData: DrinkData
}