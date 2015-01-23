package sfn.cms.models.content

abstract class Translatable[T](override val ident: String, override val value: T, translations: List[TranslatedContent[T]]) extends PlayerContent[T](ident, value) {

  // def getValue(language: Language): T = (for(translation <- translations.filter(_.language == language)) yield(translation.value)).headOption.getOrElse(getDefault())

  // private def getDefault(): T = (for(translation <- translations.filter(_.default)) yield(translation.value)).headOption.getOrElse(getAny())

  private def getAny(): T = (translations.headOption.getOrElse(throw new IllegalStateException("A Translatable must have at least one TranslatedContent"))).value

}
