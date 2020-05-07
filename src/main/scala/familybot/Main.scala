package familybot

import java.time.Instant

import cats.effect.{ExitCode, IO}
import de.martinpallmann.gchat.BotResponse
import de.martinpallmann.gchat.BotResponse._
import de.martinpallmann.gchat.Bot
import de.martinpallmann.gchat.gen._

object Main extends Bot {

  implicit def anyToList[A](a: A): List[A] =
    Option(a).toList

  implicit def anyToOptList[A](a: A): Option[List[A]] =
    Option(a).map(List.apply(_))

  def onAddedToSpace(eventTime: Instant, space: Space, user: User): Message =
    BotResponse.text("hello")

  def onRemovedFromSpace(eventTime: Instant, space: Space, user: User): Unit = {}

  def onMessageReceived(eventTime: Instant,
                        space: Space,
                        message: Message,
                        user: User): Message =
    Message(
      cards = Card(
        sections = Section(
          widgets = WidgetMarkup(
            keyValue = KeyValue(
              content = "Hello",
              onClick = OnClick(action = FormAction(actionMethodName = "hello"))
            )
          )
        )
      )
    )

  private def dbConfig: DbConfig = DbConfig()

  override def run(args: List[String]): IO[ExitCode] = args match {

    case "run" :: _ =>
      super.run(args)

    case "migrate" :: _ =>
      println("I will perform db migrations.")
      IO(ExitCode.Success)
    // DbMigration(dbConfig).migrate.map(_ => ExitCode.Success)

    case _ =>
      println("argument missing or wrong. usage: familybot [run|migrate]")
      IO(ExitCode.Error)
  }
}
