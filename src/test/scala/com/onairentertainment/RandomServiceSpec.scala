package com.onairentertainment

import cats.effect.IO
import cats.effect.std.Random
import cats.effect.unsafe.implicits.global
import com.onairentertainment.service.RandomService
import org.scalamock.scalatest.MockFactory
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class RandomServiceSpec extends AnyFreeSpec with MockFactory {
  "RandomService.apply" - {
    "should return a correct result using the given random instance" in {
      val random = mock[Random[IO]]

      val service = RandomService(random: Random[IO])

      (random.betweenInt _)
        .expects(RandomService.minimumNumber, RandomService.maximumNumber + 1)
        .returning(IO.pure(42))

      service.apply.unsafeRunSync() shouldEqual 42
    }
  }
}
