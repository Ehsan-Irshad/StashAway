package stashaway

class Portfolio (name:String){
  private[this] var _amountToBeInvested: Int = 0
  def amountToBeInvested: Int = _amountToBeInvested

  def addAmount(amount:Int): Unit ={
    _amountToBeInvested += amount
  }

  def status(): Unit ={
    println(s"Portfolio Name: $name, Amount: $amountToBeInvested")
  }
}
