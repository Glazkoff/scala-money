package misis.kafka

import akka.kafka.{ConsumerSettings, ProducerSettings, Subscriptions}
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer, StringSerializer}
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext
import misis.repository._
import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._
import misis.model._
import scala.reflect.ClassTag

class Streams(repository: Repository)(implicit val system: ActorSystem, executionContext: ExecutionContext)
    extends WithKafka {

    def group = s"account-${repository.accountId}"

    // TO BE
    kafkaSource[TransferStart]
        .filter(event => repository.account.id == event.sourceId && repository.account.amount >= event.value)
        .map { e =>
            println(s"[SOURCE ACK] Account ${e.sourceId} exists and has enough money")
            AccountFromAck(e.sourceId, e.destinationId, e.value)
        }
        .to(kafkaSink)
        .run()

    kafkaSource[TransferCheckDestination]
        .filter(event => repository.account.id == event.destinationId)
        .map { e =>
            println(s"[DESTINATION ACK] Account ${e.destinationId} exists ")
            AccountToAck(e.sourceId, e.destinationId, e.value)
        }
        .to(kafkaSink)
        .run()

    // AS IS
    kafkaSource[AccountUpdate]
        .filter(command => repository.account.id == command.accountId && repository.account.amount + command.value >= 0)
        .mapAsync(1) { command =>
            repository.update(command.value).map(_ => AccountUpdated(command.accountId, command.value))
        }
        .to(kafkaSink)
        .run()

    kafkaSource[AccountUpdated]
        .filter(event => repository.account.id == event.accountId)
        .map { e =>
            println(s"Account ${e.accountId} was updated on ${e.value}. Balance: ${repository.account.amount}")
            e
        }
        .to(Sink.ignore)
        .run()

}
