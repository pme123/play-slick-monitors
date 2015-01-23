package sfn.cms.models.concept

class SystemUser(userName: String) extends Identifiable {
  override val ident = userName
}
