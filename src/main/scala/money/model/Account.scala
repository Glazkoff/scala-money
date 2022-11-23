package money.model

import java.util.UUID

final case class Account(id: UUID, title: String)
final case class CreateAccount(title: String)
final case class UpdateAccount(id: UUID, title: String)
