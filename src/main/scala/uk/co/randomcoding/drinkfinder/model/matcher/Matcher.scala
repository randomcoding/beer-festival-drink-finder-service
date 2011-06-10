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

sealed abstract class Matcher(matcherType: MatcherId) extends Logger {
	type T
	def matcherId: MatcherId = matcherType

	def apply(matchTo : T) : Boolean
}

abstract class DrinkMatcher(matcherType : MatcherId) extends Matcher(matcherType) {
	type T = Drink
}

case object BeerTypeMatcher extends DrinkMatcher(DRINK_TYPE_BEER) {
  def apply(drink: T) = {
    drink.isInstanceOf[Beer]
  }
  
  def unapply(matcherType: String) : Option[Matcher] = {
    if (matcherType == matcherId) Some(BeerTypeMatcher) else None
  }
}

case object CiderTypeMatcher extends DrinkMatcher(DRINK_TYPE_CIDER) {
	def apply(drink: T) = {
		drink.isInstanceOf[Cider]
	}
	
	def unapply(matcherType: String) : Option[Matcher] = {
		if (matcherType == matcherId) Some(CiderTypeMatcher) else None
	}
}

case object PerryTypeMatcher extends DrinkMatcher(DRINK_TYPE_PERRY) {
	def apply(drink: T) = {
		drink.isInstanceOf[Perry]
	}
	
	def unapply(matcherType: String) : Option[Matcher] = {
		if (matcherType == matcherId) Some(PerryTypeMatcher) else None
	}
}

case object DrinkNameMatcher extends DrinkMatcher(DRINK_NAME) {
	def apply(drink: T, matchTo: String) = {
			debug("Matching drink %s to name containing %s".format(drink, matchTo))
			drink.name.toLowerCase contains matchTo.toLowerCase
		}

	def unapply(matcherType: String): Option[Matcher] = {
		if (matcherType == matcherId) Some(DrinkNameMatcher) else None
	}
}

case object DrinkDescriptionMatcher extends DrinkMatcher(DRINK_DESCRIPTION) {
	def apply(drink: T, matchTo: String) = {
		val desc = drink.description.toLowerCase
		val words = matchTo.split(", ")
		val wordMatcher = (matchWord: String) => desc contains matchWord.toLowerCase
		words.map(wordMatcher).find(_ == false).isEmpty
	}

	def unapply(matcherType: String): Option[Matcher] = {
		if (matcherType == matcherId) Some(DrinkDescriptionMatcher) else None
	}
}

case object DrinkPriceMatcher extends DrinkMatcher(DRINK_PRICE) {
	def apply(drink: T, matchTo : Double) = drink.price <= matchTo

	def unapply(matcherType: String): Option[Matcher] = {
		if (matcherType == matcherId) Some(DrinkPriceMatcher) else None
	}
}

case object DrinkAbvLessThanMatcher extends DrinkMatcher(DRINK_ABV_LESS_THAN) {

	def apply(drink: T, matchTo : Double) = drink.abv < matchTo

	def unapply(matcherType: String, matchTo : Double): Option[Matcher] = {
		if (matcherType == matcherId) Some(DrinkAbvLessThanMatcher) else None
	}
}

case object DrinkAbvGreaterThanMatcher extends DrinkMatcher(DRINK_ABV_GREATER_THAN) {

	def apply(drink: T, matchTo : Double) = drink.abv > matchTo

	def unapply(matcherType: String): Option[Matcher] = {
		if (matcherType == matcherId) Some(DrinkAbvGreaterThanMatcher) else None
	}
}

case object DrinkAbvEqualToMatcher extends DrinkMatcher(DRINK_ABV_EQUAL_TO) {

	def apply(drink: T, matchTo : Double) = drink.abv == matchTo

	def unapply(matcherType: String): Option[Matcher] = {
		if (matcherType == matcherId) Some(DrinkAbvEqualToMatcher) else None
	}
}

case object DrinkFeatureMatcher extends DrinkMatcher(DRINK_HAS_FEATURES) {

	def apply(drink: T, matchTo : List[DrinkFeature]) = matchTo.map(drink.features.contains(_)).find(_ == false).isEmpty

	def unapply(matcherType: String): Option[Matcher] = {
		if (matcherType == matcherId) Some(DrinkFeatureMatcher) else None
	}
}

case object DrinkTypeMatcher extends DrinkMatcher(DRINK_TYPE) {
  def apply[C](drink : T) = drink.isInstanceOf[C]
  
  def unapply(matcherType: String, matchTo: Class[Drink]) : Option[Matcher] = {
    if (matcherType == matcherId) Some(DrinkTypeMatcher) else None
  }
}

case object BrewerNameMatcher extends Matcher("brewer.name") {

	import uk.co.randomcoding.drinkfinder.model.brewer.Brewer

	type T = Brewer

	def apply(brewer : Brewer, matchTo: String) = brewer.name.toLowerCase.contains(matchTo.toLowerCase)

	def unapply(matcherType: String, matchTo: String): Option[Matcher] = {
		if (matcherType == matcherId) Some(BrewerNameMatcher) else None
	}
}

case object AlwaysTrueMatcher extends Matcher("empty.matcher") {
	type T = Any

	def apply(matchTo: T) = true

	def unapply(matchTo: Any): Option[Matcher] = Some(AlwaysTrueMatcher)
}