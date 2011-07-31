/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.dataloader

import org.apache.poi.ss.usermodel.{WorkbookFactory, Row}
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import uk.co.randomcoding.drinkfinder.lib.dataloader.template.DrinkDataTemplate
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature

/**
 * @author RandomCoder
 *
 * Created On: 31 Jul 2011
 */
class DrinkFeatureLoaderTest extends FunSuite with ShouldMatchers {
	val testFileLocation = "/BeerList-TestData.xls"

	private val sheet = {
		val wb = WorkbookFactory.create(getClass().getResourceAsStream(testFileLocation))
		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK)
		wb.getSheetAt(0);
	}

	private val featureLoader = new DrinkFeatureLoader()
	private val templateSource = Source.fromInputStream(getClass().getResourceAsStream("/templates/wbf_template.tpl"))
	private val template = new DrinkDataTemplate(templateSource)

	test("Feature Loader correctly identifies drink features") {
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
			featureLoader.drinkFeatures(sheet.getRow(row), template) should be(List(DrinkFeature(name)))
		}
		)
	}
}