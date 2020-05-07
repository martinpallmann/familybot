package familybot

import de.martinpallmann.gchat.gen._

sealed trait Context {
  def widgets: List[WidgetMarkup]
}

object Context {

  case object Root extends Context {
    def widgets: List[WidgetMarkup] =
      WidgetMarkup(textParagraph = TextParagraph(""))
  }

  type UserId = String

  private var data: Map[UserId, Context] =
    Map.empty.withDefaultValue(Root)

  def find(user: User, message: Message): Context =
    data(user.name.get)
}
