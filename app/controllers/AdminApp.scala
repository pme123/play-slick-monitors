package controllers

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.mvc._


object AdminApp extends Controller {

  import admin.DBDataDriver._
  import models.Articles._
  import models.Clients._

  def setupDB = DBAction { implicit request =>
    println("DB started:")
    deleteClients
    insertAddresses
    insertLocations
    insertClients
    Ok(views.html.server(articles.list, clients.list))
  }

}
