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

import uk.co.randomcoding.drinkfinder.lib.UserSession
import uk.co.randomcoding.drinkfinder.model.data.FestivalData
import uk.co.randomcoding.drinkfinder.model.drink._
import uk.co.randomcoding.drinkfinder.model.matcher.id.FESTIVAL_ID
import uk.co.randomcoding.drinkfinder.model.matcher.MatcherFactory

import net.liftweb.http.rest.RestHelper
import net.liftweb.json.Extraction.decompose
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.AnyVar.whatVarIs

/**
 * Handler for REST dispatch for data access
 *
 * @author RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * Created On: 23 Jul 2012
 */
object DefaultRestHelper extends RestHelper {

  serve {
    case "api" :: festivalId :: "drinks" :: "all" :: Nil JsonGet _ => {
      val currentFestivalId = UserSession.currentFestivalId.openTheBox
      val query = "%s=%s".format(FESTIVAL_ID, festivalId)
      val data = FestivalData(currentFestivalId).get
      val drinks = data.getMatching(MatcherFactory.generate(query)).toList
      // Convert to JSON
      toJson(drinks)
    }
    case "api" :: festivalId :: "drinks" :: "all" :: Nil Get _ => {
      val currentFestivalId = UserSession.currentFestivalId.openTheBox
      val query = "%s=%s".format(FESTIVAL_ID, festivalId)
      val data = FestivalData(currentFestivalId).get
      val drinks = data.getMatching(MatcherFactory.generate(query)).toList
      // Convert to JSON
      toJson(drinks)
    }
  }

  private[this] def toJson(drinks: Seq[Drink]): JValue = {
    decompose(drinks)
  }
}