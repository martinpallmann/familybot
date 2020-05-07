package familybot

trait DbConfig {

  def url: String
  def user: String
  def password: String
}

object DbConfig {

  def apply(): DbConfig = new DbConfig {
    def env: Map[String, String] = sys.env
    def url: String = env("JDBC_DATABASE_URL")
    def user: String = env("JDBC_DATABASE_USERNAME")
    def password: String = env("JDBC_DATABASE_PASSWORD")
  }
}
