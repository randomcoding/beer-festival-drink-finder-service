/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.drink

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

/**
 * @author RandomCoder
 *
 */
class DrinkFeatureTest extends FunSuite with ShouldMatchers {
	test("Feature String is correctly converted to First Letter Upper Case for display name") {
		DrinkFeature("Beer").displayName should be("Beer")
		DrinkFeature("beer").displayName should be("Beer")
		DrinkFeature("Golden beer").displayName should be("Golden Beer")
		DrinkFeature("golden beer").displayName should be("Golden Beer")
	}

	test("Drink Feature Equality is correctly ignore case") {
		DrinkFeature("Beer") should (equal(DrinkFeature("beer"))
			and equal(DrinkFeature("BEER"))
			and equal(DrinkFeature("bEeR")))
	}
}