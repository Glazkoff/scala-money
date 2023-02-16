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

class CashRoute(repository: AccountRepository)(implicit
    ec: ExecutionContext
) extends FailFastCirceSupport {
  def route = (path("cash") & post) {
    entity(as[CashOperation]) { cashOperation =>
      {
        cashOperation.opType match {
          case "TOP_UP" =>
            onSuccess(
              repository.refillAccount(
                cashOperation.accountId,
                cashOperation.amountChange
              )
            ) {
              case Right(value) => complete(value)
              case Left(s) =>
                complete(StatusCodes.NotAcceptable, s)
            }
          case "CASHING_OUT" =>
            onSuccess(
              repository.withdrawFromAccount(
                cashOperation.accountId,
                cashOperation.amountChange
              )
            ) {
              case Right(value) => complete(value)
              case Left(s) =>
                complete(StatusCodes.NotAcceptable, s)
            }
          case _ =>
            complete(StatusCodes.NotAcceptable, "Некорректный запрос!")
        }
      }
    }
  }
}
