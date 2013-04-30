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
package uk.co.randomcoding.drinkfinder.model.data

import uk.co.randomcoding.drinkfinder.model.record.DrinkRecord
import org.scalatest.matchers.ShouldMatchers

class DummyFestivalData extends FestivalData("Festival", "Test Festival") with ShouldMatchers {

  import uk.co.randomcoding.drinkfinder.model.data.DummyDrinks._

  final def beers = Set(FirstBeer, SecondBeer)

  final def ciders = Set(FirstCider, SecondCider)

  final def perries = Set(FirstPerry, SecondPerry)

  beers.foreach(addDrinkToDb(_))
  ciders.foreach(addDrinkToDb(_))
  perries.foreach(addDrinkToDb(_))

  private[this] def addDrinkToDb(drink: DrinkRecord) {
    addDrink(drink) should be(Some(drink))
  }
}
