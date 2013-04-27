package uk.co.randomcoding.drinkfinder.model.record

import uk.co.randomcoding.scala.util.lift.mongodb.test.MongoDbTestBase
import uk.co.randomcoding.drinkfinder.model.drink.DrinkType
import uk.co.randomcoding.drinkfinder.query._

/**
 * TODO: Brief Description
 *
 * @author RandomCoder
 */
class DrinkRecordTest extends MongoDbTestBase {
  override val dbName = "DrinkRecordTest"

  test("A Drink Record can be found by Id when Added") {
    Given("a Drink Record that is added to the database")
    val drinkName = "Great Drink"
    val added = DrinkRecord.add(DrinkRecord(drinkName, "A Great Drink", 2.4, 1.3, BrewerRecord("Brewer"), DrinkType.BEER, "festival", Nil))
    added should be('defined)
    val recordId = added.get.id.get
    When("it is queried by id and name")
    val foundById = DrinkRecord.findById(recordId)
    val foundByName = DrinkRecord.find(byName(drinkName))
    Then("the two records found match the original one")
    foundById should be('defined)
    foundById.get should be(added.get)
    foundByName should be('defined)
    foundByName.get should be(added.get)
    And("they are the same as each other")
    foundById.get should be(foundByName.get)
    foundByName.get should be(foundById.get)
  }
}
