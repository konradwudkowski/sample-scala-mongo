package uk.gov.hmrc.samplescalamongo.controllers

import javax.inject.Inject

import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.samplescalamongo.connectors.CountriesClient
import uk.gov.hmrc.samplescalamongo.model.UserData
import uk.gov.hmrc.samplescalamongo.repo.UserDataRepo
import uk.gov.hmrc.samplescalamongo.views.html.registration_form
import uk.gov.hmrc.samplescalamongo.views.html.confirmation

import scala.concurrent.{ExecutionContext, Future}

class RegistrationController @Inject()(countryClient: CountriesClient, userDataRepo: UserDataRepo)
                                      (implicit ec: ExecutionContext) extends FrontendController {

  def showForm() = Action.async { implicit request =>
    countryClient.getCountries.map { countries =>
      Ok(registration_form(UserData.form, countries))
    }
  }

  def submit() = Action.async { implicit request =>
    countryClient.getCountries.flatMap { countries =>
      UserData.form.bindFromRequest().fold(
        errors => Future.successful(BadRequest(registration_form(errors, countries))),
        submittedData => {
          userDataRepo.add(submittedData).map { resultOfAdding =>
            if (resultOfAdding) {
              Ok(confirmation(submittedData.name))
            } else {
              throw new Exception("Data not saved for unknown reason...")
            }
          }
        }
      )
    }
  }

}
