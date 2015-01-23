package actors.messages

import models.Article
import play.api.libs.json.{JsValue, Json}


class JsonArticle(article: Article) extends ClientEvent {
  var all: List[String] = List.empty
  def json: JsValue =
  Json.obj(
        "messageType" -> "article",
        "name" -> article.name,
        "descr" -> article.descr,
    "img" -> article.img,
    "playlist" -> article.playlist,
    "active" -> article.active

  )
}