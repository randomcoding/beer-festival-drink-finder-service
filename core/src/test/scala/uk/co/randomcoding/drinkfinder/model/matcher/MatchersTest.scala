/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.matcher

import org.scalatest.FunSuite
import uk.co.randomcoding.drinkfinder.model.matcher.id._
import MatcherFactory._
import uk.co.randomcoding.drinkfinder.model.data.DummyDrinks._
import org.scalatest.matchers.ShouldMatchers

import org.scalatest.junit.JUnitRunner

/**
 * @author RandomCoder
 */
class MatchersTest extends FunSuite with ShouldMatchers {
  implicit def MatcherIdToString(id: MatcherId) : String = { id.id }
  
  test("Match to drink name pattern") {
	val queryString = DRINK_NAME + "=First"
	val matchers = generate(queryString)
	matchers should have size (1)
	val matcher = matchers(0)
	matcher(FirstBeer) should be(true)
	matcher(FirstCider) should be(true)
	matcher(FirstPerry) should be(true)
	matcher(SecondBeer) should be(false)
	matcher(SecondCider) should be(false)
	matcher(SecondPerry) should be(false)
  }

  test("Match to drink description pattern") {
	val queryString = DRINK_DESCRIPTION + "=First"
	val matchers = generate(queryString)
	matchers should have size (1)
	val matcher = matchers(0)
	matcher(FirstBeer) should be(true)
	matcher(FirstCider) should be(true)
	matcher(FirstPerry) should be(true)
	matcher(SecondBeer) should be(false)
	matcher(SecondCider) should be(false)
	matcher(SecondPerry) should be(false)
  }

  test("Match to drink price pattern") {

	val queryString = DRINK_PRICE + "=1.50"
	val matchers = generate(queryString)
	matchers should have size (1)
	val matcher = matchers(0)
	matcher(FirstBeer) should be(false)
	matcher(SecondBeer) should be(false)
	matcher(FirstCider) should be(true)
	matcher(SecondCider) should be(false)
	matcher(FirstPerry) should be(true)
	matcher(SecondPerry) should be(true)
  }

  test("Match to drink abv less than pattern") {
	val queryString = DRINK_ABV_LESS_THAN + "=4"
	val matchers = generate(queryString)
	matchers should have size (1)
	val matcher = matchers(0)
	matcher(FirstBeer) should be(false)
	matcher(SecondBeer) should be(true)
	matcher(FirstCider) should be(false)
	matcher(SecondCider) should be(false)
	matcher(FirstPerry) should be(false)
	matcher(SecondPerry) should be(false)
  }

  test("Match to drink abv greater than pattern") {
	val queryString = DRINK_ABV_GREATER_THAN + "=5.2"
	val matchers = generate(queryString)
	matchers should have size (1)
	val matcher = matchers(0)
	matcher(FirstBeer) should be(false)
	matcher(SecondBeer) should be(false)
	matcher(FirstCider) should be(true)
	matcher(SecondCider) should be(true)
	matcher(FirstPerry) should be(true)
	matcher(SecondPerry) should be(false)
  }

  test("Match to drink abv equal to pattern") {
	val queryString = DRINK_ABV_EQUAL_TO + "=7.2"
	val matchers = generate(queryString)
	matchers should have size (1)
	val matcher = matchers(0)
	matcher(FirstBeer) should be(false)
	matcher(SecondBeer) should be(false)
	matcher(FirstCider) should be(false)
	matcher(SecondCider) should be(true)
	matcher(FirstPerry) should be(false)
	matcher(SecondPerry) should be(false)
  }

  test("Match drink with Real Ale feature") {
	pending
  }

  test("Match drink with Medium feature") {
	pending
  }

  test("Match drink with Real Ale feature and price <= 1.70") {
	pending
  }
}