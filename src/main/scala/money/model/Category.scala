package money.model

import java.util.UUID

final case class Category(
    id: UUID,
    name: String,
    cashbackPercent: Int
)
