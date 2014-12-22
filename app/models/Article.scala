package models

import play.api.Logger
import play.api.db.slick.Config.driver.simple._

import scala.slick.lifted.TableQuery

object Articles extends TableQuery(new Articles(_)) {
  val name = "ARTICLE"
  val nameCol = "name"
  val descrCol = "descr"
  val imgCol = "img"
  val playlistCol = "playlist"
  //create an instance of the table
  val articles = TableQuery[Articles] //see a way to architect your app in the computers-database-slick sample

  def nextArticle(lastArticle: Option[Article], playlist: String)(implicit session: Session): Option[Article] = {
    val filteredArticles = articles.filter(article => article.playlist === playlist).list

    def inner(lastArticle: Article, allArticles: List[Article]): Option[Article] = {
      allArticles match {
        case article :: nextArticle :: xs if lastArticle equals article => Some(nextArticle)
        case article :: nextArticle :: xs => inner(lastArticle, allArticles.tail)
        case _ => filteredArticles.headOption
      }
    }

    lastArticle match {
      case None => filteredArticles.headOption
      case Some(article) => inner(article, filteredArticles)
    }
  }

}

case class Article(name: String, descr: String, img: String, playlist: String)


/* Table mapping
 */
class Articles(tag: Tag) extends Table[Article](tag, Articles.name) {

  import models.Articles._

  def name = column[String](nameCol, O.PrimaryKey)

  def descr = column[String](descrCol, O.NotNull)

  def img = column[String](imgCol, O.NotNull)

  def playlist = column[String](playlistCol, O.NotNull)

  def * = (name, descr, img, playlist) <>(Article.tupled, Article.unapply)

}
