package familybot

import java.util.Collections

import com.google.api.client.googleapis.auth.oauth2.{
  GoogleIdToken,
  GoogleIdTokenVerifier,
  GooglePublicKeysManager
}

import scala.jdk.CollectionConverters._
import com.google.api.client.http.apache.ApacheHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson.JacksonFactory
import familybot.Main.getClass
import org.slf4j.LoggerFactory

object Verifyer {

  private val logger = LoggerFactory.getLogger(getClass)

  val CHAT_ISSUER = "chat@system.gserviceaccount.com"
  val PUBLIC_CERT_URL_PREFIX =
    "https://www.googleapis.com/service_accounts/v1/metadata/x509/"
  val AUDIENCE = "1234567890"

  def verify(bearerToken: String): Unit = {
    val factory = new JacksonFactory

    val keyManagerBuilder =
      new GooglePublicKeysManager.Builder(new ApacheHttpTransport, factory)

    val certUrl = PUBLIC_CERT_URL_PREFIX + CHAT_ISSUER
    keyManagerBuilder.setPublicCertsEncodedUrl(certUrl)

    val verifierBuilder =
      new GoogleIdTokenVerifier.Builder(keyManagerBuilder.build)
    verifierBuilder.setIssuer(CHAT_ISSUER)
    val verifier = verifierBuilder.build

    val idToken = GoogleIdToken.parse(factory, bearerToken)
    if (idToken == null) {
      logger.debug("ID TOKEN IS NULL")
    } else {
      (
        verifier.verify(idToken),
        idToken.verifyAudience(Collections.singletonList(AUDIENCE)),
        idToken.verifyIssuer(CHAT_ISSUER)
      ) match {
        case (t, a, i) =>
          logger.debug(
            s"""token is valid: $t, audience is valid: $a, issuer is valid: $i
             |Audience: ${idToken.getPayload.getAudienceAsList.asScala}""".stripMargin
          )
      }
    }
  }
}
