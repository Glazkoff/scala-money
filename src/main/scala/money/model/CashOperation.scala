package money.model

import java.util.UUID
import java.time.LocalDateTime

abstract class OperationType(val name: String, val code: String)

object OperationType {
  case object CASHING_OUT extends OperationType("CASHING_OUT", "CASHING_OUT")
  case object TOP_UP extends OperationType("TOP_UP", "TOP_UP")
}

final case class CashOperation(
    id: UUID,
    accountId: UUID,
    amount: Int,
    opType: OperationType,
    createdAt: LocalDateTime
)
