package familybot

import java.nio.charset.StandardCharsets
import java.security.interfaces.{RSAPrivateKey, RSAPublicKey}
import java.util.Base64

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success, Try}

object Jwt {

  private val logger = LoggerFactory.getLogger(getClass)

  private val keyManager = {
    val CHAT_ISSUER = "chat@system.gserviceaccount.com"
    val PUBLIC_CERT_URL_PREFIX =
      "https://www.googleapis.com/service_accounts/v1/metadata/x509/"
    val factory = new JacksonFactory()
    val keyManagerBuilder =
      new GooglePublicKeysManager.Builder(new ApacheHttpTransport, factory)
    val certUrl = PUBLIC_CERT_URL_PREFIX + CHAT_ISSUER
    keyManagerBuilder.setPublicCertsEncodedUrl(certUrl)
    keyManagerBuilder.build()
  }

  def publicKeys: List[RSAPublicKey] =
    keyManager.getPublicKeys.asScala.toList
      .filter(_.isInstanceOf[RSAPublicKey])
      .map(_.asInstanceOf[RSAPublicKey])

  def base64Decode(s: String) =
    Try {
      Base64.getDecoder.decode(s)
      new String(Base64.getDecoder.decode(s), StandardCharsets.UTF_8)
    }.getOrElse(s"not decoded: $s")

  def keyProvider: RSAKeyProvider = new RSAKeyProvider {
    def getPublicKeyById(keyId: String): RSAPublicKey = {
      logger.debug(s"keyId ${base64Decode(keyId)}")
      publicKeys.foreach(k => {
        logger.debug(s"PUBLIC KEY $k")
      })
      publicKeys.headOption.orNull
    }
    def getPrivateKey: RSAPrivateKey = null
    def getPrivateKeyId: String = null
  }

  def verify(token: String): Unit = {
    val jwt = JWT.decode(token)
    val verifier = JWT.require(Algorithm.RSA256(keyProvider)).build()
    Try {
      verifier.verify(jwt)
    } match {
      case Success(_)         => logger.debug("valid")
      case Failure(exception) => logger.debug("invalid", exception)
    }
  }
}
