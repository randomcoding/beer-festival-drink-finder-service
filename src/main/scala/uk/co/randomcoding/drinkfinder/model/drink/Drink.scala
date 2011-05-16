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

