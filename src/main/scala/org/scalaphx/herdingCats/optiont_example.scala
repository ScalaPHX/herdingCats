/**
  * Created with IntelliJ IDEA.
  * User: terry
  * Date: 7/12/16
  * Time: 3:07 PM
  *
  */
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object OptionExample extends App {
  val customGreeting: Future[Option[String]] = Future.successful(Some("welcome back, Lola"))

  val excitedGreeting: Future[Option[String]] = customGreeting.map(_.map(_ + "!"))

  val hasWelcome: Future[Option[String]] = customGreeting.map(_.filter(_.contains("welcome")))

  val noWelcome: Future[Option[String]] = customGreeting.map(_.filterNot(_.contains("welcome")))

  val withFallback: Future[String] = customGreeting.map(_.getOrElse("hello, there!"))

  customGreeting.map {
    results =>
      println(s"customGreeting => $results")
  }

}



