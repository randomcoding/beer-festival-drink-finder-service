package uk.co.randomcoding.drinkfinder.lib

import uk.co.randomcoding.drinkfinder.model._
import drink.{ Drink, NoDrink , DrinkFeature}
import brewer.Brewer
import matcher.id._
import comment.{Comment, DrinkComments}

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
			"#Description" #> drink.description &
			"#Quantity" #> displayQuantity(drink) &
			"#Location" #> drink.features.mkString("", ", ", "")
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
			"#Brewer" #> displayBrewer(drink) &
			"#Quantity" #> displayQuantity(drink) &
			"#Location" #> drink.features.mkString("", ", ", "")
	})
	
	def commentDisplay(comments : List[Comment]) = comments.map (comment => {
		"#drinkName" #> comment.drinkName &
		"#author" #> comment.author &
		"#date" #> comment.date.toString(DrinkComments.dateFormat) &
		"#comment" #> comment.comment
	})
	
	/*
	 * Helper functions to generate displayed values
	 */
	private def displayQuantity(drink: Drink) = {
		val style = drink.quantityRemaining.toLowerCase.replaceAll(" ", "")
		SHtml.span(Text(drink.quantityRemaining), (), "class" -> style)
	}
		
	private def displayBrewer(drink: Drink) = SHtml.link(brewerHref(drink.brewer), () => (), Text(drink.brewer.name))
	
	private def displayPrice(drink: Drink) = "%.2f".format(drink.price)

	private def displayAbv(drink: Drink) = "%.1f".format(drink.abv)
	
	private def displayName(drink : Drink) = SHtml.link(drinkHref(drink), () => (), Text(drink.name))
	
	private def displayFeatures(drink : Drink) = drink.features.map(_.displayName).sortWith(_ < _).mkString("", ", ", "")

	private def brewerHref(brewer : Brewer) : String = "brewer?%s=%s".format(BREWER_NAME, brewer.name)

	private def drinkHref(drink : Drink) : String = "drink?%s=%s&%s=%s".format(DRINK_NAME, drink.name, FESTIVAL_NAME, "Chappel Beer Festival")

}