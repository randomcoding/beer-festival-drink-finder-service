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

import uk.co.randomcoding.drinkfinder.model.matcher._
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import uk.co.randomcoding.drinkfinder.model.drink._

import org.scalatest.junit.JUnitRunner

/**
 * Tests for the filter matching capabilities of the Drink Data class/
 *
 * @author RandomCoder
 */
class FestivalDataTest extends FunSuite with ShouldMatchers {

  import DummyDrinks._

  val festivalData = new DummyFestivalData

  test("Query For Only Beers") {
	val matcher = DrinkTypeMatcher("beer")

	val matchingDrinks = festivalData.getMatching(List(matcher))

	checkMatchedDrinks(matchingDrinks.toList, festivalData.beers.toList)
  }

  test("Query For Only Ciders") {
	  val matcher = DrinkTypeMatcher("cider")

	val matchingDrinks = festivalData.getMatching(List(matcher))

	checkMatchedDrinks(matchingDrinks.toList, festivalData.ciders.toList)
  }

  test("Query For Only Perries") {
	  val matcher = DrinkTypeMatcher("perry")

	val matchingDrinks = festivalData.getMatching(List(matcher))

	checkMatchedDrinks(matchingDrinks.toList, festivalData.perries.toList)
  }

  test("Query by Drink Name") {
	val matcher = DrinkNameMatcher("first")

	val matchingDrinks = festivalData.getMatching(List(matcher))

	checkMatchedDrinks(matchingDrinks.toList, List(FirstBeer, FirstCider, FirstPerry))
  }

  test("Query description contains") {
	val matcher = DrinkDescriptionMatcher("tasty")
	checkMatchedDrinks(festivalData.getMatching(List(matcher)).toList, Nil)

	val matcherFirst = DrinkDescriptionMatcher("first")
	checkMatchedDrinks(festivalData.getMatching(List(matcherFirst)).toList, List(FirstBeer, FirstCider, FirstPerry))
  }

  test("Query Abv Less Than") {
	val abvLessThan45Matcher = DrinkAbvLessThanMatcher(4.5)

	checkMatchedDrinks(festivalData.getMatching(List(abvLessThan45Matcher)).toList, List(SecondBeer, SecondPerry))
  }

  test("Query ABV Equal To") {
	val abvEqual45Matcher = DrinkAbvEqualToMatcher(4.5)

	checkMatchedDrinks(festivalData.getMatching(List(abvEqual45Matcher)).toList, List(FirstBeer))
  }

  test("Query ABV Greater Than") {
	val abvGreater65Matcher = DrinkAbvGreaterThanMatcher(6.5)

	checkMatchedDrinks(festivalData.getMatching(List(abvGreater65Matcher)).toList, List(SecondCider, FirstPerry))
  }

  test("Query Price Less Than or equal to £1.50") {
	val matcher = DrinkPriceMatcher(1.50)
	checkMatchedDrinks(festivalData.getMatching(List(matcher)).toList, List(FirstCider, FirstPerry, SecondPerry))
  }

  test("Query for Real Ales") {
	val matcher = DrinkFeatureMatcher(RealAle)

	checkMatchedDrinks(festivalData.getMatching(List(matcher)).toList, List(FirstBeer))
  }

  test("Query for Sweet Drinks") {
	val matcher = DrinkFeatureMatcher(Sweet)
	checkMatchedDrinks(festivalData.getMatching(List(matcher)).toList, List(FirstPerry))
  }

  test("Query for Dry Drinks") {
	val matcher = DrinkFeatureMatcher(Dry)
	checkMatchedDrinks(festivalData.getMatching(List(matcher)).toList, List(FirstCider))
  }

  test("Query for Dry Ciders") {
	val dryMatcher = DrinkFeatureMatcher(Dry)
	val ciderMatcher = DrinkTypeMatcher("cider")

	checkMatchedDrinks(festivalData.getMatching(List(dryMatcher, ciderMatcher)).toList, List(FirstCider))
  }

  test("Query for Medium Ciders with ABV Greater Than 7.0") {
	val ciderMatcher = DrinkTypeMatcher("cider")
	val mediumMatcher = DrinkFeatureMatcher(Medium)
	val abvMatcher = DrinkAbvGreaterThanMatcher(7.0)

	checkMatchedDrinks(festivalData.getMatching(List(ciderMatcher, mediumMatcher, abvMatcher)).toList, List(SecondCider))
  }
  
  test("Beer features are added correctly") {
	  festivalData.beerFeatures should be (List(DrinkFeature("Real Ale"), DrinkFeature("Stout")))
  }
  
  test("Cider features are added correctly") {
	  festivalData.ciderFeatures should be (List(DrinkFeature("Dry"), DrinkFeature("Medium")))
  }
  
  test("Perry features are added correctly") {
	  festivalData.perryFeatures should be (List(DrinkFeature("Medium"), DrinkFeature("Sweet")))
  }
  
  private def checkMatchedDrinks(matched: List[Drink], expected: List[Drink]) {
	val expectedSize = expected.size

	matched should have size (expectedSize)

	val expectedDrinksContainedInMatched = for {
	  drink <- expected
	}
	yield {
	  matched contains drink
	}

	expectedDrinksContainedInMatched should not contain (false)
  }
}