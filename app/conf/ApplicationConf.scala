package conf

import play.api.Play
import play.api.Play.current

object ApplicationConf {

  lazy val NEXTARTICLE_DELAY_INSECONDS: Int = Play.application.configuration.getInt("nextarticle.delay.inseconds").getOrElse(5)
  val DEFAULT_PLAY =  "article"

}
