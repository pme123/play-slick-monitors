package controllers

import akka.util.Timeout
import models.Articles._
import play.api.Play.current
import play.api._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.mvc._
object EchoWebSockets extends Controller {


  import scala.concurrent.duration._

  implicit val timeout = Timeout(1 second)

  def index = DBAction { implicit request =>
    Ok(views.html.websocket(articles.list))
  }


  import play.api.Play.current
  import play.api.mvc._

  def echo = WebSocket.acceptWithActor[String, String] { request => out =>
    WebSocketActorEcho.props(out)
  }
  import akka.actor._


  object WebSocketActorEcho {
    def props(out: ActorRef) = Props(new WebSocketActorEcho(out))
  }


  class WebSocketActorEcho(out: ActorRef) extends Actor {

    def receive = {
      case msg: String => Logger.info("ClientEvent received: " + msg)
        out ! "Thanks for your message: "+ msg
      case msg => Logger.info("Unhandled Message received: " + msg)
        unhandled(msg)
    }

  }

}

