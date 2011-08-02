/**
 *
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
class FestivalData(val festivalName : String) extends Logger {

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
	def addDrink(drink : Drink) = {
		drinks = drinks + drink

		addDrinkFeatures(drink)
	}

	/**
	 * Remove a drink from the festival data.
	 *
	 * This does not remove the drink's features
	 */
	def removeDrink(drink : Drink) = drinks = drinks - drink

	/**
	 * Add a brewer to the festival's data
	 */
	def addBrewer(brewer : Brewer) = brewers = brewers + brewer

	/**
	 * Remove a brewer from the festival's data
	 */
	def removeBrewer(brewer : Brewer) = brewers = brewers - brewer

	/**
	 * Returns a sorted list of all the features for Beers.
	 */
	def beerFeatures() : List[DrinkFeature] = drinkFeatures.get(BEER) match {
		case None => List.empty
		case Some(features) => features.toList.sortBy(_.displayName)
	}

	/**
	 * Returns a sorted list of all the features for Ciders.
	 */
	def ciderFeatures() : List[DrinkFeature] = drinkFeatures.get(CIDER) match {
		case None => List.empty
		case Some(features) => features.toList.sortBy(_.displayName)
	}

	/**
	 * Returns a sorted list of all the features for Perries.
	 */
	def perryFeatures() : List[DrinkFeature] = drinkFeatures.get(PERRY) match {
		case None => List.empty
		case Some(features) => features.toList.sortBy(_.displayName)
	}

	/**
	 * Get all the drinks that match all the matchers provided
	 */
	def getMatching(matchers : List[DrinkMatcher[_]]) : Set[Drink] = drinks.filterNot(drink => matchers.map(matcher => matcher(drink)).contains(false))

	/**
	 * Get the brewer with the name or return [[brewer.NoBrewer]] if there is no Brewer with that name.
	 */
	def getBrewer(brewerName : String) : Brewer = brewers.find(_.name equals brewerName).getOrElse(NoBrewer)

	private def addDrinkFeatures(drink : Drink) = {
		val typeOfDrink = drinkType(drink)

		drinkFeatures.get(typeOfDrink) match {
			case None => drinkFeatures = drinkFeatures + (typeOfDrink -> MSet(drink.features : _*))
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
	
	def allBrewers() : List[Brewer] = brewers.toList
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
	 * @return If there is no data for the festival then a new, empty one is created with the '''festivalId''' as the ''festivalName'' parameter.
	 */
	def apply(festivalId : FestivalId) : FestivalData = {
		festivals.get(festivalId) match {
			case None => {
				val data = new FestivalData(festivalId)
				festivals = festivals + (festivalId -> data)
				data
			}
			case Some(data) => data
		}
	}
	
	def apply(festivalId: FestivalId, festivalData: FestivalData) = {
		festivals = festivals + (festivalId -> festivalData)
	}
}