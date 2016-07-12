package org.scalaphx.herdingCats

import cats._
import cats.implicits._

/**
  * Created with IntelliJ IDEA.
  * User: terry
  * Date: 7/12/16
  * Time: 1:20 PM
  *
  */
object MonoidExample extends App {
  val m = Monoid[Int].empty

  println(m)

}
