package money.model

import java.util.UUID

final case class User(
    id: UUID,
    firstName: String,
    lastName: String,
    username: String
)
