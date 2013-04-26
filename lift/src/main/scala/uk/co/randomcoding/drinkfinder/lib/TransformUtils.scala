package uk.co.randomcoding.drinkfinder.lib

import net.liftweb.http.SHtml
import net.liftweb.util.Helpers._
import scala.xml.Text
import uk.co.randomcoding.drinkfinder.model.comment.{DrinkComments, Comment}
import uk.co.randomcoding.drinkfinder.model.matcher.id.{DRINK_NAME, FESTIVAL_NAME, BREWER_NAME}
import uk.co.randomcoding.drinkfinder.model.record.{BrewerRecord, DrinkRecord}

object TransformUtils {

  /**
   * Transforms a list of drinks into a summary display that shows the basic drink data and a description
   */
  def toSummaryDisplay(drinks: List[DrinkRecord]) = drinks.map(drink => {
    "#Name" #> displayName(drink) &
      "#Features" #> displayFeatures(drink) &
      "#ABV" #> displayAbv(drink) &
      "#Price" #> displayPrice(drink) &
      "#Description" #> drink.description.get &
      "#Quantity" #> displayQuantity(drink)
  })

  /**
   * Transforms a list of drinks into a detailed display that shows the basic drink,
   * a description and links to the brewer etc.
   */
  def toDetailedDisplay(drinks: List[DrinkRecord]) = drinks.map(drink => {
    "#Name" #> displayName(drink) &
      "#Features" #> displayFeatures(drink) &
      "#ABV" #> displayAbv(drink) &
      "#Price" #> displayPrice(drink) &
      "#Description" #> drink.description.get &
      "#Brewer" #> displayBrewer(drink) &
      "#Quantity" #> displayQuantity(drink)
  })

  def commentDisplay(comments: List[Comment]) = comments.map(comment => {
    "#drinkName" #> comment.drinkName &
      "#author" #> comment.author &
      "#date" #> comment.date.toString(DrinkComments.dateFormat) &
      "#comment" #> comment.comment
  })

  /*
   * Helper functions to generate displayed values
   */
  private def displayQuantity(drink: DrinkRecord) = {
    val style = drink.quantityRemaining.get.toString.toLowerCase.replaceAll(" ", "")
    style match {
      case "notmeasured" => SHtml.span(Text(""), ())
      case _ => SHtml.span(Text(drink.quantityRemaining.get.toString), (), "class" -> style)
    }

  }

  private def displayBrewer(drink: DrinkRecord) = BrewerRecord.findById(drink.brewer.get) match {
    case Some(brewer) => SHtml.link(brewerHref(brewer), () => (), Text(brewer.name.get))
    case _ => Text("Unknown Brewer")
  }

  private def displayPrice(drink: DrinkRecord) = "%.2f".format(drink.price.get)

  private def displayAbv(drink: DrinkRecord) = "%.1f".format(drink.abv.get)

  private def displayName(drink: DrinkRecord) = SHtml.link(drinkHref(drink), () => (), Text(drink.name.get))

  private def displayFeatures(drink: DrinkRecord) = drink.features.get.map(_.displayName).sortWith(_ < _).mkString("(", ", ", ")")

  private def brewerHref(brewer: BrewerRecord): String = "brewer?%s=%s".format(BREWER_NAME, brewer.name.get)

  private def drinkHref(drink: DrinkRecord): String = "drink?%s=%s&%s=%s".format(DRINK_NAME, drink.name.get, FESTIVAL_NAME, "Worcester Beer, Cider and Perry Festival")

}
