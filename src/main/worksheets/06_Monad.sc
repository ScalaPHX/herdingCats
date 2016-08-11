import cats._
import cats.implicits._

/**
  * Monad extends Applicative and adds the function flatten
  *
  * Flatten takes a value in a nested context (F[F[A]]) and boils it down to a single context (F[A])
  *
  *
  */

// Flatten exists in a lot of the Scala SDK...
Option(Option(1)).flatten

List(List(1), List(1,2,3)).flatten

// example implementation
implicit def optionMonad(implicit app : Applicative[Option]) =
  new Monad[Option] {
    def flatMap[A, B](fa: Option[A])(f: (A) => Option[B]) = app.map(fa)(f).flatten

    def pure[A](x: A) = app.pure(x)
  }

implicit val listMonad = new Monad[List] {
  def flatMap[A, B](fa: List[A])(f: (A) => List[B]) = fa.flatMap(f)

  def pure[A](x: A) = List(x)
}

Monad[List].flatMap(List(1, 2, 3))(x ⇒ List(x, x))

//ifM - lifts an if statement into the monadic context
//    - provides the ablity to choose later operations in sequence based on the results of earlier ones
Monad[Option].ifM(Option(true))(Option("truthy"), Option("falsy"))
Monad[List].ifM(List(true, false, false))(List(1,2), List(3,4))
