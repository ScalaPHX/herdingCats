import org.scalaphx.herdingCats.{Show, Logger}

//Logger.log("Hi there!")

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
