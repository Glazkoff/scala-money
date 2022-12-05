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
    patricity = Some("Olegovich"),
    phone = "+796733357967",
    priorityAccountID = None,
    isAdmin = Some(false)
  )
  val repository: AccountRepositoryMutable = new AccountRepositoryMutable

  val createdAcc = repository.createAccount(
    CreateAccount(name = Some("test"), userId = user.id)
  )

  repository.updateAccount(
    id = createdAcc.id,
    UpdateAccount(name = Some("updated title"))
  )

  val createdAcc2 = repository.createAccount(
    CreateAccount(name = Some("test 2"), userId = user.id)
  )

  repository.deleteAccount(id = createdAcc2.id)

  println(repository.list().asJson)
}
