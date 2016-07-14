import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * OptionT is a monad transformer for Option
  */

val customGreeting : Future[Option[String]] = Future.successful(Some("welcome back!"))

// we want to try out all of our greetings...so much boilerplate!
val excitedGreeting: Future[Option[String]] = customGreeting.map(_.map(_ + "!"))

val hasWelcome: Future[Option[String]] = customGreeting.map(_.filter(_.contains("welcome")))

val noWelcome: Future[Option[String]] = customGreeting.map(_.filterNot(_.contains("welcome")))

val withFallback: Future[String] = customGreeting.map(_.getOrElse("hello, there!"))

// OptionT to the rescue...
import cats.data.OptionT
import cats.std.future._

val customGreetingT: OptionT[Future, String] = OptionT(customGreeting)

val excitedGreetingT: OptionT[Future, String] = customGreetingT.map(_ + "!")

val withWelcome: OptionT[Future, String] = customGreetingT.filter(_.contains("welcome"))

val noWelcomeT: OptionT[Future, String] = customGreetingT.filterNot(_.contains("welcome"))

val withFallbackT: Future[String] = customGreetingT.getOrElse("hello, there!")

// grab the value of the OptionT
excitedGreeting.value

// this won't work...:
val defaultGreeting : Future[String] = Future.successful("hello, there")

//val greeting : Future[String] = customGreeting.flatMap(custom => custom.map(Future.successful).getOrElse(defaultGreeting))

// this will:
val greeting : Future[String] = customGreetingT.getOrElseF(defaultGreeting)