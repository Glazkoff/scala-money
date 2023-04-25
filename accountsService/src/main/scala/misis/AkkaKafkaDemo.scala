package misis

import akka.actor.ActorSystem
import scala.concurrent.Future
import scala.util.Success
import scala.concurrent.ExecutionContext
import slick.jdbc.PostgresProfile.api._
import misis.route._
import scala.io.StdIn
import akka.http.scaladsl.Http
import misis.repository.Repository
import misis.kafka.Streams
import misis.model.AccountUpdate
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.ConfigFactory

object AkkaKafkaDemo extends App {
    implicit val system: ActorSystem = ActorSystem("App")
    implicit val ec: ExecutionContext = system.dispatcher
    implicit val db = Database.forConfig("database.postgres")

    val accountId = ConfigFactory.load().getInt("account.id")
    val defAmount = ConfigFactory.load().getInt("account.amount")

    val helloRoute = new HelloRoute().route

    private val repository = new Repository(accountId, defAmount)
    private val streams = new Streams(repository)

    implicit val commandTopicName = streams.simpleTopicName[AccountUpdate]
    // streams.produceCommand(AccountUpdate(0, 100))

    val bindingFuture =
        Http()
            .newServerAt("0.0.0.0", 8081)
            .bind(
                helloRoute
            )

    println(
        s"Server started. Go to -> http://localhost:8001/hello\nPress RETURN to stop..."
    )
    StdIn.readLine()
}
