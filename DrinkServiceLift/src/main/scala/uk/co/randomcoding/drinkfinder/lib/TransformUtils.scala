package uk.co.randomcoding.drinkfinder.lib

import net.liftweb._
import common.Full
import util.Helpers._
import http.{ SHtml, S }
import http.js.JsCmds._
import scala.xml.Text
import uk.co.randomcoding.drinkfinder.model._
import drink.{ Drink, NoDrink, DrinkFeature }
import brewer.Brewer
import matcher.id._
import Cookies._
import net.liftweb.http.provider.HTTPCookie

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
			"#addToWishlist" #> displayAddToWishlistButton(drink) &
			"#removeFromWishlist" #> displayRemoveFromWishlistButton(drink)
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
			"#addToWishlist" #> displayAddToWishlistButton(drink) &
			"#removeFromWishlist" #> displayRemoveFromWishlistButton(drink)
	})

	/*
	 * Helper functions to generate displayed values
	 */
	private def displayQuantity(drink : Drink) = {
		val style = drink.quantityRemaining.toLowerCase.replaceAll(" ", "")
		SHtml.span(Text(drink.quantityRemaining), (), "class" -> style)
	}

	private def displayBrewer(drink : Drink) = SHtml.link(brewerHref(drink.brewer), () => (), Text(drink.brewer.name))

	private def displayPrice(drink : Drink) = "%.2f".format(drink.price)

	private def displayAbv(drink : Drink) = "%.1f".format(drink.abv)

	private def displayName(drink : Drink) = SHtml.link(drinkHref(drink), () => (), Text(drink.name))

	private def displayFeatures(drink : Drink) = drink.features.map(_.displayName).sortWith(_ < _).mkString("(", ", ", ")")

	private def brewerHref(brewer : Brewer) : String = "brewer?%s=%s".format(BREWER_NAME, brewer.name)

	private def drinkHref(drink : Drink) : String = "drink?%s=%s&%s=%s".format(DRINK_NAME, drink.name, FESTIVAL_NAME, "Worcester Beer, Cider and Perry Festival")

	private def displayAddToWishlistButton(drink : Drink) = SHtml.ajaxButton("+ Wishlist", () => {
		generateDrinkCookie(drink)
		S.redirectTo(S.referer.get)
	})

	private def displayRemoveFromWishlistButton(drink : Drink) = SHtml.ajaxButton("Remove", () => {
		//removeDrinkFromWishlistCookie(drink)

		var cookie = S.findCookie(WISHLIST_COOKIE) match {
			case Full(c) => c
			case _ => HTTPCookie(WISHLIST_COOKIE, "")
		}
		
		cookie.setValue(cookie.value match {
			case Full(v) => v.split(";").filterNot(_.equals(drink.name)).mkString("", ";", "")
			case _ => ""
		})

		S.addCookie(cookie)
		
		S.cookieValue(WISHLIST_COOKIE) match {
			case Full(c) if (!c.contains(drink.name)) => Alert("Drink %s removed from wishlist")
			case _ => Noop
		}
	})
}