// Problem:

import cats.data.Xor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

def parseInt(s: String): Option[Int] = ???

trait SecurityError
trait Credentials

def validateLogin(cred: Credentials): Xor[SecurityError, Unit] = ???

trait Profile
trait User

def userInfo(user: User): Future[Profile] = ???

// Given User => Future[Profile], what if we want to fetch profiles for List[User]
// could do:
def uglyProfilesFor(users: List[User]): List[Future[Profile]] = users.map(userInfo)

// gives us ugly List[Future[Profile]] (which we need to use Future.traverse / Future.sequence to straighten out)
def fixUgly(ugly : List[Future[Profile]]) = Future.sequence(ugly)

// Traverse to the rescue...
import cats.Semigroup
import cats.data.{NonEmptyList, OneAnd, Validated, ValidatedNel, Xor}
import cats.implicits._

// NOTE - Xor may be ditched in favor of Either in Scala 2.12 which is 'right biased'
def parseIntXor(s: String): Xor[NumberFormatException, Int] =
  Xor.catchOnly[NumberFormatException](s.toInt)

def parseIntValidated(s: String): ValidatedNel[NumberFormatException, Int] =
  Validated.catchOnly[NumberFormatException](s.toInt).toValidatedNel

List("1", "2", "3").traverseU(parseIntXor)
List("1", "abc", "3").traverseU(parseIntXor)

implicit def nelSemigroup[A]: Semigroup[NonEmptyList[A]] = OneAnd.oneAndSemigroupK[List].algebra[A]


List("1", "2", "3").traverseU(parseIntValidated)

// So...to fix the above 'ugly' we can do...
def profilesFor(users : List[User]) : Future[List[Profile]] = users.traverse(userInfo)

// sequencing (outside of futures)
List(Option(1), Option(2), Option(3)).sequence
List(Option(1), None, Option(3)).sequence

// if you wind up with something like...
trait Data
def writeToStore(data: Data): Future[Unit] = ???

// and follow the pattern above
def writeManyToStore(data: List[Data]) = data.traverse(writeToStore)

// so use Foldable.traverse_
def writeManyToStoreF(data : List[Data]) = data.traverse_(writeToStore)














