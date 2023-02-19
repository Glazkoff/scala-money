package money

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import money.db.InitDb
import money.repository.AccountsRepositoryDB
import money.repository.CategoriesRepositoryDB
import money.route._
import scala.concurrent.ExecutionContext
import scala.io.StdIn
import slick.jdbc.PostgresProfile.api._

object MoneyDbApp extends App {
    implicit val system: ActorSystem = ActorSystem("MoneyApp")
    implicit val ec: ExecutionContext = system.dispatcher
    implicit val db = Database.forConfig("database.postgres")

    new InitDb().prepare()
    val repository = new AccountsRepositoryDB
    val categoriesRepository = new CategoriesRepositoryDB
    val accountsRoute = new AccountsRoute(repository).route
    val transfersRoute = new TransfersRoute(repository).route
    val cashRoute = new CashRoute(repository).route
    val categoriesRoute = new CategoriesRoute(categoriesRepository).route
    val helloRoute = new HelloRoute().route

    val bindingFuture =
        Http()
            .newServerAt("0.0.0.0", 8081)
            .bind(
                helloRoute
                    ~ accountsRoute
                    ~ transfersRoute
                    ~ cashRoute
                    ~ categoriesRoute
            )

    println(
        s"Server started. Go to -> http://localhost:8001/hello\nPress RETURN to stop..."
    )
    StdIn.readLine()
}
