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
package uk.co.randomcoding.drinkfinder.lib.mongodb

import MongoConfig._

/**
 * A container class for MOngoDb Collections that relate to a specific festival
 *
 * @author RandomCoder
 *
 * Created On: 20 Aug 2011
 *
 */
class FestivalMongoCollection(festivalId : String) {
	val templates = getCollection(festivalId, "%s-templates".format(festivalId))
	val comments = getCollection(festivalId, "%s-comments".format(festivalId))
	val drinks = getCollection(festivalId, "%s-drinks".format(festivalId))
}