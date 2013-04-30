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

import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory._
import uk.co.randomcoding.drinkfinder.model.drink._
import uk.co.randomcoding.drinkfinder.model.drink.DrinkRemainingStatus._
import uk.co.randomcoding.drinkfinder.model.record.BrewerRecord

/**
 * Object to contain all the drinks used in testing
 *
 * User: RandomCoder
 * Date: 22/05/11
 */
object DummyDrinks {
  val RealAle = DrinkFeature("Real Ale")
  val Stout = DrinkFeature("Stout")
  val Dry = DrinkFeature("Dry")
  val Medium = DrinkFeature("Medium")
  val Sweet = DrinkFeature("Sweet")

  val festivalId = "Festival"

  val FirstBeer = beer("First Beer", "The First Beer Brewed", 4.5, 1.70, BrewerRecord("First"), festivalId, RealAle).quantityRemaining(PLENTY)
  val SecondBeer = beer("Second Beer", "The Second Beer Brewed", 3.7, 1.80, BrewerRecord("Second"), festivalId, Stout).quantityRemaining(PLENTY)

  val FirstCider = cider("First Cider", "The First Cider Brewed", 5.7, 1.50, BrewerRecord("First"), festivalId, Dry).quantityRemaining(PLENTY)
  val SecondCider = cider("Second Cider", "The Second Cider Brewed", 7.2, 1.60, BrewerRecord("Second"), festivalId, Medium).quantityRemaining(PLENTY)

  val FirstPerry = perry("First Perry", "The First Perry Brewed", 8.1, 1.40, BrewerRecord("First"), festivalId, Sweet).quantityRemaining(PLENTY)
  val SecondPerry = perry("Second Perry", "The Second Perry Brewed", 4.4, 1.50, BrewerRecord("Second"), festivalId, Medium).quantityRemaining(PLENTY)

  val beers = List(FirstBeer, SecondBeer)
  val ciders = List(FirstCider, SecondCider)
  val perries = List(FirstPerry, SecondPerry)

  val allDrinks = beers ++ ciders ++ perries

  /*
   * Used to handle the creation of a set from a single feature
   */
  private implicit def singleElementToSet(feature: DrinkFeature): Set[DrinkFeature] = Set(feature)
}
