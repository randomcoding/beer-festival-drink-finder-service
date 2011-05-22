/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.data

class DummyDrinkData extends DrinkData {

  import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory._
  import uk.co.randomcoding.drinkfinder.model.drink._

  // TODO: Create test drinks object to contain drinks to test matching

  final def beers = Set(beer("First Beer", "The First Beer brewed", 4.5, 1.40, RealAle), beer("Second Beer",
	"The First Beer brewed", 4.5, 1.40, RealAle))

  final def ciders = Set(cider("First Cider", "The First Cider brewed", 6.5, 1.20, Dry), cider("Second Cider",
	"The Second Cider brewed", 7.2, 1.20, Medium))

  final def perries = Set(perry("First Perry", "The First Perry brewed", 5.3, 1.30, Medium), perry("Second Perry",
	"The Second Perry brewed", 4.9, 1.30, Sweet))

  beers.foreach(addDrink(_))
  ciders.foreach(addDrink(_))
  perries.foreach(addDrink(_))

  /*
  * Used to handle the creation of a set from a single feature
  */
  private implicit def singleElementToSet(feature: DrinkFeature): Set[DrinkFeature] = Set(feature)

}