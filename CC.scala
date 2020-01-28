
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

private val url = "https://api-perf1.nonprod.currencycloud.io/v2/"
private val feeder = csv("1000.csv").circular

val httpProtocol = http
    	.baseUrl(url)
    	.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    	.doNotTrackHeader("1")
    	.acceptLanguageHeader("en-US,en;q=0.5")
    	.acceptEncodingHeader("gzip, deflate")
    	.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

object Trade {

	val trade = exec(http("get_nonce")
			.get("authenticate/user?login_id=${login_id}")
			.check(jsonPath("$.nonce").find.saveAs("nonce")))
		.pause(8)
		.exec(http("authenticate")
			.post("authenticate/login")
			.formParamMap(Map(
				"nonce" -> "${nonce}",
				"security_answer" -> "test",
				"password" -> "test"))
			.check(jsonPath("$.auth_token").find.saveAs("auth_token")))
		.pause(8)
		.exec(http("create_trade")
			.post("conversions/create")
			.header("X-AUTH-TOKEN", "${auth_token}")
			.formParamMap(Map(
				"buy_currency" -> "${buy_ccy}", 
				"sell_currency" -> "${sell_ccy}", 
				"fixed_side" -> "sell",
				"amount" -> "10000",
				"term_agreement" -> "true",
				"conversion_date" -> java.time.LocalDate.now))
			.check(jsonPath("$").find.saveAs("response"))
			.check(status is 200))
			.pause(8)
			
}

	val trades = scenario("Trades").feed(feeder).exec(Trade.trade)
	setUp(trades.inject(constantUsersPerSec(4) during (1800 seconds))).protocols(httpProtocol)

	before {println("Simulation is about to start!")}
	after {println("Simulation is finished!")}
}