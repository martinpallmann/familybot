package familybot.shopping

import java.util.UUID

import de.martinpallmann.gchat.gen.User

case class ShoppingList(items: List[ShoppingListItem]) {
  def add(name: String): ShoppingList =
    this.copy(items = ShoppingListItem(name) :: items)

  def remove(uuid: UUID): ShoppingList =
    this.copy(items = items.filterNot(_.uuid == uuid))

  def save(user: User): Unit =
    user.name.foreach(id => {
      ShoppingList.lists = ShoppingList.lists + (id -> this)
    })
}

object ShoppingList {

  type UserId = String

  var lists: Map[UserId, ShoppingList] =
    Map.empty.withDefaultValue(ShoppingList())

  def apply(): ShoppingList = new ShoppingList(Nil)

  def apply(user: User): ShoppingList =
    user.name.map(lists.apply).getOrElse(ShoppingList())
}
