package money.repository

import java.util.UUID
import money.db.AccountDb._
import money.db.CategoryDb._
import money.model._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

class AccountsRepositoryDB(cashbackClient: CashbackClient)(implicit val ec: ExecutionContext, db: Database)
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

    def findAccountByUserId(userId: UUID): Future[Option[Account]] = {
        db.run(accountTable.filter(_.ownerUserId === userId).result.headOption)
    }

    def accountDetalization(id: UUID): Future[Either[APIError, Account]] = {
        val query = accountTable.filter(_.id === id)
        for {
            accountOpt <- db.run(query.result.headOption)
            res = accountOpt
                .map(Right(_))
                .getOrElse(Left(APIError("Счёт не найден!")))
        } yield res
    }

    override def createAccount(create: CreateAccount): Future[Account] = {
        val newAccount =
            Account(ownerUserId = create.ownerUserId, name = create.name, categoryId = create.categoryId)

        for {
            _ <- db.run(accountTable += newAccount)
            res <- getAccount(newAccount.id)
        } yield res
    }

    override def updateAccount(
        id: UUID,
        updateAccount: UpdateAccount
    ): Future[Either[APIError, Account]] = {
        val query = accountTable.filter(_.id === id).map(_.name)

        for {
            oldAccountNameOpt <- db.run(query.result.headOption)
            newName = updateAccount.name
            updatedName = oldAccountNameOpt
                .map { oldName =>
                    {
                        if (oldName == newName)
                            Left(APIError("Новое название совпадает с текущим!"))
                        else Right(newName)
                    }
                }
                .getOrElse(Left(APIError("Счёт не найден!")))
            future = updatedName.map(name => db.run { query.update(name) }) match {
                case Right(future) => future.map(Right(_))
                case Left(s) => Future.successful(Left(s))
            }
            updated <- future
            res <- findAccount(id)
        } yield updated.map(_ => res.get)
    }

    override def deleteAccount(id: UUID): Future[Either[APIError, Unit]] = {
        val query = accountTable.filter(_.id === id)
        for {
            accountOpt <- db.run(query.result.headOption)
            deletedAccount = accountOpt
                .map(account => {
                    if (account.amount != 0) {
                        Left(APIError("Нельзя удалить счёт, если сумма на счёте не равна 0!"))
                    } else {
                        Right(account)
                    }
                })
                .getOrElse(Left(APIError("Счёт не найден!")))
            future = deletedAccount.map(amount => db.run { query.delete }) match {
                case Right(future) => future.map(Right(_))
                case Left(s) => Future.successful(Left(s))
            }
            deleted <- future
        } yield { deleted.map(Right(_)) }
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
            future = updatedAmount.map(amount => db.run { query.update(amount) }) match {
                case Right(future) => future.map(Right(_))
                case Left(s) => Future.successful(Left(s))
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
            future = updatedAmount.map(amount => db.run { query.update(amount) }) match {
                case Right(future) => future.map(Right(_))
                case Left(s) => Future.successful(Left(s))
            }
            updated <- future
            res <- findAccount(id)
        } yield updated.map(_ => ChangeAccountAmountResult(id, res.get.amount))
    }

    override def transferByAccountId(
        transfer: TransferByAccountId
    ): Future[Either[APIError, ChangeAccountAmountResult]] = {
        val senderAccountQuery =
            accountTable.filter(_.id === transfer.senderAccountId)
        val recipientAccountQuery =
            accountTable.filter(_.id === transfer.recipientAccountId)

        for {
            senderAccountOpt <- db.run(senderAccountQuery.result.headOption)
            recipientAccountOpt <- db.run(recipientAccountQuery.result.headOption)
            transferAmount = transfer.transferAmount
            senderAmountUpd = senderAccountOpt
                .map { senderAccount =>
                    if (senderAccount.amount >= transferAmount)
                        Right(senderAccount.amount - transferAmount)
                    else
                        Left(APIError("Недостаточно средств!"))
                }
                .getOrElse(Left(APIError("Счёт не найден!")))
            recipientAmountUpd = recipientAccountOpt
                .map { recipientAccount =>
                    Right(recipientAccount.amount + transferAmount)
                }
                .getOrElse(Left(APIError("Счёт не найден!")))
            senderFuture = senderAmountUpd.map(amount =>
                db.run { senderAccountQuery.map(_.amount).update(amount) }
            ) match {
                case Right(future) => {
                    recipientAmountUpd.map(amount =>
                        db.run { recipientAccountQuery.map(_.amount).update(amount) }
                    ) match {
                        case Right(future) => future.map(Right(_))
                        case Left(s) => Future.successful(Left(s))
                    }
                }
                case Left(s) => Future.successful(Left(s))
            }
            updated <- senderFuture
            maybeRecipientAccount <- findAccount(transfer.recipientAccountId)
            maybeCategory <- maybeRecipientAccount
                .map(account =>
                    db.run(
                        categoryTable
                            .filter(_.id === account.categoryId)
                            .result
                            .headOption
                    )
                )
                .getOrElse(Future.successful(None))
            cashbackPercent <- Future.successful(maybeCategory.map(_.cashbackPercent).getOrElse(0.0))
            response <-
                if (
                    senderAccountOpt.exists(
                        _.ownerUserId != recipientAccountOpt.map(_.ownerUserId).getOrElse(-1L)
                    ) && cashbackPercent != 0.0
                ) {
                    cashbackClient.createCashback(
                        CashbackCreateRequest(senderAccountOpt.map(_.ownerUserId).get, transferAmount, cashbackPercent)
                    )
                } else {
                    Future.successful(())
                }
            res <- findAccount(transfer.senderAccountId)
        } yield updated.map(_ => ChangeAccountAmountResult(transfer.senderAccountId, res.get.amount))
    }

    override def accrualCashback(
        userId: UUID,
        additionAmount: Int
    ): Future[Either[APIError, ChangeAccountAmountResult]] = {
        // TODO: пофиксить изменение всех счетов на изменение одного
        val query = accountTable.filter(_.ownerUserId === userId).map(_.amount)

        for {
            oldAccountOpt <- db.run(query.result.headOption)
            updatedAmount = oldAccountOpt
                .map { oldAmount => Right(oldAmount + additionAmount) }
                .getOrElse(Left(APIError("Счёт не найден!")))
            future = updatedAmount.map(amount => db.run { query.update(amount) }) match {
                case Right(future) => future.map(Right(_))
                case Left(s) => Future.successful(Left(s))
            }
            updated <- future
            res <- findAccountByUserId(userId)
        } yield updated.map(_ => ChangeAccountAmountResult(res.get.id, res.get.amount))
    }
}
