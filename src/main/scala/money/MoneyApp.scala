package money

import money.model.User
import java.util.UUID
import money.repository.AccountRepositoryMutable
import money.model._

object MoneyApp extends App {
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
    UpdateAccount(id = createdAcc.id, title = "updated title")
  )

  val createdAcc2 = repository.createAccount(
    CreateAccount(title = "test 2", userId = user.id)
  )

  repository.deleteAccount(id = createdAcc2.id)

  println(repository.list())
}
