package actors.messages

sealed abstract class ConnectionEventType {
  val name: String
}

object ConnectionAdded extends ConnectionEventType {
  val name = "ADDED"
}

object ConnectionRemoved extends ConnectionEventType {
  val name = "REMOVED"
}
