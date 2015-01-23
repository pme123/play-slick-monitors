package sfn.utils

import java.util.logging.{Level, Logger}


trait Logable {
  private val logger: Logger = Logger.getAnonymousLogger

  // @inline final allows compiler optimization
  @inline final def debug(message: => Any) = if (debugIsEnabled) logger.fine(message.toString)

  @inline final def info(msg: => Any) {
    logger.info(msg.toString)
  }

  @inline final def error(msg: => Any) {
    logger.severe(msg.toString)
  }

  @inline final def error(msg: => Any, throwable: => Throwable) {
    logger.log(Level.SEVERE, msg.toString, throwable)
  }

  def debugIsEnabled() = logger.isLoggable(Level.FINE)
}
