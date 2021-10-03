package stashaway

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

//SRS account can also be added
class StashAwayAccount(code:String){
  private[this] var _totalAmount: Int = 0
  def totalAmount: Int = _totalAmount

  val portfolios:mutable.HashMap[String, Portfolio] = new mutable.HashMap
  val plans : mutable.HashMap [Int, Plan] = new mutable.HashMap

  def createPortfolio(portfolio:String): Boolean ={
    val p = portfolios.getOrElse(portfolio.toLowerCase, new Portfolio(portfolio))
    portfolios.put(portfolio, p)
    p.status()
    true //Certain checks can be applied on name or creation can be rejected if the same name exist
  }

  /**
   * @typ : 0 = One Time Transfer, 1 = Monthly Transfer
   * @distributions : Key, Value pair of portfolio Name & Amount */
  def addModifyPlan(typ: Int, distributions:List[(String, Int)] ): Boolean ={
    if(typ != PlanTypes.ONETIME && typ != PlanTypes.MONTHLY){
      println(s"Plan type does not exist. 0 = One Time Transfer, 1 = Monthly Transfer")
      false
    }
    else{
      val plan = plans.getOrElse(typ, new Plan(typ))
      val distributionList = new ListBuffer[(Portfolio, Int)]
      for(distribution <- distributions){
        val portfolioOpt = portfolios.get(distribution._1)
        if(portfolioOpt.isDefined){
          distributionList.addOne((portfolioOpt.get, distribution._2))
        }
        else {
          throw new Exception(s"Portfolio ${distribution._1} does not exist")
        }
      }

      plan.addDistributions(distributionList.toList)
      plans.put(typ, plan)
      plan.status()
      true
    }
  }

  def addDeposit(deposit:Deposit): Boolean ={
    _totalAmount += deposit.amount
    executePlans()
    true
  }

  def executePlans(): Unit ={
    // Execute one time plan first only if the full amount is present
    // Once executed, delete & then execute the monthly
    // percentage wise execution can also be managed but we hold the amount till the actual plan amount exist in account

    var isOneTimeExecuted = false
    if(plans.get(PlanTypes.ONETIME).isDefined){
      val oneTimePlan = plans.get(PlanTypes.ONETIME).get
      if(oneTimePlan.planAmount <= _totalAmount) {
        if(oneTimePlan.execute()){
          _totalAmount = _totalAmount - oneTimePlan.planAmount
          plans.remove(PlanTypes.ONETIME)
          isOneTimeExecuted = true
        }
      }
    }
    else{
      isOneTimeExecuted = true
    }

    if(plans.get(PlanTypes.MONTHLY).isDefined && isOneTimeExecuted){
      val monthlyPlan = plans.get(PlanTypes.MONTHLY).get
      if(monthlyPlan.planAmount <= _totalAmount) {
        if(monthlyPlan.execute()){
          _totalAmount = _totalAmount - monthlyPlan.planAmount
        }
      }
    }
  }
}
