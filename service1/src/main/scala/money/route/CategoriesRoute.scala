package money.route

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import money.model._
import money.repository.CategoriesRepository
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

class CategoriesRoute(repository: CategoriesRepository)(implicit
    ec: ExecutionContext
) extends FailFastCirceSupport {
    def route =
        (path("categories") & get) {
            {
                val list = repository.categoriesList()
                complete(list)
            }
        } ~ (path("categories") & post) {
            entity(as[CreateCategory]) { newCategory =>
                complete(repository.createCategory(newCategory))
            }
        }
}
