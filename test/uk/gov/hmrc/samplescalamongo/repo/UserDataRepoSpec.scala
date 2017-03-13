package uk.gov.hmrc.samplescalamongo.repo

import java.time.LocalDate

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers, WordSpec}
import play.modules.reactivemongo.ReactiveMongoComponent
import uk.gov.hmrc.mongo.MongoSpecSupport
import uk.gov.hmrc.samplescalamongo.model.{Country, UserData}

import scala.concurrent.ExecutionContext.Implicits.global

class UserDataRepoSpec extends FlatSpec with Matchers with MongoSpecSupport with ScalaFutures with BeforeAndAfterAll {

  implicit val asyncConfig = PatienceConfig(timeout = scaled(Span(2, Seconds)))

  val repo = new MongoUserDataRepo(new ReactiveMongoComponent {
    def mongoConnector = mongoConnectorForTest
  })

  override def beforeAll() = repo.removeAll().futureValue

  "Repo" should "enable saving registration data" in {
    val userData = UserData("Name", "Male", 25, Country("France"), LocalDate.now())

    repo.add(userData).futureValue shouldBe true

    repo.findAll().futureValue shouldBe List(userData)
  }
}
