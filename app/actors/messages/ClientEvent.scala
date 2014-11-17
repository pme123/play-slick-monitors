package actors.messages

import play.api.libs.json.JsValue

trait ClientEvent {
    def json: JsValue

}
