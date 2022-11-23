package money.model

import java.util.UUID

final case class Account(id: UUID, userId: UUID, title: String)
final case class CreateAccount(title: String, userId: UUID)
final case class UpdateAccount(id: UUID, title: String)
