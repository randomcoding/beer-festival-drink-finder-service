package uk.co.randomcoding.drinkfinder.lib

import uk.co.randomcoding.drinkfinder.model.drink.{ Drink, NoDrink , DrinkFeature}
import uk.co.randomcoding.drinkfinder.model.brewer.Brewer
import uk.co.randomcoding.drinkfinder.model.matcher.id._
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml
import net.liftweb.http.RequestVar
import scala.xml.Text

object TransformUtils {

	/**
	 * Transforms a list of drinks into a summary display that shows the basic drink data and a description
	 */
	def toSummaryDisplay(drinks : List[Drink]) = drinks.map(drink => {
		"#Name" #> displayName(drink) &
			"#Features" #> displayFeatures(drink) &
			"#ABV" #> displayAbv(drink) &
			"#Price" #> displayPrice(drink) &
			"#Description" #> drink.description
	})

	/**
	 * Transforms a list of drinks into a detailed display that shows the basic drink,
	 * a description and links to the brewer etc.
	 */
	def toDetailedDisplay(drinks : List[Drink]) = drinks.map(drink => {
		"#Name" #> displayName(drink) &
			"#Features" #> displayFeatures(drink) &
			"#ABV" #> displayAbv(drink) &
			"#Price" #> displayPrice(drink) &
			"#Description" #> drink.description &
			"#Brewer" #> displayBrewer(drink)
	})
	
	/*
	 * Helper functions to generate displayed values
	 */
	private def displayBrewer(drink: Drink) = SHtml.link(brewerHref(drink.brewer), () => Unit, Text(drink.brewer.name))
	
	private def displayPrice(drink: Drink) = "%.2f".format(drink.price)

	private def displayAbv(drink: Drink) = "%.1f".format(drink.abv)
	
	private def displayName(drink : Drink) = SHtml.link(drinkHref(drink), () => (), Text(drink.name))
	
	private def displayFeatures(drink : Drink) = drink.features.map(_.displayName).sortWith(_ < _).mkString("(", ", ", ")")

	private def brewerHref(brewer : Brewer) : String = "brewer?%s=%s".format(BREWER_NAME, brewer.name)

	private def drinkHref(drink : Drink) : String = {
		"drink?%s=%s&%s=%s&%s=%.1f&%s=%.2f&%s=%s&%s=%s".format(DRINK_NAME, drink.name,
			DRINK_DESCRIPTION, drink.description,
			DRINK_ABV_EQUAL_TO, drink.abv,
			DRINK_PRICE, drink.price,
			BREWER_NAME, drink.brewer.name,
			DRINK_HAS_FEATURES, drink.features.map(_.feature).mkString("", ",", ""))
	}

}