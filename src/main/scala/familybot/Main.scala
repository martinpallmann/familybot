package familybot

import java.time.Instant
import java.util.UUID

import cats.effect.{ExitCode, IO}
import de.martinpallmann.gchat.BotResponse
import de.martinpallmann.gchat.Bot
import de.martinpallmann.gchat.gen._
import familybot.shopping.ShoppingList

import scala.language.implicitConversions

object Main extends Bot {

  def onAddedToSpace(eventTime: Instant, space: Space, user: User): Message =
    BotResponse.text("hello")

  def onRemovedFromSpace(eventTime: Instant, space: Space, user: User): Unit = {}

  def onMessageReceived(eventTime: Instant,
                        space: Space,
                        message: Message,
                        user: User): Message = {
    message.argumentText.fold(BotResponse.text("well..."))(s => {
      val sl = ShoppingList(user).add(s.trim)
      sl.save(user)
      Message(
        cards = Card(
          sections = Section(header = "Shopping List", widgets = sl2Widget(sl))
        )
      )
    })
  }

  def onCardClicked(eventTime: Instant,
                    space: Space,
                    message: Message,
                    user: User,
                    a: FormAction): Message = {
    a.actionMethodName match {
      case Some("remove") =>
        val sl = a.parameters.getOrElse(Nil).foldLeft(ShoppingList(user)) {
          case (acc, ActionParameter(Some("id"), Some(uuid))) =>
            acc.remove(UUID.fromString(uuid))
          case (acc, _) => acc
        }
        sl.save(user)
        Message(
          cards = Card(
            sections =
              Section(header = "Shopping List", widgets = sl2Widget(sl))
          )
        )
      case _ => BotResponse.text("well...")
    }
  }

  def sl2Widget(sl: ShoppingList): List[WidgetMarkup] =
    sl.items.map(
      it =>
        WidgetMarkup(
          keyValue = KeyValue(
            content = it.name,
            onClick = OnClick(
              action = FormAction(
                actionMethodName = "remove",
                parameters = ActionParameter("id", it.uuid.toString)
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
