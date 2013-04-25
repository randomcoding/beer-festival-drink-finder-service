package uk.co.randomcoding.drinkfinder.model.matcher

import id._
import uk.co.randomcoding.drinkfinder.model.drink._
import net.liftweb.common.Logger
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature
import uk.co.randomcoding.drinkfinder.model.brewer.BrewerRecord

/**
 * Enumeration of the types of matchers as case classes with extractors
 *
 * User: RandomCoder
 * Date: 26/05/11
 */

sealed abstract class Matcher[M](matcherType: MatcherId, matchValue: M) extends Logger {
  type T

  def matcherId: MatcherId = matcherType

  def apply(matchWith: T): Boolean

  protected val matchTo = matchValue
}

abstract class DrinkMatcher[M](matcherType: MatcherId, matchValue: M) extends Matcher(matcherType, matchValue) {
  override type T = DrinkRecord
}

case class DrinkNameMatcher(drinkName: String) extends DrinkMatcher[String](DRINK_NAME, drinkName) {

  def apply(drink: DrinkRecord) = {
    debug("Matching drink %s to name containing %s".format(drink, matchTo))
    drink.name.get.toLowerCase contains matchTo.toLowerCase
  }
}

case class DrinkDescriptionMatcher(descriptionWords: String) extends DrinkMatcher[String](DRINK_DESCRIPTION, descriptionWords) {

  def apply(drink: DrinkRecord) = {
    val desc = drink.description.get.toLowerCase
    val words = matchTo.split(", ")
    val wordMatcher = (matchWord: String) => desc contains matchWord.toLowerCase
    words.map(wordMatcher).find(_ == false).isEmpty
  }
}

case class DrinkPriceMatcher(price: Double) extends DrinkMatcher[Double](DRINK_PRICE, price) {

  def apply(drink: DrinkRecord) = drink.price.get <= matchTo
}

case class DrinkAbvLessThanMatcher(abv: Double) extends DrinkMatcher[Double](DRINK_ABV_LESS_THAN, abv) {

  def apply(drink: DrinkRecord) = drink.abv.get < matchTo
}

case class DrinkAbvGreaterThanMatcher(abv: Double) extends DrinkMatcher[Double](DRINK_ABV_GREATER_THAN, abv) {

  def apply(drink: DrinkRecord) = drink.abv.get > matchTo
}

case class DrinkAbvEqualToMatcher(abv: Double) extends DrinkMatcher[Double](DRINK_ABV_EQUAL_TO, abv) {

  def apply(drink: DrinkRecord) = drink.abv.get == matchTo
}

case class DrinkFeatureMatcher(feature: DrinkFeature) extends DrinkMatcher[DrinkFeature](DRINK_HAS_FEATURES, feature) {

  def apply(drink: T) = drink.features.get.contains(matchTo)
}

case class DrinkTypeMatcher(drinkType: String) extends DrinkMatcher[String](DRINK_TYPE, drinkType) {

  def apply(drink: DrinkRecord) = {
    matchTo.toLowerCase match {
      case "beer" => drink.drinkType.get.toString == DrinkType.BEER.toString
      case "cider" => drink.drinkType.get.toString == DrinkType.CIDER.toString
      case "perry" => drink.drinkType.get.toString == DrinkType.PERRY.toString
      case _ => {
        error("Undefined drink type %s".format(matchTo))
        false
      }
    }
  }
}

case class BrewerNameMatcher(brewerName: String) extends DrinkMatcher[String](BREWER_NAME, brewerName) {

  override def apply(drink: DrinkRecord): Boolean = BrewerRecord.findById(drink.brewer.get) match {
    case Some(brewer) => brewer.name.get.toLowerCase.contains(matchTo.toLowerCase)
    case _ => false
  }

}

case class FestivalIdMatcher(festivalId: String) extends DrinkMatcher[String](FESTIVAL_ID, festivalId) {
  def apply(drink: DrinkRecord) = drink.festivalId.get == festivalId
}

case object AlwaysTrueDrinkMatcher extends DrinkMatcher[Any](ALWAYS_TRUE, "") {

  def apply(matchTo: T) = true
}
