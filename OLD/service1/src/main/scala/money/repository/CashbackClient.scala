package money.repository

import java.util.UUID
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{
    HttpEntity,
    HttpMethods,
    HttpRequest,
    MediaTypes,
    StatusCodes,
    HttpResponse,
    ContentTypes
}
import akka.http.scaladsl.unmarshalling.Unmarshal
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._
import money.model.{CashbackCreateRequest, CashbackCreateResponse}
import scala.concurrent.{ExecutionContext, Future}

class CashbackClient(implicit val ec: ExecutionContext, actorSystem: ActorSystem) extends FailFastCirceSupport {
    def createCashback(cashback: CashbackCreateRequest): Future[CashbackCreateResponse] = {
        val request = HttpRequest(
            method = HttpMethods.POST,
            uri = s"http://scala-money-service2:8081/cashbacks",
            entity = HttpEntity(MediaTypes.`application/json`, cashback.asJson.noSpaces)
        )
        for {
            response <- Http().singleRequest(request)
            result <- Unmarshal(response).to[CashbackCreateResponse]
        } yield result
    }
}
