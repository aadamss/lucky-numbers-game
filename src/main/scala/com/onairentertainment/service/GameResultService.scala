package com.onairentertainment.service

import com.onairentertainment.domain.Player.{Bot, Human}
import com.onairentertainment.domain._

import scala.annotation.tailrec
import scala.math.pow

trait GameResultService {
  def apply(playerHands: List[PlayerHand]): List[GameResultEntry]
}

object GameResultService {
  def apply(): GameResultService =
    (playerHands: List[PlayerHand]) =>
      evaluateGameResult(
        playerHands
          .map(evaluateHand)
          .sortBy(-_.result)
      )

  private def evaluateHand(playerHand: PlayerHand): PlayerResult = {
    @tailrec
    def numberToDigitCount(number: Int, acc: Map[Int, Int]): Map[Int, Int] =
      if (number == 0) acc
      else {
        val digit         = number % 10
        val newDigitCount = acc.getOrElse(digit, 0) + 1
        numberToDigitCount(number / 10, acc + (digit -> newDigitCount))
      }

    def digitCountToResult(digitCount: Map[Int, Int]): Int =
      digitCount.foldLeft(0) {
        case (sum, (digit, digitCount)) =>
          sum + pow(10, digitCount - 1).toInt * digit
      }

    val result = digitCountToResult(numberToDigitCount(playerHand.number, Map.empty))
    PlayerResult(playerHand.player, playerHand.number, result)
  }

  private def evaluateGameResult(playerResults: List[PlayerResult]): List[GameResultEntry] = {
    @tailrec
    def loop(
              playerResults: List[PlayerResult],
              currentPosition: Int,
              acc: Vector[GameResultEntry]
            ): Vector[GameResultEntry] =
      playerResults match {
        case result :: tail =>
          result.player match {
            case human: Human =>
              val gameResultEntry = GameResultEntry(currentPosition, human.number, result.number, result.result)
              loop(tail, currentPosition + 1, acc :+ gameResultEntry)

            case Bot          => acc
          }
        case Nil            => acc
      }

    loop(playerResults, 1, Vector.empty).toList
  }
}
