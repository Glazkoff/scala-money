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
    override def group: String = "operation"

    // TO BE
    kafkaSource[AccountFromAck]
        .map { e =>
            println(s"Ask for existence of account ${e.sourceId}")
            TransferCheckDestination(e.sourceId, e.destinationId, e.value)
        }
        .to(kafkaSink)
        .run()

    kafkaSource[AccountToAck]
        .map { e =>
            produceCommand(AccountUpdate(e.sourceId, -e.value))
            produceCommand(AccountUpdate(e.destinationId, e.value))
            println(s"[SUCCESS] Got all acknowledgments for accounts ${e.sourceId} and ${e.destinationId}")
            e
        }
        .to(Sink.ignore)
        .run()
}
