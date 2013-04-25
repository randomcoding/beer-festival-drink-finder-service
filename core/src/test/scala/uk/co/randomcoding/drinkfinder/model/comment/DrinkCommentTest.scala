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

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import com.mongodb.casbah.Imports._
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTime
import uk.co.randomcoding.drinkfinder.lib.mongodb.FestivalMongoCollection
import org.scalatest.BeforeAndAfterAll

/**
 * @author RandomCoder
 *
 * Created On: 10 Aug 2011
 *
 */
class DrinkCommentTest extends FunSuite with ShouldMatchers with BeforeAndAfterAll {
	import DrinkComments._

	val festivalId = "TestFestival"

	val drinkComments = DrinkComments(festivalId)

	private def cleanup(comments : Comment*) = {
		val mongo = new FestivalMongoCollection(festivalId).comments
		comments.foreach(comment => mongo.remove(commentToMongo(comment)))
	}
	
	override def afterAll() = MongoConnection().dropDatabase(festivalId)

	private def commentToMongo(comment : Comment) : MongoDBObject = {
		MongoDBObject("drinkName" -> comment.drinkName,
			"author" -> comment.author,
			"comment" -> comment.comment,
			"date" -> comment.date.toString(dateFormat)
		)
	}

	test("New Comment can be added and retrieved") {
		val comment = Comment("First Beer", "Test User", "Lovely Drink")

		drinkComments.addComment(comment)

		drinkComments.commentsForDrink("First Beer") should be(List(comment))

		cleanup(comment)

		drinkComments.commentsForDrink("FirstBeer") should be('empty)
	}

	test("Multiple New Comments can be added and retrieved") {
		val comment1 = Comment("First Beer", "Test User", "Lovely Drink")
		val comment2 = Comment("First Beer", "Test User 2", "Good!")
		val comment3 = Comment("First Beer", "Test User 3", "Yum")
		val comment4 = Comment("Second Beer", "Test User 2", "Horrible")
		val comment5 = Comment("Second Beer", "Test User", "Lovely Drink, Again")

		drinkComments.addComment(comment1)
		drinkComments.addComment(comment2)
		drinkComments.addComment(comment3)
		drinkComments.addComment(comment4)
		drinkComments.addComment(comment5)

		drinkComments.commentsForDrink("First Beer") should (
			have size (3) and
			contain(comment1) and
			contain(comment2) and
			contain(comment3)
		)

		drinkComments.commentsForDrink("Second Beer") should (
			have size (2) and
			contain(comment4) and
			contain(comment5)
		)

		cleanup(comment1, comment2, comment3, comment4, comment5)

		drinkComments.commentsForDrink("FirstBeer") should be('empty)
		drinkComments.commentsForDrink("Second Beer") should be('empty)
	}

	test("Duplicate Comment is not added") {
		val comment1 = Comment("Beer", "Tester", "Yum")
		val comment2 = Comment("Beer", "Tester", "Yum")

		drinkComments.addComment(comment1)
		drinkComments.addComment(comment2)

		drinkComments.commentsForDrink("Beer") should (have size (1) and
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

		drinkComments.addComment(comment1)
		drinkComments.addComment(comment2)

		drinkComments.commentsForDrink("Beer") should be(List(comment2, comment1))

		cleanup(comment1, comment2)
		drinkComments.commentsForDrink("Beer") should be('empty)
	}

	private implicit def stringToDate(dateString : String) : DateTime = new DateTime(dateString)
}