package actors

import actors.messages._
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
  var clientMap: Map[String, (Client, Active)] = Connection.databaseObject().withSession { implicit session: Session =>
    Clients.clients.list.map(c => c.uuid ->(c, false)).toMap
  }

  lazy val locationMap: Map[Location, List[Client]] = Connection.databaseObject().withSession { implicit session: Session =>
    val locations: List[Location] = Locations.locations.list

    def findClients(location: Location): List[Client] = (Clients.clients filter (_.locationId === location.id)).list
    locations.map(l => l -> findClients(l)).toMap[Location, List[Client]]
  }
  var clientSessionMap: Map[String, ActorRef] = Map.empty
  var adminServers: Map[AdminServer, ActorRef] = Map.empty

  def props() = Props(new ServerEventPublisher())
}

class ServerEventPublisher() extends Actor {

  import actors.ServerEventPublisher._


  def evalLocationStatus(clients: List[Client]): LocationStatus = {
    type MissingLoaded = (Boolean, Boolean)
    val result: MissingLoaded = clients.foldRight((true, true))((client, result) => {
      val active: Active = clientMap(client.uuid)._2
      val resultA = if (active) false else result._1
      val resultB = if (!active) false else result._2
      (resultA, resultB)
    })
    result match {
      case (true, _) => MissingAllClients
      case (false, false) => MissingClients
      case (false, true) => LoadedClients
    }
  }

  override def receive: Receive = {
    case NewConnectionEvent(client, out) =>
      handleAddedClient(client, out)
    case CloseConnectionEvent(client, out) =>
      handleClosedClient(client, out)
    case NewServerConnectionEvent(adminServer, out) =>
      adminServers = adminServers + (adminServer -> out)
      sendAllClients(out)
      for (location <- locationMap.keys) sendLocation(out, location)
      Logger.info("S: New AdminServer connected " + out + " - " + adminServer)
    case CloseServerConnectionEvent(adminServer) =>
      adminServers = adminServers - adminServer

      Logger.info("S: AdminServer " + adminServer + "is disconnected")
    case msg => Logger.info("S: unhandled msg: " + msg + "-" + sender)
      unhandled(msg)
  }


  def sendLocation(out: ActorRef, location: Location): Unit = {
    out ! new JsonLocation(location, evalLocationStatus(locationMap(location))).json
  }

  def sendLocationForClient(out: ActorRef, client: Client): Unit = Connection.databaseObject().withSession { implicit session: Session =>
    val location: Location = Locations.locations.filter(_.id === client.locationId).first
    for (out <- adminServers.values) (sendLocation(out, location))
  }

  def handleAddedClient(client: Client, out: ActorRef): Unit = {
    Logger.info("S: handleAddedClient msg: " + client)
    if (clientMap.contains(client.uuid)) {
      val hasActiveSession = clientSessionMap.contains(client.uuid)
      Logger.info("S: hasActiveSession msg: " + hasActiveSession)
      if (hasActiveSession) {
        Logger.info("S: JsonDuplicateClient msg: " + clientSessionMap)
        out ! JsonDuplicateClient(client.uuid).json
      } else {
        clientEventPublisher ! NewConnectionEvent(client, out)
        clientMap = clientMap + (client.uuid ->(client, true))
        clientSessionMap = clientSessionMap + (client.uuid -> out)
        sendLocationForClient(out, client)
        sendToAllServer(client, ConnectionAdded)
        Logger.info("S: New browser connected " + out + " - " + client)
      }
    } else {
      Logger.error(s"S: Tried to add unregistered Player: $client")
    }
  }

  def handleClosedClient(client: Client, out: ActorRef): Unit = {
    Logger.info("S: out " + out + "is " + clientSessionMap.get(client.uuid))
    if (clientSessionMap.contains(client.uuid) && clientSessionMap(client.uuid).equals(out)) {
      clientMap = clientMap + (client.uuid ->(client, false))
      clientSessionMap = clientSessionMap - client.uuid
      clientEventPublisher ! CloseConnectionEvent(client, out)
      sendLocationForClient(out, client)
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
      (_, (client, active)) <- clientMap
    } adminServerOut ! new JsonClient(client, ConnectionEventType.eventType(active)).json
  }
}
