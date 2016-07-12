import cats._
import cats.std.all._

Applicative[Option].pure(1)

Applicative[List].pure(1)

// you may compose Applicative functors

(Applicative[List] compose Applicative[Option]).pure(1)
