package money.repository

import money.model._
import money.db.CategoryDb._
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import java.util.UUID

class CategoriesRepositoryDB(implicit val ec: ExecutionContext, db: Database) extends CategoriesRepository {
    def categoriesList(): Future[Seq[Category]] = {
        db.run(categoryTable.result)
    }

    def getCategory(id: UUID): Future[Category] = {
        db.run(categoryTable.filter(_.id === id).result.head)
    }

    def createCategory(create: CreateCategory): Future[Category] = {
        val newCategory =
            Category(name = create.name, cashbackPercent = create.cashbackPercent)

        for {
            _ <- db.run(categoryTable += newCategory)
            res <- getCategory(newCategory.id)
        } yield res
    }
}
