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

import scala.collection.mutable.{ Set => MSet }
import uk.co.randomcoding.drinkfinder.model.matcher.DrinkMatcher
import uk.co.randomcoding.drinkfinder.model.drink._
import net.liftweb.common.Logger
import uk.co.randomcoding.drinkfinder.model.brewer.Brewer
import uk.co.randomcoding.drinkfinder.model.brewer.NoBrewer
import uk.co.randomcoding.drinkfinder.model.matcher.BrewerNameMatcher

/**
 * Store of information about drinks and associated data.
 *
 * @author RandomCoder
 */
class FestivalData(val festivalId: String, val festivalName: String) extends Logger {

  private val BEER = "BEER"
  private val CIDER = "CIDER"
  private val PERRY = "PERRY"

  private var drinks = Set.empty[Drink]
  private var brewers = Set.empty[Brewer]
  private var drinkFeatures = Map.empty[String, MSet[DrinkFeature]]

  /**
   * Adds the given drink to the Festival's drinks list.
   *
   * This also updates the features for the drink type by adding the drink's features to the feature set for its type
   */
  def addDrink(drink: Drink) = {
    drinks.contains(drink) match {
      case false => addNewDrink(drink)
      case true => updateDrink(drink)
    }
  }

  /**
   * Remove a drink from the festival data.
   *
   * This does not remove the drink's features
   */
  def removeDrink(drink: Drink) = drinks = drinks - drink

  /**
   * Add a brewer to the festival's data
   */
  def addBrewer(brewer: Brewer) = brewers = brewers + brewer

  /**
   * Remove a brewer from the festival's data
   */
  def removeBrewer(brewer: Brewer) = brewers = brewers - brewer

  /**
   * Returns a sorted list of all the features for Beers.
   */
  def beerFeatures(): List[DrinkFeature] = drinkFeatures.get(BEER) match {
    case None => List.empty
    case Some(features) => features.toList.sortBy(_.displayName)
  }

  /**
   * Returns a sorted list of all the features for Ciders.
   */
  def ciderFeatures(): List[DrinkFeature] = drinkFeatures.get(CIDER) match {
    case None => List.empty
    case Some(features) => features.toList.sortBy(_.displayName)
  }

  /**
   * Returns a sorted list of all the features for Perries.
   */
  def perryFeatures(): List[DrinkFeature] = drinkFeatures.get(PERRY) match {
    case None => List.empty
    case Some(features) => features.toList.sortBy(_.displayName)
  }

  def allDrinks = drinks
  /**
   * Get all the drinks that match all the matchers provided that are not '''All Gone'''
   */
  def getMatching(matchers: List[DrinkMatcher[_]]): Set[Drink] = drinks.filter(drink => (drinkRemaining(drink) && matchesAll(drink, matchers)))

  /**
   * Get the brewer with the name or return [[brewer.NoBrewer]] if there is no Brewer with that name.
   */
  def getBrewer(brewerName: String): Brewer = brewers.find(_.name equals brewerName).getOrElse(NoBrewer)

  /**
   * Accessor for a list of all the brewers stored in this FesitvalData
   */
  def allBrewers(): List[Brewer] = brewers.toList

  private def drinkRemaining(drink: Drink): Boolean = drink.quantityRemaining.toLowerCase != "all gone"

  private def matchesAll(drink: Drink, matchers: List[DrinkMatcher[_]]): Boolean = {
    matchers.filterNot(_.apply(drink)).isEmpty
  }

  private def addDrinkFeatures(drink: Drink) = {
    val typeOfDrink = drinkType(drink)

    drinkFeatures.get(typeOfDrink) match {
      case None => drinkFeatures = drinkFeatures + (typeOfDrink -> MSet(drink.features: _*))
      case Some(currentFeats) => drinkFeatures = drinkFeatures + (typeOfDrink -> (currentFeats ++ drink.features))
    }
  }

  private def drinkType(drink: Drink) = {
    drink.getClass.getSimpleName match {
      case "Beer" => BEER
      case "Cider" => CIDER
      case "Perry" => PERRY
    }
  }

  private def addNewDrink(drink: Drink) = {
    drinks = drinks + drink
    addDrinkFeatures(drink)
  }

  private def updateDrink(drink: Drink) = {
    val currentDrink = drinks.find(_.name.equals(drink.name)).get

    if (currentDrink.quantityRemaining != drink.quantityRemaining) {
      debug("Quantity is different (current: %s -> new: %s)".format(currentDrink.quantityRemaining, drink.quantityRemaining))
      currentDrink.quantityRemaining = drink.quantityRemaining
    }
  }
}

/**
 * This is the accessor object for the Festival Data for any festival.
 *
 * '''''All''''' getting of a festival data should be done through this Object.
 */
object FestivalData {
  type FestivalId = String

  private var festivals = Map.empty[FestivalId, FestivalData]

  /**
   * Get the festival data for a given festival id.
   *
   * @return If there is no data for the festival then a new, empty one is created with the '''festivalName''' as the ''festivalName'' parameter.
   */
  def apply(festivalId: String, festivalName: String): FestivalData = {
    festivals.get(festivalId) match {
      case Some(data) => data
      case _ => {
        val data = new FestivalData(festivalId, festivalName)
        festivals = festivals + (festivalId -> data)
        data
      }
    }
  }

  /**
   * Get the festival data for a given festival id.
   *
   * @return If there is no data for the festival then returns `None`.
   */
  //@deprecated("Use FestivalData(String, String) instead.", "0.5.0")
  def apply(festivalId: FestivalId): Option[FestivalData] = festivals.get(festivalId)

  def apply(festivalId: FestivalId, festivalData: FestivalData) = {
    festivals = festivals + (festivalId -> festivalData)
  }
}