package com.onairentertainment

import cats.data.EitherT
import cats.effect.std.Random
import cats.effect.{ExitCode, IO, IOApp}
import com.onairentertainment.service.{GameResultService, PlayerHandService, RandomService}
import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

object GameBootstrap extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    (for {
      config           <- EitherT.fromEither[IO](ConfigSource.default.load[AppConfig])
      _                <- EitherT.right[ConfigReaderFailures](IO.println(s"Loaded config: $config"))
      random           <- EitherT.right[ConfigReaderFailures](Random.scalaUtilRandom[IO])
      randomService     = RandomService(random)
      playerHandService = PlayerHandService(randomService)
      exitCode         <- EitherT.right[ConfigReaderFailures] {
        GameServer(playerHandService, GameResultService()).serve(config)
      }
    } yield exitCode).getOrElse(ExitCode.Error)
}
