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
import scala.io.StdIn
import money.route._

object MoneyHttpApp extends App {
  // Создаём систему акторов
  implicit val system: ActorSystem = ActorSystem("MoneyApp")

  // Создаём данные для хранения в репозитории
  val repository: AccountRepositoryMutable = new AccountRepositoryMutable
  val user: User = User(
    id = UUID.randomUUID(),
    firstName = "Nikita",
    lastName = "Glazkov",
    username = "login"
  )
  val createdAcc = repository.createAccount(
    CreateAccount(title = "test", userId = user.id)
  )

  val accountsRoute = new AccountsRoute(repository).route
  val helloRoute = new HelloRoute().route

  val bindingFuture =
    Http().newServerAt("0.0.0.0", 8081).bind(helloRoute ~ accountsRoute)

  println(
    s"Сервер запущен. Перейдите к http://localhost:8081/hello\nНажмите RETURN чтобы остановить..."
  )
  StdIn.readLine()
}
