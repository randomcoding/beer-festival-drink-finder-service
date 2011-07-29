package uk.co.randomcoding.drinkfinder.model.matcher

import scala.Some
import id._
import uk.co.randomcoding.drinkfinder.model.drink._
import net.liftweb.common.Logger

/**
 * Enumeration of the types of matchers as case classes with extractors
 *
 * User: RandomCoder
 * Date: 26/05/11
 */

sealed abstract class Matcher[M](matcherType : MatcherId, matchValue : M) extends Logger {
  type T

  def matcherId : MatcherId = matcherType

  def apply(matchWith : T) : Boolean

  protected val matchTo = matchValue
}

abstract class DrinkMatcher[M](matcherType : MatcherId, matchValue : M) extends Matcher(matcherType, matchValue) {
  override type T = Drink
}

case class DrinkNameMatcher(drinkName : String) extends DrinkMatcher[String](DRINK_NAME, drinkName) {

  def apply(drink : Drink) = {
    debug("Matching drink %s to name containing %s".format(drink, matchTo))
    drink.name.toLowerCase contains matchTo.toLowerCase
  }
}

case class DrinkDescriptionMatcher(descriptionWords : String) extends DrinkMatcher[String](DRINK_DESCRIPTION, descriptionWords) {

  def apply(drink : Drink) = {
    val desc = drink.description.toLowerCase
    val words = matchTo.split(", ")
    val wordMatcher = (matchWord : String) => desc contains matchWord.toLowerCase
    words.map(wordMatcher).find(_ == false).isEmpty
  }
}

case class DrinkPriceMatcher(price : Double) extends DrinkMatcher[Double](DRINK_PRICE, price) {

  def apply(drink : Drink) = drink.price <= matchTo
}

case class DrinkAbvLessThanMatcher(abv : Double) extends DrinkMatcher[Double](DRINK_ABV_LESS_THAN, abv) {

  def apply(drink : Drink) = drink.abv < matchTo
}

case class DrinkAbvGreaterThanMatcher(abv : Double) extends DrinkMatcher[Double](DRINK_ABV_GREATER_THAN, abv) {

  def apply(drink : Drink) = drink.abv > matchTo
}

case class DrinkAbvEqualToMatcher(abv : Double) extends DrinkMatcher[Double](DRINK_ABV_EQUAL_TO, abv) {

  def apply(drink : Drink) = drink.abv == matchTo
}

case class DrinkFeatureMatcher(feature : DrinkFeature) extends DrinkMatcher[DrinkFeature](DRINK_HAS_FEATURES, feature) {

  def apply(drink : T) = drink.features.contains(matchTo)
}

case class DrinkTypeMatcher(drinkType : String) extends DrinkMatcher[String](DRINK_TYPE, drinkType) {

  def apply(drink : Drink) = {
    matchTo.toLowerCase match {
      case "beer" => drink.isInstanceOf[Beer]
      case "cider" => drink.isInstanceOf[Cider]
      case "perry" => drink.isInstanceOf[Perry]
      case _ => {
        error("Undefined drink type %s".format(matchTo));
        false
      }
    }
  }
}

case class BrewerNameMatcher(brewerName : String) extends DrinkMatcher[String](BREWER_NAME, brewerName) {

  def apply(drink: Drink) = drink.brewer.name.toLowerCase.contains(matchTo.toLowerCase)

}

case object AlwaysTrueDrinkMatcher extends DrinkMatcher[Any](ALWAYS_TRUE, "") {

  def apply(matchTo : T) = true
}