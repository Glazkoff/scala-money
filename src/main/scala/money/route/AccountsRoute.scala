package money.route

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import money.model._
import money.repository.AccountRepositoryMutable

class AccountsRoute(repository: AccountRepositoryMutable)
    extends FailFastCirceSupport {
  def route =
    (path("accounts") & get) {
      {
        val list = repository.list()
        complete(list)
      }
    } ~
      (path("accounts") & post) {
        entity(as[CreateAccount]) { newAccount =>
          complete(repository.createAccount(newAccount))
        }
      } ~
      path("accounts" / JavaUUID) { id =>
        get {
          complete(repository.getAccount(id))
        }
        put {
          entity(as[UpdateAccount]) { updateAccount =>
            {
              complete(repository.updateAccount(id, updateAccount))
            }
          }
        }
      // delete => {
      //   complete(repository.deleteAccount(id))
      // }
      }
}
