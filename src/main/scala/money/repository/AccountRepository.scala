package money.repository

import java.util.UUID
import money.model._
import scala.concurrent.Future

trait AccountRepository {
  // Список счётов
  def accountsList(): Future[Seq[Account]]

  // Детализация по счёту
  def getAccount(id: UUID): Future[Account]

  // Поиск по счёту
  def findAccount(id: UUID): Future[Option[Account]]

  // Создание счёта
  def createAccount(create: CreateAccount): Future[Account]

  // Редактирование счёта
  def updateAccount(
      id: UUID,
      update: UpdateAccount
  ): Future[Either[String, Account]]

  // Удаление счёта
  def deleteAccount(id: UUID): Future[Unit]

  // Пополнение счёта
  def refillAccount(
      id: UUID,
      additionAmount: Int
  ): Future[Either[String, ChangeAccountAmountResult]]

  // Обналичить со счёта
  def withdrawFromAccount(
      id: UUID,
      withdrawalAmount: Int
  ): Future[Either[String, ChangeAccountAmountResult]]

  //  Перевести деньги по ID счёта
  def transferByAccountId(
      accountId: UUID,
      withdrawalAmount: Int
  ): Future[Option[ChangeAccountAmountResult]]

  // Перевести деньги по номеру телефона
  def transferByPhone(
      phone: String,
      withdrawalAmount: Int
  ): Future[Option[ChangeAccountAmountResult]]

  // Выбрать приоритетный счёт
  def setUserPriorityAccount(
      priority: UserPriorityAccount
  ): Future[Option[UserPriorityAccount]]
}
