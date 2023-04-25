package money.db

import java.util.UUID
import money.model.Cashback
import slick.jdbc.PostgresProfile.api._

object CashbackDb {
    class CashbackTable(tag: Tag) extends Table[Cashback](tag, "cashbacks") {

        val id = column[UUID]("id", O.PrimaryKey)
        val senderUserId = column[UUID]("senderUserId")
        val transferAmount = column[Int]("transferAmount")
        val cashbackPercent = column[Double]("cashbackPercent")
        val isPaidOff = column[Boolean]("isPaidOff")

        override def * = (
            id,
            senderUserId,
            transferAmount,
            cashbackPercent,
            isPaidOff
        ) <> ((Cashback.apply _).tupled, Cashback.unapply _)
    }

    val cashbackTable = TableQuery[CashbackTable]
}
