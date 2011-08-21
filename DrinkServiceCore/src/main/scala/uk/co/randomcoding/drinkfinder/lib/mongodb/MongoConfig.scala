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

	private var usingCloudfoundry = false
	private var cloudfoundryDbName = ""

	// Case classes required to parse CloudFoundry JSON
	case class CloudFoundryMongo(name : String, label : String, plan : String, credentials : CloudFoundryMongoCredentials)
	case class CloudFoundryMongoCredentials(hostname : String, port : String, username : String, password : String, name : String, db : String)

	/**
	 * Private holder for the MongoDB objects
	 */
	private var dbs = Map.empty[String, MongoDB].withDefault(dbName => init(dbName))

	/**
	 * Initialise the MongoDB system to create connections etc.
	 *
	 * If the connection is local (i.e. offline or testing, then will connect to a database called '''DrinkService''')
	 *
	 * @return The [[com.mongobd.casbah.MongoDB]] object
	 */
	private def init(dbName : String) : MongoDB = {
		logger.info("Env: VCAP_SERVICES: %s".format(Option(System.getenv("VCAP_SERVICES"))))
		var user = ""
		var port = 27017
		var host = "localhost"
		var pass = ""
		var db = dbName

		try {
			Option(System.getenv("VCAP_SERVICES")) match {
				case Some(s) => {
					try {
						parse(s) \\ "mongodb-1.8" match {
							case JArray(ary) => ary foreach { mongoJson =>
								val mongo = mongoJson.extract[CloudFoundryMongo]
								val credentials = mongo.credentials
								user = credentials.username
								port = credentials.port.toInt
								host = credentials.hostname
								pass = credentials.password
								db = credentials.db
								cloudfoundryDbName = db
								usingCloudfoundry = true
							}
							case x => logger.warn("Json parse error: %s".format(x))
						}
					}
				}
				case _ => logger.info("Not running on Cloud FOundry, assuming localhost connection on port 27017")
			}
		} catch {
			case e : MatchError => logger.error("Match Error: %s\n%s\n%s".format(e.getMessage(), e.getCause(), e.getStackTrace().mkString("", "\n", "")))
		}

		val connection = MongoConnection(host, port)
		val mongoDb = connection(db)
		if (user.nonEmpty) {
			mongoDb.authenticate(user, pass)
		}

		dbs = dbs + (db -> mongoDb)
		mongoDb
	}

	/**
	 * Get or create a named collection in the current database
	 */
	def getCollection(dbName : String, collectionId : String) : MongoCollection = {
		val conn = usingCloudfoundry match {
			case true => dbs(cloudfoundryDbName)
			case false => dbs(dbName)
		}
		logger.debug("Getting collection %s from %s".format(collectionId, conn))
		
		conn(collectionId)
	}
}