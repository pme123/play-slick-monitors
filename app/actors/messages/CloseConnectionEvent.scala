package actors.messages

import models.{AdminServer, Client}

case class CloseConnectionEvent(client: Client) {

}
case class CloseServerConnectionEvent(adminServer: AdminServer) {

}
