package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.drink.Drink
import uk.co.randomcoding.drinkfinder.model.drink.NoDrink
import net.liftweb.common.{Full, Logger}
import scala.xml.Text
import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.model.matcher.id._
import uk.co.randomcoding.drinkfinder.model.matcher.DrinkNameMatcher
import uk.co.randomcoding.drinkfinder.lib.TransformUtils._
import uk.co.randomcoding.drinkfinder.model.data.FestivalData

class DisplayDrink extends StatefulSnippet with Logger {
	
	def dispatch = {case "displayDrink" => displayDrink}
	
	def displayDrink = {
	  val drinkName = urlDecode( S.param(DRINK_NAME.toString).openOr("Unknown Drink"))
	  val festivalName = urlDecode(S.param(FESTIVAL_NAME.toString).openOr("Worcester Beer, Cider and Perry Festival"))
	  val nameMatcher = DrinkNameMatcher(drinkName)
	  "#drinkData" #> toDetailedDisplay(List(FestivalData(festivalName).getMatching(List(nameMatcher)).headOption.getOrElse(NoDrink)))
	}
}