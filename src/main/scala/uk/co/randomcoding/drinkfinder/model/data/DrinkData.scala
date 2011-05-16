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
	
	def addDrink(drink: Drink) = drinks = drinks + drink
	
	def remove(drink: Drink) = drinks = drinks - drink
	
	def getMatching(matchers : List[((Drink) => Boolean)]) : Set[Drink] = {
		val matches = for {
			drink <- drinks
			matcher <- matchers
			if matcher(drink)
		}
		yield drink
		
		matches.toSet
	}
}