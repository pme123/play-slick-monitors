package google

import admin.DBDataDriver
import models.{Address, Addresses}
import org.specs2.mutable._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.test._


class GoogleMapServiceSpec extends Specification with Before {
  private val address: Address = Address(10, "Güterstrasse", "180", 4053, "Basel")

  "The GoogleMapService" should {
    "return an empty Map without Addresses" in new WithApplication {
      val results = new GoogleMapService(List.empty).getCoordinates()
      results must be(Map.empty)
    }

    "return one Entry with one Address" in new WithApplication {
      createAddress()
      val results = new GoogleMapService(List(address)).getCoordinates()
      results.size equals 1
      results.keys.head.street must be(address.street)

      def createAddress() =
        DB.withSession { implicit s: play.api.db.slick.Session =>
          Addresses.addresses.insert(address)
        }
    }


  }

  override def before: Any = new WithApplication {
    DB.withSession { implicit s: play.api.db.slick.Session =>
      DBDataDriver.deleteClients
    }
  }


}
