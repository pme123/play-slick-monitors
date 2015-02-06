package controllers

import conf.ApplicationConf._
import play.api.Play.current
import play.api.db.slick._
import play.api.mvc._

object FamousApp extends Controller {

  def index = display(0)

  def display(order: Int) = displayPlaylist(order, DEFAULT_PLAY)

  def displayPlaylist(order: Int, playlist: String) = displayClientPlaylist(DEFAULT_CLIENT, order, playlist)

  def displayClientPlaylist(clientUUID: String, order: Int, playlist: String) = DBAction { implicit request =>
    Ok(views.html.famousclient(clientUUID, order, playlist))
  }

  def server = DBAction { implicit request =>
    Ok(views.html.famousserver())
  }

}
