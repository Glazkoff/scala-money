package money.model

import java.util.UUID

final case class User(
    id: UUID,
    lastName: String,
    firstName: String,
    patricity: Option[String],
    phone: String,
    priorityAccountID: Option[UUID],
    isAdmin: Option[Boolean]
)
