package familybot

import java.security.interfaces.RSAPublicKey

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import org.slf4j.LoggerFactory
import scala.jdk.CollectionConverters._

object Jwt {

  private val logger = LoggerFactory.getLogger(getClass)

  def publicKeys = {
    val CHAT_ISSUER = "chat@system.gserviceaccount.com"
    val PUBLIC_CERT_URL_PREFIX =
      "https://www.googleapis.com/service_accounts/v1/metadata/x509/"
    val factory = new JacksonFactory()
    val keyManagerBuilder =
      new GooglePublicKeysManager.Builder(new ApacheHttpTransport, factory)
    val certUrl = PUBLIC_CERT_URL_PREFIX + CHAT_ISSUER
    keyManagerBuilder.setPublicCertsEncodedUrl(certUrl)
    val keyManager = keyManagerBuilder.build()
    keyManager.getPublicKeys.asScala.toList
      .filter(_.isInstanceOf[RSAPublicKey])
      .map(_.asInstanceOf[RSAPublicKey])
  }

  def verify(token: String) = {
    val jwt = JWT.decode(token)
    logger.debug(s"alg ${jwt.getAlgorithm}")
    logger.debug(s"pbk ${publicKeys.size}")
//    JWT.require(Algorithm.RSA256(RSAPublicKey, null))
  }
}
