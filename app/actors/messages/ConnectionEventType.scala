package actors.messages

import actors.messages.ServerEventPublisher.Active

object ConnectionEventType {
  def eventType(active: Active) = if (active) ConnectionAdded else ConnectionRemoved
}
sealed abstract class ConnectionEventType {
  val name: String
}

object ConnectionAdded extends ConnectionEventType {
  val name = "ADDED"
}

object ConnectionRemoved extends ConnectionEventType {
  val name = "REMOVED"
}
