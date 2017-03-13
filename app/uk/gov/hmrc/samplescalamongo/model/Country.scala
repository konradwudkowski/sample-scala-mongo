package uk.gov.hmrc.samplescalamongo.model

import play.api.libs.json._

case class Country(name: String)

object Country {
  val defaultFormat = Json.format[Country]
  val simpleWrites = new Writes[Country] {
    def writes(c: Country): JsValue = JsString(c.name)
  }
  val simpleReads = new Reads[Country] {
    def reads(json: JsValue): JsResult[Country] = json match {
      case JsString(v) => JsSuccess(Country(v))
      case _ => JsError("invalid country")
    }
  }
  val mongoSimpleFormat = Format(simpleReads, simpleWrites)
}