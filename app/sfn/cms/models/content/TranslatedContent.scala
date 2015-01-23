package sfn.cms.models.content


abstract class TranslatedContent[T](override val ident: String, override val value: T, language: Language, default: Boolean) extends PlayerContent[T](ident, value) {

}
