package uk.co.randomcoding.drinkfinder.model.drink

import uk.co.randomcoding.drinkfinder.model.brewer.{ Brewer, NoBrewer }

sealed case class Drink(name: String, description: String, abv: Double, price: Double, festivalId: String) {

  private var drinkFeatures = List.empty[DrinkFeature]

  var quantityRemaining: String = "Unknown";

  var brewer: Brewer = NoBrewer

  def addFeature(drinkFeature: DrinkFeature) {
    drinkFeatures = drinkFeature :: drinkFeatures
  }

  def removeFeature(drinkFeature: DrinkFeature) {
    drinkFeatures = drinkFeatures.filterNot(_ == drinkFeature)
  }

  def features = drinkFeatures
}

class Beer(name: String, description: String, abv: Double, price: Double, festivalId: String) extends Drink(name, description, abv, price, festivalId)

class Cider(name: String, description: String, abv: Double, price: Double, festivalId: String) extends Drink(name, description, abv, price, festivalId)

class Perry(name: String, description: String, abv: Double, price: Double, festivalId: String) extends Drink(name, description, abv, price, festivalId)

object NoDrink extends Drink("No Drink", "No drinks matched the search", 0, 0, "No Festival")