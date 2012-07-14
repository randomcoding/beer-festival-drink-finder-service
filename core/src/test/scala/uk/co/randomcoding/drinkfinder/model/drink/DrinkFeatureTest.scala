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