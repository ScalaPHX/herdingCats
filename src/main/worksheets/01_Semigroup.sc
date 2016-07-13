import cats._
import cats.implicits._

/**
  * A semigroup for a type A has a single operation - combine
  *
  * combine takes two values of type A and returns the a value of type A
  *
  * This is guaranteed to be associative
  */

// combine is generally considered concatenation...

Semigroup[Int].combine(1,2)

Semigroup[List[Int]].combine(List(1,2,3), List(4,5,6))

Semigroup[Int].combine(1,2)   // 3
Semigroup[Option[Int]].combine(Option(1), Option(2)) // Some(3)
Semigroup[Option[Int]].combine(Option(1), None)      // ???
Semigroup[Int => Int].combine({(x : Int) => x + 1}, {(x : Int) => x * 10}).apply(6) // ???

// combine vs ++

println(Map("foo" -> Map("bar" -> 5)) ++  Map("foo" -> Map("bar" -> 6), "baz" -> Map()))
println(Map("foo" -> Map("bar" -> 5)).combine(Map("foo" -> Map("bar" -> 6), "baz" -> Map())))
// second example...
println(Map("foo" -> List(1, 2)) ++ Map("foo" -> List(3,4), "bar" -> List(42)))
println(Map("foo" -> List(1, 2)).combine(Map("foo" -> List(3,4), "bar" -> List(42))))

// inline syntax
val one = Option(1)
val two = Option(2)
val n : Option[Int] = None

one |+| two
n |+| two
n |+| n
two |+| n
List(1,2,3) |+| List(4,5,6)
Map("foo" -> List(1, 2)) |+| Map("foo" -> List(3,4), "bar" -> List(42))