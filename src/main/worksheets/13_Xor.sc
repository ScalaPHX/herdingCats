import cats.data.Xor

// Xor is 'right-biased' & contains flatMap & map
val right: String Xor Int = Xor.right(5)
right.map(_ + 1)

val left: String Xor Int = Xor.left("Something went wrong")
left.map(_ + 1)

val rightF: String Xor Int = Xor.right(5)
rightF.flatMap(x ⇒ Xor.right(x + 1))

val leftF: String Xor Int = Xor.left("Something went wrong")
leftF.flatMap(x ⇒ Xor.right(x + 1))

// Exception throwing code looks like:
object ExceptionStyle {
  def parse(s: String): Int =
    if (s.matches("-?[0-9]+")) s.toInt
    else throw new NumberFormatException(s"${s} is not a valid integer.")

  def reciprocal(i: Int): Double =
    if (i == 0) throw new IllegalArgumentException("Cannot take reciprocal of 0.")
    else 1.0 / i

  def stringify(d: Double): String = d.toString

}

// move the fact that we have potential failures into the type...
object XorStyle {
  def parse(s: String): Xor[NumberFormatException, Int] =
    if (s.matches("-?[0-9]+")) Xor.right(s.toInt)
    else Xor.left(new NumberFormatException(s"${s} is not a valid integer."))

  def reciprocal(i: Int): Xor[IllegalArgumentException, Double] =
    if (i == 0) Xor.left(new IllegalArgumentException("Cannot take reciprocal of 0."))
    else Xor.right(1.0 / i)

  def stringify(d: Double): String = d.toString

  def magic(s: String): Xor[Exception, String] =
    parse(s).flatMap(reciprocal).map(stringify)
}

XorStyle.magic("0")
XorStyle.magic("1")
XorStyle.magic("Not a number")

// Xor is sealed, so you need to match on all outcomes...
val result = XorStyle.magic("2") match {
  case Xor.Left(_: NumberFormatException) ⇒ "Not a number!"
  case Xor.Left(_: IllegalArgumentException) ⇒ "Can't take reciprocal of 0!"
  case Xor.Left(_) ⇒ "Unknown error"
  case Xor.Right(result) ⇒ s"Got reciprocal: $result"
}

// or even better...since we know what the potential errors are...

object XorStyleWithAdts {
  sealed abstract class Error
  final case class NotANumber(string: String) extends Error
  final case object NoZeroReciprocal extends Error

  def parse(s: String): Xor[Error, Int] =
    if (s.matches("-?[0-9]+")) Xor.right(s.toInt)
    else Xor.left(NotANumber(s))

  def reciprocal(i: Int): Xor[Error, Double] =
    if (i == 0) Xor.left(NoZeroReciprocal)
    else Xor.right(1.0 / i)

  def stringify(d: Double): String = d.toString

  def magic(s: String): Xor[Error, String] =
    parse(s).flatMap(reciprocal).map(stringify)
}

import XorStyleWithAdts._

val resultAdt = magic("2") match {
  case Xor.Left(NotANumber(_)) ⇒ "Not a number!"
  case Xor.Left(NoZeroReciprocal) ⇒ "Can't take reciprocal of 0!"
  case Xor.Right(result) ⇒ s"Got reciprocal: $result"
}

// other handy helpers...
Xor.catchOnly[NumberFormatException]("abc".toInt)

Xor.catchNonFatal(1 / 0)