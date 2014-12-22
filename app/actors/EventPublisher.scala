package actors

import actors.messages._
import akka.actor.{Actor, ActorRef, Props}
import conf.ApplicationConf._
import models.{Article, Articles, Client}
import play.api.Logger
import play.api.db.slick.Config.driver.simple._
import play.api.libs.json.JsValue
import play.libs.Akka
import slick.Connection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


object EventPublisher {
  val ref: ActorRef = Akka.system.actorOf(Props.create(classOf[EventPublisher]))
  var nextArticles: Map[String, NextArticle] = Map.empty

  def props() = Props(new EventPublisher())
}

class EventPublisher() extends Actor {

  import actors.EventPublisher._

  override def receive: Receive = {
    case NewConnectionEvent(client, out) =>
      nextArticles.get(client.playlist) match {
        case Some(nextArticle: NextArticle) => nextArticle.addClient(client, out)
        case _ =>
          val nextArticle = new NextArticle(client.playlist)
          nextArticle.addClient(client, out)
          nextArticles = nextArticles + (client.playlist -> nextArticle)
      }

      Logger.info("New browser connected " + out + " - " + client)
      out ! new JsonClient(client).json
    case CloseConnectionEvent(client) =>
      nextArticles.get(client.playlist) match {
        case Some(nextArticle: NextArticle) => nextArticle.removeClient(client)
        case None => assert(false, "There should be a NextArticle!") // should not happen
      }
      Logger.info("Browser " + client + "is disconnected")
    case msg: String => broadcastEvent()

    case msg => Logger.info("unhandled msg: " + msg + "-" + sender)
      unhandled(msg)
  }

  private def broadcastEvent() {
    Logger.info("broadcastEvent")
    for (nextArticle: NextArticle <- nextArticles.values) {
      nextArticle.broadcastEvent()
    }
  }


}

class NextArticle(playlist: String) {
  var lastArticle: Option[Article] = None
  var connections: Map[Client, ActorRef] = Map.empty

  def addClient(client: Client, actorRef: ActorRef) =  connections = connections + (client -> actorRef)

  def removeClient(client: Client) = connections = connections - client

  def retrieveNextArticle() = {
    Connection.databaseObject().withSession { implicit session: Session =>
      val articleOption: Option[Article] = Articles.nextArticle(lastArticle, playlist)
      lastArticle = articleOption
    }
  }

  def broadcastEvent() = {
    retrieveNextArticle()
    for (article <- lastArticle) {
      val json = new JsonArticle(article).json
      for (client <- connections.keys) {
        Akka.system.scheduler.scheduleOnce(client.order * NEXTARTICLE_DELAY_INSECONDS second)(sendArticle(client, json))
      }
    }
  }

  def sendArticle(client: Client, json: JsValue) = {
    connections.get(client) match {
      case Some(actorRef) => actorRef ! json
      case None =>  // client was closed in the meantime
    }
  }
}