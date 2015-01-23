package sfn.cms.models.concept

/**
 * describes a class that has a unique business key
 */
trait Identifiable {
  def ident: String
}
