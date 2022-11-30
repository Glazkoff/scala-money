package money

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import money.model._
import money.repository.AccountRepositoryMutable
import java.util.UUID

object RepositoryTest {
  val user: User = User(
    id = UUID.randomUUID(),
    firstName = "Nikita",
    lastName = "Glazkov",
    username = "login"
  )
  val repository: AccountRepositoryMutable = new AccountRepositoryMutable

  val createdAcc = repository.createAccount(
    CreateAccount(title = "test", userId = user.id)
  )

  repository.updateAccount(
    id = createdAcc.id,
    UpdateAccount(title = "updated title")
  )

  val createdAcc2 = repository.createAccount(
    CreateAccount(title = "test 2", userId = user.id)
  )

  repository.deleteAccount(id = createdAcc2.id)

  println(repository.list().asJson)
}
