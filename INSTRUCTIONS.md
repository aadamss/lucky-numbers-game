# Implementation notes

## Libraries used

- `cats-effect` as the main runtime
- `http4s` for server implementation and WebSockets
- `fs2` working with and processing WebSocket streams
- `circe` working with serialization/deserialization
- `pureconfig` to parse the application config
- `scalatest` / `scalamock` for testing

## Configuration

You may configure the host and the port to which the server will bind to. In order to do that, set your own values
in `main/resources/application.conf`.

## Running the application

Execute `stb run`. After that, you are interested in the following endpoint `/game`, which will establish the WebSocket
connection.

## Playing the game

In order to play the game `Postman` was used to create `WebSocket Requests`.
This feature can be found under `Postman -> Create New -> WebSocket Request`.

Send the following message to `localhost:8080/game`:

```json
{
  "message_type": "request.play",
  "players": 5
}
```

And here's an example response:

```json
{
  "results": [
    {
      "position": 1,
      "player": 5,
      "number": 164661,
      "result": 614
    },
    {
      "position": 2,
      "player": 4,
      "number": 694762,
      "result": 82
    },
    {
      "position": 3,
      "player": 2,
      "number": 942166,
      "result": 76
    },
    {
      "position": 4,
      "player": 1,
      "number": 135756,
      "result": 67
    }
  ],
  "message_type": "response.results"
}
```

## Ping-pong checks

Milliseconds since epoch is used as a timestamp format.

To send a ping, use the following message format and send it to `localhost:8080/game`:

```json
{
  "message_type": "request.ping",
  "id": 5,
  "timestamp": 1234567890123
}
```

The response format will look like this:

```json
{
  "requestId": 5,
  "requestAt": 1234567890123,
  "timestamp": 1644685999376,
  "message_type": "response.pong"
}
```

## Testing

Services that hold business the logic have been covered with tests. In order to run them,
execute the `sbt test` command.

## Improvements for the future

- Add tests for the WebSocket endpoint
- Add more type safety by using value classes
- Use Tagless Final approach
