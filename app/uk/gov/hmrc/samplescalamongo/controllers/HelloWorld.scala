package uk.gov.hmrc.samplescalamongo.controllers

import uk.gov.hmrc.play.frontend.controller.FrontendController
import play.api.mvc._
import scala.concurrent.Future
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

class HelloWorld extends FrontendController {
  val helloWorld = Action.async { implicit request =>
		Future.successful(Ok(uk.gov.hmrc.samplescalamongo.views.html.helloworld.hello_world()))
  }
}
