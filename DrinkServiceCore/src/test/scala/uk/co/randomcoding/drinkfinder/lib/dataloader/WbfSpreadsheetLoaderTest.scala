/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.model.brewer.Brewer
import uk.co.randomcoding.drinkfinder.model.matcher.DrinkNameMatcher

/**
 * @author RandomCoder
 *
 */
@RunWith(classOf[org.scalatest.junit.JUnitRunner])
class WbfSpreadsheetLoaderTest extends FunSuite with ShouldMatchers {
	val testFileLocation = "/BeerList-TestData.xls"

	private val loader = new SpreadsheetDataLoader()
	private val templateSource = Source.fromInputStream(getClass().getResourceAsStream("/templates/wbf_template.tpl"))
	private val template = new DrinkDataTemplate(templateSource)
	private val data = loader.loadData(getClass().getResourceAsStream(testFileLocation), template)

	test("Load Beer Data from sample spreadsheet with loaded source") {
		var matcher = DrinkNameMatcher("deception")
		var matched = data.getMatching(List(matcher))
		println("got %d matched drinks: %s".format(matched.size, matched.mkString(",")))
		matched.size should be(1)
		matched.headOption should not be (None)
		var drink = matched.head
		drink.name should be("Deception")
		drink.abv should be (4.1 plusOrMinus 0.1)
		drink.price should be (1.2)
		drink.brewer should be(Brewer("Abbeydale"))
		drink.description should be("a very pale blonde beer with Nelson Sauvin hops")
	}
}