/**
 * Copyright (C) 2012 RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *    RandomCoder - initial API and implementation and/or initial documentation
 */
package uk.co.randomcoding.drinkfinder.model.brewer

import uk.co.randomcoding.drinkfinder.model.drink.Drink

/**
 * Contains information about a brewer, and the drinks they brew
 *
 * @author RandomCoder
 */
@deprecated("Use [[uk.co.randomcoding.drinkfinder.model.brewer.BrewerRecord]] instead", "0.5.0")
case class Brewer(name : String) {

	private var brewedDrinks : List[Drink] = Nil

	def addDrink(drink : Drink) {
    brewedDrinks = drink :: brewedDrinks
  }

	def removeDrink(drink : Drink) {
    brewedDrinks = brewedDrinks.filterNot(_ == drink)
  }
}

object NoBrewer extends Brewer("No Brewer")
