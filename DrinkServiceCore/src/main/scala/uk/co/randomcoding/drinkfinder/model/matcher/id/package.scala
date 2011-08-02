package uk.co.randomcoding.drinkfinder.model.matcher

/**
 * Definitions of the different id
 * @author RandomCoder
 */
package object id {
  sealed abstract class MatcherId(idValue : String) {
    val id = idValue

    override def equals(other : Any) : Boolean = {
      Some(other).isDefined && other.isInstanceOf[MatcherId] && id == other.asInstanceOf[MatcherId].id
    }

    override def hashCode() : Int = {
      id.hashCode
    }
    
    override def toString() : String = id
  }

  case object DRINK_NAME extends MatcherId("drink.name") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(DRINK_NAME) else None
  }

  case object DRINK_DESCRIPTION extends MatcherId("drink.description") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(DRINK_DESCRIPTION) else None
  }

  case object DRINK_PRICE extends MatcherId("drink.price") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(DRINK_PRICE) else None
  }

  case object DRINK_ABV_LESS_THAN extends MatcherId("drink.abv.lessthan") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(DRINK_ABV_LESS_THAN) else None
  }

  case object DRINK_ABV_GREATER_THAN extends MatcherId("drink.abv.greaterthan") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(DRINK_ABV_GREATER_THAN) else None
  }

  case object DRINK_ABV_EQUAL_TO extends MatcherId("drink.abv.equalto") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(DRINK_ABV_EQUAL_TO) else None
  }

  case object DRINK_HAS_FEATURES extends MatcherId("drink.has.features") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(DRINK_HAS_FEATURES) else None
  }

  case object DRINK_TYPE extends MatcherId("drink.type") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(DRINK_TYPE) else None
  }

  case object BREWER_NAME extends MatcherId("brewer.name") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(BREWER_NAME) else None
  }
  
  case object FESTIVAL_NAME extends MatcherId("festival.name") {
	  def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(FESTIVAL_NAME) else None
  }

  case object ALWAYS_TRUE extends MatcherId("always.true") {
    def unapply(matcherId : String) : Option[MatcherId] = if (matcherId == id) Some(BREWER_NAME) else None
  }

}