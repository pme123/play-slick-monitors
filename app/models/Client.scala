package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted
import scala.slick.lifted.TableQuery

object Clients extends lifted.TableQuery(new Clients(_)){
  val notSetPlaylist = "intro"
  val notSetOrder = -1
  val name = "CLIENT"
  val uuidCol="uuid"
  val orderCol = "order"
  val playlistCol="playlist"
  val locationIdCol = "locationId"
  val clients = TableQuery[Clients]

  def retrieveByUUID(uuid: String)(implicit session: Session): Client = (clients filter (_.uuid === uuid)).first

  def retrieveByLocation(location: Location)(implicit session: Session): List[Client] = (clients filter (_.locationId === location.id)).list


}

case class Client(uuid: String, order: Int, playlist: String, locationId: Int)


/* Table mapping
 */
class Clients(tag: Tag) extends Table[Client](tag, Clients.name) {
  import models.Clients._

  def uuid = column[String](uuidCol, O.PrimaryKey)

  def order = column[Int](orderCol, O.NotNull)

  def playlist = column[String](playlistCol, O.NotNull)

  def locationId = column[Int](locationIdCol, O.NotNull)

  def * = (uuid, order, playlist, locationId) <>(Client.tupled, Client.unapply)

  def location = foreignKey(Locations.name, locationId, Locations.locations)(_.id)

}
