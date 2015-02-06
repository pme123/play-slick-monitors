package actors.messages

import play.api.libs.json.{JsValue, Json}

/**
 * Created by pascal.mengelt on 06.02.2015.
 */
case class JsonDuplicateClient(clientUUID: String) extends ClientEvent {
  def json: JsValue =
    Json.obj(
      "messageType" -> "exception",
      "uuid" -> clientUUID,
      "errorMsg" -> s"Sorry there is already a Client with the Ident: $clientUUID"
    )
}
