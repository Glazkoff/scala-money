package money.repository

import java.util.UUID
import money.model._
import scala.concurrent.Future

trait AccountRepository {
    // Список счётов
    def accountsList(): Future[Seq[Account]]

    // Получение счёта
    def getAccount(id: UUID): Future[Account]

    // Поиск по счёту
    def findAccount(id: UUID): Future[Option[Account]]

    // Детализация счёта в API
    def accountDetalization(id: UUID): Future[Either[APIError, Account]]

    // Создание счёта
    def createAccount(create: CreateAccount): Future[Account]

    // Редактирование счёта
    def updateAccount(
        id: UUID,
        update: UpdateAccount
    ): Future[Either[APIError, Account]]

    // Удаление счёта
    def deleteAccount(id: UUID): Future[Either[APIError, Unit]]

    // Пополнение счёта
    def refillAccount(
        id: UUID,
        additionAmount: Int
    ): Future[Either[APIError, ChangeAccountAmountResult]]

    // Обналичить со счёта
    def withdrawFromAccount(
        id: UUID,
        withdrawalAmount: Int
    ): Future[Either[APIError, ChangeAccountAmountResult]]

    //  Перевести деньги по ID счёта
    def transferByAccountId(
        transfer: TransferByAccountId
    ): Future[Either[APIError, ChangeAccountAmountResult]]

    // Зачисление кэшбека
    def accrualCashback(id: UUID, additionAmount: Int): Future[Either[APIError, ChangeAccountAmountResult]]
}
