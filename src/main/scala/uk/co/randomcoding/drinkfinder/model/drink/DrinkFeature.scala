package uk.co.randomcoding.drinkfinder.model.drink

sealed class DrinkFeature(val feature : String)

sealed class DrinkSweetness(sweetness : String) extends DrinkFeature(sweetness)

case object Dry extends DrinkSweetness("Dry")
case object Medium extends DrinkSweetness("Medium")
case object Sweet extends DrinkSweetness("Sweet")

sealed class BeerStyle(style : String) extends DrinkFeature(style)
case object RealAle extends BeerStyle("Real Ale")
// TODO: Add more styles
