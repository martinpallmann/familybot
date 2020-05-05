package familybot

import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.interfaces.{RSAPrivateKey, RSAPublicKey}
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

import com.auth0.jwt.{JWT, JWTVerifier}
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import io.circe.{Json, JsonObject}
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration._
import scala.io.Source
import io.circe.parser.parse

object Jwt {

  private val logger = LoggerFactory.getLogger(getClass)

  val CHAT_ISSUER = "chat@system.gserviceaccount.com"
  val PUBLIC_CERT_URL_PREFIX =
    "https://www.googleapis.com/service_accounts/v1/metadata/x509/"

  private val keyManager = {
    val factory = new JacksonFactory()
    val keyManagerBuilder =
      new GooglePublicKeysManager.Builder(new ApacheHttpTransport, factory)
    val certUrl = PUBLIC_CERT_URL_PREFIX + CHAT_ISSUER
    keyManagerBuilder.setPublicCertsEncodedUrl(certUrl)
    keyManagerBuilder.build()
  }

  def verifier: JWTVerifier = {
    JWT.require(Algorithm.RSA256(new KeyProvider())).build()
  }

  def publicKeys: List[RSAPublicKey] =
    keyManager.getPublicKeys.asScala.toList
      .filter(_.isInstanceOf[RSAPublicKey])
      .map(_.asInstanceOf[RSAPublicKey])

  def verifiers: List[String => Boolean] =
    publicKeys.map(k => {
      val v = JWT
        .require(Algorithm.RSA256(k, null))
        .acceptLeeway(5.minutes.toSeconds)
        .withIssuer("chat@system.gserviceaccount.com")
        .withAudience("301972490637") // Google project number
        .build()
      s: String =>
        Try {
          v.verify(s)
        }.isSuccess
    })

  def verify(token: String): Unit = {
    val ok = verifiers.exists(_(token))
    Try {
      verifier.verify(token)
    }
    if (ok) {
      logger.debug("token is valid")
    } else {
      logger.debug("token is invalid")
    }
  }

  class KeyProvider extends RSAKeyProvider {

    def publicKey(s: Json): String = {
      s.as[String].toTry.get
    }

    def publicKeys: Map[String, String] = {
      val json = parse(
        Source
          .fromInputStream(
            new URL(PUBLIC_CERT_URL_PREFIX + CHAT_ISSUER).openStream(),
            "UTF-8"
          )
          .getLines()
          .mkString("\n")
      ).toTry.get.as[JsonObject].toTry.get

      json.toMap.view.mapValues(publicKey).toMap
    }

    def getPublicKeyById(keyId: String): RSAPublicKey = {
      val key = publicKeys
        .get(keyId)
        .map(s => {

          val lines = Source
            .fromString(s)
            .getLines()
          val cert = lines
            .slice(1, lines.length - 1)
            .mkString("\n")
          val encoded = Base64.getDecoder.decode(cert)
          val kf = KeyFactory.getInstance("RSA")
          kf.generatePublic(new X509EncodedKeySpec(encoded))
        })
      logger.debug(s"THE KEY\n>>>\n$key\n<<<\n")
      null
    }

    def getPrivateKey: RSAPrivateKey = null

    def getPrivateKeyId: String = null
  }
}
