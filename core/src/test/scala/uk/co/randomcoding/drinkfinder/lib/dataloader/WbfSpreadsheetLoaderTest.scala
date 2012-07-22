/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.model.brewer.Brewer
import uk.co.randomcoding.drinkfinder.model.matcher._
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory._
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature

/**
 * @author RandomCoder
 */
class WbfSpreadsheetLoaderTest extends FunSuite with ShouldMatchers {
  val testFileLocation = "/BeerList-TestData.xls"
  val cidersLocation = "/CidersTest.xls"
  val perriesLocation = "/PerriesTest.xls"
  val wbfFestivalId = "WCBCF"

  private val loader = new SpreadsheetDataLoader()

  private val templateSource = (sourceFile: String) => Source.fromInputStream(getClass().getResourceAsStream(sourceFile))
  private val beerTemplate = new DrinkDataTemplate(templateSource("/templates/wbf_template.tpl"))
  private val ciderTemplate = new DrinkDataTemplate(templateSource("/templates/wbf_cider_template.tpl"))
  private val perryTemplate = new DrinkDataTemplate(templateSource("/templates/wbf_perry_template.tpl"))
  private val data = {
    loader.loadData(getClass().getResourceAsStream(testFileLocation), beerTemplate)
    loader.loadData(getClass().getResourceAsStream(cidersLocation), ciderTemplate)
    loader.loadData(getClass().getResourceAsStream(perriesLocation), perryTemplate)
    FestivalData(beerTemplate.festivalName)
  }

  test("Load Beer Data from sample spreadsheet with loaded source") {
    var matcher = DrinkNameMatcher("deception")
    var matched = data.getMatching(List(matcher))
    println("got %d matched drinks: %s".format(matched.size, matched.mkString(",")))
    matched.size should be(1)
    matched.headOption should not be (None)
    var drink = matched.head
    drink.name should be("Deception")
    drink.abv should be(4.1 plusOrMinus 0.1)
    //drink.price should be(2.4)
    drink.brewer should be(Brewer("Abbeydale"))
    drink.description should be("a very pale blonde beer with Nelson Sauvin hops")
  }

  test("Correct Ciders are loaded ") {
    val cidersMatcher = DrinkTypeMatcher("cider")
    val matched = data.getMatching(List(cidersMatcher))

    matched should have size (4)

    matched.find(_.name.equals("Orchard Bull")).get should be(cider("Orchard Bull", "", 6.5, 0.0, wbfFestivalId, List(DrinkFeature("Medium Dry"))))
    matched.find(_.name.equals("Orchard Bull")).get.brewer should be(Brewer("Ashgrove"))
    matched.find(_.name.equals("Orchard Harvest")).get should be(cider("Orchard Harvest", "", 6.0, 0.0, wbfFestivalId, List(DrinkFeature("Medium Sweet"))))
    matched.find(_.name.equals("Orchard Harvest")).get.brewer should be(Brewer("Ashgrove"))
    matched.find(_.name.equals("Rum Cask")).get should be(cider("Rum Cask", "", 7.3, 0.0, wbfFestivalId, List(DrinkFeature("Medium"))))
    matched.find(_.name.equals("Rum Cask")).get.brewer should be(Brewer("Barbourne"))
    matched.find(_.name.equals("Standard Orchard Blend")).get should be(cider("Standard Orchard Blend", "", 6.0, 0.0, wbfFestivalId, List(DrinkFeature("Dry"))))
    matched.find(_.name.equals("Standard Orchard Blend")).get.brewer should be(Brewer("Barbourne"))
  }

  test("Correct Perries are loaded") {
    val perriesMatcher = DrinkTypeMatcher("perry")
    val matched = data.getMatching(List(perriesMatcher))

    matched should have size (3)

    matched.find(_.name.equals("Barland")).get should be(perry("Barland", "", 7.400000000000001, 0.0, wbfFestivalId, Nil))
    matched.find(_.name.equals("Barland")).get.brewer should be(Brewer("Barbourne"))
    matched.find(_.name.equals("B.U.R.P.")).get should be(perry("B.U.R.P.", "", 6.1, 0.0, wbfFestivalId, Nil))
    matched.find(_.name.equals("B.U.R.P.")).get.brewer should be(Brewer("Barkers"))
    matched.find(_.name.equals("Blakeney Red")).get should be(perry("Blakeney Red", "", 6.5, 0.0, wbfFestivalId, Nil))
    matched.find(_.name.equals("Blakeney Red")).get.brewer should be(Brewer("Brook Farm"))
  }
}