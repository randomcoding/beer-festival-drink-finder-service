/**
 *
 */
package code.snippet

import net.liftweb._
import http._
import common._
import util.Helpers._
import js._
import JsCmds._
import JE._
import scala.xml.NodeSeq
/**
 * @author RandomCoder
 *
 */
object DrinkSearch {
  // global state/form values
  val brewers = List(("A Brewer" -> "A Brewer"), ("AN Brewer" -> "AN Brewer"))
  val comparisonTypes = List(("Eq" -> "Equal To"), ("Gt" -> "Greater Than"), ("Lt" -> "Less Than"))

  def render = {
    // where did we come here from
    val whence = S.referer openOr "/"

    // capture state from fields
    var drinkName = ""
    var descriptionContains = ""
    var brewerName = ""
    var abv = "0.0"
    var abvComparisonType = ""
    var priceLessThan = "0.0"

    def process(): JsCmd = {
      Thread.sleep(500) // show ajax spinner
      var valid = true

      // perform validation
      asDouble(abv) match {
        case Full(a) => // parsed ok
        case _ => {
          displayError("ABVError", "ABV Value is not a number")
          valid = false
        }
      }

      asDouble(priceLessThan) match {
        case Full(a) => // parsed ok
        case _ => {
          displayError("PriceError", "Price Value is not a number")
          valid = false
        }
      }

      if (valid) {
        Alert(getParameterValues())
      }
    }

    def getParameterValues(): String = {
      "Drink Name: %s\n" +
        "Description Contains: %s\n" +
        "Brewer Name: %s\n" +
        "ABV: %s%%\n" +
        "ABV Comparison: %s\n" +
        "Price Less Than: £%s".format(drinkName, descriptionContains.mkString, brewerName, abv, abvComparisonType, priceLessThan)
    }

    def displayError(formId: String, errorMessage: String) = {
      S.error(formId, errorMessage)
    }

    // bind form to vars and create display
    "name=DrinkName" #> SHtml.text(drinkName, drinkName = _, "id" -> "the_name") &
      "name=DescriptionContains" #> SHtml.text(descriptionContains, descriptionContains = _) &
      "name=Brewer" #> SHtml.select(brewers, Box(""), brewerName = _) &
      "name=ABV" #> SHtml.text(abv, abv = _) &
      "name=AbvComparisonType" #> SHtml.select(comparisonTypes, Box(""), abvComparisonType = _) &
      "name=PriceLessThan" #> (SHtml.text(priceLessThan, priceLessThan = _) ++ SHtml.hidden(process))
  }

}