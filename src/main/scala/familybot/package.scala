import de.martinpallmann.gchat.gen.TextParagraph

import scala.language.implicitConversions

package object familybot {

  implicit def anyToOption[A](a: A): Option[A] =
    Option(a)

  implicit def anyToList[A](a: A): List[A] =
    Option(a).toList

  implicit def anyToOptList[A](a: A): Option[List[A]] =
    Option(a).map(List.apply(_))

  implicit def stringToTextParagraph(s: String): TextParagraph =
    TextParagraph(s)

}
