package com.onairentertainment.domain

sealed trait Player

object Player {
  final case class Human(number: Int) extends Player

  case object Bot extends Player
}
