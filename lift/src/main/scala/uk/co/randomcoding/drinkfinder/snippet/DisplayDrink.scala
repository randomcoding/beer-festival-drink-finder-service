package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb.common.Logger
import net.liftweb.http._
import net.liftweb.util.Helpers._
import scala.xml.Text
import uk.co.randomcoding.drinkfinder.lib.TransformUtils._
import uk.co.randomcoding.drinkfinder.lib.UserSession
import uk.co.randomcoding.drinkfinder.model.comment.DrinkComments
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.matcher.DrinkNameMatcher
import uk.co.randomcoding.drinkfinder.model.matcher.id.DRINK_NAME

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
