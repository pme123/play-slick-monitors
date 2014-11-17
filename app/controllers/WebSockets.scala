package controllers

import java.util.UUID

import actors.EventPublisher
import actors.messages.{JsonClient, ClientEvent, CloseConnectionEvent, NewConnectionEvent}
import akka.util.Timeout
import com.fasterxml.jackson.databind.JsonNode
import com.sun.xml.internal.ws.resources.SenderMessages
import controllers.EchoWebSockets.WebSocketActorEcho
import models._
import play.api.Logger
import play.api.Play.current
import play.api.db.slick._
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api._
import play.api.db.slick.Config.driver.simple._
import play.libs.Akka
import play.api.Play.current
import slick.Connection


import scala.concurrent.ExecutionContext.Implicits.global

object WebSockets extends Controller {


  //JSON read/write macro
  //  implicit val articleFormat = Json.format[Article]
  //  implicit val clientFormat = Json.format[JsonNode]
  //  import play.api.mvc.WebSocket.FrameFormatter
  //
  //  implicit val inEventFrameFormatter = FrameFormatter.jsonFrame[Article]
  //  implicit val inEventFrameFormatter = FrameFormatter.jsonFrame[JsonNode]

  val ref = EventPublisher.ref

  import scala.concurrent.duration._

  implicit val timeout = Timeout(1 second)

  import play.api.mvc._
  import play.api.Play.current

  def socket = {
    WebSocket.acceptWithActor[String, JsValue] {
      implicit request => out => WebSocketActor.props(out)
    }
  }
  import akka.actor._

  // needed for Akka.system

  //  case class Start()
  //  case class Message(msg: String)
  //  case class Connected(out: (Enumerator[String], Channel[String]))
  //
  //  class EchoActor extends Actor {
  //    var out: (Enumerator[String], Channel[String]) = _
  //
  //    override def receive = {
  //      case Start() =>
  //        this.out = Concurrent.broadcast[String]
  //        sender ! Connected(out)
  //      case Message(msg) => this.out._2.push(msg)
  //    }
  //  }

  object WebSocketActor {
    def props(out: ActorRef) = Props(new WebSocketActor(out))
  }


  class WebSocketActor(out: ActorRef) extends Actor {

    import models.Clients._
    import play.api.db.slick.Config.driver.simple._

    val uuid = UUID.randomUUID().toString
    val client = new Client(uuid)

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

