package misis.kafka

import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}
import misis.kafka.WithKafka
import misis.model._

import scala.concurrent.ExecutionContext

class Streams()(implicit val system: ActorSystem, executionContext: ExecutionContext) extends WithKafka {
    override def group: String = "fees"
}
