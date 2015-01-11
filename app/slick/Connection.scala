package slick

import play.api.Play.current
import play.api.db
import play.api.db.slick.Config.driver.simple._

object Connection {
  def databaseObject(): Database = {
    Database.forDataSource(db.slick.DB.datasource)
  }
}
