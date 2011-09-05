package uk.co.randomcoding.drinkfinder.model.drink

/**
 * A feature of a drink.
 *
 * @constructor Takes a single string that is the identifier for this feature
 *
 * The identifier is used, ignoring case, to determine equality and uniqueness. So Dry is the same as dry and DRY
 */
case class DrinkFeature(val feature : String) {
	val displayName = feature.nonEmpty match {
		case true => feature.split("\\s").map(word => word.charAt(0).toUpper + word.substring(1)).mkString("", " ", "")
		case false => ""
	}

	override def canEqual(other : Any) : Boolean = other.isInstanceOf[DrinkFeature]

	override def equals(other : Any) : Boolean = feature.toLowerCase.equals(other.asInstanceOf[DrinkFeature].feature.toLowerCase)

	override def hashCode() : Int = feature.toLowerCase.hashCode

	override def toString() : String = displayName
}

