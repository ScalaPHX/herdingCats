import java.util.Date

import cats._
import cats.implicits._


/**
  * Invariant is a type class for functors that defines the imap function
  *
  * def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
  *
  * Semigroup and Monoid are Invariant
  *
  */

// since Semigroup does not have a instance for the standard covariant Functor, we can't use map here...
def longToDate : Long => Date = new Date(_)
// and since Semigroup doesn't have a covariant Functor, we can't use contramap...
def dateToLong : Date => Long = _.getTime
// imap to the rescue...
implicit val semigroupDate : Semigroup[Date] = Semigroup[Long].imap(longToDate)(dateToLong)

val today : Date = longToDate(1449088684104l)
val timeLeft : Date = longToDate(1900918893l)

today |+| timeLeft