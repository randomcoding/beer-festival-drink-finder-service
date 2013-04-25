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
package uk.co.randomcoding.drinkfinder.model.drink

import org.bson.types.ObjectId
import uk.co.randomcoding.scala.util.lift.mongodb.MongoFieldHelpers._
import uk.co.randomcoding.scala.util.lift.mongodb.BaseMongoRecordObject
import net.liftweb.mongodb.record.field.{MongoCaseClassListField, ObjectIdPk, ObjectIdRefField}
import net.liftweb.mongodb.record.{ MongoRecord, MongoMetaRecord }
import net.liftweb.record.field._
import uk.co.randomcoding.drinkfinder.model.brewer.BrewerRecord
import uk.co.randomcoding.drinkfinder.query._
import com.mongodb.QueryBuilder

/**
 * Database record class for a Drink
 *
 * @author RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * Created On: 17 Jul 2012
 */
class DrinkRecord private () extends MongoRecord[DrinkRecord] with ObjectIdPk[DrinkRecord] {
  override val meta = DrinkRecord

  object name extends StringField(this, 50)

  object description extends StringField(this, 1000)

  object abv extends DoubleField(this)

  object price extends DoubleField(this)

  object quantityRemaining extends EnumField(this, DrinkRemainingStatus)

  object brewer extends ObjectIdRefField(this, BrewerRecord)

  object festivalId extends StringField(this, 50)

  object drinkType extends EnumField(this, DrinkType)

  object features extends MongoCaseClassListField[DrinkRecord, DrinkFeature](this)
}

/**
 * Companion Object for `DrinkRecord`s, providing creation, deletion and mutation functions for record instances.
 */
object DrinkRecord extends DrinkRecord with BaseMongoRecordObject[DrinkRecord] with MongoMetaRecord[DrinkRecord] {

  def apply(name: String, description: String, abv: Double, price: Double, brewer: BrewerRecord, drinkType: DrinkType.drinkType, festivalId: String, features: Seq[DrinkFeature] = Nil): DrinkRecord = {
    DrinkRecord.createRecord.name(name).description(description).abv(abv).price(price).brewer(brewer.id.get).drinkType(drinkType).festivalId(festivalId).features(features.toList)
  }

  override def findById(oid: ObjectId): Option[DrinkRecord] = DrinkRecord.find(byId(oid))

  override def matchingRecord(drink: DrinkRecord): Option[DrinkRecord] = DrinkRecord.find(byName(drink.name))

  def remaining(drink: DrinkRecord, remaining: DrinkRemainingStatus.status) = {
    val upd = QueryBuilder.start("id").is(drink.id.get).put("quantityRemaining").is(remaining).get
    DrinkRecord.find(upd)
  }
}
