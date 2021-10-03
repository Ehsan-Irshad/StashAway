package stashaway

//Inheritance can be created for more complex problems
//Transactions: withdraw, deposit
class Deposit (amnt:Int){
  private val _amount:Int = amnt
  def amount: Int = _amount
}
