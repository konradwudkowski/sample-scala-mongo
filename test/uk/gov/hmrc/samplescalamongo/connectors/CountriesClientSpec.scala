package uk.gov.hmrc.samplescalamongo.connectors

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.play.OneAppPerSuite
import play.api.Configuration
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import play.api.routing.sird._
import play.api.test._
import play.core.server.Server
import uk.gov.hmrc.samplescalamongo.model.Country

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

class CountriesClientSpec extends FlatSpec with Matchers with ScalaFutures with OneAppPerSuite {

  implicit val asyncConfig = PatienceConfig(timeout = scaled(Span(2, Seconds)))

  "Client" should "download a list of countries" in {
    val name1 = "Åland Islands"
    val name2 = "Albania"
    val simplifiedMockedResponse =
      s"""
        [
          {
            "name": "$name1"
          },
          {
            "name": "$name2"
          }
        ]
      """

    withCountriesMockServer(simplifiedMockedResponse) { client =>
      client.getCountries.futureValue shouldBe List(Country(name1), Country(name2))
    }
  }

  it should "work with a complex real list of countries" in {
    val longListOfCountries = Source.fromInputStream(app.resourceAsStream("sample-countries.json").get).mkString
    val expectedNumberOfCountries = Json.parse(longListOfCountries).as[Array[JsObject]].length

    withCountriesMockServer(longListOfCountries) { client =>
      client.getCountries.futureValue.size shouldBe expectedNumberOfCountries
    }
  }

  def withCountriesMockServer[T](jsonResponse: String)(block: CountriesClient => T): T = {
    Server.withRouter() {
      case GET(_) => Action { Results.Ok(jsonResponse) }
    } { implicit port =>
      val config = Configuration("countries-url" -> s"http://localhost:$port")
      WsTestClient.withClient { client =>
        block(new CountriesClientImpl(client, config))
      }
    }
  }

}
