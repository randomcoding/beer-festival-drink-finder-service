/**
 * Copyright (C) 2012 - RandomCoder <randomcoder@randomcoding.co.uk>
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
 *    RandomCoder <randomcoder@randomcoding.co.uk> - initial API and implementation and/or initial documentation
 */
package uk.co.randomcoding.drinkfinder.model.drink

/**
 * Enumeration of the valid statuses of drinks at a festival
 *
 * @author RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * Created On: 29 Jul 2012
 */
object DrinkRemainingStatus {
  type status = String

  val PLENTY = "Plenty"
  val LESS_THAN_HALF = "Less than Half"
  val RUNNING_OUT = "Running Out"
  val FINISHED = "Finished"
  val BEING_PREPARED = "Being Prepared"
  val NOT_AVAILABLE = "Not Available"
}
