package controllers

import conf.ApplicationConf._
import play.api.Play.current
import play.api.db.slick._
import play.api.mvc._

object FamousApp extends Controller {

  def index = display(0)

  def display(order: Int) = run(order, DEFAULT_PLAY)

  def run(order: Int, playlist: String) = DBAction { implicit request =>
    Ok(views.html.famousclient(order, playlist))
  }

  def server = DBAction { implicit request =>
    Ok(views.html.famousserver())
  }

}
