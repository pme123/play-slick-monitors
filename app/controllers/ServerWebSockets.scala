package controllers

import java.util.UUID

import actors.EventPublisher
import actors.messages.{CloseServerConnectionEvent, NewServerConnectionEvent, CloseConnectionEvent, NewConnectionEvent}
import akka.util.Timeout
import models._
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.mvc._
import slick.Connection

object ServerWebSockets extends Controller {

  val ref = EventPublisher.ref

  import scala.concurrent.duration._

  implicit val timeout = Timeout(1 second)

  import play.api.Play.current
  import play.api.mvc._

  def socket(): WebSocket[String, JsValue] = {
    WebSocket.acceptWithActor[String, JsValue] {
      implicit request => out => WebSocketActor.props(out)
    }
  }
  import akka.actor._

  object WebSocketActor {
    def props(out: ActorRef) = Props(new WebSocketActor(out))
  }


  class WebSocketActor(out: ActorRef) extends Actor {

    import models.AdminServers._
    import play.api.db.slick.Config.driver.simple._

    val uuid = UUID.randomUUID().toString
    val adminServer = new AdminServer(uuid)

    def receive = {
      case msg: String => Logger.info("AdminServerEvent received: " + msg)
        out ! msg
      case msg => Logger.info("Unhandled Message received: " + msg)
        unhandled(msg)
    }


    @throws(classOf[Exception])
    override def preStart(): Unit = {
      super.preStart()
      Logger.info("Webactor started: " + uuid + " - " + out)
      out ! adminServer
      insertAdminServer(adminServer)
      ref ! NewServerConnectionEvent(adminServer, out)

      Logger.info("after adminServer insert: " + adminServer)


    }

    @throws(classOf[Exception])
    override def postStop(): Unit = {
      ref ! CloseServerConnectionEvent(adminServer)
      removeAdminServer(uuid)
    }

    def insertAdminServer(adminServer: AdminServer) =
      Connection.databaseObject().withSession { implicit session: Session =>
        adminServers.insert(adminServer)
        Redirect(routes.Application.index())
      }

    def removeAdminServer(uuid: String) = Connection.databaseObject().withSession { implicit session: Session =>
      (adminServers filter (_.uuid === uuid)).delete
      Redirect(routes.Application.index())
    }
  }

}

