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
import uk.co.randomcoding.drinkfinder.lib.mongodb.FestivalMongoCollection


class DrinkComments(festivalId: String) extends Logger {
	import DrinkComments._
	
	RegisterJodaTimeConversionHelpers()
	private val mongoCollection = new FestivalMongoCollection(festivalId) 
	private val comments : MongoCollection = mongoCollection.comments	

	/**
	 * Add a new comment to the MongoDB backing store.
	 * 
	 * If there is already a comment with the same drink, author and comment text then it will not be added.
	 */
	def addComment(comment : Comment) = {
		val existsCheck = MongoDBObject("drinkName" -> comment.drinkName, "author" -> comment.author, "comment" -> comment.comment)
		comments.findOne(existsCheck) match {
			case None => comments += commentToMongo(comment)
			case _ => debug("Duplicate comment detected, not adding.\n%s".format(comment))
		}
	}

	/**
	 * Get all the comments for a particular drink, sorted by date
	 */
	def commentsForDrink(drinkName : String) : List[Comment] = {
		val query = MongoDBObject("drinkName" -> drinkName)
		
		val commentsForDrink : List[Comment] = (for {
			result <- comments.find(query)
		} yield {
			mongoToComment(result)
		}).toList

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
	 * Returns an instance of a [[DrinkComments]] class for the festival with the given id
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