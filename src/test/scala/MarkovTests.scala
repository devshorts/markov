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

  "Trainer" should "create start phrases" in {
    val corpus =
      """
        |I like apples. I like pears. I like apples. Bob
      """.stripMargin

    val markov = new Trainer(Tokenizer.tokenize(corpus)).train(2)

    markov.startPhrases shouldEqual Seq(
      Phrase(Seq("i", "like")),
      Phrase(Seq("bob"))
    )
  }

  it should "create weights" in {
    val corpus =
      """
        |I like apples. I like pears. I like apples.
      """.stripMargin

    val markov = new Trainer(Tokenizer.tokenize(corpus)).train(2)

    markov.chains.keySet shouldEqual Set(
      Phrase(Seq("i", "like")),
      Phrase(Seq("like", "apples")),
      Phrase(Seq("apples", "i")),
      Phrase(Seq("like", "pears")),
      Phrase(Seq("pears", "i")),
    )

    markov.chains shouldEqual Map(
      Phrase(Stream("i", "like")) -> List(WordProb("pears", 1), WordProb("apples", 2)),
      Phrase(Stream("like", "apples")) -> List(WordProb("i", 1)),
      Phrase(Stream("apples", "i")) -> List(WordProb("like", 1)),
      Phrase(Stream("like", "pears")) -> List(WordProb("i", 1)),
      Phrase(Stream("pears", "i")) -> List(WordProb("like", 1))
    )
  }

  "Sentence creator" should "make hilarous shakespear" in {
    val corpus = getClass.getClassLoader.getResourceAsStream("corpus.txt")

    val markov = new Trainer(Tokenizer.tokenize(corpus)).train(2)

    val sentenceCreator = new SentenceCreator(markov)

    (0 until 10).foreach(_ => println(sentenceCreator.sentence()))
  }
}
