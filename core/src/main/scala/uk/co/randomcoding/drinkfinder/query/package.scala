package uk.co.randomcoding.drinkfinder

import com.mongodb.QueryBuilder
import org.bson.types.ObjectId

/**
 * A collection of common query helpers. Either common queries, or common query builders
 *
 * @author RandomCoder
 */
package object query {
  def byId(id: ObjectId) = QueryBuilder.start("_id").is(id).get

  def byName(name: String, nameField: String = "name") = QueryBuilder.start(nameField).is(name).get
}
