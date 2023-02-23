package money.repository

import java.util.UUID
import money.model._
import scala.concurrent.Future

trait CashbackRepository {
    // Список счётов
    def cashbacksList(): Future[Seq[Cashback]]

    def getCashback(id: UUID): Future[Cashback]

    def createCashback(create: CreateCashback): Future[Cashback]

    // TODO: ручка - списание кэшбека
    def payCashback(userId: UUID): Future[AccrualCashbackResponse]
}
