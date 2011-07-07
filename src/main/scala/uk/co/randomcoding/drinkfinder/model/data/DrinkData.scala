/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.data

import uk.co.randomcoding.drinkfinder.model.matcher.DrinkMatcher
import uk.co.randomcoding.drinkfinder.model.drink._
import net.liftweb.common.Logger
import uk.co.randomcoding.drinkfinder.model.brewer.Brewer
import uk.co.randomcoding.drinkfinder.model.brewer.NoBrewer

/**
 * Store of information about drinks and associated data.
 *
 * @author RandomCoder
 *
 */
trait DrinkData extends Logger {

  private var drinks = Set.empty[Drink]
  private var brewers = Set.empty[Brewer]

  def addDrink(drink : Drink) {
    drinks = drinks + drink
    debug("Added %s".format(drink))
  }

  def removeDrink(drink : Drink) {
    debug("Removed %s".format(drink))
    drinks = drinks - drink
  }

  /**
   * Get all the drinks that match all the matchers provided
   */
  def getMatching(matchers : List[DrinkMatcher[_]]) : Set[Drink] = {
    // TODO: This could be done more concisely and more elegantly, possibly with recursion or filtering and joining
    // results.

    val matches = for {
      drink <- drinks
      val drinkMatches = for {
        matcher <- matchers
      } yield {
        matcher(drink)
      }
      if drinkMatches.contains(false) == false
    } yield {
      drink
    }

    matches.toSet
  }
  
  /**
   * Get the brewer with the name or return [[brewer.NoBrewer]] if there is no Brewer with that name.
   */
  def getBrewer(brewerName: String) : Brewer = brewers.find(_.name equals brewerName).getOrElse(NoBrewer)
}