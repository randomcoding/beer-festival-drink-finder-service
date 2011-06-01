/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb.http._
import net.liftweb.util._
import Helpers._
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkDataAccess
import uk.co.randomcoding.drinkfinder.model.matcher.MatcherFactory
import uk.co.randomcoding.drinkfinder.model.drink.{Cider, Perry, Beer, Drink}
import net.liftweb.common.Box
import xml.NodeSeq

/**
 * @author RandomCoder
 *
 */
class DisplayResults {
  type Matcher = (Drink) => Boolean

  private val drinkData = new WbfDrinkDataAccess()

  def render(in : NodeSeq) : NodeSeq = {
	val params = S.queryString openOr "No Query String"

	val matchers: List[Matcher] = params match {
	  case "No Query String" => Nil
	  case paramString: String => MatcherFactory.generate(paramString)
	}

	val matchingDrinks:Set[Drink] = drinkData.getMatching(matchers)

	val beers = matchingDrinks.filter(_.isInstanceOf[Beer]).toList
	val ciders = matchingDrinks.filter(_.isInstanceOf[Cider]).toList
	val perries = matchingDrinks.filter(_.isInstanceOf[Perry]).toList

	// TODO: Use the bind mechanism to put content into each results div
	
	/*val results = "#beer-tab" #> "%d Beers".format(beers.size) & "#cider-tab" #> "%d Ciders".format(ciders.size) &
	  "#perry-tab" #> "%d Perries".format(perries.size)

	results*/
  }
}