package familybot

import doobie.Update0
import doobie.implicits._

object ShoppingListTest extends DatabaseIntegrationTest {

  test("db connection") { xa =>
    val program2 = sql"select 42".query[Int].unique
    val io = program2.transact(xa)
    val result = io.unsafeRunSync
    assertEquals(result, 42)
  }

  test("hey") { xa =>
    val y = xa.yolo
    import y._
    def insert1(name: String, age: Option[Short]): Update0 =
      sql"insert into person (name, age) values ($name, $age)".update
    insert1("Martin", None).quick.unsafeRunSync
  }
}
