import java.util.Date

import cats._
import cats.implicits._

def longToDate : Long => Date = new Date(_)
def dateToLong : Date => Long = _.getTime

implicit val semigroupDate : Semigroup[Date] = Semigroup[Long].imap(longToDate)(dateToLong)

val today : Date = longToDate(1449088684104l)
val timeLeft : Date = longToDate(1900918893l)

today |+| timeLeft
