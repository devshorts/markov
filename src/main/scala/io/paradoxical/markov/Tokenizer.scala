//
// Copyright (c) 2011-2017 by Curalate, Inc.
//

package io.paradoxical.markov

import java.io.{ByteArrayInputStream, InputStream}
import scala.collection.mutable
import scala.io.Source

class Tokenizer {
  case class WordState(
    currentWord: String = "",
    newSentence: Boolean = true
  )
  def tokenize(corpus: String): Stream[Token] = {
    tokenize(new ByteArrayInputStream(corpus.getBytes("UTF-8")))
  }

  def tokenize(stream: InputStream): Stream[Token] = {
    val seed = (WordState(), new mutable.ArrayBuffer[Token])

    Source.fromInputStream(stream, "UTF-8").
      toStream.
      foldLeft(seed)((acc, next) => {
        val (state, existing) = acc

        val WordState(currentRaw, newSentence) = state

        val currentWord = currentRaw.trim

        next.toLower match {
          // sentence end
          case Terminal(_) if currentWord.nonEmpty =>
            val token =
              if (state.newSentence) {
                StartWord(currentWord)
              } else {
                Word(currentWord)
              }

            (WordState(), existing :+ token)
          // a terminal endpoint but there's no current word (maybe just an empty period?)
          case Terminal(_) =>
            (WordState(), existing)
          // a word delimiter
          case Delimiter(_) if currentRaw.nonEmpty =>
            val nextToken =
              if (state.newSentence) {
                StartWord(currentRaw)
              } else {
                Word(currentRaw)
              }
            (WordState(newSentence = false), existing :+ nextToken)
          // drop spaces
          case Delimiter(_) =>
            acc
          // an actual letter
          case c =>
            (state.copy(currentWord = currentWord + c), existing)
        }
      })._2.toStream
  }
}

object Tokenizer extends Tokenizer