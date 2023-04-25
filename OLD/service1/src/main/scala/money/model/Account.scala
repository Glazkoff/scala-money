package money.model

import java.util.UUID

final case class Account(
    id: UUID = UUID.randomUUID(),
    ownerUserId: UUID,
    categoryId: UUID,
    amount: Int = 0,
    name: Option[String]
)
final case class CreateAccount(name: Option[String], ownerUserId: UUID, categoryId: UUID)
final case class UpdateAccount(name: Option[String])

final case class ChangeAccountAmountResult(
    id: UUID,
    amount: Int
)
