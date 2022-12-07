package money

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import money.repository.AccountsRepositoryDB
import money.route._
import scala.concurrent.ExecutionContext
import scala.io.StdIn
import slick.jdbc.PostgresProfile.api._
import money.db.InitDb

object MoneyDbApp extends App {
  implicit val system: ActorSystem = ActorSystem("MoneyApp")
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val db = Database.forConfig("database.postgres")

  new InitDb().prepare()
  val repository = new AccountsRepositoryDB
  val accountsRoute = new AccountsRoute(repository).route
  val helloRoute = new HelloRoute().route

  val bindingFuture =
    Http()
      .newServerAt("0.0.0.0", 8081)
      .bind(
        helloRoute
          ~ accountsRoute
      )

  println(
    s"Server started. Go to -> http://localhost:8081/hello\nPress RETURN to stop..."
  )
  StdIn.readLine()
}
