import cats._
import cats.implicits._

Foldable[List].fold(List("a", "b", "c"))

Foldable[List].foldMap(List(1,2,4))(_.toString)

Foldable[List].foldK(List(List(1,2,3), List(2,3,4)))

Foldable[List].reduceLeftToOption(List[Int]())(_.toString)((s, i) => s + 1)

Foldable[List].reduceLeftToOption(List(1,2,3,4))(_.toString)((s,i) => s + i)

Foldable[List].reduceRightToOption(List(1,2,3,4))(_.toString)((i,s) => Later(s.value + i)).value

Foldable[List].reduceRightToOption(List[Int]())(_.toString)((i,s) => Later(s.value + i)).value

Foldable[Set].find(Set(1,2,3))(_ > 2)

Foldable[Set].find(Set(1,2,3))(_ > 0)

Foldable[Set].exists(Set(1,2,3))(_ > 2)

Foldable[Set].forall(Set(1,2,3))(_ < 4)

Foldable[Vector].filter_(Vector(1,2,3))(_ < 3)

Foldable[List].isEmpty(List(1,2))

Foldable[Option].isEmpty(None)

Foldable[List].nonEmpty(List(1,2))

Foldable[Option].toList(None)

Foldable[List].toList(List(1,2,3))

def parseInt(s : String) : Option[Int] = scala.util.Try(Integer.parseInt(s)).toOption

// traverses the List for 'effect' and not result - gives us a ()
Foldable[List].traverse_(List("1", "2"))(parseInt)

Foldable[List].traverse_(List("1", "A"))(parseInt)

Foldable[List].sequence_(List(Option(1), Option(2)))

Foldable[List].sequence_(List(Option(1), None))

val prints : Eval[Unit] = List(Eval.always(println(1)), Eval.always(println(2))).sequence_

prints.value

Foldable[List].dropWhile_(List[Int](2,4,5,6,7))(_ % 2 == 0)

Foldable[List].dropWhile_(List[Int](1,2,4,5,6,7))(_ % 2 == 0)

val FoldableListOption = Foldable[List].compose[Option]

FoldableListOption.fold(List(Option(1), Option(2), Option(3), Option (4)))

FoldableListOption.fold(List(Option(1), Option(2), Option(3), None))

FoldableListOption.fold(List(Option("1"), Option("2"), Option("3"), None))





