import cats._
import cats.implicits._

/**
  * Foldable type class instances may be defined for data structures that can be folded into a summary value.
  *
  * provides two basic methods: foldLeft/foldRight
  *
  * both form the basis for many other operations as demonstrated below...
  */

// foldLeft
Foldable[List].foldLeft(List(1, 2, 3), 0)(_ + _)
Foldable[List].foldLeft(List("a", "b", "c"), "")(_ + _)

// foldRight - lazy right-associative fold
val lazyResult = Foldable[List].foldRight(List(1, 2, 3), Now(0))((x, rest) ⇒ Later(x + rest.value))
lazyResult.value

// fold - aka combineAll - combine every value using the given Monoid
Foldable[List].fold(List("a", "b", "c"))

// foldMap - similar to fold, but maps every A into B and combines them using Monoid[B]
Foldable[List].foldMap(List(1,2,4))(_.toString)
// foldK - similar to fold, but uses a MonoidK[G] instead of Monoid[G]
// In this sample, we're trying to combine the values within the List of Lists to a single List[Int]
Foldable[List].foldK(List(List(1,2,3), List(2,3,4)))

// helpers to reduce (left & right - right being lazy) and return value in an Option
Foldable[List].reduceLeftToOption(List[Int]())(_.toString)((s, i) => s + 1)
Foldable[List].reduceLeftToOption(List(1,2,3,4))(_.toString)((s,i) => s + i)
Foldable[List].reduceRightToOption(List(1,2,3,4))(_.toString)((i,s) => Later(s.value + i)).value
Foldable[List].reduceRightToOption(List[Int]())(_.toString)((i,s) => Later(s.value + i)).value

// find - searches for the fist element matching the predicate - if it exists...
Foldable[Set].find(Set(1,2,3))(_ > 2)
Foldable[Set].find(Set(1,2,3))(_ > 0)

// exists - checks whether at least one element satifies the predicate
Foldable[Set].exists(Set(1,2,3))(_ > 2)

// forall - checks whether all elements satisfy the predicate
Foldable[Set].forall(Set(1,2,3))(_ < 4)

// filter_ convert F[A] to List[A] - but only include those that match the predicate
Foldable[Vector].filter_(Vector(1,2,3))(_ < 3)
// isEmpty - returns whether or no the F[A] is empty or not
Foldable[List].isEmpty(List(1,2))
Foldable[Option].isEmpty(None)
// nonEmpty - opposie to isEmpty
Foldable[List].nonEmpty(List(1,2))
// toList - convert F[A] to List[A]
Foldable[Option].toList(None)
Foldable[List].toList(List(1,2,3))

// traverse_ example...
def parseInt(s : String) : Option[Int] = scala.util.Try(Integer.parseInt(s)).toOption

// traverses the List for 'effect' and not result - gives us a ()
Foldable[List].traverse_(List("1", "2"))(parseInt)
Foldable[List].traverse_(List("1", "A"))(parseInt)
// similar to traverse_, doesn't accept a function to apply to parameters
Foldable[List].sequence_(List(Option(1), Option(2)))
Foldable[List].sequence_(List(Option(1), None))

// dropWhile example - drops values that don't match the predicate
Foldable[List].dropWhile_(List[Int](2,4,5,6,7))(_ % 2 == 0)
Foldable[List].dropWhile_(List[Int](1,2,4,5,6,7))(_ % 2 == 0)

// compose examples
val FoldableListOption = Foldable[List].compose[Option]

FoldableListOption.fold(List(Option(1), Option(2), Option(3), Option (4)))
FoldableListOption.fold(List(Option(1), Option(2), Option(3), None))
FoldableListOption.fold(List(Option("1"), Option("2"), Option("3"), None))