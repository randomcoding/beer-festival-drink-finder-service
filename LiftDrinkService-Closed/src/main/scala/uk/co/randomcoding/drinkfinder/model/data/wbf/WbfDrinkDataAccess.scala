/**
 * TODO: Add License details
 */
package uk.co.randomcoding.drinkfinder.model.data.wbf

import uk.co.randomcoding.drinkfinder.model.data.DrinkInformationDataAccess
import net.liftweb.common.Logger

/**
 * Contains the drink data for the Worcester Beer Festival
 *
 * User: tim
 * Date: 24/05/11
 */

class WbfDrinkDataAccess extends DrinkInformationDataAccess with Logger {

  /**
   * We use a [[WbfDrinkData]] as the type of drink data
   */
  type DrinkDataType = WbfDrinkData

  /**
   * Returns a fully initialised ]]WbfDrinkData]] based on the the value of the [[TBD]] property.
   */
  protected def initialiseDrinkData(): WbfDrinkData = {
	  debug("Initialisation of WbfData with type: %s".format(initialisationType))
	initialisationType match {
	  case "file" => WbfDrinkData.loadFromFile()
	  case "test" => WbfDrinkData.initialiseTest()
	  case _ => WbfDrinkData.initialiseEmpty()
	}
  }

  // TODO: load property to specify which data lo load
  private def initialisationType = "test"
}