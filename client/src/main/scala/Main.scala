import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:

  def run: IO[Unit] =
    cats.effect.std.Console[IO].println("Hello world")
