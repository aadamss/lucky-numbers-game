package com.onairentertainment

import cats.effect.{ExitCode, IO}
import com.onairentertainment.service._
import fs2.Pipe
import cats.effect.std.Queue
import cats.implicits.catsSyntaxOptionId
import com.onairentertainment.message._
import com.onairentertainment.message.RequestMessage._
import com.onairentertainment.message.ResponseMessage._
import io.circe.syntax._
import io.circe.parser._
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.websocket.WebSocketFrame

class GameServer(
                  playerHandService: PlayerHandService,
                  gameResultService: GameResultService
                ) {
  def serve(config: AppConfig): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(config.port, config.host)
      .withHttpWebSocketApp { webSocketBuilder => routes(webSocketBuilder).orNotFound }
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

  private def routes(webSocketBuilder: WebSocketBuilder2[IO]): HttpRoutes[IO] = {
    def send(responseQueue: Queue[IO, Option[ResponseMessage]]): fs2.Stream[IO, WebSocketFrame] =
      fs2.Stream.fromQueueNoneTerminated(responseQueue).map { response =>
        WebSocketFrame.Text(response.asJson.toString)
      }

    def receive(responseQueue: Queue[IO, Option[ResponseMessage]]): Pipe[IO, WebSocketFrame, Unit] =
      _.collect { case text: WebSocketFrame.Text => text }
        .evalMap {
          case WebSocketFrame.Text(message, _) =>
            decode[RequestMessage](message)
              .map {
                case Play(numberOfPlayers) =>
                  for {
                    playerHands <- playerHandService(numberOfPlayers)
                    response     = Results(gameResultService(playerHands))
                    _           <- responseQueue.offer(response.some)
                  } yield ()

                case Ping(id, timestamp)   =>
                  for {
                    now     <- IO(System.currentTimeMillis())
                    response = Pong(id, timestamp, now)
                    _       <- responseQueue.offer(response.some)
                  } yield ()
              }
              .getOrElse(responseQueue.offer(Error("Unknown message").some))
        }

    HttpRoutes.of[IO] {
      case GET -> Root / "game" =>
        for {
          responseQueue <- Queue.unbounded[IO, Option[ResponseMessage]]
          response      <- webSocketBuilder.build(send(responseQueue), receive(responseQueue))
        } yield response
    }
  }
}

object GameServer {
  def apply(playerHandService: PlayerHandService, gameResultService: GameResultService): GameServer =
    new GameServer(playerHandService, gameResultService)
}
