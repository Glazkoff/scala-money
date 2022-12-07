package money.repository

import java.util.UUID
import money.model._

class AccountsRepositoryDB extends AccountRepository {

  override def list(): List[Account] = ???

  override def getAccount(id: UUID): Option[Account] = ???

  override def createAccount(item: CreateAccount): Account = ???

  override def updateAccount(id: UUID, item: UpdateAccount): Option[Account] =
    ???

  override def deleteAccount(id: UUID): Option[Account] = ???

  override def refillAccount(
      id: UUID,
      additionAmount: Int
  ): Option[ChangeAccountAmountResult] = ???

  override def withdrawFromAccount(
      id: UUID,
      withdrawalAmount: Int
  ): Option[ChangeAccountAmountResult] = ???

  override def transferByAccountId(
      accountId: UUID,
      withdrawalAmount: Int
  ): Option[ChangeAccountAmountResult] = ???

  override def transferByPhone(
      phone: String,
      withdrawalAmount: Int
  ): Option[ChangeAccountAmountResult] = ???

  def setUserPriorityAccount(
      priority: UserPriorityAccount
  ): Option[UserPriorityAccount] = ???
}
