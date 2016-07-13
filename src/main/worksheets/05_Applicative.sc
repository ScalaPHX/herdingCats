import cats._
import cats.std.all._

/**
  * Applicative extends Apply by adding a single method - pure
  *
  * Pure takes a value and returns the value in the context of the functor
  *
  */

Applicative[Option].pure(1)

Applicative[List].pure(1)

// you may compose Applicative functors

(Applicative[List] compose Applicative[Option]).pure(1)
