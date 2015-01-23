package sfn.cms.models.content

import sfn.cms.models.concept.Auditable
import sfn.cms.models.utils.ValidationHelper._

/**
 * Created by pascal.mengelt on 17.01.2015.
 */
abstract class PlayerContent[T](val ident: String, val value: T) extends SfnContent[T]
with Auditable {
  val FIELD_IDENT = "ident"
  val FIELD_VALUE = "value"

  protected val init = {
    nonEmpty(ident, FIELD_IDENT)
  }

  def getIdent() = ident

  def getValue() = value

}
