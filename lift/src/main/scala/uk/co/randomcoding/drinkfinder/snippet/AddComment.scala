/**
 * Copyright (C) 2012 RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *    RandomCoder - initial API and implementation and/or initial documentation
 */
package uk.co.randomcoding.drinkfinder.snippet

import net.liftweb.http._
import js.JsCmd
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.model._
import matcher.id.DRINK_NAME
import comment.Comment
import comment.DrinkComments
import uk.co.randomcoding.drinkfinder.lib.UserSession

/**
 * Snippet to provide logic to add a comment for a drink.
 *
 * @author RandomCoder
 *
 * Created On: 10 Aug 2011
 *
 */
class AddComment {

	def render = {
		var author = ""
		val drinkName = urlDecode(S.param(DRINK_NAME.toString).get)
		var commentText = ""
		//val currentFestival = UserSession.currentFestival.is.getOrElse("Festival")
		val currentFestival = UserSession.currentFestival.is.getOrElse("Chappel Beer Festival")
		val comments = DrinkComments(currentFestival)

		def process() : JsCmd = {
			if (!commentText.isEmpty) {
				if (author isEmpty) author = "Anonymous"
				val comment = Comment(drinkName, author, commentText)
				comments.addComment(comment)
			}

			S.redirectTo("/drink?%s=%s".format(DRINK_NAME.toString, drinkName))
		}

		"#author" #> SHtml.text(author, author = _) &
			"#commentText" #> SHtml.textarea(commentText, commentText = _) &
			"type=submit" #> (SHtml.onSubmitUnit(process))
	}

}