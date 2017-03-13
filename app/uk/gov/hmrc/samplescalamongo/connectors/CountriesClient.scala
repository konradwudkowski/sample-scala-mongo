package uk.gov.hmrc.samplescalamongo.connectors

import javax.inject.Inject

import com.google.inject.ImplementedBy
import play.api.Configuration
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import uk.gov.hmrc.samplescalamongo.model.Country

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@ImplementedBy(classOf[CountriesClientImpl])
trait CountriesClient {
  def getCountries(implicit ec: ExecutionContext): Future[List[Country]]
}

class CountriesClientImpl @Inject() (ws: WSClient, config: Configuration) extends CountriesClient {
  def withCountriesUrl[A](f: String => Future[A]) =
    config.getString("countries-url")
      .map(f)
      .getOrElse(Future.failed(new Exception("Missing config for `countries-url`")))

  def getCountries(implicit ec: ExecutionContext): Future[List[Country]] = withCountriesUrl { url =>
    implicit val countryFormat = Country.defaultFormat
    ws.url(url).get()
      .map { r => Json.parse(r.body).as[List[Country]] }
      .recover { case NonFatal(ex) => println(ex); offlineCountries }
  }

  def offlineCountries = List(Country("France"), Country("Germany"))
}
