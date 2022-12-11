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

class AccountsRoute(repository: AccountRepository)(implicit
    ec: ExecutionContext
) extends FailFastCirceSupport {
  def route =
    (path("accounts") & get) {
      {
        val list = repository.accountsList()
        complete(list)
      }
    } ~
      (path("accounts") & post) {
        entity(as[CreateAccount]) { newAccount =>
          complete(repository.createAccount(newAccount))
        }
      } ~
      (path("accounts" / JavaUUID) & get) { id =>
        complete(repository.getAccount(id))
      } ~
      (path("accounts" / JavaUUID) & put) { id =>
        entity(as[UpdateAccount]) { updateAccount =>
          {
            onSuccess(repository.updateAccount(id, updateAccount)) {
              case Right(value) => complete(value)
              case Left(s) =>
                complete(StatusCodes.NotAcceptable, s)
            }
          }
        }
      } ~
      (path("accounts" / JavaUUID) & delete) { id =>
        complete(repository.deleteAccount(id))
      } ~
      (path("cash") & post) {
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
