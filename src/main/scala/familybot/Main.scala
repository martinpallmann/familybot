package familybot

import java.time.Instant

import cats.data.Kleisli
import cats.effect.IO
import de.martinpallmann.gchat.{BotRequest, BotResponse}
import de.martinpallmann.gchat.bot.Bot
import de.martinpallmann.gchat.gen.{Message, Space, User}
import org.http4s.{Credentials, HttpRoutes, Request, Response}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.implicits._
import cats.implicits._
import de.martinpallmann.gchat.circe._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.headers.Authorization
import org.slf4j.LoggerFactory

object Main extends Bot {
  def onAddedToSpace(eventTime: Instant,
                     space: Space,
                     user: User): BotResponse = BotResponse.Text("hello")

  def onRemovedFromSpace(eventTime: Instant, space: Space, user: User): Unit = {}

  def onMessageReceived(eventTime: Instant,
                        space: Space,
                        message: Message,
                        user: User): BotResponse = BotResponse.Text("hello")

  final val service = {

    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case req @ POST -> Root =>
        for {
          evt <- req.as[BotRequest]
          resp <- {
            validate(req)
            Ok(BotResponse.Text("Hello").toMessage)
          }
        } yield (resp)
    }
  }

  private val logger = LoggerFactory.getLogger(getClass)

  def validate(req: Request[IO]): Unit = {
    def extractToken(c: Credentials) = c match {
      case Credentials.Token(_, token) => Some(token)
      case _                           => None
    }
    for {
      a <- req.headers.get(Authorization)
      t <- extractToken(a.credentials)
    } yield {
      Verifyer.verify(t)
      Jwt.verify(t)
    }
  }

  override def httpApp: Kleisli[IO, Request[IO], Response[IO]] =
    Router("/" -> service).orNotFound
}
