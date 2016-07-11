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


// map
Apply[Option].map(Some(1))(intToString)

Apply[Option].map(Some(1))(double)

Apply[Option].map(None)(double)



// compose
val listOpt = Apply[List] compose Apply[Option]

val plusOne = (x : Int) => x + 1

listOpt.ap(List(Some(plusOne)))(List(Some(1), None, Some(3)))

