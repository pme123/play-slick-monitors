package sfn.cms.models.content

/**
 * Created by pascal.mengelt on 17.01.2015.
 */
trait SfnContent[T] {

  def getIdent(): String

  def getValue(): T

  def createCopy(): T
}
