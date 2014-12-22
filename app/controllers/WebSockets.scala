package controllers

import java.util.UUID

import actors.EventPublisher
import actors.messages.{CloseConnectionEvent, NewConnectionEvent}
import akka.util.Timeout
import conf.ApplicationConf
import models._
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.mvc._
import slick.Connection

object WebSockets extends Controller {

  val ref = EventPublisher.ref

  import scala.concurrent.duration._

  implicit val timeout = Timeout(1 second)

  import play.api.Play.current
  import play.api.mvc._

  def socket(): WebSocket[String, JsValue] =   orderredSocket(0)
  def orderredSocket(order: Int): WebSocket[String, JsValue] = orderredPlaySocket(order, ApplicationConf.DEFAULT_PLAY)

  def orderredPlaySocket(order: Int, playlist: String): WebSocket[String, JsValue] = {
    WebSocket.acceptWithActor[String, JsValue] {
      implicit request => out => WebSocketActor.props(out, order, playlist)
    }
  }
  import akka.actor._

  object WebSocketActor {
    def props(out: ActorRef, order: Int, playlist: String) = Props(new WebSocketActor(out, order, playlist))
  }


  class WebSocketActor(out: ActorRef, order: Int, playlist: String) extends Actor {

    import models.Clients._
    import play.api.db.slick.Config.driver.simple._

    val uuid = UUID.randomUUID().toString
    val client = new Client(uuid, order, playlist)

    def receive = {
      case msg: String => Logger.info("ClientEvent received: " + msg)
        out ! msg
      case msg => Logger.info("Unhandled Message received: " + msg)
        unhandled(msg)
    }


    @throws(classOf[Exception])
    override def preStart(): Unit = {
      super.preStart()
      Logger.info("Webactor started: " + uuid + " - " + out)
      out ! client
      insertClient(client)
      ref ! NewConnectionEvent(client, out)

      Logger.info("after client insert: " + client)


    }

    @throws(classOf[Exception])
    override def postStop(): Unit = {
      ref ! CloseConnectionEvent(client)
      removeClient(uuid)
    }

    def insertClient(client: Client) =
      Connection.databaseObject().withSession { implicit session: Session =>
        clients.insert(client)
        Redirect(routes.Application.index())
      }

    def removeClient(uuid: String) = Connection.databaseObject().withSession { implicit session: Session =>
      (clients filter (_.uuid === uuid)).delete
      Redirect(routes.Application.index())
    }
  }

}

