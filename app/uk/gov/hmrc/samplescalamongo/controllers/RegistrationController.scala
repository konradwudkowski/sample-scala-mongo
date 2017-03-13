package uk.gov.hmrc.samplescalamongo.controllers

import javax.inject.Inject

import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.samplescalamongo.connectors.CountriesClient
import uk.gov.hmrc.samplescalamongo.model.RegistrationRequest
import uk.gov.hmrc.samplescalamongo.views.html.registration_form

import scala.concurrent.ExecutionContext

class RegistrationController @Inject()(countryClient: CountriesClient)(implicit ec: ExecutionContext) extends FrontendController {
  def showForm() = Action.async { implicit request =>
    countryClient.getCountries.map { countries =>
      Ok(registration_form(RegistrationRequest.form, countries))
    }
  }

  def submit() = Action.async { implicit request =>
    countryClient.getCountries.map { countries =>
      RegistrationRequest.form.bindFromRequest().fold(
        errors => BadRequest(registration_form(errors, countries)),
        registrationDetails => Ok("successful submission")
      )
    }
  }
}
