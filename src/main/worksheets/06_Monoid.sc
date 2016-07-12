import cats._
import cats.implicits._

Monoid[String].empty

Monoid[String].combineAll(List("a", "b", "c"))

Monoid[String].combineAll(List())

// more complex types...
Monoid[Map[String, Int]].combineAll(List(Map("a" -> 1, "b" -> 2), Map("a" -> 3)))

Monoid[Map[String, Int]].combineAll(List())

// uses Foldable's foldMap
val l = List(1,2,3,4,5)
l.foldMap(identity)

l.foldMap(i => i.toString)

// produce a tuple that will be valid for any tuple where the types it contains also have a Monoid available...:
l.foldMap(i => (i, i.toString))






