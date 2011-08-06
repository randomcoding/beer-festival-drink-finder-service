/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import org.apache.poi.ss.usermodel.{ WorkbookFactory, Row }
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature
import org.apache.poi.ss.usermodel.Sheet

/**
 * @author RandomCoder
 *
 * Created On: 31 Jul 2011
 */
class DrinkFeatureLoaderTest extends FunSuite with ShouldMatchers {
	val beerFileLocation = "/BeerList-TestData.xls"

	private val loadSheet = (sheetLocation : String) => {
		val wb = WorkbookFactory.create(getClass().getResourceAsStream(sheetLocation))
		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK)
		wb.getSheetAt(0);

	}

	private val beerSheet = loadSheet(beerFileLocation)

	private val featureLoader = new DrinkFeatureLoader()
	private val beerTemplateSource = Source.fromInputStream(getClass().getResourceAsStream("/templates/wbf_template.tpl"))
	private val beerTemplate = new DrinkDataTemplate(beerTemplateSource)

	test("Feature Loader correctly identifies drink features for beers sheet") {
		val testRowResults = Map((7 -> List("Golden", "Bitter")),
			(8 -> List("Brown", "Bitter")),
			(9 -> List("Brown", "Strong")),
			(10, List("Golden", "Best Bitter")),
			(11 -> List("Stout", "Premium")),
			(12 -> List("Brown", "Bitter")),
			(13 -> List("Brown", "Best Bitter")),
			(14 -> List("Brown", "Unusual")),
			(15 -> List("Golden", "Bitter")),
			(16 -> List("Brown", "Best Bitter")),
			(17 -> List("Lager")),
			(18 -> List("Unusual", "Best Bitter")),
			(19 -> List("Organic", "Premium")))

		testRowResults.foreach(
			tuple => {
				val (row, name) = tuple
				featureLoader.drinkFeatures(beerSheet.getRow(row), beerTemplate).sortBy(_.feature) should be(name.map(DrinkFeature(_)).sortBy(_.feature))
			}
		)
	}

	val ciderFileLocation = "/CidersTest.xls"
	private val ciderTemplateSource = Source.fromInputStream(getClass().getResourceAsStream("/templates/wbf_cider_template.tpl"))
	private val ciderTemplate = new DrinkDataTemplate(ciderTemplateSource)
	private val ciderSheet = loadSheet(ciderFileLocation)
	test("Feature loader correctly loads cider") {
		val testRowResults = Map((1 -> "Medium Dry"), (2 -> "Medium Sweet"), (3 -> "Medium"), (4 -> "Dry"))

		testRowResults.foreach(
			tuple => {
				val (row, name) = tuple
				featureLoader.drinkFeatures(ciderSheet.getRow(row), ciderTemplate) should be(List(DrinkFeature(name)))
			}
		)
	}

	val perryFileLocation = "/PerriesTest.xls"
	private val perryTemplateSource = Source.fromInputStream(getClass().getResourceAsStream("/templates/wbf_perry_template.tpl"))
	private val perryTemplate = new DrinkDataTemplate(perryTemplateSource)
	private val perrySheet = loadSheet(perryFileLocation)
	test("Feature loader correctly loads perry features") {
		val testRowResults = Map((1 -> ""), (2 -> ""), (3 -> "Medium"))

		testRowResults.foreach(
			tuple => {
				val (row, name) = tuple
				name match {
					case featureName if featureName.nonEmpty => featureLoader.drinkFeatures(perrySheet.getRow(row), ciderTemplate) should be(List(DrinkFeature(featureName)))
					case _ => featureLoader.drinkFeatures(perrySheet.getRow(row), ciderTemplate) should be(List())
				}
			}
		)
	}
	
	val beers2011FileLocation = "/Beers2011Test.xls"
	private val beer2011TemplateSource = Source.fromInputStream(getClass().getResourceAsStream("/templates/wbf_beer_2011_template.tpl"))
	private val beer2011Template = new DrinkDataTemplate(beer2011TemplateSource)
	private val beer2011Sheet = loadSheet(beers2011FileLocation)
	test("Feature loader correctly loads 2011 Beers") {
		val testRowResults = Map(
				(7 -> List("Mild")),
				(8 -> List("Strong", "Brown")),
				(9 -> List("Golden", "Lager")),
				(10 -> List("Speciality")),
				(11 -> List("Strong")),
				(12 -> List("Golden", "Lager")),
				(13 -> List("Best Bitter", "Brown")),
				(14 -> List("Golden")),
				(15 -> List("Strong", "Dark")),
				(16 -> List("Golden", "Lager"))
		)


		testRowResults.foreach(
			tuple => {
				val (row, name) = tuple
				featureLoader.drinkFeatures(beer2011Sheet.getRow(row), beer2011Template).sortBy(_.feature) should be(name.map(DrinkFeature(_)).sortBy(_.feature))
			}
		)
	}
}