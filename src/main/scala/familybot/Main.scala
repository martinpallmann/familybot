package familybot

import java.time.Instant

import de.martinpallmann.gchat.BotResponse
import de.martinpallmann.gchat.Bot
import de.martinpallmann.gchat.gen.{Message, Space, User}

object Main extends Bot {
  def onAddedToSpace(eventTime: Instant,
                     space: Space,
                     user: User): BotResponse = BotResponse.Text("hello")

  def onRemovedFromSpace(eventTime: Instant, space: Space, user: User): Unit = {}

  def onMessageReceived(eventTime: Instant,
                        space: Space,
                        message: Message,
                        user: User): BotResponse = BotResponse.Text("hello")
}
