package com.onairentertainment.service

import cats.effect.IO
import cats.effect.std.Random

trait RandomService {
  def apply: IO[Int]
}

object RandomService {
  val minimumNumber: Int = 0
  val maximumNumber: Int = 999999

  def apply(randomInstance: Random[IO]): RandomService =
    new RandomService {
      override def apply: IO[Int] = randomInstance.betweenInt(minimumNumber, maximumNumber + 1)
    }
}
