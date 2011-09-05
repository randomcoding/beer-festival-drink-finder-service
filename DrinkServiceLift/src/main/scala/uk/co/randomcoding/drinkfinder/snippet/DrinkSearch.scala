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
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.drink.DrinkFeature
import uk.co.randomcoding.drinkfinder.model.matcher.id._
import uk.co.randomcoding.drinkfinder.lib.util.TextUtils._

/**
 * Snippet code to handle the display of the search form.
 *
 * This takes the form parameters and generates a redirection to the results page with the query parameters to display the results of the search.
 * @author RandomCoder
 *
 */
object DrinkSearch extends Logger {
	// hack to provide access to the (currently) only data object
	//private lazy val festivalData = FestivalData("Worcester Beer, Cider and Perry Festival")
	private lazy val festivalData = FestivalData("Chappel Beer Festival")

	// function to convert a Drink Feature into a combo box tuple
	//private val featureToDisplay = (feature : DrinkFeature, drinkType : String) => (feature.feature -> "%s (%s)".format(feature.displayName, drinkType))
	private val featureToDisplay = (feature : DrinkFeature, drinkType : String) => (feature.feature -> "%s Bar".format(feature.displayName))

	// global form values
	private val comparisonTypes = List(("" -> "Any"), ("Equal" -> "Equal To"), ("Greater Than" -> "Greater Than"), ("Less Than" -> "Less Than"))

	private val drinkTypes = List(("" -> "Any"), ("Beer" -> "Beer"), ("Cider" -> "Cider"), ("Perry" -> "Perry"))

	private def brewers = List(("" -> "Any")) ::: (festivalData.allBrewers.sortBy(_.name).map(brewer => (brewer.name -> firstLetterCaps(brewer.name))))

	private def drinkFeatures = {
		val beerFeatures = festivalData.beerFeatures.sortBy(_.feature)
		val ciderAndPerryFeatures = {
			val uniqueList = (festivalData.ciderFeatures ::: festivalData.perryFeatures).toSet.toList
			uniqueList.sortBy(_.feature)
		}
		List(("" -> "Any")) ::: beerFeatures.map(featureToDisplay(_, "Beer")) ::: ciderAndPerryFeatures.map(featureToDisplay(_, "Cider & Perry"))
	}

	def render = {
		// where did we come here from
		val whence = S.referer openOr "/"

		// store state from fields
		var drinkName = ""
		var descriptionContains = ""
		var abv = "Any"
		var abvValue = 0.0
		var abvComparisonType = ""
		var priceLessThan = "Any"
		var priceValue = 0.0
		var drinkType = ""
		var brewerName = ""
		var drinkFeature = ""

		Focus("DrinkName")

		def process() : JsCmd = {
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
					if (abv.nonEmpty && !abv.equals("Any")) {
						setInvalid("ABVError", "ABV Value is not a number")
					}
					-1.0
				}
			}

			priceValue = asDouble(priceLessThan) match {
				case Full(a) => a
				case _ => {
					if (priceLessThan.nonEmpty && !priceLessThan.equals("Any")) {
						setInvalid("PriceError", "Price Value is not a number")
					}
					-1.0
				}
			}

			valid match {
				case true => {
					val resultString = getParameterValues()
					val redirectTo = if (isOnlyBrewerSearch()) "/brewer?%s" else "/results?%s"
					S.notice("Name: " + drinkName)
					S.redirectTo(redirectTo.format(resultString))
				}
				case false => // do nothing - the user is shown an error and can correct it
			}
		}

		def isOnlyBrewerSearch() : Boolean = {
			(brewerName.nonEmpty && !brewerName.equals("Any")) && drinkName.isEmpty && descriptionContains.isEmpty && (abvValue equals 0.0) && (priceValue equals 0.0)
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

			if (drinkType.nonEmpty) {
				paramsList = ("%s=%s".format(DRINK_TYPE, drinkType)) :: paramsList
			}

			if (brewerName.nonEmpty) {
				paramsList = (BREWER_NAME + "=" + brewerName) :: paramsList
			}

			if (drinkFeature.nonEmpty) {
				paramsList = ("%s=%s".format(DRINK_HAS_FEATURES, drinkFeature)) :: paramsList
			}

			paramsList.mkString("", "&", "")
		}

		// bind form to vars and create display
		"#DrinkName" #> SHtml.text(drinkName, drinkName = _, "id" -> "the_name") &
			"#DescriptionContains" #> SHtml.text(descriptionContains, descriptionContains = _) &
			"#ABV" #> SHtml.text(abv, abv = _) &
			"#AbvComparisonType" #> SHtml.select(comparisonTypes, Box("Any"), abvComparisonType = _) &
			"#DrinkType" #> (SHtml.select(drinkTypes, Box("Any"), drinkType = _)) &
			"#DrinkFeature" #> (SHtml.select(drinkFeatures, Box("Any"), drinkFeature = _)) &
			"#BrewerName" #> (SHtml.select(brewers, Box("Any"), brewerName = _)) &
			"#PriceLessThan" #> (SHtml.text(priceLessThan, priceLessThan = _)) &
			"type=submit" #> (SHtml.onSubmitUnit(process))
	}

	private def displayError(formId : String, errorMessage : String) = {
		S.error(formId, errorMessage)
	}
}