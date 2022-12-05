package money.model

import java.util.UUID
import java.time.LocalDateTime

final case class Trnasfer(
    id: UUID,
    status: String,
    senderAccountId: UUID,
    recipientAccountId: UUID,
    categoryId: Option[UUID],
    transferAmount: Int,
    cashbackAmount: Option[Int],
    createdAt: LocalDateTime
)
