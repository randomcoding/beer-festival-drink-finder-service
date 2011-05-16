package uk.co.randomcoding.drinkfinder.model.drink

sealed class Drink(name : String, description : String, abv : Double, price : Double) {

	private var features : List[DrinkFeature] = Nil

	def addFeature(drinkFeature : DrinkFeature) = features = drinkFeature :: features

	def removeFeature(drinkFeature : DrinkFeature) = features = features.filterNot(_ == drinkFeature)

	def drinkFeatures = features
}

case class Beer(name : String, description : String, abv : Double, price : Double) extends Drink(name, description, abv, price)

case class Cider(name : String, description : String, abv : Double, price : Double) extends Drink(name, description, abv, price)

case class Perry(name : String, description : String, abv : Double, price : Double) extends Drink(name, description, abv, price)

/**
 * Object to generate drinks
 * @author RandomCoder
 *
 */
object DrinkFactory {
	def beer(name: String, description: String, abv: Double, price: Double, features: Iterable[DrinkFeature]) : Beer = {
		val beer = Beer(name, description, abv, price)
		addFeatures(beer, features)
		return beer
	}

	def cider(name: String, description: String, abv: Double, price: Double, features: Iterable[DrinkFeature]) : Cider = {
			val cider = Cider(name, description, abv, price)
			addFeatures(cider, features)
			return cider
	}
	
	def perry(name: String, description: String, abv: Double, price: Double, features: Iterable[DrinkFeature]) : Perry = {
			val perry = Perry(name, description, abv, price)
			addFeatures(perry, features)
			return perry
	}
	
	private def addFeatures(drink: Drink, features : Iterable[DrinkFeature]) = {
		features.foreach(drink.addFeature(_))
	}
}