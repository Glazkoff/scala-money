package money.db

import money.db.AccountDb._
import money.db.CategoryDb._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

class InitDb(implicit val ec: ExecutionContext, db: Database) {
    def prepare(): Future[_] = {
        db.run(accountTable.schema.createIfNotExists)
        db.run(categoryTable.schema.createIfNotExists)
    }
}
