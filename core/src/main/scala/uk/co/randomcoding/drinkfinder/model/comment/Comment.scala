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
import org.joda.time.DateTime

/**
 * TODO: Comment for
 *
 * @author RandomCoder
 *
 * Created On: 9 Aug 2011
 *
 */
case class Comment(drinkName: String, author: String, comment: String) {
	var date = new DateTime()

	override def toString() : String = "Comment: %S, %s, %s\n%s".format(drinkName, author, date, comment)
}

/*object DrinkComment extends MongoDocumentMeta[DrinkComment] {
	override def collectionName = "drinkComments"
}

case class DrinkComment(_id: ObjectId, drinkName: String, author: String, date: String, comment: String) extends MongoDocument[DrinkComment] {
	def meta = DrinkComment
}*/
