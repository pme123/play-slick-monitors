package actors.messages

import akka.actor.ActorRef
import models.{AdminServer, Client}

case class CloseConnectionEvent(client: Client, out: ActorRef) {

}
case class CloseServerConnectionEvent(adminServer: AdminServer) {

}
