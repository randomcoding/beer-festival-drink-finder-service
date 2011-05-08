/**
 *
 */
package code.snippet

import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.jquery.JqJsCmds._
import net.liftweb.http.js.jquery.JqJsCmds
import net.liftweb._
import net.liftweb.util.Helpers._
/**
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

    // capture state from fields
    var drinkName = ""
    var descriptionContains = ""
    var brewerName = ""
    var abv = "0.0"
    var abvValue = 0.0
    var abvComparisonType = ""
    var priceLessThan = "0.0"
    var priceValue = 0.0

    Focus("DrinkName")
    
    def process(): JsCmd = {
      Thread.sleep(500) // allow time to show ajax spinner
      var valid = true

      // perform validation
      abvValue = asDouble(abv) match {
        case Full(a) => a // parsed ok
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
          Show("results")
          SetHtml("results", <span class='alt'>{ resultString }</span>)
        }
        case false => // do nothing for nowHide("results")
      }
    }

    def getParameterValues(): String = {
      val formatString = "Drink Name: %s<br>Description Contains: %s<br>Brewer Name: %s<br>ABV: %s %.1f%%<br>Price Less Than: £%.2f"
      formatString.format(drinkName, descriptionContains, brewerName, abvComparisonType, abvValue, priceValue)
    }

    // bind form to vars and create display
    "name=DrinkName" #> SHtml.text(drinkName, drinkName = _, "id" -> "the_name") &
      "name=DescriptionContains" #> SHtml.text(descriptionContains, descriptionContains = _) &
      "name=Brewer" #> SHtml.select(brewers, Box(""), brewerName = _) &
      "name=ABV" #> SHtml.text(abv, abv = _) &
      "name=AbvComparisonType" #> SHtml.select(comparisonTypes, Box(""), abvComparisonType = _) &
      "name=PriceLessThan" #> (SHtml.text(priceLessThan, priceLessThan = _) ++ SHtml.hidden(process))
  }

  private def displayError(formId: String, errorMessage: String) = {
    S.error(formId, errorMessage)
  }
}