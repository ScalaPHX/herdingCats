package org.scalaphx.herdingCats

import cats._

import cats.std.AllInstances
import cats.syntax.AllSyntax

/**
  * Created with IntelliJ IDEA.
  * User: terry
  * Date: 7/12/16
  * Time: 12:06 PM
  *
  */
object SemigroupExample extends App with AllInstances with AllSyntax{
  println("basics...")
  println(Semigroup[Int].combine(1,2))   // 3
  println(Semigroup[Option[Int]].combine(Option(1), Option(2))) // Some(3)
  println(Semigroup[Option[Int]].combine(Option(1), None))      // ???
  println(Semigroup[Int => Int].combine({(x : Int) => x + 1}, {(x : Int) => x * 10}).apply(6)) // ???

  // combine vs ++
  println("combine vs ++")
  println(Map("foo" -> Map("bar" -> 5)) ++  Map("foo" -> Map("bar" -> 6), "baz" -> Map()))
  println("vs combine...")
  println(Map("foo" -> Map("bar" -> 5)).combine(Map("foo" -> Map("bar" -> 6), "baz" -> Map())))
  // second example...
  println(Map("foo" -> List(1, 2)) ++ Map("foo" -> List(3,4), "bar" -> List(42)))
  println("vs combine...")
  println(Map("foo" -> List(1, 2)).combine(Map("foo" -> List(3,4), "bar" -> List(42))))

  // inline syntax
  val one = Option(1)
  val two = Option(2)
  val n : Option[Int] = None
  println("inline syntax...")
  println(one |+| two)
  println(n |+| two)
  println(n |+| n)
  println(two |+| n)
  println(List(1,2,3) |+| List(4,5,6))
  println(Map("foo" -> List(1, 2)) |+| Map("foo" -> List(3,4), "bar" -> List(42)))

}
