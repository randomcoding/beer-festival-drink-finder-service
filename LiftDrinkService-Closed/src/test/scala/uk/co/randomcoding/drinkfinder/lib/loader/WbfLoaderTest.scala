/**
 * 
 */
package uk.co.randomcoding.drinkfinder.lib.loader

import uk.co.randomcoding.drinkfinder.model.matcher._
import uk.co.randomcoding.drinkfinder.model.brewer.Brewer

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.io.Source


/**
 * @author RandomCoder
 *
 */
@RunWith(classOf[JUnitRunner])
class WbfLoaderTest extends FunSuite with ShouldMatchers {
	val testFileLocation = "/BeerList-TestData.xls"
		
		test("Load Beer Data from sample spreadsheet with loaded source") {
		val loader = new WbfDataLoader()
		println("Created Loader")
		val templateSource = Source.fromInputStream(getClass().getResourceAsStream("/templates/wbf_template.tpl"))
		println("Created template source")
		val template = new DrinkDataTemplate(templateSource)
		println("Created Template")
		val data = loader.loadData(getClass().getResourceAsStream(testFileLocation), template)
		println("Loaded Data")
		var matcher = DrinkNameMatcher("Deception")
		var matched = data.getMatching(List(matcher))
		println("got %d matched drinks: %s".format(matched.size, matched.mkString(",")))
		matched.size should be (1)
		matched.headOption should not be (None)
		var drink = matched.head
		drink.name should be ("Deception")
		drink.abv should be (4.1)
		drink.brewer should be (Brewer("Abbeydale"))
		drink.description should be  ("a very pale blonde beer with Nelson Sauvin hops")
	}
}