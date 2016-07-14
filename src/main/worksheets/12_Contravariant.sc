import cats._
import cats.implicits._

/**
  * Contravariant extends Invariant and provides the contramap function
  *
  * contramap derives an order for B given Order[A] and B => A
  *
  */

// ordering example
case class Money(amount : Int)
case class Salary(size : Money)

implicit val showMoney : Show[Money] = Show.show(m => s"$$${m.amount}")
implicit val showSalary : Show[Salary] = showMoney.contramap(_.size)

// uses Show's contravariant...
Salary(Money(1000)).show

// scala.math.Ordering ...
Ordering.Int.compare(2,1)
Ordering.Int.compare(1,2)

import scala.math.Ordered._ // need this for < to work

implicit val moneyOrdering : Ordering[Money] = Ordering.by(_.amount)

Money(100) < Money(200)
