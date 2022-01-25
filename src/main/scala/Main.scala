import cats.effect.{IOApp, IO}
import cats.effect.std.Console

object Main extends IOApp.Simple:

  def run: IO[Unit] =
    Console[IO].println("Hello world")
