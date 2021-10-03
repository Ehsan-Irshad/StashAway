package stashaway

//Inheritance can be created for more complex problems
//Plan: onetime, monthly
class Plan (typ:Int){
  private[this] var _planAmount: Int = 0
  def planAmount: Int = _planAmount

  var _distributions : List[(Portfolio, Int)] = List()

  def addDistributions(distributions: List[(Portfolio, Int)]): Unit ={
    _planAmount = 0
    _distributions = distributions
    for(distribution <- _distributions){
      _planAmount += distribution._2
    }
  }

  def execute(): Boolean ={
    for(distribution <- _distributions){
      distribution._1.addAmount(distribution._2)
    }
    true
  }

  def status(): Unit ={
    println(s"PlanType: $typ PlanAmount: $planAmount ")
  }
}
