/**
 * Copyright (C) 2012 - RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *    RandomCoder <randomcoder@randomcoding.co.uk> - initial API and implementation and/or initial documentation
 */
package uk.co.randomcoding.drinkfinder.lib.rest

import net.liftweb.common.Logger
import net.liftweb.http.rest.RestHelper
import net.liftweb.json.JsonAST.{ JValue, JArray }
import net.liftweb.json.JsonDSL._
import uk.co.randomcoding.drinkfinder.lib.UserSession
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.matcher.MatcherFactory
import uk.co.randomcoding.drinkfinder.model.matcher.id.FESTIVAL_ID
import uk.co.randomcoding.drinkfinder.model.record.{BrewerRecord, DrinkRecord}


/**
 * Handler for REST dispatch for data access
 *
 * @author RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * Created On: 23 Jul 2012
 */
object DefaultRestHelper extends RestHelper with Logger {

  private[this] implicit val formats = net.liftweb.json.DefaultFormats

  serve {
    case "api" :: festivalId :: "drinks" :: "all" :: Nil Get _ => {
      val currentFestivalId = UserSession.currentFestivalId.openTheBox
      val query = "%s=%s".format(FESTIVAL_ID, festivalId)
      val data = FestivalData(currentFestivalId).get
      val drinks = data.getMatching(MatcherFactory.generate(query)).toList

      (JArray(drinks map (drinkToJValue)))
    }
  }

  private[this] def drinkToJValue(drink: DrinkRecord): JValue = {
    ("drinkType" -> (drink.drinkType.get.toString)) ~
      ("name" -> drink.name.get) ~
      ("description" -> drink.description.get) ~
      ("abv" -> "%.1f".format(drink.abv.get)) ~
      ("price" -> "£%.2f".format(drink.price.get)) ~
      ("remaining" -> drink.quantityRemaining.get.toString) ~
      ("brewer" -> (BrewerRecord.findById(drink.brewer.get) match {
        case Some(brewer) => brewer.name.get
        case _ => "Unknown Brewer"
      })) ~
      ("features" -> drink.features.get.map(_.displayName))
  }
}
