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
import uk.co.randomcoding.drinkfinder.model.brewer.Brewer
import uk.co.randomcoding.drinkfinder.model.matcher._
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFactory._
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature

/**
 * @author RandomCoder
 */
class ChappelSpreadsheetLoaderTest extends FunSuite with ShouldMatchers {
	val testFileLocation = "/ChappelTestData.xls"
	
	private val loader = new SpreadsheetDataLoader()
	
	private val templateSource = (sourceFile : String) => Source.fromInputStream(getClass().getResourceAsStream(sourceFile))
	private val beerTemplate = new DrinkDataTemplate(templateSource("/templates/chappelbeerstemplate.tpl"))
	private val data = {
		loader.loadData(getClass().getResourceAsStream(testFileLocation), beerTemplate)
		FestivalData(beerTemplate.festivalName)
	}

	test("Load Beer Data from sample spreadsheet with loaded source") {
		var matcher = DrinkNameMatcher("chorister")
		var matched = data.getMatching(List(matcher))
		matched.size should be(1)
		matched.headOption should not be (None)
		var drink = matched.head
		drink.name should be("CHORISTER")
		drink.abv should be(4.5 plusOrMinus 0.1)
		drink.brewer should be(Brewer("ABBEY ALES"))
		drink.description should be('empty)
		drink.quantityRemaining should be ("Plenty")
		drink.features should be (List(DrinkFeature("Bar: Resto")))
	}

	
	test("Correct Beers are loaded") {
		val beersMatcher = DrinkTypeMatcher("beer")
		val matched = data.getMatching(List(beersMatcher))

		matched should have size (6)
		
		var drink = matched.find(_.name.equals("CHORISTER")).get 
		drink  should be (beer("CHORISTER", "", 4.5, 0.0, List(DrinkFeature("Bar: Resto"))))
		drink.quantityRemaining should be ("Plenty")
		drink.brewer should be (Brewer("ABBEY ALES"))
		
		drink = matched.find(_.name.equals("KLETSWATER")).get
		drink  should be (beer("KLETSWATER", "", 4.0, 0.0, List(DrinkFeature("Bar: Goods"))))
		drink.quantityRemaining should be ("Not Yet Ready")
		drink.brewer should be (Brewer("ANGLO DUTCH"))
		
		/*drink = matched.find(_.name.equals("ON THE RAILS")).get
		drink should be (beer("ON THE RAILS", "", 3.8, 0.0, List(DrinkFeature("Bar: Resto"))))
		drink.quantityRemaining should be ("All Gone")
		drink.brewer should be (Brewer("B&T"))*/
		
		drink = matched.find(_.name.equals("DUNSTABLE GIANT")).get
		drink should be (beer("DUNSTABLE GIANT", "", 4.4, 0.0, List(DrinkFeature("Bar: Resto"))))
		drink.quantityRemaining should be ("Plenty")
		drink.brewer should be (Brewer("B&T"))
		
		drink = matched.find(_.name.equals("EDWIN TAYLOR'S STOUT")).get
		drink should be (beer("EDWIN TAYLOR'S STOUT", "", 4.5, 0.0, List(DrinkFeature("Bar: Resto"))))
		drink.quantityRemaining should be ("Plenty")
		drink.brewer should be (Brewer("B&T"))
		
		drink = matched.find(_.name.equals("SHEFFORD DARK MILD")).get
		drink should be (beer("SHEFFORD DARK MILD", "", 3.8, 0.0, List(DrinkFeature("Bar: Resto"))))
		drink.quantityRemaining should be ("Plenty")
		drink.brewer should be (Brewer("B&T"))
		
		/*drink = matched.find(_.name.equals("SOS")).get
		drink should be (beer("SOS", "", 5.0, 0.0, List(DrinkFeature("Bar: Resto"))))
		drink.quantityRemaining should be ("All Gone")
		drink.brewer should be (Brewer("B&T"))
		
		drink = matched.find(_.name.equals("BEST BITTER")).get
		drink should be (beer("BEST BITTER", "", 4.2, 0.0, List(DrinkFeature("Bar: Resto"))))
		drink.quantityRemaining should be ("All Gone")
		drink.brewer should be (Brewer("BALLARDS"))*/
		
		drink = matched.find(_.name.equals("GOLDEN BINE")).get
		drink should be (beer("GOLDEN BINE", "", 3.8, 0.0, List(DrinkFeature("Bar: Resto"))))
		drink.quantityRemaining should be ("Plenty")
		drink.brewer should be (Brewer("BALLARDS"))
	}
}