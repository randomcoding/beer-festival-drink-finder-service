/**
 * TODO: Add License details
 */
package uk.co.randomcoding.drinkfinder.model.data.wbf

import uk.co.randomcoding.drinkfinder.model.data.DrinkData
import uk.co.randomcoding.drinkfinder.model.brewer.Brewer

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
	  val firstBeer = beer("First Beer", "The First Beer", 4.5, 1.80, List(RealAle))
	  val secondBeer = beer("Second Beer", "The Second Beer", 4.5, 1.70, List(RealAle))
	  val firstCider = cider("First Cider", "The First Cider", 6.5, 1.30, List(Dry))
	  val secondCider = cider("Second Cider", "The Second Cider", 6.5, 1.35, List(Medium))
	  val firstPerry = perry("First Perry", "The First Perry", 7.3, 1.60, List(Medium))
	  val secondPerry = perry("Second Perry", "The Second Perry", 7.3, 1.50, List(Sweet))
	  val firstBrewer = Brewer("First Brewer")
	  val secondBrewer = Brewer("Second Brewer")
	  val thirdBrewer = Brewer("Third Brewer")
	  
	  firstBeer.brewer = firstBrewer
	  secondBeer.brewer = secondBrewer
	  firstCider.brewer = firstBrewer
	  secondCider.brewer = firstBrewer
	  firstPerry.brewer = secondBrewer
	  secondPerry.brewer = firstBrewer
	  
	  data.addBrewer(firstBrewer)
	  data.addBrewer(secondBrewer)
	  data.addBrewer(thirdBrewer)

	  data.addDrink(firstBeer)
	  data.addDrink(firstCider)
	  data.addDrink(firstPerry)
	  data.addDrink(secondBeer)
	  data.addDrink(secondCider)
	  data.addDrink(secondPerry)
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