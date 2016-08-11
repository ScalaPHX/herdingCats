
import cats.Apply
import cats.data.{ValidatedNel, Xor}
import cats.std.list._

// Parallel validation for scala
// report any and all errors across independent bits of data

// config parsing...

trait Read[A] {
  def read(s : String) : Option[A]
}

object Read {
  def apply[A](implicit A : Read[A]) : Read[A] = A

  implicit val stringRead : Read[String] = new Read[String] {
    def read(s : String) : Option[String] = Some(s)
  }

  implicit val intRead : Read[Int] = new Read[Int] {
    def read(s: String) : Option[Int] =
      if (s.matches("-?[0-9]+"))
        Some(s.toInt)
      else
        None
  }
}

// errors
sealed abstract class ConfigError
final case class MissingConfig(field : String) extends ConfigError
final case class ParseError(field : String) extends ConfigError

// parser...

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}

case class Config(map : Map[String, String]) {
  def parse[A : Read](key : String) : Validated[ConfigError, A] =
    map.get(key) match {
      case None => Invalid(MissingConfig(key))
      case Some(value) =>
        Read[A].read(value) match {
          case None => Invalid(ParseError(key))
          case Some(a) => Valid(a)
        }
    }
}

def parallelValidate1[E, A, B, C](v1 : Validated[E, A], v2 : Validated[E, B])(f : (A, B) => C) : Validated[E, C] =
  (v1, v2) match {
    case (Valid(a), Valid(b)) => Valid(f(a,b))
    case (Valid(_), i@Invalid(_)) => i
    case (i@Invalid(_), Valid(_)) => i
    case (Invalid(e1), Invalid(e2)) => ???
  }

// how to handle last case and combine both invalid cases?  Semigroup...

import cats.Semigroup

def parallelValidate[E : Semigroup, A, B, C](v1: Validated[E, A], v2: Validated[E, B])(f: (A, B) => C): Validated[E, C] =
  (v1, v2) match {
    case (Valid(a), Valid(b))       => Valid(f(a, b))
    case (Valid(_), i@Invalid(_))   => i
    case (i@Invalid(_), Valid(_))   => i
    case (Invalid(e1), Invalid(e2)) => Invalid(Semigroup[E].combine(e1, e2))
  }


case class ConnectionParams(url : String, port : Int)

val config = Config(Map("endpoint" -> "127.0.0.1", "port" -> "not an int"))

// semigroup knows how to combine due to ValidatedNel (non-empty-list) - sample implementation below
//  - you get this via import cats.std.list._
//implicit val nelSemigroup : Semigroup[NonEmptyList[ConfigError]] = SemigroupK[NonEmptyList].algebra[ConfigError]

implicit val readString : Read[String] = Read.stringRead
implicit val readInt : Read[Int] = Read.intRead

val v1 = parallelValidate(config.parse[String]("url").toValidatedNel, config.parse[Int]("port").toValidatedNel)(ConnectionParams.apply)

// above looks suspiciously familiar - maybe liek Apply.map2 ?  So can we use Apply?

// expand the config example   -  requires the Kind Projector (see build.sbt) or will not compile!
val config2 = Config(Map(("name", "cat"), ("age", "not a number"), ("houseNumber", "1234"), ("lane", "feline street")))

case class Address(houseNumber: Int, street: String)
case class Person(name: String, age: Int, address: Address)

val personFromConfig : ValidatedNel[ConfigError, Person] =
      Apply[ValidatedNel[ConfigError, ?]].map4(
        config2.parse[String]("name").toValidatedNel,
        config2.parse[Int]("age").toValidatedNel,
        config2.parse[Int]("house_number").toValidatedNel,
        config2.parse[String]("street").toValidatedNel) {
        case (name, age, houseNumber, street) => Person(name, age, Address(houseNumber, street))
      }

// Validated is not a Monad; it is only Applicative!  (error accumulation breaks due to how ap would have to be modified and thus be inconsistent with map)
// if you want error accumulation, use Validated; if you need short-circuiting monadic behavior, use Xor

// Sequential Validation...

config2.parse[Int]("house_number").andThen {
  n =>
    if (n >= 0)
      Validated.valid(n)
    else
      Validated.invalid(ParseError("house_number"))
}

// withXor
def positive(field : String, i : Int) : ConfigError Xor Int = {
  if (i >=0) Xor.right(i)
  else Xor.left(ParseError(field))
}

config.parse[Int]("house_number").withXor {
  xor => xor.flatMap {
    i => positive("house_number", i)
  }
}