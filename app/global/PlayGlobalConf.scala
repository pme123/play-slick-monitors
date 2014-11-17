package global

import _root_.actors.EventPublisher
import models.{Article, Articles}
import play.api._
import play.api.db.slick.Config.driver.simple._
import play.libs.Akka
import slick.Connection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object PlayGlobalConf extends GlobalSettings {

  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger.info("Scheduler started")
    var lastArticle: Option[Article] = None
    def nextArticle() = {
      Connection.databaseObject().withSession { implicit session: Session =>
        val articleOption: Option[Article] = Articles.nextArticle(lastArticle)

        lastArticle = articleOption
        for (article <- articleOption) {
          EventPublisher.ref ! article
        }
      }

    }
    Akka.system.scheduler.schedule(5 second, 5 seconds)(nextArticle())
  }



}
