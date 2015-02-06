package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted
import scala.slick.lifted.TableQuery

object Locations extends lifted.TableQuery(new Locations(_)) {
  val name = "LOCATION"
  val idCol = "ID"
  val uuidCol = "uuid"
  val addressIdCol = "addressId"
  val locations = TableQuery[Locations]

  def findByUUID(uuid: String) = locations filter (_.uuid === uuid)

}

case class Location(id: Int, uuid: String, addressId: Int)


/* Table mapping
 */
class Locations(tag: Tag) extends Table[Location](tag, Locations.name) {

  import models.Locations._

  def id = column[Int](idCol, O.PrimaryKey, O.AutoInc)

  def uuid = column[String](uuidCol, O.NotNull)

  def addressId = column[Int](addressIdCol, O.NotNull)

  def * = (id, uuid, addressId) <>(Location.tupled, Location.unapply)

  def address = foreignKey(Addresses.name, addressId, Addresses.addresses)(_.id)
}
