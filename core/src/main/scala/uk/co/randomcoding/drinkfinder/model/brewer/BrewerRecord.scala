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
package uk.co.randomcoding.drinkfinder.model.brewer

import net.liftweb.mongodb.record.{ MongoRecord, MongoMetaRecord }
import net.liftweb.mongodb.record.field.ObjectIdPk
import net.liftweb.record.field.StringField

/**
 * `MongoRecord` implementation of a Brewer
 *
 * @author RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * Created On: 17 Jul 2012
 *
 */
class BrewerRecord private () extends MongoRecord[BrewerRecord] with ObjectIdPk[BrewerRecord] {
  override def meta = BrewerRecord

  object name extends StringField(this, 100)

  override def equals(that: Any): Boolean = that match {
    case other: BrewerRecord => name.get == other.name.get
    case _ => false
  }

  override def hashCode = getClass.hashCode + name.get.hashCode
}

object BrewerRecord extends BrewerRecord with MongoMetaRecord[BrewerRecord] {

  def apply(brewerName: String): BrewerRecord = BrewerRecord.createRecord.name(brewerName)
}