/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.matcher.id._
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
object DrinkSearch extends Logger {
  // global form values
  private val comparisonTypes = List(("Any" -> "Any"), ("Equal" -> "Equal To"), ("Greater Than" -> "Greater Than"), ("Less Than" -> "Less Than"))
  private val drinkTypes = List(("Any" -> "Any"), ("Beer" -> "Beer"), ("Cider" -> "Cider"), ("Perry" -> "Perry"))

  def render = {
    // where did we come here from
    val whence = S.referer openOr "/"

    // store state from fields
    var drinkName = ""
    var descriptionContains = ""
    var abv = "0.0"
    var abvValue = 0.0
    var abvComparisonType = ""
    var priceLessThan = "0.0"
    var priceValue = 0.0
    var drinkType = ""
    var brewerName = ""

    Focus("DrinkName")

    def process() : JsCmd = {
      Thread.sleep(500) // allow time to show ajax spinner
      var valid = true

      val setInvalid : ((String, String) => Double) = ((errorId : String, errorMessage : String) => {
        displayError(errorId, errorMessage)
        valid = false
        -1.0
      })

      abvValue = asDouble(abv) match {
        case Full(a) => {
          debug("ABV=%f and comparison=%s".format(a, abvComparisonType))
          if (abvComparisonType equals "Any") {
            if (a > 0) setInvalid("ABVError", "Please Enter an ABV for Comparison") else a
          } else {
            if (a equals 0) setInvalid("ABVError", "Please Select a Comparison Type") else a
          }
        }
        case _ => {
          setInvalid("ABVError", "ABV Value is not a number")
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
          S.notice("Name: " + drinkName)
          S.redirectTo(redirectTo)
        }
        case false => // do nothing - should probably display an error here
      }
    }

    def getParameterValues() : String = {
      var paramsList = List.empty[String]
      if (drinkName.nonEmpty) {
        paramsList = (DRINK_NAME + "=" + drinkName) :: paramsList
      }

      if (descriptionContains.nonEmpty) {
        paramsList = (DRINK_DESCRIPTION + "=" + descriptionContains) :: paramsList
      }

      if (abvValue > 0.0) {
        val abvParam = abvComparisonType match {
          case "Equal" => DRINK_ABV_EQUAL_TO
          case "Less Than" => DRINK_ABV_LESS_THAN
          case "Greater Than" => DRINK_ABV_GREATER_THAN
        }

        paramsList = (abvParam + "=%.1f".format(abvValue)) :: paramsList
      }

      if (priceValue > 0.0) {
        paramsList = ("%s=%.2f".format(DRINK_PRICE, priceValue)) :: paramsList
      }

      if (drinkType.nonEmpty && !drinkType.equals("Any")) {
        paramsList = ("%s=%s".format(DRINK_TYPE, drinkType)) :: paramsList
      }

      debug("BrewerName is" + brewerName)
      if (brewerName.nonEmpty) {
        paramsList = (BREWER_NAME + "=" + brewerName) :: paramsList
      }

      paramsList.mkString("", "&", "")
    }

    // bind form to vars and create display
    "name=DrinkName" #> SHtml.text(drinkName, drinkName = _, "id" -> "the_name") &
      "name=DescriptionContains" #> SHtml.text(descriptionContains, descriptionContains = _) &
      "name=ABV" #> SHtml.text(abv, abv = _) &
      "name=AbvComparisonType" #> SHtml.select(comparisonTypes, Box("Any"), abvComparisonType = _) &
      "name=DrinkType" #> (SHtml.select(drinkTypes, Box("Any"), drinkType = _)) &
      "name=BrewerName" #> (SHtml.text(brewerName, brewerName = _)) &
      "name=PriceLessThan" #> (SHtml.text(priceLessThan, priceLessThan = _)) &
      "type=submit" #> (SHtml.onSubmitUnit(process))
  }

  private def displayError(formId : String, errorMessage : String) = {
    S.error(formId, errorMessage)
  }
}