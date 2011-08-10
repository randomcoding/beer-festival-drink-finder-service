package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder._
import model.drink.{Drink, NoDrink}
import model.matcher.id._
import model.matcher.DrinkNameMatcher
import lib.TransformUtils._
import model.data.FestivalData
import model.comment.DrinkComments
import net.liftweb.common.{Full, Logger}
import scala.xml.Text
import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.util.Helpers._

class DisplayDrink extends Logger {
	
	def displayDrink = {
	  val drinkName = urlDecode( S.param(DRINK_NAME.toString).openOr("Unknown Drink"))
	  val festivalName = urlDecode(S.param(FESTIVAL_NAME.toString).openOr("Worcester Beer, Cider and Perry Festival"))
	  val nameMatcher = DrinkNameMatcher(drinkName)
	  "#drinkData" #> toDetailedDisplay(List(FestivalData(festivalName).getMatching(List(nameMatcher)).headOption.getOrElse(NoDrink))) &
	  "#comments" #> commentDisplay(DrinkComments.commentsForDrink(drinkName)) &
	  "#addcomment" #> SHtml.link("/addcomment?%s=%s".format(DRINK_NAME, drinkName), () => (), Text("Add Comment"))
	}
}