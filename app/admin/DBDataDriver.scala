package admin

import google.GoogleMapService
import models._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.Session

/**
 * Created by pascal.mengelt on 06.02.2015.
 */
object DBDataDriver extends App {

  import google.GoogleMapService._
  import models.Addresses._
  import models.Clients._
  import models.Locations._

  def deleteClients(implicit request: Session): Unit = {
    println("Delete:")
    println("Clients: " + clients.filterNot(c => c.uuid === "x").delete)
    println("Locations: " + locations.filterNot(l => l.uuid === "x").delete)
    println("Addresses: " + addresses.filterNot(a => a.street === "x").delete)
  }

  def insertAddresses(implicit request: Session): Unit = {
    println("Create Addresses:")
    val allAddresses: List[Address] = List(
      Address(1, "Badenerstrasse", "670", 8048, "Zürich"),
      Address(2, "Limmatstrasse", "152", 8005, "Zürich"),
      Address(3, "Nansenstrasse", "21", 8050, "Zürich"),
      Address(4, "Seidengasse", "12", 8001, "Zürich"),
      Address(5, "Stockerstrasse ", "41", 8002, "Zürich"),
      Address(6, "via Pretorio", "7a", 6900, "Lugano"),
      Address(7, "rue du Mont-Blanc", "7", 1201, "Genève"),
      Address(8, "rue Pierre-Fatio", "15", 1204, "Genève"),
      Address(9, "Aeschenvorstadt", "72", 4051, "Basel"),
      Address(10, "Güterstrasse", "180", 4053, "Basel"),
      Address(11, "Bahnhofstrasse", "4", 9000, "St. Gallen"),
      Address(12, "Aarbergergasse", "20-22", 3011, "Bern"),
      Address(13, "Gilberte-de-Courgenay-Platz", "4", 3027, "Bern"),
      Address(14, "Unterer Graben", "35", 8400, "Winterthur"),
      Address(15, "place de l'Europe", "3-4", 1003, "Lausanne"),
      Address(17, "avenue de France", "10", 1950, "Sion"),
      Address(18, "Bahnhofstrasse", "6", 6003, "Luzern")
    )
    addresses ++= allAddresses
  }

  def insertLocations(implicit session: Session): Unit = {
    println("Create Locations:")
    val allAddresses: List[Addresses#TableElementType] = addresses.list
    def getLatLng(): AddressesWithLatLng = new GoogleMapService(allAddresses).getCoordinates()
    locations ++= (for {addressWithLatLng <- getLatLng()
    } yield (createLocation(addressWithLatLng))).toSeq

    def createLocation(addressWithLatLng: AddressWithLatLng): Location = {
      val address = addressWithLatLng._1
      val latLng: (Double, Double) = addressWithLatLng._2
      Location(1, "MB-" + address.cityZip, latLng._1, latLng._2, address.id)
    }
  }

  def insertClients(implicit request: Session): Unit = {
    println("Create Clients:")

    clients ++= locations.list.flatMap(l => for {
      index <- 0 to 9
    } yield (Client(l.uuid + "-" + index, index, "article", Locations.retrieveByUUID(l.uuid).id)))
  }

}
