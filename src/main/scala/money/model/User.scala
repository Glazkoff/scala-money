package money.model

import java.util.UUID

final case class User(
    id: UUID = UUID.randomUUID(),
    lastName: String,
    firstName: String,
    patricity: Option[String],
    phone: String,
    priorityAccountID: Option[UUID],
    isAdmin: Option[Boolean]
)

final case class UserPriorityAccount(
    userId: UUID,
    accountId: UUID
)
