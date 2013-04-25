/**
 * Copyright (C) 2012 - RandomCoder <randomcoder@randomcoding.co.uk>
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
 *    RandomCoder <randomcoder@randomcoding.co.uk> - initial API and implementation and/or initial documentation
 */
package uk.co.randomcoding.drinkfinder.model.drink

import uk.co.randomcoding.scala.util.lift.mongodb.test.MongoDbTestBase
import uk.co.randomcoding.drinkfinder.model.record.bson.DrinkFeatureRecord

/**
 * @author RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * Created On: 18 Jul 2012
 */
class DrinkFeatureRecordTest extends MongoDbTestBase {
  override val dbName = "DrinkFeatureRecordTest"

  test("Equality Properties") {
    val feature1 = DrinkFeatureRecord("Feature")
    val feature2 = DrinkFeatureRecord("Feature")
    val feature3 = DrinkFeatureRecord("Feature")

    feature1 should (equal(feature2) and equal(feature3))
    feature2 should (equal(feature1) and equal(feature3))
    feature3 should (equal(feature1) and equal(feature2))

    feature1.hashCode should (equal(feature2.hashCode) and equal(feature3.hashCode))
  }

  test("Inequality Properties") {
    val feature1 = DrinkFeatureRecord("Feature 1")
    val feature2 = DrinkFeatureRecord("Feature 2")
    val feature3 = DrinkFeatureRecord("Feature 3")

    feature1 should (not equal (feature2) and not equal (feature3))
    feature2 should (not equal (feature1) and not equal (feature3))
    feature3 should (not equal (feature2) and not equal (feature2))
  }
}
