/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.matcher.BrewerNameMatcher
import uk.co.randomcoding.drinkfinder.model.matcher.id.BREWER_NAME
import net.liftweb.http._
import net.liftweb.util.Helpers._
import scala.xml.{NodeSeq, Text}
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkDataAccess
import uk.co.randomcoding.drinkfinder.model.drink.{Beer, Cider, Drink, Perry}
import uk.co.randomcoding.drinkfinder.model.matcher.MatcherFactory
import net.liftweb.common.Logger

/**
 * @author RandomCoder
 *
 */
class DisplayResults extends Logger {

	private val drinkData = new WbfDrinkDataAccess()
	private val BrewerNameQueryRegex = "$%s=(\\.*)".format(BREWER_NAME.id)

	def calculateResults(in: NodeSeq): NodeSeq = {
		import uk.co.randomcoding.drinkfinder.model.matcher.Matcher
		val params = S.queryString openOr "No Query String"
		debug("Received Query String: %s".format(params))
		
		val matchers/*: List[Matcher]*/ = params match {
			case "No Query String" => Nil
			case paramString: String => MatcherFactory.generate(paramString)
		}
		debug("Generated %d matchers:\n%s".format(matchers.size, matchers.mkString("\n\t")))

		val matchingDrinks: Set[Drink] = drinkData.getMatching(matchers)

		debug("There are %d matching drinks:\n%s".format(matchingDrinks.size, matchingDrinks.mkString("\n\t")))
		val beers = matchingDrinks.filter(_.isInstanceOf[Beer]).toList
		debug("There are %d matching beers".format(beers.size))
		val ciders = matchingDrinks.filter(_.isInstanceOf[Cider]).toList
		debug("There are %d matching ciders".format(ciders.size))
		val perries = matchingDrinks.filter(_.isInstanceOf[Perry]).toList
		debug("There are %d matching perries".format(perries.size))

		bind("results", in,
			"beers" -> convertResultsToDisplayForm("Beers", beers),
			"ciders" -> convertResultsToDisplayForm("Ciders", ciders),
			"perries" -> convertResultsToDisplayForm("Perries", perries))
	}

	private def convertResultsToDisplayForm(titleText: String = "", results: List[Drink]): NodeSeq = {
		Text("%d %s".format(results.size, titleText))
	}
}