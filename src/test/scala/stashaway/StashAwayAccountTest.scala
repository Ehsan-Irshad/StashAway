package stashaway
import org.scalatest.FunSuite

class StashAwayAccountTest extends FunSuite {
  test("happy"){
    val account = new StashAwayAccount("ABC")
    account.createPortfolio("High Risk")
    account.createPortfolio("Retirement")

    account.addModifyPlan( PlanTypes.ONETIME, List(("High Risk", 10000), ("Retirement", 500)))
    account.addModifyPlan( PlanTypes.MONTHLY, List(("High Risk", 0), ("Retirement", 100)))

    account.addDeposit(new Deposit(10500))
    account.addDeposit(new Deposit(100))

    assert(account.portfolios.get("High Risk").get.amountToBeInvested === 10000)
    assert(account.portfolios.get("Retirement").get.amountToBeInvested === 600)
  }

  test("norum"){
    val account = new StashAwayAccount("ABC")
    account.createPortfolio("High Risk")
    account.createPortfolio("Retirement")

    account.addModifyPlan( PlanTypes.ONETIME, List(("High Risk", 10000), ("Retirement", 500)))
    account.addModifyPlan( PlanTypes.MONTHLY, List(("High Risk", 0), ("Retirement", 100)))

    account.addDeposit(new Deposit(10400))

    assert(account.portfolios.get("High Risk").get.amountToBeInvested === 0)
    assert(account.portfolios.get("Retirement").get.amountToBeInvested === 0)
    assert(account.totalAmount === 10400)
  }

  test("single_deposit"){
    val account = new StashAwayAccount("ABC")
    account.createPortfolio("High Risk")
    account.createPortfolio("Retirement")

    account.addModifyPlan( PlanTypes.ONETIME, List(("High Risk", 10000), ("Retirement", 500)))
    account.addModifyPlan( PlanTypes.MONTHLY, List(("High Risk", 0), ("Retirement", 100)))

    account.addDeposit(new Deposit(10700))

    assert(account.portfolios.get("High Risk").get.amountToBeInvested === 10000)
    assert(account.portfolios.get("Retirement").get.amountToBeInvested === 600)
    assert(account.totalAmount === 100)
  }
}
