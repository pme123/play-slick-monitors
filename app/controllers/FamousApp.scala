package controllers

import models._
import play.api.Play.current
import play.api.db.slick._
import play.api.libs.json.Json
import play.api.mvc._

object FamousApp extends Controller {

  //JSON read/write macro
  implicit val articleFormat = Json.format[Article]
  implicit val clientFormat = Json.format[Client]

  def index = DBAction { implicit request =>
    Ok(views.html.famousclient())
  }

  def display(order: Int) = DBAction { implicit request =>
    Ok(views.html.famousclient())
  }
}
