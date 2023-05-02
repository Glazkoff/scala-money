package misis.repository

import misis.model.Account
import scala.concurrent.Future
import scala.collection.mutable

class Repository() {
    private var currentId = 0
    private def getNextId(): Int = {
        currentId += 1
        currentId
    }
    private val accounts = mutable.Map.empty[Int, Account]

    def getAccountKeys(): Iterable[String] = {
        accounts.keys.map(_.toString)
    }

    def accountExists(accountId: Int): Boolean = {
        accounts.contains(accountId)
    }

    def accountExistsWithIdAndAmount(accountId: Int, amount: Int): Boolean = {
        accounts.get(accountId).exists(_.amount >= amount)
    }

    def getAccount(accountId: Int): Future[Option[Account]] = {
        Future.successful(accounts.get(accountId))
    }

    def createAccount(initialAmount: Int = 0): Future[Account] = {
        val newId = getNextId()
        val account = Account(newId, initialAmount)
        accounts += (newId -> account)
        Future.successful(account)
    }

    def updateAccount(id: Int, value: Int): Future[Unit] = {
        accounts.get(id) match {
            case Some(account) =>
                val updatedAccount = account.update(value)
                accounts += (id -> updatedAccount)
                Future.successful(())
            case None =>
                Future.failed(new IllegalArgumentException(s"Account $id does not exist"))
        }
    }
}
