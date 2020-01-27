package blazemeter

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BlazemeterPricing extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.demoblaze.com")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.9,ru;q=0.8,uk;q=0.7")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36")

	val headers_0 = Map("Proxy-Connection" -> "keep-alive")



	val scn = scenario("BlazemeterPricing")
		.exec(http("request_0")
			.get("/?utm_source=blog&utm_medium=BM_blog&utm_campaign=how-to-run-a-simple-load-test-with-gatling")
			.headers(headers_0)
			.check(status.is(302)))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}