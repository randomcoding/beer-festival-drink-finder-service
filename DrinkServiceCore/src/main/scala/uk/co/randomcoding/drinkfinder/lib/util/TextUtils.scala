/**
 * 
 */
package uk.co.randomcoding.drinkfinder.lib.util

/**
 * TODO: Comment for 
 *
 * @author RandomCoder
 *
 * Created On: 5 Sep 2011
 *
 */
object TextUtils {
	/**
	 * Converts a string to first letter caps format.
	 */
	def firstLetterCaps(inString : String) : String = inString.split(" ").map(elem => elem.charAt(0).toUpper + elem.substring(1).toLowerCase).mkString("", " ", "")
}