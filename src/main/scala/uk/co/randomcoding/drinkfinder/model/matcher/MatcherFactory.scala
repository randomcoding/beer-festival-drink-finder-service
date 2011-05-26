/**
 * TODO: Add License details
 */
package uk.co.randomcoding.drinkfinder.model.matcher

import uk.co.randomcoding.drinkfinder.model.drink.{DrinkFeature, Drink}

/**
 * Factory object to generate matchers from a supplied query string
 *
 * User: RandomCoder
 * Date: 26/05/11
 */

object MatcherFactory {
  type Matcher = (Drink) => Boolean

  def generate(queryString: String): List[Matcher[AnyRef]] = {
	val queryParts = queryString.split("&")
	(for {
	  queryPart <- queryParts
	}
	yield {
	  createMatcher(queryPart.split("="))
	}).toList
  }

  private implicit def arrayTo2Tuple[T](array: Array[T]): (T, T) = (array(0), array(1))

  private def createMatcher(query: (String, String)): Matcher = {
	val queryId = query._1
	val queryValue = query._2

	// TODO: Add rest of matchers
	queryId match {
	  case DrinkNameMatcher(queryId) => DrinkNameMatcher(queryValue)
	  case DrinkDescriptionMatcher(queryId) => DrinkDescriptionMatcher(queryValue)
	  case _ => anyMatcher()
	}
  }

  private def drinkNameMatcher(drinkName: String): Matcher = {
	(drink: Drink) => drink.name.toLowerCase.contains(drinkName.toLowerCase)
  }

  private def drinkDescriptionMatcher(words: List[String]): Matcher = {
	(drink: Drink) => {
	  val desc = drink.description.toLowerCase
	  words.map(desc.contains(_)).find(_ == false).isEmpty
	}
  }

  private def drinkPriceMatcher(price: Double): Matcher = (drink: Drink) => drink.price <= price

  private def drinkAbvLessThanMatcher(abv: Double): Matcher = (drink: Drink) => drink.abv < abv

  private def drinkAbvGreaterThanMatcher(abv: Double): Matcher = (drink: Drink) => drink.abv > abv

  private def drinkAbvEqualToMatcher(abv: Double): Matcher = (drink: Drink) => drink.abv == abv

  private def drinkHasFeaturesMatcher(features: List[DrinkFeature]): Matcher = {
	(drink: Drink) => drink.features.map(features.contains(_)).find(_ == false).isEmpty
  }

  /*private def brewerNameMatcher(brewerName: String): Matcher[Brewer] = {
	(brewer: Brewer) => brewer.name.toLowerCase == brewerName.toLowerCase
  }*/

  private def anyMatcher(): Matcher = (drink: Drink) => true
}
