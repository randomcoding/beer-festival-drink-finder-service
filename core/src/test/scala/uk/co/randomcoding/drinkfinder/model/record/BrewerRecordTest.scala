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
package uk.co.randomcoding.drinkfinder.model.record

import uk.co.randomcoding.scala.util.lift.mongodb.test.MongoDbTestBase

/**
 * @author RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * Created On: 17 Jul 2012
 */
class BrewerRecordTest extends MongoDbTestBase {
  override val dbName = "BrewerRecordTest"

  test("Equality Properties") {
    val brewer1 = BrewerRecord("Brewer")
    val brewer2 = BrewerRecord("Brewer")
    val brewer3 = BrewerRecord("Brewer")

    brewer1 should (equal(brewer2) and equal(brewer3))
    brewer2 should (equal(brewer1) and equal(brewer3))
    brewer3 should (equal(brewer1) and equal(brewer2))

    brewer1.hashCode should (equal(brewer2.hashCode) and equal(brewer3.hashCode))
  }

  test("Inequality Properties") {
    val brewer1 = BrewerRecord("Brewer 1")
    val brewer2 = BrewerRecord("Brewer 2")
    val brewer3 = BrewerRecord("Brewer 3")

    brewer1 should (not equal (brewer2) and not equal (brewer3))
    brewer2 should (not equal (brewer1) and not equal (brewer3))
    brewer3 should (not equal (brewer1) and not equal (brewer2))

    brewer1.hashCode should (not equal (brewer2.hashCode) and not equal (brewer3.hashCode))
  }

  test("A Brewer Record can be found by name and Id") {
    val brewerName: String = "A Brewer"
    Given("a Brewer Record is added to the database")
    val addedRecord = BrewerRecord.add(BrewerRecord(brewerName))
    addedRecord should be('defined)
    val recordId = addedRecord.get.id.get
    When("The record is queries by id")
    val foundByIdRecord = BrewerRecord.findById(recordId)
    And("the record is found by name")
    val foundByNameRecord = BrewerRecord.findByName(brewerName)
    Then("the same record as was added is found in each case")
    foundByIdRecord should be ('defined)
    foundByIdRecord.get should be(addedRecord.get)
    foundByNameRecord should be ('defined)
    foundByNameRecord.get should be(addedRecord.get)
    And("the two found records are the same")
    foundByIdRecord.get should be(foundByNameRecord.get)
  }
}
