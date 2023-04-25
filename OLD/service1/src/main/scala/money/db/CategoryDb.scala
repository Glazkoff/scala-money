package money.db

import java.util.UUID
import money.model.Category
import slick.jdbc.PostgresProfile.api._

object CategoryDb {
    class CategoryTable(tag: Tag) extends Table[Category](tag, "category") {

        val id = column[UUID]("id", O.PrimaryKey)
        val name = column[String]("name")
        val cashbackPercent = column[Double]("cashbackPercent")

        override def * = (
            id,
            name,
            cashbackPercent
        ) <> ((Category.apply _).tupled, Category.unapply _)
    }

    val categoryTable = TableQuery[CategoryTable]
}
