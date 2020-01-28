package amazon

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Amazon extends Simulation {

  val httpProtocol = http
    .baseUrl("http://www.amazon.com")

  val headers = Map("Accept" -> "text/html")

  val scn = scenario("Amazon")
    .exec(http("Name_of_the_action")
      .get("/").check(status.is(200)))

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)

}
