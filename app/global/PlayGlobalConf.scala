package global

import _root_.actors.EventPublisher
import models.{Article, Articles}
import play.api._
import play.api.db.slick.Config.driver.simple._
import play.libs.Akka
import slick.Connection

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object PlayGlobalConf extends GlobalSettings {
  lazy val cancellable =  Akka.system.scheduler.schedule(5 second, 5 seconds)(nextArticle())

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

  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger.info("Scheduler started: "+cancellable)


  }

  override def onStop(application : play.api.Application) {
    cancellable.cancel()
  }

}
