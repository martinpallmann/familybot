package familybot

import cats.effect.IO
import org.flywaydb.core.Flyway

class DbMigration(flyway: Flyway) {
  def migrate: IO[Unit] = IO(flyway.migrate())
}

object DbMigration {
  def apply(dbConfig: DbConfig): DbMigration =
    new DbMigration(
      Flyway
        .configure()
        .dataSource(dbConfig.url, dbConfig.user, dbConfig.password)
        .load()
    )
}
