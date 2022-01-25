import cats.effect.{IO, IOApp}
import cats.effect.std.Dispatcher
import cats.syntax.all.*
import fs2.Stream
import org.scalajs.dom
import org.scalajs.dom.Node
import slinky.core.facade.ReactElement
import slinky.web
import cats.effect.kernel.Async
import slinky.web.html.*
import slinky.core.FunctionalComponent
import slinky.core.facade.Hooks.{useEffect, useState}

object Main extends IOApp.Simple:

  def render[F[_]: Async](reactElement: ReactElement) =
    Async[F]
      .delay(dom.document.getElementById("app"))
      .flatMap( parent =>
        Async[F].delay {
          val container = dom.document.createElement("container")
          parent.appendChild(container)
          web.ReactDOM.render(reactElement, container)
        }
      )


  def createAppDiv[F[_]: Async]: F[Node] =
    Async[F].delay {
      val appDiv = dom.document.createElement("div")
      appDiv.id = "app"
      dom.document.body.appendChild(appDiv)
    }

  object HelloWorld:

    case class Props(name: String)

    def apply(name: String): ReactElement = component(Props(name))

    private val component: FunctionalComponent[Props] = FunctionalComponent[Props] { props =>
      div(
        p(s"Hello ${props.name}")
      )
    }

  def run: IO[Unit] =
    //cats.effect.std.Console[IO].println("Hello world")
    createAppDiv[IO] >> render[IO](HelloWorld("Peter")).void

