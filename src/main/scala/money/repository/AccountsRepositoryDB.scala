package money.repository

import java.util.UUID
import money.db.AccountDb._
import money.model._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

class AccountsRepositoryDB(implicit val ec: ExecutionContext, db: Database)
    extends AccountRepository {

  override def accountsList(): Future[Seq[Account]] = {
    db.run(accountTable.result)
  }

  override def getAccount(id: UUID): Future[Account] = {
    db.run(accountTable.filter(_.id === id).result.head)
  }

  override def findAccount(id: UUID): Future[Option[Account]] = {
    db.run(accountTable.filter(_.id === id).result.headOption)
  }

  override def createAccount(create: CreateAccount): Future[Account] = {
    val newAccount =
      Account(ownerUserId = create.ownerUserId, name = create.name)

    for {
      _ <- db.run(accountTable += newAccount)
      res <- getAccount(newAccount.id)
    } yield res
  }

  override def updateAccount(
      id: UUID,
      updateAccount: UpdateAccount
  ): Future[Option[Account]] = {
    for {
      _ <- db.run {
        accountTable
          .filter(_.id === id)
          .map(_.name)
          .update(updateAccount.name)
      }
      res <- findAccount(id)
    } yield res
  }

  override def deleteAccount(id: UUID): Future[Unit] = {
    db.run(accountTable.filter(_.id === id).delete).map(_ => ())
  }

  override def refillAccount(
      id: UUID,
      additionAmount: Int
  ): Future[Option[ChangeAccountAmountResult]] = ???

  override def withdrawFromAccount(
      id: UUID,
      withdrawalAmount: Int
  ): Future[Option[ChangeAccountAmountResult]] = ???

  override def transferByAccountId(
      accountId: UUID,
      withdrawalAmount: Int
  ): Future[Option[ChangeAccountAmountResult]] = ???

  override def transferByPhone(
      phone: String,
      withdrawalAmount: Int
  ): Future[Option[ChangeAccountAmountResult]] = ???

  def setUserPriorityAccount(
      priority: UserPriorityAccount
  ): Future[Option[UserPriorityAccount]] = ???
}
