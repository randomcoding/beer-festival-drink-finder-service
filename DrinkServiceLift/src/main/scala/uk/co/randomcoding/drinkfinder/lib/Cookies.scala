/**
 * 
 */
package uk.co.randomcoding.drinkfinder.lib

import net.liftweb.common.Full
import net.liftweb.http.S
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.provider.HTTPCookie

import uk.co.randomcoding.drinkfinder.model.drink.Drink

/**
 * Contains constants for cookies used
 *
 * @author RandomCoder
 *
 * Created On: 11 Aug 2011
 *
 */
object Cookies {
	val WISHLIST_COOKIE = "festibal.drink.finder.wishlist"
		
	def generateDrinkCookie(drink : Drink) = {
		
		var cookie = S.findCookie(WISHLIST_COOKIE) match {
			case Full(c) => c
			case _ => HTTPCookie(WISHLIST_COOKIE, "")
		}
		
		val cookieValue = cookie.value
		val newValue = cookieValue match {
			case Full(v) if v.nonEmpty => "%s;%s".format(drink.name, v)
			case _ => drink.name
		}
		
		cookie = cookie.setValue(newValue)

		S.addCookie(cookie)

		S.cookieValue(WISHLIST_COOKIE) match {
			case Full(c) if (c.contains(drink.name)) => Alert("Drink %s added to wishlist".format(drink.name))
			case _ => Noop
		}
		/*val cookie = S.findCookie(WISHLIST_COOKIE) match {
			case Full(c) => c
			case _ => HTTPCookie(WISHLIST_COOKIE, "")
		}

		cookie.setValue(cookie.value match {
			case Full(v) => "%s;%s".format(v, drink.name)
			case _ => drink.name
		})
		
		S.addCookie(cookie)*/
	}

	def removeDrinkFromWishlistCookie(drink : Drink) = {
		val cookie = S.findCookie(WISHLIST_COOKIE) match {
		case Full(c) => c
		case _ => HTTPCookie(WISHLIST_COOKIE, "")
		}
		
		cookie.setValue(cookie.value match {
		case Full(v) => v.split(";").filterNot(_.equals(drink.name)).mkString("", ";", "")
		case _ => ""
		})
		
		S.addCookie(cookie)
	}
}