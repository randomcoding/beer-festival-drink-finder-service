/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb.http._
import net.liftweb.util.Helpers._
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

  def render/*(in : NodeSeq) : NodeSeq*/ = {
	val params = S.queryString openOr "No Query String"
	println("Received params: " + params)

	val matchers: List[Matcher] = params match {
	  case "No Query String" => Nil
	  case paramString: String => MatcherFactory.generate(paramString)
	}

	val matchingDrinks:Set[Drink] = drinkData.getMatching(matchers)

	val beers = matchingDrinks.filter(_.isInstanceOf[Beer]).toList
	val ciders = matchingDrinks.filter(_.isInstanceOf[Cider]).toList
	val perries = matchingDrinks.filter(_.isInstanceOf[Perry]).toList

	SHtml.radio(List("Beers", "Ciders", "Perries"), Box("Beers"), displayDrinks)

	def displayDrinks: (String) => Any = (drinkType: String) => {
	  val drinks = drinkType match {
		case "Beers" => beers
		case "Ciders" => ciders
		case "Perries" => perries
		case _ => Nil
	  }

		"* *" #> drinkType + "(" + drinks.size + ")"
	}
  }
}