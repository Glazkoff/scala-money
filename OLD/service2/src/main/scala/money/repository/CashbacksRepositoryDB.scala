package money.repository

import java.util.UUID
import money.db.CashbackDb._
import money.model._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

class CashbacksRepositoryDB(payCashbackClient: PayCashbackClient)(implicit val ec: ExecutionContext, db: Database)
    extends CashbackRepository {

    override def cashbacksList(): Future[Seq[Cashback]] = {
        db.run(cashbackTable.result)
    }

    override def getCashback(id: UUID): Future[Cashback] = {
        db.run(cashbackTable.filter(_.id === id).result.head)
    }

    override def createCashback(create: CreateCashback): Future[Cashback] = {
        val newCashback =
            Cashback(
                senderUserId = create.senderUserId,
                transferAmount = create.transferAmount,
                cashbackPercent = create.cashbackPercent
            )

        for {
            _ <- db.run(cashbackTable += newCashback)
            res <- getCashback(newCashback.id)
        } yield res
    }

    override def payCashback(userId: UUID): Future[AccrualCashbackResponse] = {
        val query = cashbackTable.filter(cashback => cashback.senderUserId === userId && !cashback.isPaidOff)
        val result = for {
            cashbacks <- query.result
        } yield {
            cashbacks
                .groupBy(_.cashbackPercent)
                .map { case (cashbackPercent, cashbacks) =>
                    cashbackPercent -> cashbacks.map(_.transferAmount).sum
                }
        }

        db.run(result.transactionally).flatMap { cashbackSumsByPercent =>
            val cashbackAmount = cashbackSumsByPercent
                .map { case (cashbackPercent, transferAmountSum) =>
                    cashbackPercent * transferAmountSum
                }
                .sum
                .round
                .toInt

            val accrualCashbackRequest = AccrualCashbackRequest(userId, cashbackAmount)

            val action = for {
                _ <- cashbackTable.filter(_.senderUserId === userId).map(_.isPaidOff).update(true)
                accrualCashbackResponse <- DBIO.from(payCashbackClient.accrualCashback(accrualCashbackRequest))
            } yield accrualCashbackResponse

            db.run(action)
        }
    }
}
