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
package uk.co.randomcoding.drinkfinder.model.record.bson

import net.liftweb.mongodb.record.{ BsonRecord, BsonMetaRecord }
import net.liftweb.record.field.StringField
import uk.co.randomcoding.scala.util.core.string.StringHelpers._


/**
 * BSON record class for a drink feature
 *
 * @author RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * Created On: 18 Jul 2012
 */
class DrinkFeatureRecord private () extends BsonRecord[DrinkFeatureRecord] {
  override val meta = DrinkFeatureRecord

  object featureName extends StringField(this, 50)

  override def equals(that: Any): Boolean = that match {
    case other: DrinkFeatureRecord => featureName.get == other.featureName.get
    case _ => false
  }

  override def hashCode: Int = getClass.hashCode + featureName.get.hashCode
}

object DrinkFeatureRecord extends DrinkFeatureRecord with BsonMetaRecord[DrinkFeatureRecord] {
  /**
   * Create a new [[bson.DrinkFeatureRecord]] instance.
   *
   * The feature should be formatted appropriately as this will be used as the display name
   */
  def apply(featureName: String): DrinkFeatureRecord = DrinkFeatureRecord.createRecord.featureName(firstLetterCaps(featureName))
}
