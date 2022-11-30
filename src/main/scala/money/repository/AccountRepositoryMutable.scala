package money.repository

import scala.collection.mutable
import java.util.UUID
import money.model._

class AccountRepositoryMutable extends AccountRepository {

  private val accountsStore = mutable.Map[UUID, Account]()

  override def list(): List[Account] =
    accountsStore.toList.map(_._2)

  override def getAccount(accountId: UUID): Account = accountsStore(accountId)

  override def createAccount(create: CreateAccount): Account = {
    val account =
      Account(
        id = UUID.randomUUID(),
        userId = create.userId,
        title = create.title
      )
    accountsStore.put(account.id, account)
    account
  }
  override def updateAccount(
      id: UUID,
      update: UpdateAccount
  ): Option[Account] = {
    accountsStore.get(id).map { account =>
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
