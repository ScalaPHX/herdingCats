import cats._
import cats.std.all._
import cats.implicits._

Semigroup[Int].combine(1,2)

Semigroup[List[Int]].combine(List(1,2,3), List(4,5,6))