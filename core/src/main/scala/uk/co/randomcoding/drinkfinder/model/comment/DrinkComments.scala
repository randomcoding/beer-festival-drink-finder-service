/**
 * Copyright (C) 2012 RandomCoder <randomcoder@randomcoding.co.uk>
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
 *    RandomCoder - initial API and implementation and/or initial documentation
 */
package uk.co.randomcoding.drinkfinder.model.comment

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._
import net.liftweb.common.Logger
import org.joda.time.format.DateTimeFormat
import uk.co.randomcoding.drinkfinder.lib.mongodb.FestivalMongoCollection


// TODO: Update this to use new comment record class
class DrinkComments(festivalId: String) extends Logger {

  import DrinkComments._

  RegisterJodaTimeConversionHelpers()
	private val mongoCollection = new FestivalMongoCollection(festivalId)
	//private val comments : MongoCollection = _//mongoCollection.comments

	/**
	 * Add a new comment to the MongoDB backing store.
	 *
	 * If there is already a comment with the same drink, author and comment text then it will not be added.
	 */
	def addComment(comment : Comment) = {
		/*val existsCheck = MongoDBObject("drinkName" -> comment.drinkName, "author" -> comment.author, "comment" -> comment.comment)
		comments.findOne(existsCheck) match {
			case None => comments += commentToMongo(comment)
			case _ => debug("Duplicate comment detected, not adding.\n%s".format(comment))
		}*/
	}

	/**
	 * Get all the comments for a particular drink, sorted by date
	 */
	def commentsForDrink(drinkName : String) : List[Comment] = {
		val query = MongoDBObject("drinkName" -> drinkName)

		val commentsForDrink : List[Comment] = List.empty /*(for {
			result <- comments.find(query)
		} yield {
			mongoToComment(result)
		}).toList*/

		commentsForDrink.sortWith((c1, c2) => c1.date.isBefore(c2.date))
	}
}

/**
 * Object to provide access to the comments database
 *
 * @author RandomCoder
 *
 * Created On: 9 Aug 2011
 *
 */
object DrinkComments extends Logger {

	private val commentsByFestival  = Map.empty[String,  DrinkComments].withDefault(key => new DrinkComments(key))

	/**
	 * Returns an instance of a [[uk.co.randomcoding.drinkfinder.model.comment.DrinkComments]] class for the festival with the given id
	 */
	def apply(festivalId: String) : DrinkComments = {
		commentsByFestival(festivalId)
	}

	val dateFormat = DateTimeFormat.forStyle("MM")

	implicit def commentToMongo(comment : Comment) : MongoDBObject = {
		MongoDBObject("drinkName" -> comment.drinkName,
			"author" -> comment.author,
			"comment" -> comment.comment,
			"date" -> comment.date.toString(dateFormat)
		)
	}

	implicit def mongoToComment(result: DBObject) : Comment = {
		val comment = Comment(result.getAs[String]("drinkName").get, result.getAs[String]("author").get, result.getAs[String]("comment").get)
			comment.date = dateFormat.parseDateTime(result.getAs[String]("date").get)

			comment
	}
}
