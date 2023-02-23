package money.route

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import money.model._
import money.repository.AccountRepository
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

class TransfersRoute(repository: AccountRepository)(implicit
    ec: ExecutionContext
) extends FailFastCirceSupport {
    def route = (path("transfer" / "by_account_id") & post) {
        entity(as[TransferByAccountId]) { transferByAccountId =>
            {
                onSuccess(repository.transferByAccountId(transferByAccountId)) {
                    case Right(value) => complete(value)
                    case Left(s) =>
                        complete(StatusCodes.NotAcceptable, s)
                }
            }
        }
    } ~ (path("accrual_cashback") & post) {
        entity(as[CashbackAccrual]) { cashbackAccrual =>
            onSuccess(
                repository.accrualCashback(
                    cashbackAccrual.userId,
                    cashbackAccrual.amountChange
                )
            ) {
                case Right(value) => {
                    complete(value)
                }
                case Left(s) => {
                    complete(StatusCodes.NotAcceptable, s)
                }

            }
        }
    }
}
