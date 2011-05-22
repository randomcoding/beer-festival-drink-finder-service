/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.data

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import uk.co.randomcoding.drinkfinder.model.drink.{Perry, Cider, Beer, Drink}

/**
 * Tests for the filter matching capabilities of the Drink Data class/
 *
 * @author RandomCoder
 */
class DrinkDataTest extends FunSuite with ShouldMatchers {
  // TODO: Create test drinks object to contain drinks to test matching
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

  private def checkMatchedDrinks(matched: List[Drink], expected: List[Drink]) = {
	val matchedSize = matched.size

	expected should have size (matchedSize)

	val expectedDrinksContainedInMatched = for {
	  drink <- expected
	}
	yield {
	  matched contains drink
	}

	expectedDrinksContainedInMatched should not contain (false)
  }
}