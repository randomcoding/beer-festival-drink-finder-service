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

	// Case classes required to parse CloudFoundry JSON
	case class CloudFoundryMongo(name : String, label : String, plan : String, credentials : CloudFoundryMongoCredentials)
	case class CloudFoundryMongoCredentials(hostname : String, port : String, username : String, password : String, name : String, db : String)

	/**
	 * Private holder for the MongoDB objects
	 */
	private val dbs = Map.empty[String, MongoDB].withDefault(dbName => init(dbName))
	
	/**
	 * Initialise the MongoDB system to create connections etc.
	 * 
	 * If the connection is local (i.e. offline or testing, then will connect to a database called '''DrinkService''')
	 * 
	 * @return The [[com.mongobd.casbah.MongoDB]] object
	 */
	private def init(dbName: String) : MongoDB = {
		val (host : String, port: Int, user: String, pass: String) = Option(System.getenv("VCAP_SERVICES")) match {
			case Some(s) => {
				try {
					parse(s) \\ "mongodb-1.8" match {
						case JArray(ary) => ary foreach { mongoJson =>
							val mongo = mongoJson.extract[CloudFoundryMongo]
							val credentials = mongo.credentials
							(credentials.hostname, credentials.port.toInt, credentials.password)
						}
						case x => logger.warn("Json parse error: %s".format(x))
					}
				}
			}
			case _ => ("localhost", 27017, "", "")
		}
		
		val connection = MongoConnection(host, port)
		val mongoDb = connection(dbName)
		if (user.nonEmpty) {
			mongoDb.authenticate(user, pass)
		}
		
		mongoDb
	}
	
	/**
	 * Get or create a named collection in the current database
	 */
	def getCollection(dbName: String, collectionId: String) : MongoCollection = {
		dbs(dbName)(collectionId)
	}
}