package global

import _root_.actors.EventPublisher
import conf.ApplicationConf._
import models.{Article, Articles}
import play.api._
import play.api.db.slick.Config.driver.simple._
import play.libs.Akka
import slick.Connection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object PlayGlobalConf extends GlobalSettings {

  lazy val cancellable = Akka.system.scheduler.schedule(NEXTARTICLE_DELAY_INSECONDS second, NEXTARTICLE_DELAY_INSECONDS seconds)(EventPublisher.ref ! "go")



  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger.info("Scheduler started: "+cancellable)


  }

  override def onStop(application : play.api.Application) {
    cancellable.cancel()
  }

}
