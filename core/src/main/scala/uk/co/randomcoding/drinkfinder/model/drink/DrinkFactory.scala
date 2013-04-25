package uk.co.randomcoding.drinkfinder.model.drink

import uk.co.randomcoding.drinkfinder.model.brewer.BrewerRecord

/**
 * Object to generate drinks
 * @author RandomCoder
 *
 */
object DrinkFactory {
  def beer(name: String, description: String, abv: Double, price: Double, brewer: BrewerRecord, festivalId: String, features: Iterable[DrinkFeature]): DrinkRecord = {
    DrinkRecord(name, description, abv, price, brewer, DrinkType.BEER, festivalId, features.toSeq)
  }

  def cider(name: String, description: String, abv: Double, price: Double, brewer: BrewerRecord, festivalId: String, features: Iterable[DrinkFeature]): DrinkRecord = {
    DrinkRecord(name, description, abv, price, brewer, DrinkType.CIDER, festivalId, features.toSeq)
  }

  def perry(name: String, description: String, abv: Double, price: Double, brewer: BrewerRecord, festivalId: String, features: Iterable[DrinkFeature]): DrinkRecord = {
    DrinkRecord(name, description, abv, price, brewer, DrinkType.PERRY, festivalId, features.toSeq)
  }

}
