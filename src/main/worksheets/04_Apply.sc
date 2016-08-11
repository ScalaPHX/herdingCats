/**
  * Apply extends Functor and adds a new function - ap
  *
  * The ap function is similar to map - we are still transforming a value in a context.
  * (A context may be an Option, List or Future)
  * The difference is that for ap, the function that does the transformation is of type F[A => B] whereas for map it is A => B
  */


import cats._

val intToString : Int => String = _.toString
val double : Int => Int = _ * 2
val addTwo : Int => Int = _ + 2

implicit val optionApply : Apply[Option] = new Apply[Option] {
  def ap[A, B](f: Option[(A) => B])(fa: Option[A]) : Option[B] = fa.flatMap(a => f.map(ff => ff(a)))

  def map[A, B](fa: Option[A])(f: (A) => B) : Option[B] = fa map f
}

implicit val listApply : Apply[List] = new Apply[List] {
  def ap[A, B](f: List[(A) => B])(fa: List[A]) : List[B] = fa.flatMap(a => f.map(ff => ff(a)))

  def map[A, B](fa: List[A])(f: (A) => B) : List[B] = fa map f
}

// ap
Apply[Option].ap(Some(intToString))(Some(1))

Apply[Option].ap(Some(double))(Some(1))

Apply[Option].ap(Some(double))(None)

Apply[Option].ap(None)(None)

// ap arity (i.e. ap2, ap3, etc. 2 - 22)
val addArity2 = (a : Int, b : Int) => a + b
Apply[Option].ap2(Some(addArity2))(Some(1), Some(2))

val addArity3 = (a : Int, b : Int, c: Int) => a + b + c
Apply[Option].ap3(Some(addArity3))(Some(1), Some(2), Some(3))

Apply[Option].ap2(Some(addArity2))(Some(1), None)


// since we are a Functor, we get map as well...
Apply[Option].map(Some(1))(intToString)

Apply[Option].map(Some(1))(double)

Apply[Option].map(None)(double)

// map arity
Apply[Option].map2(Some(1), Some(2))(addArity2)

Apply[Option].map3(Some(1), Some(2), Some(3))(addArity3)

// tuple arity
Apply[Option].tuple2(Some(1), Some(2))

Apply[Option].tuple3(Some(1), Some(2), Some(3))

// compose
val listOpt = Apply[List] compose Apply[Option]

val plusOne = (x : Int) => x + 1

listOpt.ap(List(Some(plusOne)))(List(Some(1), None, Some(3)))

// apply builder syntax
import cats.syntax.cartesian._

def f1(a : Option[Int], b : Option[Int], c : Option[Int]) = ( a |@| b |@| c) map { _ * _ * _}

def f2(a : Option[Int], b : Option[Int], c : Option[Int]) = Apply[Option].map3(a,b,c)(_ * _ * _)

f1(Some(1), Some(2), Some(3))

f2(Some(1), Some(2), Some(3))

// builder instances have map, ap & tupled methods
val option2 = Option(1) |@| Option(2)

val option3 = option2 |@| Option.empty[Int]

option2 map addArity2

option3 map addArity3

option2 apWith Some(addArity2)

option3 apWith Some(addArity3)

option2.tupled

option3.tupled