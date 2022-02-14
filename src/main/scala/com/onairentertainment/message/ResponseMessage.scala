package com.onairentertainment.message

import com.onairentertainment.domain.GameResultEntry
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.circe.generic.extras.auto._

sealed trait ResponseMessage

object ResponseMessage {
  final case class Results(results: List[GameResultEntry]) extends ResponseMessage
  final case class Pong(
                         requestId: Int,
                         requestAt: Long,
                         timestamp: Long
                       )                                                        extends ResponseMessage
  final case class Error(error: String)                    extends ResponseMessage

  implicit val config: Configuration           = CirceConfiguration(typePrefix = "response")
  implicit val codec: Encoder[ResponseMessage] = deriveConfiguredEncoder[ResponseMessage]
}
