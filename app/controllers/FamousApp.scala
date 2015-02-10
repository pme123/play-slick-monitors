package controllers

import models.Clients
import play.api.Play.current
import play.api.db.slick._
import play.api.mvc._

object FamousApp extends Controller {


  def display(clientUUID: String) = displayPlaylist(clientUUID, Clients.notSetPlaylist)

  def displayPlaylist(clientUUID: String, playlist: String) = displayPlaylistOrdered(clientUUID, playlist, Clients.notSetOrder)

  def displayPlaylistOrdered(clientUUID: String, playlist: String, order: Int) = DBAction { implicit request =>
    Ok(views.html.famousclient(clientUUID, order, playlist))
  }

  def server = DBAction { implicit request =>
    Ok(views.html.famousserver())
  }

  def map = DBAction { implicit request =>
    Ok(views.html.famousmap())
  }

}
