package uk.co.randomcoding.drinkfinder.snippet

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

/**
 * TODO: Class Documentation
 *
 * @author: RandomCoder
 * Date: 02/06/11
 */
class DisplayResultsTest extends FunSuite with ShouldMatchers {

	import xml.NodeSeq

	test("Correct nodes are added to html for empty match") {
		import net.liftweb.http.LiftSession
		import net.liftweb.util.StringHelpers
		val snippet = new DisplayResults()

		val inputNodeSeq = testNodeSeq
		val session = new LiftSession("results", StringHelpers.randomString(20))
		snippet
	}

	private val testNodeSeq: NodeSeq = {
		<div id="beer-tab">
				<results:beers/>
		</div> <div id="cider-tab">
				<results:ciders/>
		</div> <div
		id="perry-tab">
				<results:perries/>
		</div>
	}
}