import cats._

/**
  * What is a functor?
  *
  * Type class that involves types that have one 'hole' - i.e. types that have the shape F[?]
  * i.e. Option, List & Future
  *
  * Contains a single operation, named: map
  *
  * def map[A,B](fa : F[A])(f : A => B) : F[B]
  *
  * Functor is short for 'Covariant functor'
  *
  */

// normal scala map
val numbers = List(1,2,3)
numbers.map(_ + 1) // increment each in the list by 1


implicit val optionFunctor : Functor[Option] = new Functor[Option] {
  def map[A,B](fa : Option[A])(f : A => B) = fa map f
}

val optNumber = Option(1)

Functor[Option].map(optNumber)(_ + 2)

implicit val listFunctor : Functor[List] = new Functor[List] {
  def map[A,B](fa : List[A])(f : A => B) = fa map f
}

Functor[List].map(numbers)(_ + 1)

// NOTE - you may also use a functor with types that don't have a map, just use andThen to implement
//        Doing this requires the kind-projector compiler plugin, which I have chosen not to for simplicity's sake

// Functor map...

val len : String => Int = _.length

Functor[List].map(List("qwer", "asdfg"))(len)


// Functor may 'lift' a function from A => B to F[A] => F[B]
val lenOption : Option[String] => Option[Int] = Functor[Option].lift(len)

lenOption(Some("abcd"))

// fproduct - pairs a value with the result of applying function to that value
val source = List("a", "aa", "b", "ccccc")

Functor[List].fproduct(source)(len).toMap

// compose: given any functor F[_] and any functor G[_] create a new functor F[G[_]]
val listOpt = Functor[List] compose Functor[Option]

listOpt.map(List(Some(1), None, Some(3)))(_ + 1)

val optList = Functor[Option] compose Functor[List]

optList.map(Some(List(1,2,3)))(_ + 1)

val listOptList = listOpt compose Functor[List]

listOptList.map(List(Some(List(1,2)), None, Some(List(3,4))))(_ + 1)