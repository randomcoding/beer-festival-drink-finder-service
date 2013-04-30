package uk.co.randomcoding.drinkfinder

import com.mongodb.QueryBuilder
import org.bson.types.ObjectId

/**
 * A collection of common query helpers. Either common queries, or common query builders
 *
 * @author RandomCoder
 */
package object query {
  /**
   * @param id The `ObjectId` of the record to find
   * @return A query that will finds a record by its `ObjectId`
   */
  def byId(id: ObjectId) = QueryBuilder.start("_id").is(id).get

  /**
   * Generate a query that will find a record by finding a match in a given '''name''' field
   *
   * @param name The name to find
   * @param nameField The name (or identifier) of the field to use to find the name value in
   * @return The query that will search for a record by name
   */
  def byName(name: String, nameField: String = "name") = QueryBuilder.start(nameField).is(name).get

  /**
   * Generate a query that will match multiple fields.
   *
   * The generated query will match records that have the required value(s) in '''all''' fields
   * i.e. it uses ''and'' logic to chain the matches and each match checks the value ''is'' the
   * expected one
   *
   * @param matches The pairs `(Field Name, Match Value)` to build the query from
   * @return The query that will perform the match
   */
  def byAllMatches(matches: (String, Any)*) = {
    val builder = matches.toList match {
      case head :: Nil => QueryBuilder.start(head._1).is(head._2)
      case head :: tail => {
        val blder = QueryBuilder.start(head._1).is(head._2)
        tail.foreach(matcher => blder.and(matcher._1).is(matcher._2))
        blder
      }
    }

    builder.get
  }
}
