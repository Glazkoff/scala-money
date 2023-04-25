package money.model

import java.util.UUID

case class CashbackCreateRequest(senderUserId: UUID, transferAmount: Int, cashbackPercent: Double)
case class CashbackCreateResponse(
    id: UUID,
    senderUserId: UUID,
    transferAmount: Int,
    cashbackPercent: Double,
    isPaidOff: Boolean
)
