package uk.co.randomcoding.drinkfinder.model.data

import uk.co.randomcoding.drinkfinder.model.matcher.DrinkMatcher
import uk.co.randomcoding.drinkfinder.model.drink.Drink

/**
 * Defines the means of accessing drink information data 
 */
trait DrinkInformationDataAccess {
	/**
   * The type of [[DrinkData]] used
   */
  type DrinkDataType <: DrinkData

  /**
   * Initialises the **drinkData**.
   *
   * This is called by the lazy initialisation of the **drinkData* variable and returns an initialised
   * [[DrinkDataType]] object.
   *
   * @return A fully initialised [[DrinkData]] object.
   */
  protected def initialiseDrinkData() : DrinkDataType

  private lazy val drinkData: DrinkDataType = initialiseDrinkData()

  /**
   * Get the [[drink.Drink]]s that match all the matchers provided
   *
   * @param matchers A List of [[uk.co.randomcoding.drinkfinder.model.matcher.DrinkMatcher]]s used to search the drink data
   * @return The [[drink.Drink]]s that match **all** the matchers
   */
  def getMatching(matchers: List[DrinkMatcher[_]]) = drinkData.getMatching(matchers)
  
  /**
   * Get the [[drink.Drink]]s that match the matchers provided
   *
   * @param matcher A [[uk.co.randomcoding.drinkfinder.model.matcher.DrinkMatcher]] that is used to search the drink data
   * @return The [[drink.Drink]]s that match **all** the matchers
   */
  def getMatching(matcher: DrinkMatcher[_]) = drinkData.getMatching(List(matcher))
  
  /**
   * Get the [[brewer.Brewer]] that has the given name or [[brewer.NoBrewer]] if one does not exist
   */
  def getBrewer(brewerName: String)  = drinkData.getBrewer(brewerName)
}