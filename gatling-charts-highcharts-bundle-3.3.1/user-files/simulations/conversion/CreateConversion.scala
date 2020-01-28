package conversion

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent._
import scala.concurrent.duration._

class CreateConversion extends Simulation {

  val baseUrl = "http://api-v2.test8.nonprod.ccycloud.io:8080/v2/"

  val httpProtocol = http
    .baseUrl(baseUrl)
    .userAgentHeader("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5 (.NET CLR 3.5.30729)")

  val scn = scenario("Create conversion")
    .exec(http("get_nonce")
      .get("authenticate/user?login_id=ml.account3")
      .check(jsonPath("$.nonce").find.saveAs("nonce"))
      .check(status.is(200)))
    .pause(5)
    .exec(http("authentication")
      .post("authenticate/login")
      .formParamMap(Map(
        "nonce" -> "${nonce}",
        "security_answer" -> "test",
        "password" -> "test"))
      .check(status.is(200))
      .check(jsonPath("$.auth_token").find.saveAs("auth_token")))
    .pause(5)
    .exec(http("create_trade")
      .post("conversions/create")
      .header("X-AUTH-TOKEN", "${auth_token}")
      .formParamMap(Map(
        "buy_currency" -> "GBP",
        "sell_currency" -> "USD",
        "fixed_side" -> "sell",
        "amount" -> "400",
        "term_agreement" -> "true"))
      .check(status is 200))

    setUp(scn.inject(atOnceUsers(4))).protocols(httpProtocol)

}

