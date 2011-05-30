package uk.co.randomcoding.drinkfinder.model.matcher

import scala.Some
import MatcherId._
import uk.co.randomcoding.drinkfinder.model.drink.{DrinkFeature, Drink}


/**
 * Enumeration of the types of matchers as case classes with extractors
 *
 * User: RandomCoder
 * Date: 26/05/11
 */

sealed abstract class Matcher(matcherType: String) {
  def matcherId: String = matcherType
}

case object DrinkNameMatcher extends Matcher(DRINK_NAME) {
  def apply(matchTo: String) = (drink: Drink) => drink.name.toLowerCase contains matchTo.toLowerCase

  def unapply(matcherType: String): Option[Matcher] = {
	if (matcherType == matcherId) Some(DrinkNameMatcher) else None
  }
}

case object DrinkDescriptionMatcher extends Matcher(DRINK_DESCRIPTION) {
  def apply(matchTo: String) = (drink: Drink) => {
	val desc = drink.description.toLowerCase
	val words = matchTo.split(", ")
	val wordMatcher = (matchWord: String) => desc contains matchWord.toLowerCase
	words.map(wordMatcher).find(_ == false).isEmpty
  }

  def unapply(matcherType: String): Option[Matcher] = {
	if (matcherType == matcherId) Some(DrinkDescriptionMatcher) else None
  }
}

case object DrinkPriceMatcher extends Matcher(DRINK_PRICE) {
  def apply(matchTo: Double) = (drink: Drink) => drink.price <= matchTo

  def unapply(matcherType: String): Option[Matcher] = {
	if (matcherType == matcherId) Some(DrinkPriceMatcher) else None
  }
}

case object DrinkAbvLessThanMatcher extends Matcher(DRINK_ABV_LESS_THAN) {
  def apply(matchTo: Double) = (drink: Drink) => drink.abv < matchTo

  def unapply(matcherType: String): Option[Matcher] = {
	if (matcherType == matcherId) Some(DrinkAbvLessThanMatcher) else None
  }
}

case object DrinkAbvGreaterThanMatcher extends Matcher(DRINK_ABV_GREATER_THAN) {
  def apply(matchTo: Double) = (drink: Drink) => drink.abv > matchTo

  def unapply(matcherType: String): Option[Matcher] = {
	if (matcherType == matcherId) Some(DrinkAbvGreaterThanMatcher) else None
  }
}

case object DrinkAbvEqualToMatcher extends Matcher(DRINK_ABV_EQUAL_TO) {
  def apply(matchTo: Double) = (drink: Drink) => drink.abv == matchTo

  def unapply(matcherType: String): Option[Matcher] = {
	if (matcherType == matcherId) Some(DrinkAbvEqualToMatcher) else None
  }
}

case object DrinkFeatureMatcher extends Matcher(DRINK_HAS_FEATURES) {
  def apply(matchTo: List[DrinkFeature]) = {
	(drink: Drink) => matchTo.map(drink.features.contains(_)).find(_ == false).isEmpty
  }

  def unapply(matcherType: String): Option[Matcher] = {
	if (matcherType == matcherId) Some(DrinkFeatureMatcher) else None
  }
}

case class BrewerNameMatcher(matchTo: String) extends Matcher("brewer.name") {
  def unapply(matcherType: String, matchTo: String): Option[BrewerNameMatcher] = {
	if (matcherType == matcherId) Some(new BrewerNameMatcher(matchTo)) else None
  }
}

case object AlwaysTrueMatcher extends Matcher("empty.matcher") {
  def apply(any: Any) = (drink: Drink) => true

  def apply() = (drink: Drink) => true

  def unapply(matchTo: String): Option[Matcher] = Some(AlwaysTrueMatcher)
}