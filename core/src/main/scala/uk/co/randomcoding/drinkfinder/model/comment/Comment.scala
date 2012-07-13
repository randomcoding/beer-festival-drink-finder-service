/**
 * 
 */
package uk.co.randomcoding.drinkfinder.model.comment

import com.mongodb.casbah.Imports._
import org.joda.time.DateTime

/**
 * TODO: Comment for 
 *
 * @author RandomCoder
 *
 * Created On: 9 Aug 2011
 *
 */
case class Comment(val drinkName: String, val author: String, val comment: String) {
	var date = new DateTime()
	
	override def toString() : String = "Comment: %S, %s, %s\n%s".format(drinkName, author, date, comment)
}

/*object DrinkComment extends MongoDocumentMeta[DrinkComment] {
	override def collectionName = "drinkComments"
}

case class DrinkComment(_id: ObjectId, drinkName: String, author: String, date: String, comment: String) extends MongoDocument[DrinkComment] {
	def meta = DrinkComment
}*/