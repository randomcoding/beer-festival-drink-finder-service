/**
 *
 */
package uk.co.randomcoding.drinkfinder.snippet

import uk.co.randomcoding.drinkfinder.model.matcher.id.{BREWER_NAME, DRINK_NAME}
import net.liftweb.http._
import net.liftweb.util.Helpers._
import scala.xml.{ NodeSeq, Text }
import uk.co.randomcoding.drinkfinder.model.data.wbf.WbfDrinkDataAccess
import uk.co.randomcoding.drinkfinder.model.drink.{ Beer, Cider, Drink, Perry }
import uk.co.randomcoding.drinkfinder.model.matcher.{MatcherFactory, BrewerNameMatcher}
import net.liftweb.common.Logger

/**
 * @author RandomCoder
 *
 */
class DisplayResults extends Logger {

  private val drinkData = new WbfDrinkDataAccess()
  private val BrewerNameQueryRegex = "$%s=(\\.*)".format(BREWER_NAME.id)
  
  private val beerResultsId = "beerResults"
  private val ciderResultsId = "ciderResults"
  private val perryResultsId = "perryResults"

  def calculateResults/*(in : NodeSeq) : NodeSeq*/ = {
    import uk.co.randomcoding.drinkfinder.model.matcher.Matcher
    val params = S.queryString openOr "No Query String"
    debug("Received Query String: %s".format(params))

    val matchers = params match {
      case "No Query String" => Nil
      case paramString : String => MatcherFactory.generate(paramString)
    }
    debug("Generated %d matchers:\n%s".format(matchers.size, matchers.mkString("\n\t")))

    val matchingDrinks = drinkData.getMatching(matchers)

    debug("There are %d matching drinks:\n%s".format(matchingDrinks.size, matchingDrinks.mkString("\n\t")))
    val beers = matchingDrinks.filter(_.isInstanceOf[Beer])
    debug("There are %d matching beers".format(beers.size))
    val ciders = matchingDrinks.filter(_.isInstanceOf[Cider])
    debug("There are %d matching ciders".format(ciders.size))
    val perries = matchingDrinks.filter(_.isInstanceOf[Perry])
    debug("There are %d matching perries".format(perries.size))
    
    "#resultTabs" #> generateResultTabs(beers, ciders, perries) &
    "#%s".format(beerResultsId) #> convertResultsToDisplayForm(beerResultsId, beers) &
    "#%s".format(ciderResultsId) #> convertResultsToDisplayForm(ciderResultsId, ciders) &
    "#%s".format(perryResultsId) #> convertResultsToDisplayForm(perryResultsId, perries)
  }
  
  val anchorRef = ((anchor: String) => "#" + anchor)
  
  private [this] def generateResultTabs(beers: Iterable[Drink], ciders: Iterable[Drink], perries: Iterable[Drink]) : NodeSeq = {
    val beersLink = beers.toList match {
      case Nil => Text("")
      case _ => <li><a href={anchorRef(beerResultsId)}>Beers</a></li>
    }
    val cidersLink = ciders.toList match {
    case Nil => Text("")
    case _ => <li><a href={anchorRef(ciderResultsId)}>Ciders</a></li>
    }
    val perryLink = perries.toList match {
    case Nil => Text("")
    case _ => <li><a href={anchorRef(perryResultsId)}>Perries</a></li>
    }
    
    <ul>  {beersLink} {cidersLink} {perryLink }</ul>
  }

  private def convertResultsToDisplayForm(divId: String, results : Iterable[Drink]) : NodeSeq = {
    val sortedDrinks = results.toList.sortBy(_.name)
    
    println("Sorted %s into %s".format(results.mkString(","), sortedDrinks.mkString(",")))

    val drinkEntries = for {
      drink <- sortedDrinks
    } yield {
      val drinkHref = "drink?" + DRINK_NAME + "=" + drink.name
      val abvString = "%.1f".format(drink.abv)
      val priceString = "%.2f".format(drink.price)

        <span class="drinkText">
          <table class="drinkList" style="border: thin;">
            <tr>
              <td colspan="2" class="drinkTitle"><a href={ drinkHref }>{ drink.name }</a></td>
            </tr>
            <tr class="drinkData">
              <td>ABV:{ abvString }%</td>
              <td>£{ priceString }</td>
            </tr>
            <tr class="drinkDescription">
              <td colspan="2">{ drink.description }</td>
            </tr>
          </table>
        </span>
    }
    <div id={divId}> {drinkEntries} </div>
  }
}