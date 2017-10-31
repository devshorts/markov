//
// Copyright (c) 2011-2017 by Curalate, Inc.
//

package io.paradoxical.markov

import scala.collection.mutable

class Trainer(tokens: Stream[Token]) {
  def train(chainSize: Int): MarkovModel = {
    val map = new mutable.HashMap[Phrase, Seq[WordProb]]()
    val startPhrases = new mutable.HashSet[Phrase]()


    tokens.
      sliding(chainSize + 1).
      foreach(chain => {
        val rootPhrase = Phrase(chain.take(chainSize).map(_.word))

        if (chain.headOption.exists(_.isStart)) {
          startPhrases += rootPhrase
        }

        val nextWord = chain.drop(chainSize).headOption.map(w => WordProb(w.word, 1)).getOrElse(WordProb("", 1))

        if (!map.contains(rootPhrase)) {
          map += rootPhrase -> Seq(nextWord)
        } else {
          val existingPhrase = map(rootPhrase)

          val toAdd = existingPhrase.find(_.word == nextWord.word).map(_.inc).getOrElse(nextWord)

          map.update(rootPhrase, (existingPhrase.filterNot(_.word == toAdd.word) :+ toAdd).distinct)
        }
      })

    MarkovModel(map.toMap, startPhrases = startPhrases.toSeq)
  }
}
