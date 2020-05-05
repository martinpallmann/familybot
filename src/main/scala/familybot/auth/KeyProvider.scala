package familybot.auth

import java.io.ByteArrayInputStream
import java.net.URL
import java.security.cert.CertificateFactory
import java.security.interfaces.{RSAPrivateKey, RSAPublicKey}

import com.auth0.jwt.interfaces.RSAKeyProvider
import io.circe.JsonObject
import io.circe.parser.parse

import scala.io.Source
import scala.util.Try
import KeyProvider._

class KeyProvider extends RSAKeyProvider {

  val CHAT_ISSUER = "chat@system.gserviceaccount.com"
  val PUBLIC_CERT_URL_PREFIX =
    "https://www.googleapis.com/service_accounts/v1/metadata/x509/"

  private def publicKeys: Map[String, String] = {
    val json = parse(
      Source
        .fromInputStream(
          new URL(PUBLIC_CERT_URL_PREFIX + CHAT_ISSUER).openStream(),
          "UTF-8"
        )
        .getLines()
        .mkString("\n")
    ).toTry.get.as[JsonObject].toTry.get
    json.toMap.view.mapValues(_.as[String].toTry.get).toMap
  }

  def getPublicKeyById(keyId: String): RSAPublicKey = {
    publicKeys
      .get(keyId)
      .flatMap(extractPublicKey(_).toOption)
      .orNull
  }
  def getPrivateKey: RSAPrivateKey = null
  def getPrivateKeyId: String = null
}

object KeyProvider {

  def apply(): KeyProvider = new KeyProvider()

  def extractPublicKey(certValue: String): Try[RSAPublicKey] =
    Try {
      CertificateFactory
        .getInstance("X.509")
        .generateCertificate(
          new ByteArrayInputStream(certValue.getBytes("UTF-8"))
        )
        .getPublicKey
        .asInstanceOf[RSAPublicKey]
    }
}
