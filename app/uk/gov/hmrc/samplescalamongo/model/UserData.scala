package uk.gov.hmrc.samplescalamongo.model

import java.time.LocalDate

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json

case class UserData(name: String, sex: String, age: Int, country: Country, dateCreated: LocalDate)

object UserData {
  val countryMapper = text.transform[Country](Country.apply, _.name)
  implicit val countryFormat = Country.mongoSimpleFormat
  val format = Json.format[UserData]
  val form = Form(
    mapping(
      "name" -> text.verifying("Please enter your name", _.nonEmpty),
      "sex" -> text,
      "age" -> number,
      "country" -> countryMapper,
      "dateCreated" -> localDate
    )(UserData.apply)(UserData.unapply)
  )
}
