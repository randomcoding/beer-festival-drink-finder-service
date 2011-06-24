package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.drink.{Drink, NoDrink}
import net.liftweb.common.{Full, Logger}
import scala.xml.Text
import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkDataAccess
import uk.co.randomcoding.drinkfinder.model.matcher.id.{BREWER_NAME, DRINK_NAME}
import uk.co.randomcoding.drinkfinder.model.matcher.DrinkNameMatcher

class DisplayDrink extends Logger {
	private val drinkData = new WbfDrinkDataAccess()
	
	def showDrink = {
	  val drinkName = S.param(DRINK_NAME.toString).openOr("Unknown Drink")
	   debug("Got Drink Name: %s".format(drinkName))
	       
	  val drink = S.param(DRINK_NAME.toString) match {
	    case Full(drinkName) => drinkData.getMatching(DrinkNameMatcher(drinkName)).head
	    case _ => NoDrink
	  }
	   
	   debug("Returned Drink is %s".format(drink))
	  
	   "#drinkName" #> drink.name &
	   "#abvDetail" #> Text("%.1f".format(drink.abv)) &
	   "#priceDetail" #> Text("%.2f".format(drink.price)) &
	   "#brewerDetail" #> <a href={brewerHRef(drink)}>{drink.brewer.name}</a> &
		"#descriptionDetail" #> Text(drink.description)
	}
	
	private def brewerHRef(drink: Drink) : String =  "brewer?%s=%s".format(BREWER_NAME, drink.brewer.name)

}