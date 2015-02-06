package actors.messages

import play.api.libs.json.{JsValue, Json}

/**
 * Created by pascal.mengelt on 05.02.2015.
 */
case class JsonNoClient(clientUUID: String) extends ClientEvent {
  def json: JsValue =
    Json.obj(
      "messageType" -> "exception",
      "uuid" -> clientUUID,
      "errorMsg" -> s"Sorry there is no Client with the Ident: $clientUUID"
    )
}
