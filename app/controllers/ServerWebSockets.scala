package controllers

import java.util.UUID

import actors.messages._
import actors.{EventPublisher, ServerEventPublisher}
import akka.util.Timeout
import models._
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.mvc._
import slick.Connection

object ServerWebSockets extends Controller {

  val ref = EventPublisher.ref
  val serverRef = ServerEventPublisher.ref

  import scala.concurrent.duration._

  implicit val timeout = Timeout(1 second)

  import play.api.Play.current
  import play.api.mvc._

  def socket(): WebSocket[String, JsValue] = {
    WebSocket.acceptWithActor[String, JsValue] {
      implicit request => out => WebSocketActor.props(out)
    }
  }

  def famousSocket(): WebSocket[String, JsValue] = {
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
      val event = NewServerConnectionEvent(adminServer, out)
      ref ! event
      serverRef ! event
      Logger.info("after adminServer insert: " + adminServer)
    }

    @throws(classOf[Exception])
    override def postStop(): Unit = {
      val event = CloseServerConnectionEvent(adminServer)
      ref ! event
      serverRef ! event
      removeAdminServer(uuid)
    }

    def insertAdminServer(adminServer: AdminServer) =
      Connection.databaseObject().withSession { implicit session: Session =>
        adminServers.insert(adminServer)
        Redirect(routes.Application.server())
      }

    def removeAdminServer(uuid: String) = Connection.databaseObject().withSession { implicit session: Session =>
      (adminServers filter (_.uuid === uuid)).delete
      Redirect(routes.Application.server())
    }
  }

}

