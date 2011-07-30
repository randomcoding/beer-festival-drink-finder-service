/**
 * TODO: Add License details
 */
package uk.co.randomcoding.drinkfinder.model.data

import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory._
import uk.co.randomcoding.drinkfinder.model.drink._

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

	val FirstBeer = beer("First Beer", "The First Beer Brewed", 4.5, 1.70, RealAle)
	val SecondBeer = beer("Second Beer", "The Second Beer Brewed", 3.7, 1.80, Stout)

	val FirstCider = cider("First Cider", "The First Cider Brewed", 5.7, 1.50, Dry)
	val SecondCider = cider("Second Cider", "The Second Cider Brewed", 7.2, 1.60, Medium)

	val FirstPerry = perry("First Perry", "The First Perry Brewed", 8.1, 1.40, Sweet)
	val SecondPerry = perry("Second Perry", "The Second Perry Brewed", 4.4, 1.50, Medium)

	val beers = List(FirstBeer, SecondBeer)
	val ciders = List(FirstCider, SecondCider)
	val perries = List(FirstPerry, SecondPerry)

	val allDrinks = beers ++ ciders ++ perries

	/*
   * Used to handle the creation of a set from a single feature
   */
	private implicit def singleElementToSet(feature : DrinkFeature) : Set[DrinkFeature] = Set(feature)
}