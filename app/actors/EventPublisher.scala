package actors

import actors.messages._
import akka.actor.{Actor, ActorRef, Props}
import models.{Article, Client}
import play.api.Logger
import play.api.mvc.WebSocket
import play.libs.Akka
import play.mvc.Results.Chunks.Out
import play.mvc.WebSocket.In

object EventPublisher{
  val ref: ActorRef = Akka.system.actorOf(Props.create(classOf[EventPublisher]))
  var connections:Map[Client, ActorRef]=Map.empty

  def props() = Props(new EventPublisher())
}

class EventPublisher() extends Actor  {
   import actors.EventPublisher._

  override def receive: Receive = {
    case  NewConnectionEvent(client, out) =>    connections= connections + (client -> out)
      Logger.info("New browser connected " +out+ " - "+ client)
      out ! new  JsonClient(client).json
    case CloseConnectionEvent(uuid) => connections = connections - uuid
      Logger.info("Browser " + uuid + "is disconnected")
    case msg: Article => broadcastEvent(new JsonArticle(msg))
//    case msg: Client => broadcastEvent(new JsonClient(msg))
//    if (message.isInstanceOf[NewConnectionEvent]) {
//      val nce: NewConnectionEvent = message.asInstanceOf[NewConnectionEvent]
//      connections++=(nce.uuid, nce.out)
//
//    }
//    else if (message.isInstanceOf[CloseConnectionEvent]) {
//      val cce: CloseConnectionEvent = message.asInstanceOf[CloseConnectionEvent]
//      val uuid: String = cce.uuid
//      connections.remove(uuid)
//
//    }

    case msg => Logger.info("unhandled msg: "+msg+"-"+sender)
      unhandled(msg)
  }

  private def broadcastEvent(message: ClientEvent) {
    for (out <- connections.values) {
      Logger.info("-->broadcastEvent " + message.json +" - "+out)
      out ! message.json
    }
  }

}
