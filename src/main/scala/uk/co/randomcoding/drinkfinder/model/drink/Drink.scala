package uk.co.randomcoding.drinkfinder.model.drink

sealed case class Drink(name: String, description: String, abv: Double, price: Double) {

  private var drinkFeatures = List.empty[DrinkFeature]

  def addFeature(drinkFeature: DrinkFeature) {
	drinkFeatures = drinkFeature :: drinkFeatures
  }

  def removeFeature(drinkFeature: DrinkFeature) {
	drinkFeatures = drinkFeatures.filterNot(_ == drinkFeature)
  }

  def features = drinkFeatures
}

class Beer(name: String, description: String, abv: Double, price: Double) extends Drink(name, description, abv, price)

class Cider(name: String, description: String, abv: Double, price: Double) extends Drink(name, description, abv, price)

class Perry(name: String, description: String, abv: Double, price: Double) extends Drink(name, description, abv, price)

