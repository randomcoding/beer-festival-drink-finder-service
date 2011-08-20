/**
 *
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