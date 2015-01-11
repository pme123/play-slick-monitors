package models

import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted
import scala.slick.lifted.TableQuery

object AdminServers extends lifted.TableQuery(new AdminServers(_)){
  val name = "ADMIN_SERVER"
  val uuidCol="uuid"
  val adminServers = TableQuery[AdminServers]

  def findByUUID(uuid:String) = adminServers filter (_.uuid === uuid)

}

case class AdminServer(uuid: String)


/* Table mapping
 */
class AdminServers(tag: Tag) extends Table[AdminServer](tag, AdminServers.name) {
  import models.AdminServers._

  def uuid = column[String](uuidCol, O.PrimaryKey)

  def * = uuid <>(AdminServer.apply, AdminServer.unapply)
}
