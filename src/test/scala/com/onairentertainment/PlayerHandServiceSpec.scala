package com.onairentertainment

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.onairentertainment.domain.Player.{Bot, Human}
import com.onairentertainment.domain.PlayerHand
import com.onairentertainment.service.{PlayerHandService, RandomService}
import org.scalamock.scalatest.MockFactory
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class PlayerHandServiceSpec extends AnyFreeSpec with MockFactory {
  "PlayerHandService.apply" - {
    "should generate player hands correctly" in {
      val randomService = mock[RandomService]
      val service       = PlayerHandService(randomService)

      val numberOfPlayers      = 5
      val totalNumberOfPlayers = numberOfPlayers + 1
      (randomService.apply _)
        .expects()
        .returning(IO.pure(1337))
        .repeated(totalNumberOfPlayers)

      service(numberOfPlayers).unsafeRunSync() shouldEqual List(
        PlayerHand(Bot, 1337),
        PlayerHand(Human(1), 1337),
        PlayerHand(Human(2), 1337),
        PlayerHand(Human(3), 1337),
        PlayerHand(Human(4), 1337),
        PlayerHand(Human(5), 1337)
      )
    }
  }
}
