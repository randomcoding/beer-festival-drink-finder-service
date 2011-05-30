/**
 * TODO: Add License details
 */
package uk.co.randomcoding.drinkfinder.model.matcher

import uk.co.randomcoding.drinkfinder.model.drink.Drink

/**
 * Factory object to generate matchers from a supplied query string
 *
 * User: RandomCoder
 * Date: 26/05/11
 */

object MatcherFactory {
  type Matcher = (Drink) => Boolean

  def generate(queryString: String): List[Matcher] = {
	val queryParts = queryString.split("&")
	(for {
	  queryPart <- queryParts
	} yield {
	  createMatcher(queryPart.split("="))
	}).toList
  }

  private def createMatcher(query: (String, String)): Matcher = {
	val (queryId, queryValue) = query

	queryId match {
	  case DrinkNameMatcher(queryId) => DrinkNameMatcher(queryValue)
	  case DrinkDescriptionMatcher(queryId) => DrinkDescriptionMatcher(queryValue)
	  case DrinkPriceMatcher(queryId) => DrinkPriceMatcher(queryValue.toDouble)
	  case DrinkAbvLessThanMatcher(queryId) => DrinkAbvLessThanMatcher(queryValue.toDouble)
	  case DrinkAbvGreaterThanMatcher(queryId) => DrinkAbvGreaterThanMatcher(queryValue.toDouble)
	  case DrinkAbvEqualToMatcher(queryId) => DrinkAbvEqualToMatcher(queryValue.toDouble)
	  case _ => AlwaysTrueMatcher()
	}
  }

  private implicit def arrayTo2Tuple[T](array: Array[T]): (T, T) = (array(0), array(1))
}
