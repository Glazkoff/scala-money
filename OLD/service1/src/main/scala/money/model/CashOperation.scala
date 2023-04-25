package money.model

import java.util.UUID
import java.time.LocalDateTime

final case class CashbackAccrual(
    userId: UUID,
    amountChange: Int
)

final case class CashOperation(
    accountId: UUID,
    amountChange: Int,
    opType: String // TOP_UP или CASHING_OUT
)

final case class CashOperationHistory(
    id: UUID = UUID.randomUUID(),
    accountId: UUID,
    amountChange: Int,
    opType: String, // TOP_UP или CASHING_OUT
    createdAt: LocalDateTime = LocalDateTime.now()
)
