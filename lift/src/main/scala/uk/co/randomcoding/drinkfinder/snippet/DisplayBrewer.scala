package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb.common.Logger
import net.liftweb.http._
import net.liftweb.util.Helpers._
import scala.xml.Text
import uk.co.randomcoding.drinkfinder.lib.TransformUtils._
import uk.co.randomcoding.drinkfinder.lib.UserSession
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.matcher.MatcherFactory
import uk.co.randomcoding.drinkfinder.model.matcher.id._

class DisplayBrewer extends Logger {

  def showBrewer = {
    val currentFestivalId = UserSession.currentFestivalId.openTheBox
    //val festivalName = urlDecode(S.param(FESTIVAL_NAME.toString).openOr("Worcester Beer, Cider and Perry Festival"))
    //val festivalName = urlDecode(S.param(FESTIVAL_NAME.toString).openOr("Chappel Beer Festival"))
    val drinkData = FestivalData(currentFestivalId).get
    val brewerName = urlDecode(S.param(BREWER_NAME.toString).openOr("No Brewer"))

    val drinks = drinkData.getMatching(MatcherFactory.generate("%s=%s".format(BREWER_NAME.toString, brewerName))).toList.sortBy(_.name.get)

    "#brewerName" #> Text(brewerName) &
      "#drinks" #> toSummaryDisplay(drinks)
  }
}
