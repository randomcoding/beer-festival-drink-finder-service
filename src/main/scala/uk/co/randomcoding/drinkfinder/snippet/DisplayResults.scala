/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb.http._
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkDataAccess

/**
 * @author RandomCoder
 *
 */
class DisplayResults {
  private val drinkData = new WbfDrinkDataAccess()

  def render = {
	val params = S.queryString openOr "No Query String"
	println("Received params: " + params)
	val matchers = params match {
	  case "No Query String" => Nil
	  case paramString: String => MatcherFactory.generateMatchers(paramString)
	}
	"* *" #> params
  }

}