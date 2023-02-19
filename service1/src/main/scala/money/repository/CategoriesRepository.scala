package money.repository

import money.model._
import scala.concurrent.Future

trait CategoriesRepository {
    // Список категорий
    def categoriesList(): Future[Seq[Category]]

    //

    //
}
