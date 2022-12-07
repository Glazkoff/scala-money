package money

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import java.util.UUID
import money.model._
import money.repository.AccountRepositoryMutable
import money.route._
import scala.concurrent.ExecutionContext
import scala.io.StdIn

object MoneyMemoryApp extends App {
  // Создаём систему акторов
  implicit val system: ActorSystem = ActorSystem("MoneyApp")
  implicit val ec: ExecutionContext = system.dispatcher

  // Создаём данные для хранения в репозитории
  val repository: AccountRepositoryMutable = new AccountRepositoryMutable
  val user: User = User(
    id = UUID.randomUUID(),
    firstName = "Nikita",
    lastName = "Glazkov",
    patricity = Some("Olegovich"),
    phone = "+796733357967",
    priorityAccountID = None,
    isAdmin = Some(false)
  )
  repository
    .createAccount(
      CreateAccount(name = Some("test"), ownerUserId = user.id)
    )
    .map { account =>
      {
        repository.refillAccount(account.id, 1000)
        repository.withdrawFromAccount(account.id, 500)
      }
    }

  val accountsRoute = new AccountsRoute(repository).route
  val helloRoute = new HelloRoute().route

  val bindingFuture =
    Http().newServerAt("0.0.0.0", 8081).bind(helloRoute ~ accountsRoute)

  println(
    s"Server started. Go to -> http://localhost:8081/hello\nPress RETURN to stop..."
  )
  StdIn.readLine()
}
