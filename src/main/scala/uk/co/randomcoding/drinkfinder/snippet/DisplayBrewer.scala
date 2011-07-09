package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.drink.{ Drink, NoDrink }
import uk.co.randomcoding.drinkfinder.lib.TransformUtils._
import net.liftweb.common.{ Full, Logger }
import scala.xml.Text
import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkDataAccess
import uk.co.randomcoding.drinkfinder.model.matcher.id._
import uk.co.randomcoding.drinkfinder.model.matcher.{ DrinkNameMatcher, BrewerNameMatcher }

class DisplayBrewer extends Logger {
  private val drinkData = new WbfDrinkDataAccess()

  def showBrewer = {
    val brewerName = urlDecode(S.param(BREWER_NAME.toString).openOr("No Brewer"))

    val drinks = drinkData.getMatching(BrewerNameMatcher(brewerName)).toList.sortBy(_.name)

    "#brewerName" #> Text(brewerName) &
      "#drinks" #> toSummaryDisplay(drinks)
    //generateDrinksList(drinks.sortBy(_.name))
  }

  /*private def generateDrinksList(drinks : List[Drink]) : NodeSeq = {
    // again, this could de done with an embeded template
    drinks.flatMap(drink => {
      <div>
        <a href={ drinkHref(drink.name) }>{ drink.name }</a>
      </div>
    })
  }

  private def drinkHref(drinkName : String) : String = "drink?%s=%s".format(DRINK_NAME, drinkName)*/

}