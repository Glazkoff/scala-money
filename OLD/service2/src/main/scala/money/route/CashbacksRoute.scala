package money.route

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import money.model._
import money.repository.CashbackRepository
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

class CashbacksRoute(repository: CashbackRepository)(implicit
    ec: ExecutionContext
) extends FailFastCirceSupport {
    def route =
        (path("cashbacks") & get) {
            {
                val list = repository.cashbacksList()
                complete(list)
            }
        } ~
            (path("cashbacks") & post) {
                entity(as[CreateCashback]) { newCashback =>
                    complete(repository.createCashback(newCashback))
                }
            } ~ (path("cashbacks_pay") & post) {
                entity(as[PayCashback]) { payCashbackBody =>
                    onComplete(repository.payCashback(payCashbackBody.userId)) {
                        case Success(accrualCashbackResponse) =>
                            complete(StatusCodes.OK -> accrualCashbackResponse)
                        case Failure(error) =>
                            complete(
                                StatusCodes.InternalServerError -> s"Unexpected error occurred: ${error.getMessage}"
                            )
                    }
                }
            }
}
