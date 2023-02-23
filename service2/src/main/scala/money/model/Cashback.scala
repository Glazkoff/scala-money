package money.model

import java.util.UUID

final case class Cashback(
    id: UUID = UUID.randomUUID(),
    senderUserId: UUID,
    transferAmount: Int = 0,
    cashbackPercent: Double = 0,
    isPaidOff: Boolean = false
)

final case class CreateCashback(senderUserId: UUID, transferAmount: Int, cashbackPercent: Double)

final case class PayCashback(
    userId: UUID
)
final case class AccrualCashbackRequest(userId: UUID, amountChange: Int)
final case class AccrualCashbackResponse(id: UUID, amount: Int)
