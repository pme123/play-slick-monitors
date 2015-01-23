package sfn.cms.models.concept

import org.joda.time.DateTime
import sfn.utils.Logable

/**
 * describes a class that is audited
 */
trait Auditable extends Identifiable with Logable {
  def logAudit() = info(this)
}

case class Audited(
                    auditable: Auditable,
                    createdBy: SystemUser,
                    createDate: DateTime,
                    lastModifiedBy: Option[SystemUser],
                    lastModifiedDate: Option[DateTime]
                    ) extends Logable {


  def modify(user: SystemUser): Audited = {
    val audit = new Audited(
      auditable,
      createdBy,
      createDate,
      Some(user),
      Some(DateTime.now()))
    logAudited()
    audit
  }

  protected def logAudited() = {
    info(this)
    auditable.logAudit()
  }
}


object Audited extends Logable {
  def createBy(auditable: Auditable, user: SystemUser): Audited = {
    val audit = new Audited(auditable, user, DateTime.now(), None, None)
    audit.logAudited()
    audit
  }

}
