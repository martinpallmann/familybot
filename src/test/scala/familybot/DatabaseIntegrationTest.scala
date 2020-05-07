package familybot

import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import cats.implicits._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import minitest._
import org.flywaydb.core.Flyway

abstract class DatabaseIntegrationTest extends TestSuite[Aux[IO, Unit]] {

  private val config: DbConfig = new DbConfig {
    override def url: String = "jdbc:postgresql:world"
    override def user: String = "postgres"
    override def password: String = ""
  }

  def setup(): Aux[IO, Unit] = {
    val flyway = Flyway
      .configure()
      .dataSource(config.url, config.user, config.password)
      .load()
    flyway.clean()
    flyway.migrate()

    implicit val cs: ContextShift[IO] =
      IO.contextShift(ExecutionContexts.synchronous)

    Transactor.fromDriverManager[IO](
      "org.postgresql.Driver",
      config.url,
      config.user,
      config.password,
      Blocker
        .liftExecutionContext(ExecutionContexts.synchronous)
    )
  }

  def tearDown(env: Aux[IO, Unit]): Unit = {}
}
