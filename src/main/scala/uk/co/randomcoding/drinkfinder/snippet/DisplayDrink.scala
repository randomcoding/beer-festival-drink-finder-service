package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.drink.{Drink, NoDrink}
import net.liftweb.common.{Full, Logger}
import scala.xml.Text
import scala.xml.NodeSeq
import net.liftweb.http._
import net.liftweb.util.Helpers._
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkDataAccess
import uk.co.randomcoding.drinkfinder.model.matcher.id._
import uk.co.randomcoding.drinkfinder.model.matcher.DrinkNameMatcher

class DisplayDrink extends Logger {
	private val drinkData = new WbfDrinkDataAccess()
	
	def showDrink = {
	  val drinkName = urlDecode( S.param(DRINK_NAME.toString).openOr("Unknown Drink"))
	  val drinkDescription = S.param(DRINK_DESCRIPTION.toString).openOr("Unknown Description")
	  val drinkAbv = S.param(DRINK_ABV_EQUAL_TO.toString).openOr("Unknown ABV")
	  val drinkPrice = S.param(DRINK_PRICE.toString).openOr("Unknown Price")
	  val drinkBrewer = S.param(BREWER_NAME.toString).openOr("Unknown Brewer")
	  val features = S.param(DRINK_HAS_FEATURES.toString).openOr("Unknown Features")
	   debug("Got Drink Name: %s, Description: %s, ABV: %s, Price: %s, Brewer: %s, Features: %s".format(drinkName, drinkDescription, drinkAbv, drinkPrice, drinkBrewer, features))
	  
	   "#drinkFeatures" #> drinkFeatures(features) &
	   "#drinkName" #> drinkName &
	   "#abvDetail" #> Text("%.1f".format(drinkAbv.toDouble)) &
	   "#priceDetail" #> Text("%.2f".format(drinkPrice.toDouble)) &
	   "#brewerDetail" #> <a href={brewerHref(drinkBrewer)}>{drinkBrewer}</a> &
		"#descriptionDetail" #> Text(drinkDescription)
	}
	
	private def brewerHref(brewerName: String) : String =  "brewer?%s=%s".format(BREWER_NAME, brewerName)

	private def drinkFeatures(featuresString: String) : NodeSeq = {
	  val features = for {
	    feature <- featuresString.split(",")
	  } yield {
	    // TODO add feature
	    Text(feature)
	  }
	
	
	var nodes : NodeSeq = Text("")
	features.foreach((node : NodeSeq) => nodes = nodes ++ node)
	
	nodes
	}
}