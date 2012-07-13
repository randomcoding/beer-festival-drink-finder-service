/**
 * TODO: Add License details
 */
package uk.co.randomcoding.drinkfinder.model.matcher

import net.liftweb.common.Logger
import id._
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature

/**
 * Factory object to generate matchers from a supplied query string
 *
 * User: RandomCoder
 * Date: 26/05/11
 */

object MatcherFactory extends Logger {

  def generate(queryString : String) : List[DrinkMatcher[_]] = {
    val queryParts = queryString.split("&")
    (for {
      queryPart <- queryParts
    } yield {
      createMatcher(queryPart.split("="))
    }).toList
  }

  private def createMatcher(query : (String, String)) : DrinkMatcher[_] = {
    val (queryId, queryValue) = query

    debug("Query Id: %s, Query Value: %s".format(queryId, queryValue))

    queryId match {
      case DRINK_ABV_EQUAL_TO(queryId) => DrinkAbvEqualToMatcher(queryValue.toDouble)
      case DRINK_ABV_GREATER_THAN(queryId) => DrinkAbvGreaterThanMatcher(queryValue.toDouble)
      case DRINK_ABV_LESS_THAN(queryId) => DrinkAbvLessThanMatcher(queryValue.toDouble)
      case DRINK_DESCRIPTION(queryId) => DrinkDescriptionMatcher(queryValue)
      case DRINK_HAS_FEATURES(queryId) => DrinkFeatureMatcher(DrinkFeature(queryValue))
      case DRINK_NAME(queryId) => DrinkNameMatcher(queryValue)
      case DRINK_PRICE(queryId) => DrinkPriceMatcher(queryValue.toDouble)
      case DRINK_TYPE(queryId) => DrinkTypeMatcher(queryValue)
      case BREWER_NAME(queryId) => BrewerNameMatcher(queryValue)
      case _ => AlwaysTrueDrinkMatcher
    }
  }

  private implicit def arrayTo2Tuple[T](array : Array[T]) : (T, T) = (array(0), array(1))
}
