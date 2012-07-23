/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.matcher.id._
import uk.co.randomcoding.drinkfinder.lib.TransformUtils._
import net.liftweb.http._
import net.liftweb.util.Helpers._
import scala.xml.{ NodeSeq, Text }
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.drink.{ Beer, Cider, Drink, Perry, DrinkFeature }
import uk.co.randomcoding.drinkfinder.model.matcher.{ MatcherFactory, BrewerNameMatcher }
import net.liftweb.common.Logger
import uk.co.randomcoding.drinkfinder.model.matcher.AlwaysTrueDrinkMatcher

/**
 * @author RandomCoder
 *
 */
class DisplayResults extends Logger {

  private val beerResultsId = "beerResults"
  private val ciderResultsId = "ciderResults"
  private val perryResultsId = "perryResults"

  def calculateResults = {
    import uk.co.randomcoding.drinkfinder.model.matcher.Matcher

    val params = S.queryString openOr "No Query String"
    //val festivalName = urlDecode(S.param(FESTIVAL_NAME.toString).openOr( "Worcester Beer, Cider and Perry Festival"))
    val festivalId = urlDecode(S.param(FESTIVAL_NAME.toString).openOr("WCBCF"))
    //val festivalName = urlDecode(S.param(FESTIVAL_NAME.toString).openOr( "Chappel Beer Festival"))

    val festivalData = FestivalData(festivalId)
    debug("Received Query String: %s".format(params))

    val matchers = params match {
      case "No Query String" => List(AlwaysTrueDrinkMatcher)
      case paramString: String if paramString.trim.isEmpty => List(AlwaysTrueDrinkMatcher)
      case paramString: String => MatcherFactory.generate(urlDecode(paramString))
    }
    debug("Generated %d matchers:\n%s".format(matchers.size, matchers.mkString("\n\t")))

    val matchingDrinks = festivalData.getMatching(matchers)

    debug("There are %d matching drinks:\n%s".format(matchingDrinks.size, matchingDrinks.mkString("\n\t")))
    val beers = matchingDrinks.filter(_.isInstanceOf[Beer]).toList.sortBy(_.name)
    debug("There are %d matching beers".format(beers.size))
    val ciders = matchingDrinks.filter(_.isInstanceOf[Cider]).toList.sortBy(_.name)
    debug("There are %d matching ciders".format(ciders.size))
    val perries = matchingDrinks.filter(_.isInstanceOf[Perry]).toList.sortBy(_.name)
    debug("There are %d matching perries".format(perries.size))

    "#resultTabs" #> generateResultTabs(beers, ciders, perries) &
      "#beers *" #> toSummaryDisplay(beers) &
      "#ciders *" #> toSummaryDisplay(ciders) &
      "#perries *" #> toSummaryDisplay(perries)
  }

  val anchorRef = ((anchor: String) => "#" + anchor)

  private[this] def generateResultTabs(beers: Iterable[Drink], ciders: Iterable[Drink], perries: Iterable[Drink]): NodeSeq = {
    val beersLink = beers.toList match {
      case Nil => Text("")
      case _ => <li><a href={ anchorRef(beerResultsId) }>Beers ({ beers.size })</a></li>
    }
    val cidersLink = ciders.toList match {
      case Nil => Text("")
      case _ => <li><a href={ anchorRef(ciderResultsId) }>Ciders ({ ciders.size })</a></li>
    }
    val perryLink = perries.toList match {
      case Nil => Text("")
      case _ => <li><a href={ anchorRef(perryResultsId) }>Perries ({ perries.size })</a></li>
    }

    <ul>{ beersLink }{ cidersLink }{ perryLink }</ul>
  }

}