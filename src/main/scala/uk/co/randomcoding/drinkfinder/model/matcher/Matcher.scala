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

  //def unapply(matchId: String, matchTo: M) : Option[Matcher[_]]

  protected val matchTo = matchValue
}

abstract class DrinkMatcher[M](matcherType : MatcherId, matchValue : M) extends Matcher(matcherType, matchValue) {
  override type T = Drink
}

case object BeerTypeMatcher extends DrinkMatcher[Any](DRINK_TYPE_BEER, "") {

  def apply(drink : T) = {
    drink.isInstanceOf[Beer]
  }

  /*def unapply(matcherType : String) : Option[Matcher[_]] = {
    if (matcherType == matcherId) Some(BeerTypeMatcher) else None
  }*/
}

case object CiderTypeMatcher extends DrinkMatcher[Any](DRINK_TYPE_CIDER, "") {

  def apply(drink : T) = {
    drink.isInstanceOf[Cider]
  }

  /*def unapply(matcherType : String) : Option[Matcher[_]] = {
    if (matcherType == matcherId) Some(CiderTypeMatcher) else None
  }*/
}

case object PerryTypeMatcher extends DrinkMatcher[Any](DRINK_TYPE_PERRY, "") {

  def apply(drink : T) = {
    drink.isInstanceOf[Perry]
  }

  /*def unapply(matcherType : String) : Option[Matcher[_]] = {
    if (matcherType == matcherId) Some(PerryTypeMatcher) else None
  }*/
}

case class DrinkNameMatcher(drinkName : String) extends DrinkMatcher[String](DRINK_NAME, drinkName) {

  def apply(drink : T) = {
    debug("Matching drink %s to name containing %s".format(drink, matchTo))
    drink.name.toLowerCase contains matchTo.toLowerCase
  }

  /*def unapply(matcherType : String, matchTo : String) : Option[Matcher[_]] = {
    if (matcherType == matcherId) Some(DrinkNameMatcher(matchTo)) else None
  }*/
}

case class DrinkDescriptionMatcher(descriptionWords : String) extends DrinkMatcher[String](DRINK_DESCRIPTION, descriptionWords) {

  def apply(drink : T) = {
    val desc = drink.description.toLowerCase
    val words = matchTo.split(", ")
    val wordMatcher = (matchWord : String) => desc contains matchWord.toLowerCase
    words.map(wordMatcher).find(_ == false).isEmpty
  }

  /*def unapply(matcherType : String, matchTo : String) : Option[Matcher[_]] = {
    if (matcherType == matcherId) Some(DrinkDescriptionMatcher(matchTo)) else None
  }*/
}

case class DrinkPriceMatcher(price : Double) extends DrinkMatcher[Double](DRINK_PRICE, price) {

  def apply(drink : T) = {
    drink.price <= matchTo
  }

  /*def unapply(matcherType : String, matchTo : Double) : Option[Matcher[_]] = {
    if (matcherType == matcherId) Some(DrinkPriceMatcher(matchTo)) else None
  }*/
}

case class DrinkAbvLessThanMatcher(abv : Double) extends DrinkMatcher[Double](DRINK_ABV_LESS_THAN, abv) {

  def apply(drink : T) = drink.abv < matchTo

  /* def unapply(matcherType : String, matchTo : String) : Option[Matcher] = {
    if (matcherType == matcherId) Some(DrinkAbvLessThanMatcher(matchTo)) else None
  }*/
}

case class DrinkAbvGreaterThanMatcher(abv : Double) extends DrinkMatcher[Double](DRINK_ABV_GREATER_THAN, abv) {

  def apply(drink : T) = drink.abv > matchTo

  /*def unapply(matcherType : String, matchTo : String) : Option[Matcher] = {
    if (matcherType == matcherId) Some(DrinkAbvGreaterThanMatcher(matchTo)) else None
  }*/
}

case class DrinkAbvEqualToMatcher(abv : Double) extends DrinkMatcher[Double](DRINK_ABV_EQUAL_TO, abv) {

  def apply(drink : T) = drink.abv == matchTo

  /*  def unapply(matcherType : String, matchTo: String) : Option[Matcher] = {
    if (matcherType == matcherId) Some(DrinkAbvEqualToMatcher(matchTo)) else None
  }*/
}

case class DrinkFeatureMatcher(features : List[DrinkFeature]) extends DrinkMatcher[List[DrinkFeature]](DRINK_HAS_FEATURES, features) {

  def apply(drink : T) = matchTo.map(drink.features.contains(_)).find(_ == false).isEmpty

  /*def unapply(matcherType : String) : Option[Matcher] = {
    if (matcherType == matcherId) Some(DrinkFeatureMatcher) else None
  }*/
}

case class DrinkTypeMatcher(drinkType : String) extends DrinkMatcher[String](DRINK_TYPE, drinkType) {

  def apply(drink : T) = {
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
  /*  def unapply(matcherType : String, matchTo : Class[Drink]) : Option[Matcher] = {
    if (matcherType == matcherId) Some(DrinkTypeMatcher) else None
  }*/
}

case class BrewerNameMatcher(brewerName : String) extends Matcher[String](BREWER_NAME, brewerName) {

  import uk.co.randomcoding.drinkfinder.model.brewer.Brewer

  override type T = Brewer

  def apply(brewer : Brewer) = brewer.name.toLowerCase.contains(matchTo.toLowerCase)

  /*def unapply(matcherType : String, matchTo : String) : Option[Matcher] = {
    if (matcherType == matcherId) Some(BrewerNameMatcher(matchTo)) else None
  }*/
}

case object AlwaysTrueDrinkMatcher extends DrinkMatcher[Any](ALWAYS_TRUE, "") {

  def apply(matchTo : T) = true

  /*def unapply(matchTo : Any) : Option[Matcher] = Some(AlwaysTrueDrinkMatcher)*/
}