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
		val testRowResults = Map((7 -> "Golden"),
			(8 -> "Brown"),
			(9 -> "Brown"),
			(10, "Golden"),
			(11 -> "Stout"),
			(12 -> "Brown"),
			(13 -> "Brown"),
			(14 -> "Brown"),
			(15 -> "Golden"),
			(16 -> "Brown"),
			(17 -> "Lager"),
			(18 -> "Unusual"),
			(19 -> "Organic"))

		testRowResults.foreach(tuple => {
			val (row, name) = tuple
			featureLoader.drinkFeatures(beerSheet.getRow(row), beerTemplate) should be(List(DrinkFeature(name)))
		}
		)
	}

	val ciderFileLocation = "/CidersTest.xls"
	private val ciderTemplateSource = Source.fromInputStream(getClass().getResourceAsStream("/templates/wbf_cider_template.tpl"))
	private val ciderTemplate = new DrinkDataTemplate(ciderTemplateSource)
	private val ciderSheet = loadSheet(ciderFileLocation)
	test("Feature loader correctly loads cider") {
		val testRowResults = Map((1 -> "Medium Dry"), (2 -> "Medium Sweet"), (3 -> "Medium"), (4 -> "Dry"))

		testRowResults.foreach(tuple => {
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

		testRowResults.foreach(tuple => {
			val (row, name) = tuple
			name match {
				case featureName if featureName.nonEmpty => featureLoader.drinkFeatures(perrySheet.getRow(row), ciderTemplate) should be(List(DrinkFeature(featureName)))
				case _ => featureLoader.drinkFeatures(perrySheet.getRow(row), ciderTemplate) should be(List())
			}

		}
		)

	}
}