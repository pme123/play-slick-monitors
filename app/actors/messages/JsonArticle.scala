package actors.messages

import models.Article
import play.api.libs.json.JsValue
import play.api.libs.json.Json


class JsonArticle(article: Article) extends ClientEvent {

  def json: JsValue =
  Json.obj(
        "messageType" -> "article",
        "name" -> article.name,
        "descr" -> article.descr,
        "img" -> article.img

  )
}