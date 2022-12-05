package money.repository

import scala.collection.mutable
import java.util.UUID
import money.model._

class AccountRepositoryMutable extends AccountRepository {

  private val accountsStore = mutable.Map[UUID, Account]()

  override def list(): List[Account] =
    accountsStore.toList.map(_._2)

  override def getAccount(accountId: UUID): Option[Account] = accountsStore.get(
    accountId
  )

  override def createAccount(create: CreateAccount): Account = {
    val account =
      Account(
        id = UUID.randomUUID(),
        ownerUserId = create.ownerUserId,
        amount = 0,
        name = create.name
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
        val updatedAccount = account.copy(name = update.name)
        accountsStore.put(account.id, updatedAccount)
        updatedAccount
      }
    }
  }
  override def deleteAccount(id: UUID): Option[Account] = {
    accountsStore.remove(id)
  }

  // TODO:
  override def refillAccount(
      additionAmount: Int
  ): Option[ChangeAccountAmountResult] = ???

  // TODO:
  override def withdrawFromAccount(
      withdrawalAmount: Int
  ): Option[ChangeAccountAmountResult] = ???

  // TODO:
  override def transferByAccountId(
      accountId: UUID,
      withdrawalAmount: Int
  ): Option[ChangeAccountAmountResult] = ???

  // TODO:
  def transferByPhone(
      phone: String,
      withdrawalAmount: Int
  ): Option[ChangeAccountAmountResult] = ???

  // TODO:
  def setUserPriorityAccount(
      priority: UserPriorityAccount
  ): Option[UserPriorityAccount] = ???
}
