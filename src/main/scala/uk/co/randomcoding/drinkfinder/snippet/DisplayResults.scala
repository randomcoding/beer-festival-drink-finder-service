/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb.common.Box
import net.liftweb.http._
import net.liftweb.util.Helpers._
import net.liftweb.util._
import scala.xml.{ NodeSeq, Text }
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkDataAccess
import uk.co.randomcoding.drinkfinder.model.drink.{ Beer, Cider, Drink, Perry }
import uk.co.randomcoding.drinkfinder.model.matcher.MatcherFactory

/**
 * @author RandomCoder
 *
 */
class DisplayResults {
  type Matcher = (Drink) => Boolean

  private val drinkData = new WbfDrinkDataAccess()

  def calculateResults(in : NodeSeq) : NodeSeq = {
    val params = S.queryString openOr "No Query String"

    val matchers : List[Matcher] = params match {
      case "No Query String" => Nil
      case paramString : String => MatcherFactory.generate(paramString)
    }

    val matchingDrinks : Set[Drink] = drinkData.getMatching(matchers)

    val beers = matchingDrinks.filter(_.isInstanceOf[Beer]).toList
    val ciders = matchingDrinks.filter(_.isInstanceOf[Cider]).toList
    val perries = matchingDrinks.filter(_.isInstanceOf[Perry]).toList

    bind("results", in,
      "beers" -> convertResultsToDisplayForm("Beers", beers),
      "ciders" -> convertResultsToDisplayForm("Ciders", ciders),
      "perries" -> convertResultsToDisplayForm("Perries", perries))
  }

  private def convertResultsToDisplayForm(titleText : String = "", results : List[Drink]) : NodeSeq = {
    Text("%d %s".format(results.size, titleText))
  }
}