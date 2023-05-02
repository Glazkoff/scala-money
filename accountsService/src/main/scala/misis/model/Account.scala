package misis.model

case class Account(id: Int, amount: Int) {
    def update(value: Int) = this.copy(amount = amount + value)
}

trait Command
case class ShowAccountBalance(accountId: Int) extends Command
case class CreateAccount(initialAmount: Int) extends Command
case class AccountUpdate(accountId: Int, value: Int) extends Command

trait Event
case class AccountCreated(accountId: Int, amount: Int) extends Event
case class AccountUpdated(accountId: Int, value: Int) extends Event
