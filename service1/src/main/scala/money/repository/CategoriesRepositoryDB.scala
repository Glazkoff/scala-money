package money.repository

import money.model._
import money.db.CategoryDb._
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

class CategoriesRepositoryDB(implicit val ec: ExecutionContext, db: Database) extends CategoriesRepository {
    def categoriesList(): Future[Seq[Category]] = {
        db.run(categoryTable.result)
    }
}
