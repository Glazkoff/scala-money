package misis.repository

import misis.kafka.TopicName
import misis.kafka.Streams
import misis.model._
import io.circe.generic.auto._
import scala.concurrent.Future

class Repository(streams: Streams) {

    def startTransfer(transfer: TransferStart) = {
        if (transfer.value > 0) {
            implicit val commandTopicName: TopicName[TransferStart] = streams.simpleTopicName[TransferStart]

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
