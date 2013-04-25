package uk.co.randomcoding.drinkfinder

import org.bson.types.ObjectId
import com.mongodb.QueryBuilder

/**
 * A collection of common query helpers. Either common queries, or common query builders
 *
 * @author RandomCoder
 */
package object query {
  def byId(id: ObjectId) = QueryBuilder.start("id").is(id).get

  def byName(name: String, nameField: String = "name") = QueryBuilder.start(nameField).is(name).get
}
