//
// Copyright (c) 2011-2017 by Curalate, Inc.
//

package io.paradoxical.markov

import scala.collection.mutable
import scala.util.Random

class SentenceCreator(model: MarkovModel) {
  private val random = new Random()

  private def startPhrase(): Phrase = {
    model.startPhrases(random.nextInt(model.startPhrases.length - 1))
  }

  private def nextWord(seed: Seq[String]): String = {
    val words = model.chains.getOrElse(Phrase(seed), Nil)

    val totalWeights = words.map(_.count).sum

    val probabilityWords = words.filter(word => isMatch(word.count, totalWeights))

    Random.shuffle(probabilityWords).headOption.map(_.word).getOrElse("")
  }

  private def isMatch(weight: Integer, max: Integer): Boolean = {
    weight > random.nextInt(max)
  }

  def sentence(): String = {
    val seed = startPhrase().words

    val sentence = new mutable.ArrayBuffer[String]

    sentence.appendAll(seed)

    while (sentence.last != "") {
      val lastPhrase = sentence.takeRight(seed.length)

      sentence.append(nextWord(lastPhrase))
    }

    sentence.mkString(" ").trim + "."
  }
}
