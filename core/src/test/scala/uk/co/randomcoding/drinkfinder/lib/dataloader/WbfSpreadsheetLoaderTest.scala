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
package uk.co.randomcoding.drinkfinder.lib.dataloader

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory._
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature
import uk.co.randomcoding.drinkfinder.model.matcher._
import uk.co.randomcoding.drinkfinder.model.record.BrewerRecord

/**
 * @author RandomCoder
 */
class WbfSpreadsheetLoaderTest extends FunSuite with ShouldMatchers {
  val testFileLocation = "/BeerList-TestData.xls"
  val cidersLocation = "/CidersTest.xls"
  val perriesLocation = "/PerriesTest.xls"
  val festivalId = "WCBCF"

  private val loader = new SpreadsheetDataLoader()

  private val templateSource = (sourceFile: String) => Source.fromInputStream(getClass.getResourceAsStream(sourceFile))
  private val beerTemplate = new DrinkDataTemplate(templateSource("/templates/wbf_template.tpl"))
  private val ciderTemplate = new DrinkDataTemplate(templateSource("/templates/wbf_cider_template.tpl"))
  private val perryTemplate = new DrinkDataTemplate(templateSource("/templates/wbf_perry_template.tpl"))
  private val data = {
    loader.loadData(getClass.getResourceAsStream(testFileLocation), beerTemplate)
    loader.loadData(getClass.getResourceAsStream(cidersLocation), ciderTemplate)
    loader.loadData(getClass.getResourceAsStream(perriesLocation), perryTemplate)
    FestivalData(festivalId, beerTemplate.festivalName)
  }

  test("Load Beer Data from sample spreadsheet with loaded source") {
    val matcher = DrinkNameMatcher("deception")
    val matched = data.getMatching(List(matcher))
    println("got %d matched drinks: %s".format(matched.size, matched.mkString(",")))
    matched.size should be(1)
    matched.headOption should not be (None)
    val drink = matched.head
    drink should be(beer("Deception", "a very pale blonde beer with Nelson Sauvin hops", 4.1, 2.4, BrewerRecord("Abbeydale"), festivalId, Nil))
  }

  test("Correct Ciders are loaded ") {
    val cidersMatcher = DrinkTypeMatcher("cider")
    val matched = data.getMatching(List(cidersMatcher))

    matched should have size (4)

    matched.find(_.name.get.equals("Orchard Bull")).get should be(cider("Orchard Bull", "", 6.5, 0.0, BrewerRecord("Ashgrove"), festivalId, List(DrinkFeature("Medium Dry"))))
    matched.find(_.name.get.equals("Orchard Harvest")).get should be(cider("Orchard Harvest", "", 6.0, 0.0, BrewerRecord("Ashgrove"), festivalId, List(DrinkFeature("Medium Sweet"))))
    matched.find(_.name.get.equals("Rum Cask")).get should be(cider("Rum Cask", "", 7.3, 0.0, BrewerRecord("Barbourne"), festivalId, List(DrinkFeature("Medium"))))
  }

  test("Correct Perries are loaded") {
    val perriesMatcher = DrinkTypeMatcher("perry")
    val matched = data.getMatching(List(perriesMatcher))

    matched should have size (3)

    matched.find(_.name.get.equals("Barland")).get should be(perry("Barland", "", 7.400000000000001, 0.0, BrewerRecord("Barbourne"), festivalId, Nil))
    matched.find(_.name.get.equals("B.U.R.P.")).get should be(perry("B.U.R.P.", "", 6.1, 0.0, BrewerRecord("Barkers"), festivalId, Nil))
    matched.find(_.name.get.equals("Blakeney Red")).get should be(perry("Blakeney Red", "", 6.5, 0.0, BrewerRecord("Brook Farm"), festivalId, Nil))
  }
}
