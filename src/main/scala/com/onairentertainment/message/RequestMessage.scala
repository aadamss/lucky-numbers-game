package com.onairentertainment.message

import io.circe.Decoder
import io.circe.generic.extras._
import io.circe.generic.extras.auto._
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder

sealed trait RequestMessage

object RequestMessage {
  final case class Play(players: Int)             extends RequestMessage
  final case class Ping(id: Int, timestamp: Long) extends RequestMessage

  implicit val config: Configuration          = CirceConfiguration(typePrefix = "request")
  implicit val codec: Decoder[RequestMessage] = deriveConfiguredDecoder[RequestMessage]
}
