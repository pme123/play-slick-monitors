package actors.messages

import actors.ServerEventPublisher.Active

object ConnectionEventType {
  def eventType(active: Active) = if (active) ConnectionAdded else ConnectionRemoved
}

sealed abstract class ConnectionEventType(val name: String) {

}

object ConnectionAdded extends ConnectionEventType("ADDED") {
}

object ConnectionRemoved extends ConnectionEventType("REMOVED") {
}
