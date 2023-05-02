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
import scala.util.{Failure, Success}
import com.typesafe.config.ConfigFactory

class Streams(repository: Repository, groupId: Int)(implicit
    val system: ActorSystem,
    executionContext: ExecutionContext
) extends WithKafka {
    def group = s"account-test-${groupId}"

    kafkaSource[ShowAccountBalance]
        .mapAsync(1) { event =>
            repository.getAccount(event.accountId).map {
                case Some(account) =>
                    println(s"Account ${event.accountId} has balance: ${account.amount}")
                    event
                case None =>
                    println(
                        s"Account ${event.accountId} didn't found. Available accounts: ${repository.getAccountKeys()}"
                    )
                    event
            }
        }
        .to(Sink.ignore)
        .run()

    kafkaSource[TransferStart]
        .map(event => {
            println("(2) Got TransferStart message")
            event
        })
        .filter(event => repository.accountExists(event.sourceId))
        .map { e =>
            println(s"(4) Send AccountFromAck - [SOURCE ACK] Account ${e.sourceId} exists and has enough money")
            AccountFromAck(e.sourceId, e.destinationId, e.value)
        }
        .to(kafkaSink)
        .run()

    kafkaSource[TransferCheckDestination]
        .map(event => {
            println("(6) Got TransferCheckDestination message")
            event
        })
        .filter(event => {
            println(
                s"(6.5) repository.accountExists(event.destinationId) ${repository.accountExists(event.destinationId)}"
            )
            repository.accountExists(event.destinationId)
        })
        .map { e =>
            println(s"(8) Send AccountToAck - [DESTINATION ACK] Account ${e.destinationId} exists ")
            AccountToAck(e.sourceId, e.destinationId, e.value)
        }
        .to(kafkaSink)
        .run()

    kafkaSource[AccountUpdate]
        .map(event => {
            println(s"(10) NEED TO UPDATE ACCOUNT #${event.accountId} WITH AMOUNT ${event.value}")
            println(s"(10.5) ACCOUNT #${event.accountId} EXISTS: ${repository.accountExists(event.accountId)}")
            event
        })
        .filter(command => repository.accountExists(command.accountId))
        .mapAsync(1) { command =>
            repository
                .updateAccount(command.accountId, command.value)
                .map(_ => AccountUpdated(command.accountId, command.value))
        }
        .to(kafkaSink)
        .run()

    kafkaSource[AccountCreated]
        .filter(event => repository.accountExists(event.accountId))
        .map { event =>
            println(s"[GROUP ${groupId}] Account ${event.accountId} was created with amount: ${event.amount}")
            event
        }
        .to(Sink.ignore)
        .run()

    kafkaSource[AccountUpdated]
        .filter(event => repository.accountExists(event.accountId))
        .mapAsync(1) { event =>
            repository.getAccount(event.accountId).map {
                case Some(account) =>
                    println(s"Account ${event.accountId} was updated on ${event.value}. Balance: ${account.amount}")
                    event
                case None => event
            }
        }
        .to(Sink.ignore)
        .run()
}
