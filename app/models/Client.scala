package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted
import scala.slick.lifted.TableQuery

object Clients extends lifted.TableQuery(new Clients(_)){
  val name = "CLIENT"
  val uuidCol="uuid"
  val clients = TableQuery[Clients]

  def findByUUID(uuid:String) = clients filter (_.uuid === uuid)

}
case class Client(uuid: String)


/* Table mapping
 */
class Clients(tag: Tag) extends Table[Client](tag, Clients.name) {
  import models.Clients._

  def uuid = column[String](uuidCol, O.PrimaryKey)

  def * = uuid <> (Client.apply, Client.unapply)
}
