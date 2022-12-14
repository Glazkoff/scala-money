package money.model

import java.util.UUID

final case class Category(
    id: UUID = UUID.randomUUID(),
    name: String,
    cashbackPercent: Int
)
