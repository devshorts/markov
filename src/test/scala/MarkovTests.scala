package io.paradox.markov

import io.paradoxical.markov._
import org.scalatest._

class Tests extends FlatSpec with Matchers {
  "Tokenizer" should "tokenize" in {
    val corpus =
      """Hello my name is.  Foo. Bar.
        |
        |biz
        |baz
      """.stripMargin

    Tokenizer.tokenize(corpus) shouldEqual List(
      StartWord("hello"),
      Word("my"),
      Word("name"),
      Word("is"),
      StartWord("foo"),
      StartWord("bar"),
      StartWord("biz"),
      StartWord("baz")
    )
  }

  "Trainer" should "train" in {
    val corpus = getClass.getClassLoader.getResourceAsStream("corpus.txt")

    val markov = new Trainer(Tokenizer.tokenize(corpus)).train(2)

    val sentenceCreator = new SentenceCreator(markov)

    (0 until 10).foreach(_ => println(sentenceCreator.sentence()))
  }
}
