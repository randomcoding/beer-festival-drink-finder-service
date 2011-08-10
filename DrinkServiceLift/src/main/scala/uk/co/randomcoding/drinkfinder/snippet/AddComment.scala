/**
 * 
 */
package uk.co.randomcoding.drinkfinder.snippet


import net.liftweb.http._
import js.JsCmd
import net.liftweb.util.Helpers._

import uk.co.randomcoding.drinkfinder.model._
import matcher.id.DRINK_NAME
import comment.Comment
import comment.DrinkComments._

/**
 * TODO: Comment for 
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
			
		def process() : JsCmd = {				
			if (!commentText.isEmpty) {
				if (author isEmpty) author = "Anonymous"
				val comment = Comment(drinkName, author, commentText)
				addComment(comment)
			}
			
			S.redirectTo("/drink?%s=%s".format(DRINK_NAME.toString, drinkName))
		}
			
		"#author" #> SHtml.text(author, author = _) &
		"#commentText" #> SHtml.textarea(commentText, commentText = _) &
		"type=submit" #> (SHtml.onSubmitUnit(process))
	}

}