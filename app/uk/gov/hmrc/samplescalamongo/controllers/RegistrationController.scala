package uk.gov.hmrc.samplescalamongo.controllers

import javax.inject.Inject

import uk.gov.hmrc.play.frontend.controller.FrontendController
import play.api.mvc._

import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import uk.gov.hmrc.samplescalamongo.connectors.CountriesClient

class RegistrationController @Inject()(countryClient: CountriesClient) extends FrontendController {
  def showForm() = Action.async { implicit request =>
		Future.successful(Ok(uk.gov.hmrc.samplescalamongo.views.html.registration_form()))
  }
}
