/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.data

import uk.co.randomcoding.drinkfinder.model.drink._

/**
 * Store of information about drinks and associated data.
 *
 * @author RandomCoder
 *
 */
trait DrinkData {
  private var drinks = Set.empty[Drink]

  def addDrink(drink: Drink) {
	drinks = drinks + drink
  }

  def removeDrink(drink: Drink) {
	drinks = drinks - drink
  }

  /**
   * Get all the drinks that match all the matchers provided
   */
  def getMatching(matchers: List[((Drink) => Boolean)]): Set[Drink] = {
	// TODO: This could be done more concisely and more elegantly, possibly with recursion or filtering and joining
	// results.

	val matches = for {
	  drink <- drinks
	  val drinkMatches = for {
		matcher <- matchers
	  }
	  yield {
		matcher(drink)
	  }
	  if drinkMatches.contains(false) == false
	}
	yield {
	  drink
	}

	matches.toSet
  }
}