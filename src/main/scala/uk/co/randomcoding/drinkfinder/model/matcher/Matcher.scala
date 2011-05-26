package uk.co.randomcoding.drinkfinder.model.matcher

import scala.Some
import uk.co.randomcoding.drinkfinder.model.drink.Drink

/**
 * Enumeration of the types of matchers as case classes with extractors
 *
 * User: RandomCoder
 * Date: 26/05/11
 */

sealed abstract case class Matcher(matcherId: String)

case object DrinkNameMatcher extends Matcher("drink.name") {
  def apply(matchTo: String) = (drink: Drink) => drink.name.toLowerCase == matchTo.toLowerCase

  def unapply(matcherType: String): Option[Matcher] = {
	if (matcherType == matcherId) Some(DrinkNameMatcher) else None
  }
}

case object DrinkDescriptionMatcher extends Matcher("drink.description") {
  def apply(matchTo: String) = (drink: Drink) => {
	val desc = drink.description.toLowerCase
	matchTo.split(", ").map((matchWord: String) => desc.contains(matchWord.toLowerCase)).find(_ == false)
	  .isEmpty
  }

  def unapply(matcherType: String, matchTo: String): Option[Matcher] = {
	if (matcherType == matcherId) Some(DrinkDescriptionMatcher) else None
  }
}

// TODO: Continue creation of objects
case class DrinkPriceMatcher(matchTo: Double) extends Matcher("drink.price") {
  def unapply(matcherType: String, matchTo: String): Option[DrinkPriceMatcher] = {
	if (matcherType == matcherId) Some(new DrinkPriceMatcher(matchTo)) else None
  }
}

case class DrinkAbvLessThan(matchTo: Double) extends Matcher("drink.abv.lessthan") {
  def unapply(matcherType: String, matchTo: String): Option[DrinkAbvLessThan] = {
	if (matcherType == matcherId) Some(new DrinkAbvLessThan(matchTo)) else None
  }
}

case class DrinkAbvGreaterThan(matchTo: Double) extends Matcher("drink.abv.greaterthan") {
  def unapply(matcherType: String, matchTo: String): Option[DrinkAbvGreaterThan] = {
	if (matcherType == matcherId) Some(new DrinkAbvGreaterThan(matchTo)) else None
  }
}

case class DrinkAbvEqualTo(matchTo: Double) extends Matcher("drink.abv.equalto") {
  def unapply(matcherType: String, matchTo: String): Option[DrinkAbvEqualTo] = {
	if (matcherType == matcherId) Some(new DrinkAbvEqualTo(matchTo)) else None
  }
}

case class BrewerNameMatcher(matchTo: String) extends Matcher("brewer.name") {
  def unapply(matcherType: String, matchTo: String): Option[BrewerNameMatcher] = {
	if (matcherType == matcherId) Some(new BrewerNameMatcher(matchTo)) else None
  }
}

case class EmptyMatcher(matchTo: String) extends Matcher("empty.matcher") {
  def unapply(matchTo: String): Option[EmptyMatcher] = Some(new EmptyMatcher(matchTo))
}