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
protected class FestivalData(val festivalName : String) extends Logger {

	private val BEER = "BEER"
	private val CIDER = "CIDER"
	private val PERRY = "PERRY"

	private var drinks = Set.empty[Drink]
	private var brewers = Set.empty[Brewer]
	private var drinkFeatures = Map.empty[String, MSet[DrinkFeature]]

	def addDrink(drink : Drink) = drinks = drinks + drink

	def removeDrink(drink : Drink) = drinks = drinks - drink

	def addBrewer(brewer : Brewer) = brewers = brewers + brewer

	def removeBrewer(brewer : Brewer) = brewers = brewers - brewer
	
	def addBeerFeature(feature: DrinkFeature) = addFeature(feature, BEER)
	
	def addCiderFeature(feature: DrinkFeature) = addFeature(feature, CIDER)
	
	def addPerryFeature(feature: DrinkFeature) = addFeature(feature, PERRY)
	
	def removeBeerFeature(feature: DrinkFeature) = removeFeature(feature, BEER)
	
	def removeCiderFeature(feature: DrinkFeature) = removeFeature(feature, CIDER)
	
	def removePerryFeature(feature: DrinkFeature) = removeFeature(feature, PERRY)

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
	def getBrewer(brewerName : String) : Brewer = brewers.find(_.name equals brewerName).getOrElse(NoBrewer)
	
	private def addFeature(feature : DrinkFeature, drinkType: String) = {
		drinkFeatures.get(drinkType) match {
			case None => drinkFeatures = drinkFeatures + (drinkType-> MSet(feature))
			case Some(_) => 	drinkFeatures(drinkType) += feature
		}
	}

	private def removeFeature(feature : DrinkFeature, drinkType: String) = {
		drinkFeatures.get(drinkType) match {
			case None => // nothing to do
			case Some(_) => 	drinkFeatures(drinkType) -= feature
		}
	}
}

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
}