package money

import money.model.User
import java.util.UUID
import money.repository.AccountRepositoryMutable
import money.model._

object MoneyApp extends App {
  val repository: AccountRepositoryMutable = new AccountRepositoryMutable

  val createdAcc = repository.createAccount(
    CreateAccount(title = "test")
  )

  repository.updateAccount(
    UpdateAccount(id = createdAcc.id, title = "updated title")
  )

  val createdAcc2 = repository.createAccount(
    CreateAccount(title = "test 2")
  )

  repository.deleteAccount(id = createdAcc2.id)

  println(repository.list())
}
