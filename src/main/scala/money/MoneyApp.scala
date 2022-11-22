package money

import money.model.User
import java.util.UUID
import money.repository.AccountRepositoryMutable
import money.model._

object MoneyApp extends App {
  // val u1: User = User(UUID.randomUUID(), "Nikita", "Glazkov", "!")

  val repository = new AccountRepositoryMutable
  repository.createAccount(
    CreateAccount(id = UUID.randomUUID(), title = "test")
  )
  println("MoneyApp!")
  println(repository.list())
}
