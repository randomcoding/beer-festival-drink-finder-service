/**
 * TODO: Add License details
 */
package uk.co.randomcoding.drinkfinder.model.matcher

import scala.Enumeration
import net.liftweb.common.Logger
import MatcherId._

/**
 * Factory object to generate matchers from a supplied query string
 *
 * User: RandomCoder
 * Date: 26/05/11
 */

object MatcherFactory extends Logger {

  def generate(queryString : String) : List[Matcher] = {
    val queryParts = queryString.split("&")
    (for {
      queryPart <- queryParts
    } yield {
      createMatcher(queryPart.split("="))
    }).toList
  }

  private def createMatcher(query : (String, String)) : Matcher = {
    val (queryId, queryValue) = query

    debug("Query Id: %s, Query Value: %s".format(queryId, queryValue))

    queryId match {
      case DRINK_ABV_EQUAL_TO .toString=> DrinkAbvEqualToMatcher
      case DRINK_ABV_GREATER_THAN => DrinkAbvGreaterThanMatcher
      case DRINK_ABV_LESS_THAN => DrinkAbvLessThanMatcher
      case DRINK_DESCRIPTION => DrinkDescriptionMatcher
      case DRINK_HAS_FEATURES => DrinkFeatureMatcher
      case DRINK_NAME => DrinkNameMatcher
      case DRINK_PRICE => DrinkPriceMatcher
      case DRINK_TYPE => DrinkTypeMatcher
      case DRINK_TYPE_BEER => BeerTypeMatcher
      case DRINK_TYPE_CIDER => CiderTypeMatcher
      case DRINK_TYPE_PERRY => PerryTypeMatcher
      case _ => AlwaysTrueMatcher
    }
  }

  private implicit def arrayTo2Tuple[T](array : Array[T]) : (T, T) = (array(0), array(1))
}
