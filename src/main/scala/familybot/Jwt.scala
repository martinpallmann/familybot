package familybot

import com.auth0.jwt.JWT
import org.slf4j.LoggerFactory

object Jwt {

  private val logger = LoggerFactory.getLogger(getClass)

  def verify(token: String) = {
    val jwt = JWT.decode(token)
    logger.debug(s"alg ${jwt.getAlgorithm}")
    logger.debug(s"typ ${jwt.getType}")
    logger.debug(s"cty ${jwt.getContentType}")
    logger.debug(s"kid ${jwt.getKeyId}")
  }
}
