package uk.co.randomcoding.drinkfinder.model.data

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
   * @param matchers A List of functions **(Drink) => Boolean** that are used to match [[drink.Drink]]s from the
   * **drinkData***
   * @return The [[drink.Drink]]s that match **all** the matchers
   */
  def getMatching(matchers: List[((Drink) => Boolean)]) = drinkData.getMatching(matchers)
}