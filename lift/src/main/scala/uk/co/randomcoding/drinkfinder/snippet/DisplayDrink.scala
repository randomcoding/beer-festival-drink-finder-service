package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder._
import model.drink.{ Drink, NoDrink }
import model.matcher.id._
import model.matcher.DrinkNameMatcher
import lib.TransformUtils._
import model.data.FestivalData
import model.comment.DrinkComments
import net.liftweb.common.{ Full, Logger }
import scala.xml.Text
import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.lib.UserSession

class DisplayDrink extends Logger {

  def displayDrink = {
    val drinkName = urlDecode(S.param(DRINK_NAME.toString).openOr("Unknown Drink"))

    val nameMatcher = DrinkNameMatcher(drinkName)
    "#drinkData" #> toDetailedDisplay(FestivalData(currentFestivalId).get.getMatching(List(nameMatcher)).toList) &
      "#comments" #> "" & //commentDisplay(displayComments(drinkName)) &
      "#addcomment" #> SHtml.link("/addcomment?%s=%s".format(DRINK_NAME, drinkName), () => (), Text("Add Comment"))
  }

  def displayComments(drinkName: String) = {
    val comments = DrinkComments(currentFestivalId)
    comments.commentsForDrink(drinkName)
  }

  private[this] def currentFestivalId = UserSession.currentFestivalId.openTheBox
}
