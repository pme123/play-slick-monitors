package actors.messages

import akka.actor.ActorRef
import models.{AdminServer, Client}


case class NewConnectionEvent(client: Client, out: ActorRef) {

}
case class NewServerConnectionEvent(adminServer: AdminServer, out: ActorRef) {

}
