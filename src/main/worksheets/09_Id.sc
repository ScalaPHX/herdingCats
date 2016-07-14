import cats._

/**
  * Id (identity) - is an ambient monad that encodes the effect of having no effect
  *
  * Pure plain values are values of Id
  *
  * defined as: type Id[A] = A
  *
  * Id is effectively a building block used in other things...
  *
  */
// same thing...
val x: Id[Int] = 1
val y : Int = x

val one : Int = 1
Functor[Id].map(one)(_ + 1)

Applicative[Id].pure(42)

// for Id, map and flatMap are effecitvely the same
Monad[Id].map(one)(_ + 1)
Monad[Id].flatMap(one)(_ + 1)