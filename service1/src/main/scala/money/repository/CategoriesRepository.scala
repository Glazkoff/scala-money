package money.repository

import java.util.UUID
import money.model._
import scala.concurrent.Future

trait CategoriesRepository {
    // Список категорий
    def categoriesList(): Future[Seq[Category]]

    // Детализация категории
    def getCategory(id: UUID): Future[Category]

    // Создание категории
    def createCategory(create: CreateCategory): Future[Category]

    //
}
