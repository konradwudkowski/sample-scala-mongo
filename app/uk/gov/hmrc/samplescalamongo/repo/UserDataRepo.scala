package uk.gov.hmrc.samplescalamongo.repo

import javax.inject.Inject

import com.google.inject.AbstractModule
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.mongo.ReactiveRepository
import uk.gov.hmrc.samplescalamongo.model.UserData

import scala.concurrent.{ExecutionContext, Future}

trait UserDataRepo {
  def add(userData: UserData)(implicit ec: ExecutionContext): Future[Boolean]
}

class MongoUserDataRepo @Inject()(connector: ReactiveMongoComponent) extends
  ReactiveRepository[UserData, BSONObjectID](
    "userData", connector.mongoConnector.db, UserData.format) with UserDataRepo {

  override def add(userData: UserData)(implicit ec: ExecutionContext): Future[Boolean] = {
    insert(userData).map { result =>
      result.ok && result.n == 1
    }
  }
}

class MongoModule extends AbstractModule {
  def configure() = {
    bind(classOf[UserDataRepo])
      .to(classOf[MongoUserDataRepo]).asEagerSingleton()
  }
}