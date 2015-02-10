package google

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.google.maps.GeoApiContext
import models.{Address, Addresses}
import play.libs.Akka
import slick.Connection

import scala.concurrent._
import scala.concurrent.duration._

/**
 * Created by pascal.mengelt on 07.02.2015.
 */

object GoogleMapService {
  type AddressWithLatLng = (Address, (Double, Double))
  type AddressesWithLatLng = Map[Address, (Double, Double)]
}

case class GoogleMapService(addresses: List[Address]) {

  import google.GoogleMapService._

  implicit val timeout = Timeout(20 seconds) // needed for `?` below


  def getCoordinates(): AddressesWithLatLng = {

    val ref: ActorRef = Akka.system.actorOf(Props.create(classOf[MapServiceActor]))

    val futureResults = (ref ? LatLngRequest).mapTo[AddressesWithLatLng] // call by implicit conversion

    // all to finish
    Await.result(futureResults, 20 seconds)
  }
}

object MapServiceActor {

  def props() = Props(new MapServiceActor())
}

case object LatLngRequest

class MapServiceActor() extends Actor {
  val apiKey = "AIzaSyBZztmrv7hqwutjpQWB3Jtvj15Sq3IxxP4"
  val geoContext: GeoApiContext = new GeoApiContext().setApiKey(apiKey);


  import play.api.db.slick.Config.driver.simple._

  var results: Map[Address, (Double, Double)] = Map.empty
  var addresses: List[Address] = List.empty
  var origSender: ActorRef = ActorRef.noSender

  override def receive: Receive = {
    case LatLngRequest =>
      origSender = sender
      startRetrieving()
    case result: (Address, (Double, Double)) =>
      results = results + result
      if (results.size == addresses.size) {
        println("Result to get: " + results.size + "::" + results)
        origSender ! results
      }
  }

  private def startRetrieving() = Connection.databaseObject().withSession { implicit session: Session =>
    addresses = Addresses.addresses.list
    println("addresses to get: " + addresses.size)
    if (addresses.isEmpty) origSender ! results
    for (address: Address <- addresses) {
      val ref: ActorRef = Akka.system.actorOf(Props.create(classOf[GeocodingActor], geoContext))
      ref ! address
    }
  }
}
