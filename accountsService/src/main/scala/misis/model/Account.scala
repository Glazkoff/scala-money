package misis.model

case class Account(id: Int, amount: Int) {
    def update(value: Int) = this.copy(amount = amount + value)
}

trait Command
case class AccountUpdate(accountId: Int, value: Int) extends Command

trait Event
case class AccountUpdated(accountId: Int, value: Int) extends Event
