package money.repository

import java.util.UUID
import money.model._
import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class AccountRepositoryMutable(implicit val ex: ExecutionContext)
    extends AccountRepository {

  private val accountsStore = mutable.Map[UUID, Account]()

  override def accountsList(): Future[List[Account]] = Future {
    accountsStore.toList.map(_._2)
  }

  override def getAccount(accountId: UUID): Future[Account] = Future {
    accountsStore(
      accountId
    )
  }

  override def findAccount(accountId: UUID): Future[Option[Account]] = Future {
    accountsStore.get(
      accountId
    )
  }

  override def accountDetalization(
      id: UUID
  ): Future[Either[APIError, Account]] = ???

  override def createAccount(create: CreateAccount): Future[Account] = Future {
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
  ): Future[Either[APIError, Account]] = Future {
    accountsStore
      .get(id)
      .map { account =>
        {
          val updatedAccount = account.copy(name = update.name)
          accountsStore.put(account.id, updatedAccount)
          Right(updatedAccount)
        }
      }
      .getOrElse(Left(APIError("Счёт не найден!")))
  }

  override def deleteAccount(id: UUID): Future[Either[APIError, Unit]] =
    Future {
      Right(accountsStore.remove(id))
    }

  override def refillAccount(
      id: UUID,
      additionAmount: Int
  ): Future[Either[APIError, ChangeAccountAmountResult]] = Future {
    accountsStore
      .get(id)
      .map { account =>
        {
          val updatedAccount =
            account.copy(amount = account.amount + additionAmount)
          accountsStore.put(account.id, updatedAccount)
          Right(
            ChangeAccountAmountResult(
              id,
              updatedAccount.amount
            )
          )
        }
      }
      .getOrElse(Left(APIError("Аккаунт не найден!")))
  }

  // TODO:
  override def withdrawFromAccount(
      id: UUID,
      withdrawalAmount: Int
  ): Future[Either[APIError, ChangeAccountAmountResult]] = Future {
    accountsStore
      .get(id)
      .map { account =>
        // TODO: добавить проверку на account.amount >= withdrawalAmount
        val updatedAccount =
          account.copy(amount = account.amount - withdrawalAmount)
        accountsStore.put(account.id, updatedAccount)
        Right(
          ChangeAccountAmountResult(
            id,
            updatedAccount.amount
          )
        )
      }
      .getOrElse(Left(APIError("Счёт не найден!")))
  }

  // TODO:
  override def transferByAccountId(
      transfer: TransferByAccountId
  ): Future[Either[APIError, ChangeAccountAmountResult]] = ???

  // TODO:
  def transferByPhone(
      phone: String,
      transferAmount: Int
  ): Future[Either[APIError, ChangeAccountAmountResult]] = ???

  // TODO:
  def setUserPriorityAccount(
      priority: UserPriorityAccount
  ): Future[Option[UserPriorityAccount]] = ???
}
