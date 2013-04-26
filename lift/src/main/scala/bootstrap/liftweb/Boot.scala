package bootstrap.liftweb

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.auth.{userRoles, HttpBasicAuthentication, AuthRole}
import net.liftweb.sitemap.Loc._
import net.liftweb.sitemap._
import uk.co.randomcoding.drinkfinder.lib.rest.DefaultRestHelper

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot extends Loggable {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("uk.co.randomcoding.drinkfinder")

    // authentication
    LiftRules.httpAuthProtectedResource.prepend {
      case (Req("admin" :: _, _, _)) => Full(AuthRole("admin"))
    }

    LiftRules.authentication = HttpBasicAuthentication("Find a Drink") {
      case ("WbfAdmin", "L0aDMyDr1nk5", req) => {
        logger.info("You are now authenticated !")
        userRoles(List(AuthRole("admin")))
        true
      }
    }

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index", // the simple way to declare a menu
      Menu.i("Results") / "results",
      Menu.i("Drink Display") / "drink",
      Menu.i("Brewer Display") / "brewer",
      Menu.i("Add Comment") / "addcomment",
      Menu.i("Data Upload") / "admin" / "upload",
      Menu.i("Upload Completed") / "admin" / "uploadcompleted",

      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"),
        "Static Content")))

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries: _*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

    // Use jQuery 1.4
    LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts
    ResourceServer.allow {
      case "css" :: _ => true
      case "js" :: _ => true
    }

    LiftRules.statelessDispatchTable.append(DefaultRestHelper)
  }
}
