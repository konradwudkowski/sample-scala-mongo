package uk.gov.hmrc.samplescalamongo.model

import play.api.data.format.Formatter
import play.api.libs.json.Json

case class Country(name: String) {
  def id = name.replaceAll("\\s","-")
}

object Country {
  implicit val format = Json.format[Country]
}