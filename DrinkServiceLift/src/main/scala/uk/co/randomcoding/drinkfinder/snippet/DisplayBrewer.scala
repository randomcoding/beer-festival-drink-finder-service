package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.lib.TransformUtils._
import net.liftweb.common.{ Full, Logger }
import scala.xml.Text
import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.model.matcher.id._
import uk.co.randomcoding.drinkfinder.model.matcher.{ DrinkNameMatcher, BrewerNameMatcher, MatcherFactory }

class DisplayBrewer extends Logger {

	def showBrewer = {

		//val festivalName = urlDecode(S.param(FESTIVAL_NAME.toString).openOr("Worcester Beer, Cider and Perry Festival"))
		val festivalName = urlDecode(S.param(FESTIVAL_NAME.toString).openOr("Chappel Beer Festival"))
		val drinkData = FestivalData(festivalName)
		val brewerName = urlDecode(S.param(BREWER_NAME.toString).openOr("No Brewer"))

		val drinks = drinkData.getMatching(MatcherFactory.generate("%s=%s".format(BREWER_NAME.toString, brewerName))).toList.sortBy(_.name)

		"#brewerName" #> Text(brewerName) &
			"#drinks" #> toSummaryDisplay(drinks)
	}
}