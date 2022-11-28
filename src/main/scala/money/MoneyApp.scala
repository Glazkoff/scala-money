package money

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn

object MoneyApp extends App {
  implicit val system: ActorSystem = ActorSystem("MoneyApp")

  val route = path("hello") {
    get {
      complete(
        HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          "<h1>Тестовый HTML-заголовок!</h1>"
        )
      )
    }
  }

  val bindingFuture = Http().newServerAt("0.0.0.0", 8081).bind(route)

  println(
    s"Сервер запущен. Перейдите к http://localhost:8081/hello\nНажмите RETURN чтобы остановить..."
  )
  StdIn.readLine()

  // TODO: Доделать по инструкции - 1:18:51 видео от 23.11.2022
}
