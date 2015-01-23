package sfn.cms.models.utils


object ValidationHelper {

  def nonEmpty(str: String, field: String) = require(str != null && !str.isEmpty, s"The $field must not be null or empty. Your value was: '$str'")

}
