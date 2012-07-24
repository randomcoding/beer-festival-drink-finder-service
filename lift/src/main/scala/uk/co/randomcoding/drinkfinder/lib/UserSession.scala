/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib

import net.liftweb.http.SessionVar
import net.liftweb.common.{ Box, Full }

/**
 * Contains definitions of session variables to be modified by user sessions as required
 *
 * @author RandomCoder
 *
 * Created On: 21 Aug 2011
 *
 */
object UserSession {

  /**
   * Stores the currently attended festivalId
   */
  object currentFestivalId extends SessionVar[Box[String]](Full("WCBCF"))
}