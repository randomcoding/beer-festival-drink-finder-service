package uk.co.randomcoding.drinkfinder.model.matcher

object MatcherId extends Enumeration {
  val DRINK_NAME = Value("drink.name")
  val DRINK_DESCRIPTION = Value("drink.description")
  val DRINK_PRICE = Value("drink.price")
  val DRINK_ABV_LESS_THAN = Value("drink.abv.lessthan")
  val DRINK_ABV_GREATER_THAN = Value("drink.abv.greaterthan")
  val DRINK_ABV_EQUAL_TO = Value("drink.abv.equalto")
  val DRINK_HAS_FEATURES = Value("drink.has.features")

  implicit def enumToString(matcherId: Value): String = matcherId.toString
}