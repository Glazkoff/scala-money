package money.model

import java.util.UUID
import java.time.LocalDateTime

final case class TransferByAccountId(
    senderAccountId: UUID,
    recipientAccountId: UUID,
    transferAmount: Int
)

final case class TransferHistory(
    id: UUID = UUID.randomUUID(),
    status: String,
    senderAccountId: UUID,
    recipientAccountId: UUID,
    categoryId: Option[UUID],
    transferAmount: Int,
    cashbackAmount: Option[Int],
    createdAt: LocalDateTime = LocalDateTime.now()
)
