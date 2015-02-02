package actors.messages

import models.Client
import play.api.libs.json.{JsValue, Json}


class JsonClient(client: Client, connectionEventType: ConnectionEventType) extends ClientEvent {

  def json: JsValue =
    Json.obj(
      "messageType" -> "client",
      "connectionEvent" -> connectionEventType.name,
      "uuid" -> client.uuid
    )
}
