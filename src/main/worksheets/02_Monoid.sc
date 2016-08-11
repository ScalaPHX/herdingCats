import cats._
import cats.implicits._

/**
  * Monoid extends the Semigroup - adding an empty method
  *
  * The empty method must return a value that when combined with any other instance of that type
  * it returns the other instance
  *
  * i.e. - if Monoid[String] is defined with a combine that does string concatenation, then empty = ""
  *
  * Allows us to avoid use of Option[T] for cases where T is not defined.
  *
  */

Monoid[String].empty

Monoid[String].combineAll(List("a", "b", "c"))

Monoid[String].combineAll(List())

// more complex types...
Monoid[Map[String, Int]].combineAll(List(Map("a" -> 1, "b" -> 2), Map("a" -> 3)))

Monoid[Map[String, Int]].combineAll(List())

// uses Foldable's foldMap
val l = List(1,2,3,4,5)
l.foldMap(identity)

l.foldMap(i => i.toString)

// produce a tuple that will be valid for any tuple where the types it contains also have a Monoid available...:
l.foldMap(i => (i, i.toString))

/**
  * MonoidK
  *
  * A monoid that operates on kinds
  *
  * A MonoidK[F] can produce Monoid[F[A]] for any type A
  *
  * How to distinguish between the two...
  *   - Monoid[A] - allows A values to be combined and means there's an empty A value that is an identity
  *   - MonoidK[F] - allows two F[A] values to be combined for any A.  Also means that for any A there is an empty F[A] value.  The combine function and empty value depend on the structure of F but not on the structure of A
  *
  *   @see the Foldable.foldK sample ...
  */







