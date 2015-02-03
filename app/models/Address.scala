package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted
import scala.slick.lifted.TableQuery

object Addresses extends lifted.TableQuery(new Addresses(_)) {
  val name = "ADDRESS"
  val idCol = "ID"
  val streetCol = "street"
  val streetNrCol = "streetNr"
  val cityZipCol = "cityZip"
  val cityCol = "city"
  val addresses = TableQuery[Addresses]

  def findByCityZip(cityZip: Int) = addresses filter (_.cityZip === cityZip)

}

case class Address(id: Int, street: String, streetNr: String, cityZip: Int, city: String)


/* Table mapping
 */
class Addresses(tag: Tag) extends Table[Address](tag, Addresses.name) {

  import models.Addresses._

  def id = column[Int](idCol, O.PrimaryKey, O.AutoInc)

  def street = column[String](streetCol, O.NotNull)

  def streetNr = column[String](streetNrCol)

  def cityZip = column[Int](cityZipCol, O.NotNull)

  def city = column[String](cityCol, O.NotNull)

  def * = (id, street, streetNr, cityZip, city) <>(Address.tupled, Address.unapply)
}
