package admin

import models.{Addresses, Clients, Locations}
import org.specs2.mutable.Specification
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import play.api.test.WithApplication

/**
 * Created by pascal.mengelt on 08.02.2015.
 */
class DBDataDriverSpec extends Specification {

  "The DBDataDriver" should {
    "return nothing after delete the Clients." in new WithApplication {
      DB.withSession { implicit s: play.api.db.slick.Session =>
        DBDataDriver.deleteClients
        Clients.clients.list must be(List.empty)
      }
    }

    "return Addresses, Locations and Clients after inserting." in new WithApplication {
      DB.withSession { implicit s: play.api.db.slick.Session =>
        DBDataDriver.insertAddresses
        Addresses.addresses.list must not be (List.empty)
        DBDataDriver.insertLocations
        Locations.locations.list must not be (List.empty)
        DBDataDriver.insertClients
        Clients.clients.list must not be (List.empty)
      }
    }


  }


}
