import familybot.auth.KeyProvider
import minitest._

object TestKeyGen extends SimpleTestSuite {
  test("get key") {
    val key =
      "-----BEGIN CERTIFICATE-----\nMIIDDjCCAfagAwIBAgIIGVHnOEM455gwDQYJKoZIhvcNAQEFBQAwKjEoMCYGA1UE\nAxMfY2hhdC5zeXN0ZW0uZ3NlcnZpY2VhY2NvdW50LmNvbTAeFw0yMDA0MjkwODU1\nNTZaFw0yMDA1MTUyMTEwNTZaMCoxKDAmBgNVBAMTH2NoYXQuc3lzdGVtLmdzZXJ2\naWNlYWNjb3VudC5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDB\n9dcGMBZhQ9CH6rQHzVeHl07kfbXEFYZacmQYfQ+zsCE/1nMXCmoMnrOS5KsGn4sd\nRiegXsu4p5o03SnmHCIOrTA8Wl6V2hc9WCvpLuXmALHGl8S4/nhaMjuS0Cvpny9z\nbAb1F7ja9njiEvI0PsholsNIUq4bSjjRYQV0+M37bftpAIXkwdkwbnpHegS3uTT3\nY15Y4l7O5o0Fcd0/niKoVVRHH87V9DHpKK6v5440ldDWYLSlUBir7mXGWAwlcM7b\nS+iU4m4jXIq3w2SMtr+0B4ngOZ1Jl/1/hwY5sTx4RvQ4NkxkEFe5vtM8Y/ELAjMl\nIY/xFtbgDsbW/xj+c5qPAgMBAAGjODA2MAwGA1UdEwEB/wQCMAAwDgYDVR0PAQH/\nBAQDAgeAMBYGA1UdJQEB/wQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBBQUAA4IB\nAQBwFkCXMe1oFzryDKlkdso10t7mQUPxCqQxWJ+GLtqONaXyJz+kNO2jL9Jup6//\nC/+xG1w7uH/X3+Zg6rKQL/e80vkE2ytzE8MYo8gsCWyLBcMRMKIRtr6V7js8LMXp\nyiCtRuDf2ib4RgyqpoKl7F3nOYD6YjlkgHSfkAZf5ZEgMNz0qmT97zBUjkpNuqFs\nwllaw/0OSmwKq9zQiz5wqhVSdmPD3g2afHvaNPrsM/3CfY7JjNg7PuTKl16Y8KPg\n8X0pAe03m7mMet0izqs9WhzDlTvp1uHVwwYZM6s18mUcsq12UZHxt3t41NX22RKO\ntbkLpoMjziANdioquKodk1b4\n-----END CERTIFICATE-----\n"
    assertEquals(KeyProvider.extractPublicKey(key).isSuccess, true)
  }
}