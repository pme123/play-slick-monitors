package sfn.cms.models.content

/**
 * Created by pascal.mengelt on 17.01.2015.
 */
sealed trait Language {
  def print(): String

  override def toString = print()
}

object DE extends Language {
  def print() = "DE"
}

object FR extends Language {
  def print() = "FR"
}

object IT extends Language {
  def print() = "IT"
}

object EN extends Language {
  def print() = "EN"
}