package actors.messages

import akka.actor.ActorRef
import models.Client


case class NewConnectionEvent(client: Client, out: ActorRef) {

}
