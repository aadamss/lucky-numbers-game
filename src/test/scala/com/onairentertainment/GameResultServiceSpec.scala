package com.onairentertainment

import com.onairentertainment.domain.Player.{Bot, Human}
import com.onairentertainment.domain.{GameResultEntry, PlayerHand, PlayerResult}
import com.onairentertainment.service.GameResultService
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class GameResultServiceSpec extends AnyFreeSpec {
  "GameResultService.apply" - {
    val service = GameResultService()
    val player  = Human(1)

    "should calculate results correctly" in {
      val hands = List(
        PlayerHand(player, 112),
        PlayerHand(player, 33),
        PlayerHand(player, 22)
      )

      service(hands) shouldEqual List(
        GameResultEntry(position = 1, player = 1, number = 33, result = 30),
        GameResultEntry(position = 2, player = 1, number = 22, result = 20),
        GameResultEntry(position = 3, player = 1, number = 112, result = 12)
      )
    }

    "should exclude results below bot result" in {
      val hands = List(
        PlayerHand(player, 999),
        PlayerHand(Bot, 111),
        PlayerHand(player, 22),
        PlayerHand(Bot, 99)
      )

      service(hands) shouldEqual List(
        GameResultEntry(position = 1, player = 1, number = 999, result = 900)
      )
    }
  }
}
