package money.db

import java.util.UUID
import money.model.Account
import slick.jdbc.PostgresProfile.api._

object AccountDb {
    class AccountTable(tag: Tag) extends Table[Account](tag, "accounts") {

        val id = column[UUID]("id", O.PrimaryKey)
        val ownerUserId = column[UUID]("ownerUserId")
        val amount = column[Int]("amount")
        val name = column[Option[String]]("name")

        override def * = (
            id,
            ownerUserId,
            amount,
            name
        ) <> ((Account.apply _).tupled, Account.unapply _)
    }

    val accountTable = TableQuery[AccountTable]
}
