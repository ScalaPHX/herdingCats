package org.scalaphx.herdingCats

/**
  * Basic type class pattern
  *
  * @see http://typelevel.org/cats/typeclasses.html
  *
  * User: terry
  * Date: 7/11/16
  * Time: 12:06 PM
  *
  */


/**
  * Trait that converts classes of type A into Strings
  * @tparam A - type to convert
  */
 trait Show[A] {
  def show(f : A): String
}

object Logger {
  def log[A](a : A)(implicit s : Show[A]) = println(s.show(a))
}