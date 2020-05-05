package familybot

import com.auth0.jwt.JWT
import org.slf4j.LoggerFactory

object Jwt {

  private val logger = LoggerFactory.getLogger(getClass)

  def verify(token: String) = {
    val jwt = JWT.decode(token)
    logger.debug(s"Header: ${jwt.getHeader}")
    logger.debug(s"Payload: ${jwt.getPayload}")
    logger.debug(s"Signature: ${jwt.getSignature}")
    logger.debug(s"Token: ${jwt.getToken}")
  }
}
