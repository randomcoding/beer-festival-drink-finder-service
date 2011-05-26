package uk.co.randomcoding.drinkfinder.model.data.wbf

import uk.co.randomcoding.drinkfinder.model.data.DrinkInformationDataAccess

/**
 * TODO: Add License details
 */
/**
 * Contains the drink data for the Worcester Beer Festival
 *
 * User: tim
 * Date: 24/05/11
 */

class WbfDrinkDataAccess extends DrinkInformationDataAccess {
  lazy val drinkData = initialiseDrinkData()

  def initialiseDrinkData(): WbfDrinkData = {
	// TODO: load property to specify which data lo load
	val loadProperty = "test"

	loadProperty.toLowerCase match {
	  case "file" => WbfDrinkData.loadFromFile()
	  case "test" => WbfDrinkData.initialiseTest()
	  case _ => WbfDrinkData.initialiseEmpty()
	}
  }
}