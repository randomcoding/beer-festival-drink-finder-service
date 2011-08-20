/**
 *
 */
package uk.co.randomcoding.drinkfinder.lib.mongodb

import com.mongodb.casbah.Imports._

import net.liftweb._
import json._
import common.Loggable

/**
 * Configuration for Mongo DB and access to mongo connections
 *
 * @author RandomCoder
 *
 * Created On: 19 Aug 2011
 *
 */
object MongoConfig extends Loggable {

	private implicit val formats = DefaultFormats

	case class CloudFoundryMongo(name : String, label : String, plan : String, credentials : CloudFoundryMongoCredentials)
	case class CloudFoundryMongoCredentials(hostname : String, port : String, username : String, password : String, name : String, db : String)

	/**
	 * Initialise the MongoDB system to create connections etc.
	 */
	def init() {
		val (host : String, port: Int, user: String, pass: String, db: String) = Option(System.getenv("VCAP_SERVICES")) match {
			case Some(s) => {
				try {
					parse(s) \\ "mongodb-1.8" match {
						case JArray(ary) => ary foreach { mongoJson =>
							val mongo = mongoJson.extract[CloudFoundryMongo]
							val credentials = mongo.credentials
							(credentials.hostname, credentials.port.toInt, credentials.password, credentials.db)
						}
						case x => logger.warn("Json parse error: %s".format(x))
					}
				}
			}
			case _ => ("localhost", 27017, "", "", "mongo")
		}
		
		val connection = MongoConnection(host, port)
		val mongoDb = connection(db)
		if (user.nonEmpty) {
			mongoDb.authenticate(user, pass)
		}
	}
}