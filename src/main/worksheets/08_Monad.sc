import cats._
import cats.implicits._

Option(Option(1)).flatten

List(List(1), List(1,2,3)).flatten

// monads
implicit def optionMonad(implicit app : Applicative[Option]) =
  new Monad[Option] {
    def flatMap[A, B](fa: Option[A])(f: (A) => Option[B]) = app.map(fa)(f).flatten

    def pure[A](x: A) = app.pure(x)
  }

implicit val listMonad = new Monad[List] {
  def flatMap[A, B](fa: List[A])(f: (A) => List[B]) = fa.flatMap(f)

  def pure[A](x: A) = List(x)
}

//ifM - lifts an if statement into the monadic context
Monad[Option].ifM(Option(true))(Option("truthy"), Option("falsy"))
Monad[List].ifM(List(true, false, true))(List(1,2), List(3,4))
