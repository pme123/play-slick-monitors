package actors.messages

import actors.EventPublisher
import akka.actor.{Actor, ActorRef, Props}
import models._
import play.api.Logger
import play.api.db.slick.Config.driver.simple._
import play.libs.Akka
import slick.Connection


object ServerEventPublisher {
  val clientEventPublisher = EventPublisher.ref

  type Active = Boolean

  val ref: ActorRef = Akka.system.actorOf(Props.create(classOf[ServerEventPublisher]))
  var clientMap: Map[Client, Active] = Connection.databaseObject().withSession { implicit session: Session =>
    Clients.clients.list.map(c => c -> false).toMap
  }

  var clientSessionMap: Map[Client, ActorRef] = Map.empty
  var adminServers: Map[AdminServer, ActorRef] = Map.empty

  def props() = Props(new ServerEventPublisher())
}

class ServerEventPublisher() extends Actor {

  import actors.messages.ServerEventPublisher._

  override def receive: Receive = {
    case NewConnectionEvent(client, out) =>
      handleAddedClient(client, out)
    case CloseConnectionEvent(client) =>
      handleClosedClient(client)
    case NewServerConnectionEvent(adminServer, out) =>
      adminServers = adminServers + (adminServer -> out)
      sendAllClients(out)
      Logger.info("S: New AdminServer connected " + out + " - " + adminServer)
    case CloseServerConnectionEvent(adminServer) =>
      adminServers = adminServers - adminServer
      Logger.info("S: AdminServer " + adminServer + "is disconnected")
    case msg => Logger.info("S: unhandled msg: " + msg + "-" + sender)
      unhandled(msg)
  }


  def handleAddedClient(client: Client, out: ActorRef): Unit = {
    Logger.info("S: handleAddedClient msg: " + client)
    if (clientMap.contains(client)) {
      val hasActiveSession = clientSessionMap.contains(client)
      Logger.info("S: hasActiveSession msg: " + hasActiveSession)
      if (hasActiveSession) {
        out ! JsonDuplicateClient(client.uuid)
      } else {
        clientEventPublisher ! NewConnectionEvent(client, out)
        clientMap + (client -> true)
        clientSessionMap + (client -> out)
        sendToAllServer(client, ConnectionAdded)
        Logger.info("S: New browser connected " + out + " - " + client)
      }
    } else {
      Logger.error(s"S: Tried to add unregistered Player: $client")
    }
  }

  def handleClosedClient(client: Client): Unit = {
    if (clientMap.contains(client)) {
      clientMap + (client -> false)
      clientEventPublisher ! CloseConnectionEvent(client)
      sendToAllServer(client, ConnectionRemoved)
    }
    Logger.info("S: Browser " + client + "is disconnected")
  }

  private def sendToAllServer(client: Client, connectionEventType: ConnectionEventType) = {
    for (server <- adminServers.values) server ! new JsonClient(client, connectionEventType).json
  }

  private def sendAllClients(adminServerOut: ActorRef) = {
    Logger.info("S: All clients: " + clientMap)
    for {
      (client, active) <- clientMap
    } adminServerOut ! new JsonClient(client, ConnectionEventType.eventType(active)).json
  }
}