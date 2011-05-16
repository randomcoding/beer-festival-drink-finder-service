/**
 * 
 */
package uk.co.randomcoding.drinkfinder.model.data

class DummyDrinkData extends DrinkData {
	import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory._
	import uk.co.randomcoding.drinkfinder.model.drink._
	// add drinks
	addDrink(beer("First Beer", "The First Beer brewed", 4.5, 1.40, RealAle))
	addDrink(beer("Second Beer", "The First Beer brewed", 4.5, 1.40, RealAle))
	
	/**
	 * USed to handle the creation of a set from a single feature
	 */
	private implicit def singleElementToSet(feature: DrinkFeature) : Set[DrinkFeature] = Set(feature)
}

/**
 * Test object to contain all drink data for testing.
 * 
 * @author RandomCoder
 */
object DummyDrinkData extends DummyDrinkData {

}