/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb._
import util.Helpers._
import http._
import common.Full

import uk.co.randomcoding.drinkfinder._
import lib.Cookies._
import lib.TransformUtils._
import model.matcher.DrinkNameMatcher
import model.data.FestivalData
import model.matcher.id.FESTIVAL_NAME

/**
 * Displays the wishlist for the current user
 *
 * @author RandomCoder
 *
 * Created On: 11 Aug 2011
 *
 */
class DisplayWishlist extends StatefulSnippet {
	def dispatch = {case "render" => render }
	
	def render = {
		val cookie = S.cookieValue(WISHLIST_COOKIE) match {
			case Full(c) => c
			case _ => ""
		}

		val drinkMatchers = cookie match {
			case "" => List.empty
			case _ => {
				for {
					drinkName <- cookie.split(";").toList
					if !drinkName.isEmpty
				} yield {
					println("Drink Name: %s".format(drinkName))
					DrinkNameMatcher(drinkName)
				}
			}
		}

		val festivalName = urlDecode(S.param(FESTIVAL_NAME.toString).openOr("Worcester Beer, Cider and Perry Festival"))
		val drinks = drinkMatchers match {
			case Nil => List.empty
			case _ => FestivalData(festivalName).getMatching(drinkMatchers).toList.sortBy(_.name)
		}

		"#drinks" #> toDetailedDisplay(drinks)
	}
}