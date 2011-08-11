/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.comment

import Comment._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import net.liftweb.common.Logger

/**
 * Object to provide access to the comments database
 *
 * @author RandomCoder
 *
 * Created On: 9 Aug 2011
 *
 */
object DrinkComments extends Logger {
	RegisterJodaTimeConversionHelpers()
	private val mongo = MongoConnection()("FestivalDrinkFinderComments")("FestivalComments")

	val dateFormat = DateTimeFormat.forStyle("MM")

	/**
	 * Add a new comment to the MongoDB backing store.
	 * 
	 * If there is already a comment with the same drink, author and comment text then it will not be added.
	 */
	def addComment(comment : Comment) = {
		val existsCheck = MongoDBObject("drinkName" -> comment.drinkName, "author" -> comment.author, "comment" -> comment.comment)
		mongo.findOne(existsCheck) match {
			case None => mongo += commentToMongo(comment)
			case _ => debug("Duplicate comment detected, not adding.\n%s".format(comment))
		}
	}

	/**
	 * Get all the comments for a particular drink, sorted by date
	 */
	def commentsForDrink(drinkName : String) : List[Comment] = {
		val query = MongoDBObject("drinkName" -> drinkName)
		
		val comments : List[Comment] = (for {
			result <- mongo.find(query)
		} yield {
			mongoToComment(result)
		}).toList

		comments.sortWith((c1, c2) => c1.date.isBefore(c2.date))
	}
	
	
	private implicit def commentToMongo(comment : Comment) : MongoDBObject = {
		MongoDBObject("drinkName" -> comment.drinkName,
			"author" -> comment.author,
			"comment" -> comment.comment,
			"date" -> comment.date.toString(dateFormat)
		)
	}
	
	private implicit def mongoToComment(result: DBObject) : Comment = {
		val comment = Comment(result.getAs[String]("drinkName").get, result.getAs[String]("author").get, result.getAs[String]("comment").get)
			comment.date = dateFormat.parseDateTime(result.getAs[String]("date").get)

			comment
	}
}