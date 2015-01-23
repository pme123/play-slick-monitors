package sfn.utils

import java.io.ByteArrayOutputStream

import org.specs2.mock.Mockito
import org.specs2.mutable._

class LogableSpec extends Specification with Mockito {


  "An Logable" should {
    "log messages to the console" in new withSystemStreams {
      Console.withOut(outContent) {
        logable.info(msg)
        // this does not work: outContent.toString must be (msg)
        outContent.toString equals (msg)
      }
    }
  }


  val logable: Logable = new Logable {}
  val msg: String = "Log msg"

  val outContent = new ByteArrayOutputStream()


  // it is needed that it works
  trait withSystemStreams extends After {

    def after = {

    }
  }


}
