package money.model

import java.util.UUID
import java.time.LocalDateTime

final case class CashOperation(
    id: UUID,
    accountId: UUID,
    amount: Int,
    opType: String,
    createdAt: LocalDateTime
)
