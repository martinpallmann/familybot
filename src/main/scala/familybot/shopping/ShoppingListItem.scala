package familybot.shopping

import java.util.UUID

case class ShoppingListItem(uuid: UUID, name: String)

object ShoppingListItem {
  def apply(name: String): ShoppingListItem =
    ShoppingListItem(UUID.randomUUID(), name)
}
