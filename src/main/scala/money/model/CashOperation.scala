package money.model

import java.util.UUID
import java.time.LocalDateTime

// TODO: разобраться с ENUM
// abstract class OperationType(val name: String, val code: String)
// object OperationType {
//   case object CASHING_OUT extends OperationType("CASHING_OUT", "CASHING_OUT")
//   case object TOP_UP extends OperationType("TOP_UP", "TOP_UP")
// }

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
