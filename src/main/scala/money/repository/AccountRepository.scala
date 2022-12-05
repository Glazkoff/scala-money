package money.repository

import money.model._
import java.util.UUID

trait AccountRepository {
  def list(): List[Account]
  def getAccount(id: UUID): Option[Account]
  def createAccount(item: CreateAccount): Account
  def updateAccount(id: UUID, item: UpdateAccount): Option[Account]
  def deleteAccount(id: UUID): Option[Account]
}
