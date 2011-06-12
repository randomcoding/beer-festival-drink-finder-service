/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb.http.js.JsCmds._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb._
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.model.matcher.id._

/**
 * Snippet code to handle the display of the search form.
 *
 * This takes the form parameters and generates a redirection to the results page with the query parameters to display the results of the search.
 * @author RandomCoder
 *
 */
object DrinkSearch {
	// global state/form values
	private val brewers = List(("None" -> ""), ("A Brewer" -> "A Brewer"), ("AN Brewer" -> "AN Brewer"))
	private val comparisonTypes = List(("Equal" -> "Equal To"), ("Greater Than" -> "Greater Than"), ("Less Than" -> "Less Than"))

	def render = {
		// where did we come here from
		val whence = S.referer openOr "/"

		// store state from fields
		var drinkName = ""
		var descriptionContains = ""
		var brewerName = ""
		var abv = "0.0"
		var abvValue = 0.0
		var abvComparisonType = ""
		var priceLessThan = "0.0"
		var priceValue = 0.0

		Focus("DrinkName")

		def process() : JsCmd = {
			Thread.sleep(500) // allow time to show ajax spinner
			var valid = true

			abvValue = asDouble(abv) match {
				case Full(a) => a
				case _ => {
					displayError("ABVError", "ABV Value is not a number")
					valid = false
					-1.0
				}
			}

			priceValue = asDouble(priceLessThan) match {
				case Full(a) => a
				case _ => {
					displayError("PriceError", "Price Value is not a number")
					valid = false
					-1.0
				}
			}

			valid match {
				case true => {
					val resultString = getParameterValues()
					val redirectTo = "/results?" + resultString
					println("Redirecting to: " + redirectTo)
					S.notice("Name: " + drinkName)
					S.redirectTo(redirectTo)
				}
				case false => // do nothing
			}
		}

		def getParameterValues() : String = {
			var paramsList  = List.empty[String]
			if (drinkName.nonEmpty)
			{
				paramsList = (getParam(DRINK_NAME) + "=" + drinkName) :: paramsList
			}
			
			if (descriptionContains.nonEmpty)
			{
				paramsList = (getParam(DRINK_DESCRIPTION) +"=" + descriptionContains) :: paramsList
			}
			
			/*if (brewerName != "None")
			{
				paramsList = ("brewerName=" + brewerName) :: paramsList
			}*/
			
			if (abvValue > 0.0)
			{
			  val abvParam = abvComparisonType match {
			    case "Equal" => getParam(DRINK_ABV_EQUAL_TO)
			    case "Less Than" => getParam(DRINK_ABV_LESS_THAN)
			    case "Greater Than" =>getParam(DRINK_ABV_GREATER_THAN)
			  }
				paramsList = ( abvParam + "=%.1f".format(abvValue)) :: paramsList
			}
			
			if (priceValue > 0.0)
			{
				paramsList = ("%S=%.2f".format(getParam(DRINK_PRICE) ,priceValue)) :: paramsList
			}

			paramsList.mkString("", "&", "")
		}

		// bind form to vars and create display
		"name=DrinkName" #> SHtml.text(drinkName, drinkName = _, "id" -> "the_name") &
			"name=DescriptionContains" #> SHtml.text(descriptionContains, descriptionContains = _) &
			"name=Brewer" #> SHtml.select(brewers, Box(""), brewerName = _) &
			"name=ABV" #> SHtml.text(abv, abv = _) &
			"name=AbvComparisonType" #> SHtml.select(comparisonTypes, Box(""), abvComparisonType = _) &
			"name=PriceLessThan" #> (SHtml.text(priceLessThan, priceLessThan = _) ++ SHtml.hidden(process))
	}

	private def displayError(formId : String, errorMessage : String) = {
		S.error(formId, errorMessage)
	}
	
	private def getParam(queryId: MatcherId) : String = queryId.id
}