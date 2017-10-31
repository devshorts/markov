package io.paradoxical.markov

case class Phrase(words: Seq[String])

case class MarkovModel(
  chains: Map[Phrase, Seq[WordProb]],
  startPhrases: Seq[Phrase]
)

sealed trait Token {
  def word: String

  def isStart: Boolean = false
}

case class StartWord(word: String) extends Token {
  override def isStart = true
}

case class Word(word: String) extends Token

case class WordProb(word: String, count: Int) {
  def inc = copy(count = count + 1)
}

object Terminal {
  def unapply(s: Char): Option[Char] = s match {
    case '.' | '\n' | '!' | '?' => Some(s)
    case _ => None
  }
}

object Delimiter {
  def unapply(s: Char): Option[Char] = s match {
    case '&' | ',' | ';' | ' ' | ':' => Some(s)
    case _ => None
  }
}