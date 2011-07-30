/**
 * TODO: Set license details
 */
package uk.co.randomcoding.drinkfinder.model.data

class DummyFestivalData extends FestivalData("Test Festival") {

  import uk.co.randomcoding.drinkfinder.model.data.DummyDrinks._

  final def beers = Set(FirstBeer, SecondBeer)

  final def ciders = Set(FirstCider, SecondCider)

  final def perries = Set(FirstPerry, SecondPerry)

  beers.foreach(addDrink(_))
  ciders.foreach(addDrink(_))
  perries.foreach(addDrink(_))
}