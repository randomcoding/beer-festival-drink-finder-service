/**
 *
 */
package uk.randomcoding.drinkfinder.model.brewer

/**
 * @author RandomCoder
 *
 */
class Brewer(name : String, region : String) {
	import uk.randomcoding.drinkfinder.model.drink.Drink
	
	private var brewedDrinks : List[Drink] = Nil

	def addDrink(drink : Drink) = brewedDrinks = drink :: brewedDrinks

	def removeDrink(drink : Drink) = brewedDrinks = brewedDrinks.filterNot(_ == drink)
}