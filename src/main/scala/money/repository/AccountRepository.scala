package money.repository

import money.model._
import java.util.UUID

trait AccountRepository {
  // Список счётов
  def list(): List[Account]

  // Детализация по счёту
  def getAccount(id: UUID): Option[Account]

  // Создание счёта
  def createAccount(item: CreateAccount): Account

  // Редактирование счёта
  def updateAccount(id: UUID, item: UpdateAccount): Option[Account]

  // Удаление счёта
  def deleteAccount(id: UUID): Option[Account]

  // Пополнение счёта
  def refillAccount(additionAmount: Int): Option[ChangeAccountAmountResult]

  // Обналичить со счёта
  def withdrawFromAccount(
      withdrawalAmount: Int
  ): Option[ChangeAccountAmountResult]

  // TODO: Перевести деньги по ID счёта
  def transferByAccountId(
      accountId: UUID,
      withdrawalAmount: Int
  ): Option[ChangeAccountAmountResult]

  // TODO: Перевести деньги по номеру телефона
  def transferByPhone(
      phone: String,
      withdrawalAmount: Int
  ): Option[ChangeAccountAmountResult]

  // TODO: Выбрать приоритетный счёт
  def setUserPriorityAccount(
      priority: UserPriorityAccount
  ): Option[UserPriorityAccount]
}
