package google

import akka.actor.{Actor, Props}
import com.google.maps.{GeoApiContext, GeocodingApi}
import models.Address

object GeocodingActor {

  def props(geoContext: GeoApiContext): Props = Props(new GeocodingActor(geoContext))
}

/**
 * Created by pascal.mengelt on 08.02.2015.
 */
class GeocodingActor(geoContext: GeoApiContext) extends Actor {

  override def receive: Receive = {
    case address: Address =>
      val req = GeocodingApi.geocode(geoContext,
        s"${address.street} ${address.streetNr} ${address.cityZip} ${address.city}")
      val latLng = req.await()(0).geometry.location
      sender ! (address ->(latLng.lat, latLng.lng))
  }
}
