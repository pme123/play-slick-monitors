package actors.messages

import models.Location
import play.api.libs.json.{JsValue, Json}


case class JsonLocation(location: Location, locationStatus: LocationStatus) extends ClientEvent {

  import models.Locations._

  def json: JsValue =
    Json.obj(
      "messageType" -> "location",
      "locationStatus" -> locationStatus.name,
      uuidCol -> location.uuid,
      latitudeCol -> location.latitude,
      longitudeCol -> location.longitude,
      addressIdCol -> location.addressId
    )
}

sealed abstract class LocationStatus(val name: String)

case object MissingAllClients extends LocationStatus("missingall")

case object MissingClients extends LocationStatus("missing")

case object LoadedClients extends LocationStatus("loaded")
