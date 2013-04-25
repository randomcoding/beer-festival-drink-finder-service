package uk.co.randomcoding.drinkfinder.model.drink

/**
 * Object to generate drinks
 * @author RandomCoder
 *
 */
object DrinkFactory {
  def beer(name: String, description: String, abv: Double, price: Double, festivalId: String, features: Iterable[DrinkFeature]): Beer = {
    val beer = new Beer(name, description, abv, price, festivalId)
    addFeatures(beer, features)
    beer
  }

  def cider(name: String, description: String, abv: Double, price: Double, festivalId: String, features: Iterable[DrinkFeature]): Cider = {
    val cider = new Cider(name, description, abv, price, festivalId)
    addFeatures(cider, features)
    cider
  }

  def perry(name: String, description: String, abv: Double, price: Double, festivalId: String, features: Iterable[DrinkFeature]): Perry = {
    val perry = new Perry(name, description, abv, price, festivalId)
    addFeatures(perry, features)
    perry
  }

  private def addFeatures(drink: Drink, features: Iterable[DrinkFeature]) {
    features.foreach(drink.addFeature(_))
  }
}