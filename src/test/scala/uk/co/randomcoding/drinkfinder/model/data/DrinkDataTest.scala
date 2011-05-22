/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.data

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import uk.co.randomcoding.drinkfinder.model.drink._

/**
 * Tests for the filter matching capabilities of the Drink Data class/
 *
 * @author RandomCoder
 */
class DrinkDataTest extends FunSuite with ShouldMatchers {

  import uk.co.randomcoding.drinkfinder.model.data.DummyDrinks._

  val drinkData = new DummyDrinkData

  test("Query For Only Beers") {
	val matcher = (drink: Drink) => drink.isInstanceOf[Beer]

	val matchingDrinks = drinkData.getMatching(List(matcher))

	checkMatchedDrinks(matchingDrinks.toList, drinkData.beers.toList)
  }

  test("Query For Only Ciders") {
	val matcher = (drink: Drink) => drink.isInstanceOf[Cider]

	val matchingDrinks = drinkData.getMatching(List(matcher))

	checkMatchedDrinks(matchingDrinks.toList, drinkData.ciders.toList)
  }

  test("Query For Only Perries") {
	val matcher = (drink: Drink) => drink.isInstanceOf[Perry]

	val matchingDrinks = drinkData.getMatching(List(matcher))

	checkMatchedDrinks(matchingDrinks.toList, drinkData.perries.toList)
  }

  test("Query by Drink Name") {
	val matcher = (drink: Drink) => drink.name.toLowerCase.contains("first")

	val matchingDrinks = drinkData.getMatching(List(matcher))

	checkMatchedDrinks(matchingDrinks.toList, List(FirstBeer, FirstCider, FirstPerry))
  }

  test("Query description contains") {
	val matcher = (drink: Drink) => drink.description.toLowerCase.contains("tasty")
	checkMatchedDrinks(drinkData.getMatching(List(matcher)).toList, Nil)

	val matcherFirst = (drink: Drink) => drink.description.toLowerCase.contains("first")
	checkMatchedDrinks(drinkData.getMatching(List(matcherFirst)).toList, List(FirstBeer, FirstCider, FirstPerry))
  }

  test("Query Abv Less Than") {
	val abvLessThan45Matcher = (drink: Drink) => drink.abv < 4.5

	checkMatchedDrinks(drinkData.getMatching(List(abvLessThan45Matcher)).toList, List(SecondBeer, SecondPerry))
  }

  test("Query ABV Equal To") {
	val abvEqual45Matcher = (drink: Drink) => drink.abv == 4.5

	checkMatchedDrinks(drinkData.getMatching(List(abvEqual45Matcher)).toList, List(FirstBeer))
  }

  test("Query ABV Greater Than") {
	val abvGreater65Matcher = (drink: Drink) => drink.abv > 6.5

	checkMatchedDrinks(drinkData.getMatching(List(abvGreater65Matcher)).toList, List(SecondCider, FirstPerry))
  }

  test("Query Price Less Than £1.30") {
	val matcher = (drink: Drink) => drink.price < 1.30
	checkMatchedDrinks(drinkData.getMatching(List(matcher)).toList, List(FirstPerry))
  }

  test("Query Price Equal To £1.40") {
	val matcher = (drink: Drink) => drink.price == 1.40
	checkMatchedDrinks(drinkData.getMatching(List(matcher)).toList, List(FirstBeer))
  }

  test("Query Price Greater Than, £1.50") {
	val matcher = (drink: Drink) => drink.price > 1.50
	checkMatchedDrinks(drinkData.getMatching(List(matcher)).toList, List(SecondBeer))
  }

  test("Query for Real Ales") {
	val matcher = (drink: Drink) => drink.features.contains(RealAle)

	checkMatchedDrinks(drinkData.getMatching(List(matcher)).toList, beers)
  }

  test("Query for Sweet Drinks") {
	val matcher = (drink: Drink) => drink.features.contains(Sweet)
	checkMatchedDrinks(drinkData.getMatching(List(matcher)).toList, List(FirstPerry))
  }

  test("Query for Dry Drinks") {
	val matcher = (drink: Drink) => drink.features.contains(Dry)
	checkMatchedDrinks(drinkData.getMatching(List(matcher)).toList, List(FirstCider))
  }

  test("Query for Dry Ciders") {
	val dryMatcher = (drink: Drink) => drink.features.contains(Dry)
	val ciderMatcher = (drink: Drink) => drink.isInstanceOf[Cider]

	checkMatchedDrinks(drinkData.getMatching(List(dryMatcher, ciderMatcher)).toList, List(FirstCider))
  }

  test("Query for Dry Ciders with ABV Less Than 6.0") {
	pending
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