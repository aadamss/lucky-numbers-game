package com.onairentertainment.message

import io.circe.generic.extras.Configuration

object CirceConfiguration {
  def apply(discriminator: String = "message_type", typePrefix: String): Configuration =
    Configuration.default
      .withDiscriminator(discriminator)
      .copy(transformConstructorNames = name => s"$typePrefix.${name.toLowerCase}")
}
