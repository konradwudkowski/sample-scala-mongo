package uk.gov.hmrc.samplescalamongo.model

import play.api.data.Form
import play.api.data.Forms._

case class RegistrationRequest(name: String, sex: String, age: Int, country: Country)

object RegistrationRequest {
  val countryMapper = text.transform[Country](Country.apply, _.name)
  val form = Form(
    mapping(
      "name" -> text.verifying("Please enter your name", _.nonEmpty),
      "sex" -> text,
      "age" -> number,
      "country" -> countryMapper
    )(RegistrationRequest.apply)(RegistrationRequest.unapply)
  )
}
