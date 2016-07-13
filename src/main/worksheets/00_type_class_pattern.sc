
trait Show[A] {
  def show(f : A): String
}

object Logger {
  def log[A](a : A)(implicit s : Show[A]) = println(s.show(a))
}

implicit val stringShow = new Show[String] {
  def show(s: String) = s
}

Logger.log("Hi there!")

// option show
implicit def optionShow[A](implicit sa : Show[A]) = new Show[Option[A]] {
  def show(oa : Option[A]) : String = oa match {
    case None => "None"
    case Some(a) => s"Some(${sa.show(a)})"
  }
}

Logger.log(Option("Logging an option!"))