package sfn.cms.models.concept

import org.joda.time.DateTime
import org.specs2.mutable._

class AuditedSpec extends Specification {

  "An Audited" should {
    "have created infos but no modified infos after creation" in {
      val audit = Audited.createBy(auditable, user)
      checkAfterCreated(audit)
      audit.lastModifiedBy must be(None)
      audit.lastModifiedDate must be(None)
    }
  }

  "An Audited" should {
    "have created infos and modified infos after modification" in {
      val audit = Audited.createBy(auditable, user)
        .modify(user)
      checkAfterCreated(audit)
      audit.lastModifiedBy.get must be(user)
      audit.lastModifiedDate.get.isInstanceOf[DateTime]
    }
  }

  def checkAfterCreated(audit: Audited) {
    audit.auditable must be(auditable)
    audit.createdBy must be(user)
    audit.createDate.isInstanceOf[DateTime]
  }

  val auditable = new Auditable {
    override def ident: String = "identifiable"
  }

  val user = new SystemUser("user")

}
