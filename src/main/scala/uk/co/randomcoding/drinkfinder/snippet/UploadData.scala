/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.matcher.id._
import net.liftweb.http.js.JsCmds._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb._
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.model.matcher.id._
import uk.co.randomcoding.drinkfinder.lib.DataTemplates

/**
 * Snippet code to handle the display of the search form.
 *
 * This takes the form parameters and generates a redirection to the results page with the query parameters to display the results of the search.
 * @author RandomCoder
 *
 */
object UploadData extends Logger {

	def showForm = {
		var selectedTemplate = ""

		def process() : JsCmd = {
			Thread.sleep(500) // allow time to show ajax spinner
			var valid = true

		}

		val availableTemplates = DataTemplates.templatesMap
		object uploadFile extends RequestVar[Box[FileParamHolder]](Empty)

		// bind form to vars and create display
		"#availableTemplates" #> (SHtml.select(availableTemplates.toSeq, Box("--- Select Template ---"), selectedTemplate = _)) &
			"#uploadButton" #> (SHtml.fileUpload((ul => uploadFile(Full(ul))))) &
			"type=submit" #> (SHtml.onSubmitUnit(process))
	}

	private def displayError(formId : String, errorMessage : String) = {
		S.error(formId, errorMessage)
	}
}