package net.furitsch.sentiment
package pipeline

import config.ConfigLoader
import org.apache.spark.ml.{Pipeline, PipelineStage}
import org.apache.spark.ml.feature.{CountVectorizer, HashingTF, IDF, NGram, RegexTokenizer, StopWordsRemover, Word2Vec}

object PipelineBuilder {

  def buildPipeline(): Pipeline = {
    val vectorizerInput = if (ConfigLoader.features.useNGram) "ngrams" else "cleanedTokens"
    val useNgram = ConfigLoader.features.useNGram
    val useIDF = ConfigLoader.features.useIDF
    val vectorizerOutput = if (useIDF) "vectorized" else "features"

    val tokenizer = new RegexTokenizer()
      .setInputCol("text")
      .setOutputCol("tokens")
      .setToLowercase(true)
      .setPattern("\\W+")

    val cleaned = new StopWordsRemover()
      .setInputCol("tokens")
      .setOutputCol("cleanedTokens")

    val nGramStage:Seq[PipelineStage] =
      if(useNgram) Seq(new NGram()
        .setN(ConfigLoader.features.nGramSize)
        .setInputCol("cleanedTokens")
        .setOutputCol("ngrams"))
      else Seq.empty

    val tf = new HashingTF()
      .setInputCol(vectorizerInput)
      .setOutputCol(vectorizerOutput)

    val word2vec = new Word2Vec()
      .setInputCol("cleanedTokens")
      .setOutputCol("features")

    val countVectorizer = new CountVectorizer()
      .setInputCol(vectorizerInput)
      .setOutputCol(vectorizerOutput)

    val idf = new IDF()
      .setInputCol("vectorized")
      .setOutputCol("features")

    val features = ConfigLoader.features.vectorizer match {
      case "tf" => if (useIDF) Seq(tf, idf) else Seq(tf)
      case "countVectorizer" => if (useIDF) Seq(countVectorizer, idf) else Seq(countVectorizer)
      case "word2vec" => Seq(word2vec)
    }

    val stages = Seq(tokenizer, cleaned) ++ nGramStage ++ features

    new Pipeline().setStages(stages.toArray)
  }
}
