package money.repository

import scala.collection.mutable
import java.util.UUID
import money.model._

class AccountRepositoryMutable extends AccountRepository {
  private val accountsStore = mutable.Map[UUID, Account]()

  override def list(): List[Account] =
    accountsStore.toList.map(_._2)

  override def createAccount(create: CreateAccount): Account = {
    val account =
      Account(id = UUID.randomUUID(), title = create.title)
    accountsStore.put(account.id, account)
    account
  }
  override def updateAccount(update: UpdateAccount): Option[Account] = {
    accountsStore.get(update.id).map { account =>
      {
        val updatedAccount = account.copy(title = update.title)
        accountsStore.put(account.id, updatedAccount)
        updatedAccount
      }
    }
  }
  override def deleteAccount(id: UUID): Option[Account] = {
    accountsStore.remove(id)
  }
}
