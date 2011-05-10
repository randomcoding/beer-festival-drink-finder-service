package uk.randomcoding.drinkfinder.model

package drink {
	sealed class Drink(name : String, description : String, abv : Double, price : Double) {
		private var features : List[DrinkFeature] = Nil
		
		def addFeature(drinkFeature: DrinkFeature) = features = drinkFeature :: features
		
		def removeFeature(drinkFeature: DrinkFeature) = features = features.filterNot(_ == drinkFeature)
		
		def drinkFeatures = features
	}

	case class Beer(name : String, description : String, abv : Double, price : Double) extends Drink(name, description, abv, price)

	case class Cider(name : String, description : String, abv : Double, price : Double) extends Drink(name, description, abv, price)

	case class Perry(name : String, description : String, abv : Double, price : Double) extends Drink(name, description, abv, price)

	sealed class DrinkFeature(feature : String)

	sealed class DrinkSweetness(sweetness: String) extends DrinkFeature(sweetness)
	case object Dry extends DrinkSweetness("Dry")
	case object Medium extends DrinkSweetness("Medium")
	case object Sweet extends DrinkSweetness("Sweet")
	
	sealed class BeerStyle(style: String) extends DrinkFeature(style)
	case object RealAle extends BeerStyle("Real Ale")
	// TODO: Add more styles
}

package brewer {
	class Brewer(name : String, region : String) {
		import uk.randomcoding.drinkfinder.model.drink.Drink
		private var brewedDrinks : List[Drink] = Nil

		def addDrink(drink : Drink) = brewedDrinks = drink :: brewedDrinks

		def removeDrink(drink : Drink) = brewedDrinks = brewedDrinks.filterNot(_ == drink)
	}
}