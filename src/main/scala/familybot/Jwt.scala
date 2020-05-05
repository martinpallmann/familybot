package familybot

import com.auth0.jwt.{JWT, JWTVerifier}
import com.auth0.jwt.algorithms.Algorithm
import familybot.auth.KeyProvider
import org.slf4j.LoggerFactory
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._

object Jwt {

  private val logger = LoggerFactory.getLogger(getClass)

  def verifier: JWTVerifier =
    JWT
      .require(Algorithm.RSA256(KeyProvider()))
      .acceptLeeway(5.minutes.toSeconds)
      .withIssuer("chat@system.gserviceaccount.com")
      .withAudience("301972490637")
      .build()

  def verify(token: String): Unit = {
    Try {
      verifier.verify(token)
    } match {
      case Success(_) => logger.debug("Token is valid")
      case Failure(e) => logger.debug("Token is invalid")
    }
  }
}
