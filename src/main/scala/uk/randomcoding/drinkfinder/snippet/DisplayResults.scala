/**
 * 
 */
package uk.randomcoding.drinkfinder.snippet

import net.liftweb._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util.Helpers._

/**
 * @author RandomCoder
 *
 */
class DisplayResults {
	def render = {
		val params = S.queryString openOr "No Query String"
		"* *" #> params
	}

}