package controllers

import actors.messages.{CloseConnectionEvent, JsonNoClient, NewConnectionEvent, ServerEventPublisher}
import akka.util.Timeout
import conf.ApplicationConf._
import models._
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.mvc._
import slick.Connection

object WebSockets extends Controller {

  val serverEventPublisher = ServerEventPublisher.ref


  import scala.concurrent.duration._

  implicit val timeout = Timeout(1 second)

  import play.api.Play.current
  import play.api.mvc._

  def socket(): WebSocket[String, JsValue] = orderredSocket(0)

  def orderredSocket(order: Int): WebSocket[String, JsValue] = {
    orderredPlaySocket(order, DEFAULT_PLAY)
  }

  def orderredPlaySocket(order: Int, playlist: String): WebSocket[String, JsValue] = {
    WebSocket.acceptWithActor[String, JsValue] {
      implicit request => out => WebSocketActor.props(out, DEFAULT_CLIENT, order, playlist)
    }
  }

  def clientOrderredPlaySocket(clientUUID: String, order: Int, playlist: String): WebSocket[String, JsValue] = {
    WebSocket.acceptWithActor[String, JsValue] {
      implicit request => out => WebSocketActor.props(out, clientUUID, order, playlist)
    }
  }

  import akka.actor._

  object WebSocketActor {
    def props(out: ActorRef, clientUUID: String, order: Int, playlist: String) = Props(new WebSocketActor(out, clientUUID: String, order, playlist))
  }


  class WebSocketActor(out: ActorRef, clientUUID: String, order: Int, playlist: String) extends Actor {

    import play.api.db.slick.Config.driver.simple._


    def receive = {
      case msg: String => Logger.info("ClientEvent received: " + msg)
        out ! msg
      case msg => Logger.info("Unhandled Message received: " + msg)
        unhandled(msg)
    }


    @throws(classOf[Exception])
    override def preStart(): Unit = {
      super.preStart()
      Logger.info("Webactor started: " + clientUUID + " - " + out)
      for (client <- retrieveClient(clientUUID)) {
        Logger.info("after client insert: " + clientUUID)
        serverEventPublisher ! NewConnectionEvent(client, out)
      }
    }

    @throws(classOf[Exception])
    override def postStop(): Unit = {
      for (client <- retrieveClient(clientUUID)) (serverEventPublisher ! CloseConnectionEvent(client))
    }

    private def retrieveClient(clientUUID: String): Option[Client] = Connection.databaseObject().withSession { implicit session: Session =>
      val clientQuery = Clients.findByUUID(clientUUID)
      if (clientQuery.exists.run) {
        Some(clientQuery.first)
      } else {
        Logger.info("WebSocket No client found: " + JsonNoClient(clientUUID) + " - " + out)
        out ! JsonNoClient(clientUUID).json
        None
      }
    }
  }


}

