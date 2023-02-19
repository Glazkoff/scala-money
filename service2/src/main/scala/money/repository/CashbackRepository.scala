package money.repository

import java.util.UUID
import money.model._
import scala.concurrent.Future

trait CashbackRepository {
    // Список счётов
    def cashbacksList(): Future[Seq[Cashback]]
}
