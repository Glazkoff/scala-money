package misis.repository

import misis.kafka.TopicName
import misis.kafka.Streams
import misis.model._
import io.circe.generic.auto._
import scala.concurrent.Future

class Repository(streams: Streams) {

    def createAccount(createAccount: CreateAccount) = {
        implicit val commandTopicName: TopicName[CreateAccount] = streams.simpleTopicName[CreateAccount]

        streams.produceCommand(createAccount)
    }

    def showAccountBalance(showAccountBalance: ShowAccountBalance) = {
        implicit val commandTopicName: TopicName[ShowAccountBalance] = streams.simpleTopicName[ShowAccountBalance]

        streams.produceCommand(showAccountBalance)
    }

    def startTransfer(transfer: TransferStart) = {
        if (transfer.value > 0) {
            implicit val commandTopicName: TopicName[TransferStart] = streams.simpleTopicName[TransferStart]

            println("(1) SEND TransferStart COMMAND")
            streams.produceCommand(transfer)
        }
    }

    def completeTransfer(transfer: AccountToAck) = {
        if (transfer.value > 0) {
            implicit val commandTopicName: TopicName[AccountUpdate] = streams.simpleTopicName[AccountUpdate]

            streams.produceCommand(AccountUpdate(transfer.sourceId, -transfer.value))
            streams.produceCommand(AccountUpdate(transfer.destinationId, transfer.value))
        }
    }
}