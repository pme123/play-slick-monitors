package controllers

import actors.ServerEventPublisher
import actors.messages.{CloseConnectionEvent, JsonNoClient, NewConnectionEvent}
import akka.util.Timeout
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

  def clientSocket(clientUUID: String, playlist: String, order: Int): WebSocket[String, JsValue] = {
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
        val playlistTmp = if (playlist != Clients.notSetPlaylist) playlist else client.playlist
        val orderTmp = if (order != Clients.notSetOrder) order else client.order
        serverEventPublisher ! NewConnectionEvent(client.copy(order = orderTmp, playlist = playlistTmp), out)
      }
    }

    @throws(classOf[Exception])
    override def postStop(): Unit = {
      for (client <- retrieveClient(clientUUID)) serverEventPublisher ! CloseConnectionEvent(client, out)
    }

    private def retrieveClient(clientUUID: String): Option[Client] = Connection.databaseObject().withSession { implicit session: Session =>
      val clientQuery = Clients.clients filter (_.uuid === clientUUID)
      if (clientQuery.exists.run) {
        val client: Clients#TableElementType = clientQuery.first
        val overrideClientOrderPlaylist: Client = client.copy(order = order, playlist = playlist)
        Some(overrideClientOrderPlaylist)
      } else {
        Logger.info("WebSocket No client found: " + JsonNoClient(clientUUID) + " - " + out)
        out ! JsonNoClient(clientUUID).json
        None
      }
    }
  }


}

