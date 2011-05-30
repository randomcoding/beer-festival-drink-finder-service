/**
 * TODO: Add License details
 */
package uk.co.randomcoding.drinkfinder.model.data.wbf

import uk.co.randomcoding.drinkfinder.model.data.DrinkData

/**
 * Drink data specifically for the Worcester Beer Festival
 *
 * User: RandomCoder
 * Date: 24/05/11
 */
class WbfDrinkData extends DrinkData {

}

/**
 * Companion object accessor for the private class WbfDrinkData
 */
object WbfDrinkData extends WbfDrinkData {
  private lazy val drinkData = initialiseData()

  private var initialiseData: (() => WbfDrinkData) = _

  def initialiseEmpty(): WbfDrinkData = {
	initialiseData = (() => {
	  new WbfDrinkData()
	})

	drinkData
  }

  def initialiseTest(): WbfDrinkData = {
	initialiseData = (() => {
	  import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory._
	  import uk.co.randomcoding.drinkfinder.model.drink._
	  val data = new WbfDrinkData()
	  data.addDrink(beer("First Beer", "The First Beer", 4.5, 1.40, List(RealAle)))
	  data.addDrink(cider("First Cider", "The First Cider", 6.5, 1.30, List(Dry)))
	  data.addDrink(perry("First Perry", "The First Perry", 7.3, 1.40, List(Medium)))
	  data
	})

	drinkData
  }

  def loadFromFile(): WbfDrinkData = {
	initialiseData = (() => {
	  val data = new WbfDrinkData()
	  // TODO: Load from spreadsheet in default location which is put there by upload and location set in loaded
	  // properties file
	  data
	})
	drinkData
  }
}