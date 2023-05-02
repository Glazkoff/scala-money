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
import akka.http.scaladsl.server.Directives._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

object AkkaKafkaDemo extends App {
    implicit val system: ActorSystem = ActorSystem("App")
    implicit val ec: ExecutionContext = system.dispatcher
    implicit val db = Database.forConfig("database.postgres")

    val helloRoute = new HelloRoute().route

    private val streams = new Streams()
    private val repository = new Repository(streams)
    val mainRoute = new Route(streams, repository).route

    // implicit val commandTopicName = streams.simpleTopicName[AccountUpdate]
    // streams.produceCommand(AccountUpdate(0, 100))

    val bindingFuture =
        Http()
            .newServerAt("0.0.0.0", 8081)
            .bind(
                helloRoute
                    ~ mainRoute
            )

    println(
        s"Server started. Go to -> http://localhost:8001/hello\nPress RETURN to stop..."
    )
    StdIn.readLine()
}
