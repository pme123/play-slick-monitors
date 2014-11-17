package controllers

import models._
import play.api.Logger
import play.api.Play.current
import play.api.data.Forms._
import play.api.data._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.mvc._

object Application extends Controller {

  import Articles._
  import Clients._

  //JSON read/write macro
  implicit val articleFormat = Json.format[Article]
  implicit val clientFormat = Json.format[Client]

  def index = DBAction { implicit request =>
    Ok(views.html.index(articles.list, clients.list))
  }

  def server = DBAction { implicit request =>
    Ok(views.html.server(articles.list, clients.list))
  }

  def deleteClient(name: String) = DBAction { implicit request =>
    Logger.info("delete client: "+name)
    (articles filter (_.name === name)).delete

    Ok(views.html.server(articles.list, clients.list))
  }

  def client = DBAction { implicit request =>
    Ok(views.html.client())
  }

  val articleForm = Form(
    mapping(
      Articles.nameCol -> text(),
      Articles.descrCol -> text(),
      Articles.imgCol -> text()
    )(Article.apply)(Article.unapply)
  )

  def insertArticle() = DBAction { implicit request =>
    val article = articleForm.bindFromRequest.get
    articles.insert(article)
    Redirect(routes.Application.server())
  }


  def jsonFindAllArticles() = DBAction { implicit request =>
    Ok(toJson(articles.list))
  }

  def jsonFindAllClients() = DBAction { implicit request =>
    Ok(toJson(clients.list))
  }

  def jsonInsert() = DBAction(parse.json) { implicit request =>
    request.request.body.validate[Article].map { article =>
      articles.insert(article)
      Ok(toJson(article))
    }.getOrElse(BadRequest("invalid json"))
  }

}
