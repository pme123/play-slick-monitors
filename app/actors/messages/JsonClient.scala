package actors.messages

import models.Client
import play.api.libs.json.{JsValue, Json}


class JsonClient(client: Client) extends ClientEvent {

  def json: JsValue =
    Json.obj(
      "messageType" -> "client",
      "uuid" -> client.uuid
    )
}
