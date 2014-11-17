package slick

import play.api.db
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current

object Connection {
  def databaseObject(): Database = {
    Database.forDataSource(db.slick.DB.datasource)
  }
}
