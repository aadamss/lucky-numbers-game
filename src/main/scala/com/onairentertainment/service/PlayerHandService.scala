package com.onairentertainment.service

import cats.effect.IO
import com.onairentertainment.domain.Player._
import com.onairentertainment.domain.PlayerHand
import cats.implicits.toTraverseOps

trait PlayerHandService {
  def apply(numberOfPlayers: Int): IO[List[PlayerHand]]
}

object PlayerHandService {
  def apply(randomService: RandomService): PlayerHandService =
    (numberOfPlayers: Int) => {
      val players              = Bot :: List.tabulate(numberOfPlayers)(index => Human(index + 1))
      val totalNumberOfPlayers = numberOfPlayers + 1

      for {
        randomNumbers <- List.fill(totalNumberOfPlayers)(randomService.apply).sequence
      } yield players
        .zip { randomNumbers }
        .map { case (player, randomNumber) => PlayerHand(player, randomNumber) }
    }
}
