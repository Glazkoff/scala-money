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
  ): Future[Either[String, Account]] = {
    val query = accountTable.filter(_.id === id).map(_.name)

    for {
      oldAccountNameOpt <- db.run(query.result.headOption)
      newName = updateAccount.name
      updatedName = oldAccountNameOpt
        .map { oldName =>
          {
            if (oldName == newName) Left("Новое название совпадает с текущим!")
            else Right(newName)
          }
        }
        .getOrElse(Left("Счёт не найден!"))
      future = updatedName.map(name => db.run { query.update(name) }) match {
        case Right(future) => future.map(Right(_))
        case Left(s)       => Future.successful(Left(s))
      }
      updated <- future
      res <- findAccount(id)
    } yield updated.map(_ => res.get)
  }

  override def deleteAccount(id: UUID): Future[Unit] = {
    db.run(accountTable.filter(_.id === id).delete).map(_ => ())
  }

  override def refillAccount(
      id: UUID,
      additionAmount: Int
  ): Future[Either[APIError, ChangeAccountAmountResult]] = {
    val query = accountTable.filter(_.id === id).map(_.amount)

    for {
      oldAccountOpt <- db.run(query.result.headOption)
      updatedAmount = oldAccountOpt
        .map { oldAmount => Right(oldAmount + additionAmount) }
        .getOrElse(Left(APIError("Счёт не найден!")))
      future = updatedAmount.map(amount =>
        db.run { query.update(amount) }
      ) match {
        case Right(future) => future.map(Right(_))
        case Left(s)       => Future.successful(Left(s))
      }
      updated <- future
      res <- findAccount(id)
    } yield updated.map(_ => ChangeAccountAmountResult(id, res.get.amount))
  }

  override def withdrawFromAccount(
      id: UUID,
      withdrawalAmount: Int
  ): Future[Either[APIError, ChangeAccountAmountResult]] = {
    val query = accountTable.filter(_.id === id).map(_.amount)

    for {
      oldAccountOpt <- db.run(query.result.headOption)
      updatedAmount = oldAccountOpt
        .map { oldAmount =>
          {
            if (oldAmount >= withdrawalAmount)
              Right(oldAmount - withdrawalAmount)
            else
              Left(APIError("Недостаточно средств!"))
          }
        }
        .getOrElse(Left(APIError("Счёт не найден!")))
      future = updatedAmount.map(amount =>
        db.run { query.update(amount) }
      ) match {
        case Right(future) => future.map(Right(_))
        case Left(s)       => Future.successful(Left(s))
      }
      updated <- future
      res <- findAccount(id)
    } yield updated.map(_ => ChangeAccountAmountResult(id, res.get.amount))
  }

  override def transferByAccountId(
      transfer: TransferByAccountId
  ): Future[Either[APIError, ChangeAccountAmountResult]] = {
    val senderAccountQuery =
      accountTable.filter(_.id === transfer.senderAccountId).map(_.amount)
    val recipientAccountQuery =
      accountTable.filter(_.id === transfer.recipientAccountId).map(_.amount)

    for {
      senderAccountOpt <- db.run(senderAccountQuery.result.headOption)
      recipientAccountOpt <- db.run(recipientAccountQuery.result.headOption)
      transferAmount = transfer.transferAmount
      senderAmountUpd = senderAccountOpt
        .map { senderAmount =>
          {
            if (senderAmount >= transferAmount)
              Right(senderAmount - transferAmount)
            else
              Left(APIError("Недостаточно средств!"))
          }
        }
        .getOrElse(Left(APIError("Счёт не найден!")))
      recipientAmountUpd = recipientAccountOpt
        .map { recipientAmount =>
          {
            Right(recipientAmount + transferAmount)
          }
        }
        .getOrElse(Left(APIError("Счёт не найден!")))
      senderFuture = senderAmountUpd.map(amount =>
        db.run { senderAccountQuery.update(amount) }
      ) match {
        case Right(future) => {
          recipientAmountUpd.map(amount =>
            db.run { recipientAccountQuery.update(amount) }
          ) match {
            case Right(future) => future.map(Right(_))
            case Left(s)       => Future.successful(Left(s))
          }
        }
        case Left(s) => Future.successful(Left(s))
      }
      updated <- senderFuture
      res <- findAccount(transfer.senderAccountId)
    } yield updated.map(_ =>
      ChangeAccountAmountResult(transfer.senderAccountId, res.get.amount)
    )
  }

  override def transferByPhone(
      phone: String,
      transferAmount: Int
  ): Future[Either[APIError, ChangeAccountAmountResult]] = ???

  def setUserPriorityAccount(
      priority: UserPriorityAccount
  ): Future[Option[UserPriorityAccount]] = ???
}
