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
import money.model.{AccrualCashbackRequest, AccrualCashbackResponse}
import scala.concurrent.{ExecutionContext, Future}

class PayCashbackClient(implicit val ec: ExecutionContext, actorSystem: ActorSystem) extends FailFastCirceSupport {
    def accrualCashback(requestBody: AccrualCashbackRequest): Future[AccrualCashbackResponse] = {
        val request = HttpRequest(
            method = HttpMethods.POST,
            uri = s"http://scala-money-service1:8081/accrual_cashback",
            entity = HttpEntity(MediaTypes.`application/json`, requestBody.asJson.noSpaces)
        )
        val test = requestBody.asJson.noSpaces
        for {
            response <- Http().singleRequest(request)
            result <- Unmarshal(response).to[AccrualCashbackResponse]
        } yield result
    }
}
