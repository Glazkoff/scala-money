package money.repository

import java.util.UUID
import money.db.CashbackDb._
import money.model._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

class CashbacksRepositoryDB(implicit val ec: ExecutionContext, db: Database) extends CashbackRepository {

    override def cashbacksList(): Future[Seq[Cashback]] = {
        db.run(cashbackTable.result)
    }
}
