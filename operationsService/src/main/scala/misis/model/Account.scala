package misis.model

import java.util.UUID

case class Account(id: Int, amount: Int) {
    def update(value: Int) = this.copy(amount = amount + value)
}

trait Command
case class ShowAccountBalance(accountId: Int) extends Command
case class CreateAccount(initialAmount: Int) extends Command
case class AccountUpdate(
    accountId: Int,
    value: Int,
    nextAccountId: Option[Int] = None
) extends Command

trait Event
case class AccountUpdated(accountId: Int, value: Int, nextAccountId: Option[Int] = None) extends Event
