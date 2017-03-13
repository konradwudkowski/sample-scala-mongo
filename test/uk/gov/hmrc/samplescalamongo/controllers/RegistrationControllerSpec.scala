package uk.gov.hmrc.samplescalamongo.controllers

import org.jsoup.Jsoup
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers, WordSpec}
import org.scalatestplus.play.OneAppPerSuite
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.filters.csrf.CSRFAddToken
import uk.gov.hmrc.samplescalamongo.connectors.CountriesClient
import uk.gov.hmrc.samplescalamongo.model.{Country, UserData}
import uk.gov.hmrc.samplescalamongo.repo.UserDataRepo

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class RegistrationControllerSpec extends WordSpec with Matchers with ScalaFutures with OneAppPerSuite {

  "Registration Form" should {
    "include all required elements" in {
      val expectedCountry = "Belgium"
      val controller = new RegistrationController(countriesClient(List(Country(expectedCountry))), userDataRepo())

      val result = csrfAddToken(controller.showForm())(FakeRequest())

      val parsedHtml = Jsoup.parse(contentAsString(result))

      parsedHtml.getElementById("name") should not be null
      parsedHtml.getElementById("age") should not be null
      parsedHtml.getElementById("sex-male") should not be null
      parsedHtml.getElementById("sex-female") should not be null
      parsedHtml.getElementById("country") should not be null
      parsedHtml.getElementById("dateCreated") should not be null

      parsedHtml.getElementById("country").html() should include(expectedCountry)
    }
  }

  val allRequiredFields =
    List("name" -> "foo", "age" -> "1", "sex" -> "male", "country" -> "UK", "dateCreated" -> "2017-03-17")

  "Submitting a form" should {
    "work if all elements are present" in {
      val controller = new RegistrationController(countriesClient(), userDataRepo())

      val result = csrfAddToken(controller.submit())(FakeRequest().withFormUrlEncodedBody(allRequiredFields: _*))

      status(result) shouldBe OK
    }
    "fail if name is absent" in {
      assertBadRequest(allRequiredFields.filterNot(_._1 == "name"))
    }
    "fail if name is zero chars long" in {
      assertBadRequest(allRequiredFields.filterNot(_._1 == "name") ++ List("name" -> ""))
    }
    "fail if age is missing" in {
      assertBadRequest(allRequiredFields.filterNot(_._1 == "age"))
    }
    "fail if age is not a number" in {
      assertBadRequest(allRequiredFields.filterNot(_._1 == "age") ++ List("age" -> "NaN"))
    }
    "fail if sex is missing" in {
      assertBadRequest(allRequiredFields.filterNot(_._1 == "sex"))
    }
    "fail if country is missing" in {
      assertBadRequest(allRequiredFields.filterNot(_._1 == "country"))
    }
  }

  def assertBadRequest(requestParams: List[(String,String)]) = {
    val controller = new RegistrationController(countriesClient(), userDataRepo())
    val result = csrfAddToken(controller.submit())(FakeRequest().withFormUrlEncodedBody(requestParams: _*))
    status(result) shouldBe BAD_REQUEST
  }

  def countriesClient(countries: List[Country] = List(Country("Belgium"))) = new CountriesClient {
    def getCountries(implicit ec: ExecutionContext) = Future.successful(List(Country("Belgium")))
  }

  def userDataRepo(result: Boolean = true) = new UserDataRepo {
    def add(userData: UserData)(implicit ec: ExecutionContext) = Future.successful(true)
  }

  val csrfAddToken = app.injector.instanceOf[CSRFAddToken]

}
