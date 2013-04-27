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
package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb.common.{Full, Logger}
import net.liftweb.http._
import net.liftweb.util.Helpers._
import scala.xml.NodeSeq
import scala.xml.Text
import uk.co.randomcoding.drinkfinder.lib.TransformUtils._
import uk.co.randomcoding.drinkfinder.lib.UserSession
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.drink._
import uk.co.randomcoding.drinkfinder.model.matcher.{AlwaysTrueDrinkMatcher, MatcherFactory}
import uk.co.randomcoding.drinkfinder.model.record.DrinkRecord

/**
 * @author RandomCoder
 */
class DisplayResults extends Logger {

  private val beerResultsId = "beerResults"
  private val ciderResultsId = "ciderResults"
  private val perryResultsId = "perryResults"

  def calculateResults = {

    val params = S.queryString openOr "No Query String"
    val currentFestivalId = UserSession.currentFestivalId.is match {
      case Full(id) => id
      case _ => "" // TODO: Throw error?
    }

    val festivalData = FestivalData(currentFestivalId).get
    debug("Received Query String: %s".format(params))
    debug("Data has %d drinks".format(festivalData.allDrinks.size))

    val matchers = params match {
      case "No Query String" => List(AlwaysTrueDrinkMatcher)
      case paramString: String if paramString.trim.isEmpty => List(AlwaysTrueDrinkMatcher)
      case paramString: String => MatcherFactory.generate(urlDecode(paramString))
    }
    debug("Generated %d matchers:\n%s".format(matchers.size, matchers.mkString("\n\t")))

    val matchingDrinks = festivalData.getMatching(matchers)

    debug("There are %d matching drinks:\n%s".format(matchingDrinks.size, matchingDrinks.mkString("\n\t")))
    val beers = matchingDrinks.filter(isBeer).toList.sortBy(_.name.get)
    debug("There are %d matching beers".format(beers.size))
    val ciders = matchingDrinks.filter(isCider).toList.sortBy(_.name.get)
    debug("There are %d matching ciders".format(ciders.size))
    val perries = matchingDrinks.filter(isPerry).toList.sortBy(_.name.get)
    debug("There are %d matching perries".format(perries.size))

    "#resultTabs" #> generateResultTabs(beers, ciders, perries) &
      "#beers *" #> toSummaryDisplay(beers) &
      "#ciders *" #> toSummaryDisplay(ciders) &
      "#perries *" #> toSummaryDisplay(perries)
  }

  def isBeer(drink: DrinkRecord) = drink.drinkType.get.toString == DrinkType.BEER

  def isCider(drink: DrinkRecord) = drink.drinkType.get.toString == DrinkType.CIDER

  def isPerry(drink: DrinkRecord) = drink.drinkType.get.toString == DrinkType.PERRY

  val anchorRef = ((anchor: String) => "#" + anchor)

  private[this] def generateResultTabs(beers: Iterable[DrinkRecord], ciders: Iterable[DrinkRecord], perries: Iterable[DrinkRecord]): NodeSeq = {
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
