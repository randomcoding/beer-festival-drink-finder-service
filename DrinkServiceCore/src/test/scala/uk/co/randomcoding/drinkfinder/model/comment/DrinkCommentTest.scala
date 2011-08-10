/**
 *
 */
package uk.co.randomcoding.drinkfinder.model.comment

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.mongodb.casbah.Imports._
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime

/**
 * @author RandomCoder
 *
 * Created On: 10 Aug 2011
 *
 */
class DrinkCommentTest extends FunSuite with ShouldMatchers {
	import DrinkComments._
	
	private def cleanup(comments : Comment*) = {
		val mongo = MongoConnection()("FestivalDrinkFinderComments")("FestivalComments")
		comments.foreach(comment => mongo.remove(commentToMongo(comment)))
	}

	private def commentToMongo(comment : Comment) : MongoDBObject = {
		MongoDBObject("drinkName" -> comment.drinkName,
			"author" -> comment.author,
			"comment" -> comment.comment,
			"date" -> comment.date.toString(dateFormat)
		)
	}

	test("New Comment can be added and retrieved") {
		val comment = Comment("First Beer", "Test User", "Lovely Drink")

		addComment(comment)

		commentsForDrink("First Beer") should be(List(comment))

		cleanup(comment)

		commentsForDrink("FirstBeer") should be('empty)
	}

	test("Multiple New Comments can be added and retrieved") {
		val comment1 = Comment("First Beer", "Test User", "Lovely Drink")
		val comment2 = Comment("First Beer", "Test User 2", "Good!")
		val comment3 = Comment("First Beer", "Test User 3", "Yum")
		val comment4 = Comment("Second Beer", "Test User 2", "Horrible")
		val comment5 = Comment("Second Beer", "Test User", "Lovely Drink, Again")

		addComment(comment1)
		addComment(comment2)
		addComment(comment3)
		addComment(comment4)
		addComment(comment5)

		commentsForDrink("First Beer") should (
			have size (3) and
			contain(comment1) and
			contain(comment2) and
			contain(comment3)
		)

		commentsForDrink("Second Beer") should (
			have size (2) and
			contain(comment4) and
			contain(comment5)
		)

		cleanup(comment1, comment2, comment3, comment4, comment5)

		commentsForDrink("FirstBeer") should be('empty)
		commentsForDrink("Second Beer") should be('empty)
	}

	test("Duplicate Comment is not added") {
		val comment1 = Comment("Beer", "Tester", "Yum")
		val comment2 = Comment("Beer", "Tester", "Yum")

		addComment(comment1)
		addComment(comment2)

		commentsForDrink("Beer") should (have size (1) and
			contain(comment1) or
			contain(comment2)
		)
		
		cleanup(comment1, comment2)
	}

	test("Comments are returned in date order") {
		val comment1 = Comment("Beer", "Tester", "Yum")
		val comment2 = Comment("Beer", "Tester 2", "Yum Yum")
		comment1.date = "2011-08-11T12:00:00Z"
		comment2.date = "2011-08-11T11:00:00Z"
			
			addComment(comment1)
			addComment(comment2)

		commentsForDrink("Beer") should be(List(comment2, comment1))

		cleanup(comment1, comment2)
		commentsForDrink("Beer") should be('empty)
	}

	private implicit def stringToDate(dateString : String) : DateTime = new DateTime(dateString)
}