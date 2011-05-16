/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.brewer

import uk.co.randomcoding.drinkfinder.model.drink.Drink

/**
 * Contains information about a brewer, and the drinks they brew
 * 
 * @author RandomCoder
 */
case class Brewer(name : String, region : String) {
	
	private var brewedDrinks : List[Drink] = Nil

	def addDrink(drink : Drink) = brewedDrinks = drink :: brewedDrinks

	def removeDrink(drink : Drink) = brewedDrinks = brewedDrinks.filterNot(_ == drink)
}