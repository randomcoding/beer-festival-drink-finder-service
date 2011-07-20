package uk.co.randomcoding.drinkfinder.lib

import uk.co.randomcoding.drinkfinder.model.drink.Drink
import uk.co.randomcoding.drinkfinder.model.brewer.Brewer
import uk.co.randomcoding.drinkfinder.model.matcher.id._
import net.liftweb.util.Helpers._

object TransformUtils {
	
  /**
   * Transforms a list of drinks into a 
   */
  def toSummaryDisplay(drinks: List[Drink]) = drinks.map(drink => { 
    "#Name" #> <a href={drinkHref(drink)}>{drink.name}</a> &
    "#ABV" #> "%.1f".format(drink.abv) &
    "#Price" #> "%.2f".format(drink.price) &
    "#Description" #> drink.description
    })
  
 def toDetailedDisplay(drinks: List[Drink]) = drinks.map(drink => { 
   "#Name" #> <a href={drinkHref(drink)}>{drink.name}</a> & 
   "#ABV" #> "%.1f".format(drink.abv) & 
   "#Price" #> "%.2f".format(drink.price) & 
   "#Description" #> drink.description & 
   "#Brewer" #> <a href={brewerHref(drink.brewer)}>drink.brewer.name</a>
   })
  
 
 private def brewerHref(brewer: Brewer) : String = "brewer?%s=%s".format(BREWER_NAME, brewer.name)
  
  private def drinkHref(drink : Drink) : String = {
    "drink?%s=%s&%s=%s&%s=%.1f&%s=%.2f&%s=%s&%s=%s".format(DRINK_NAME, drink.name,
      DRINK_DESCRIPTION, drink.description,
      DRINK_ABV_EQUAL_TO, drink.abv,
      DRINK_PRICE, drink.price,
      BREWER_NAME, drink.brewer.name,
      DRINK_HAS_FEATURES, drink.features.map(_.feature).mkString("", ",", ""))
  }
	  
}