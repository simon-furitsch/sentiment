package net.furitsch.sentiment
package pipeline

import config.Config

import org.apache.spark.ml.{Estimator, Pipeline, PipelineStage}
import org.apache.spark.ml.feature.{CountVectorizer, HashingTF, IDF, NGram, RegexTokenizer, SQLTransformer, StopWordsRemover, VectorAssembler, Word2Vec}

object PipelineBuilder {
  /**
   * Builds the pipeline, defined in the configuration file
   * @param config the configuration of the run
   * @param estimator the classifier for the run
   * @return the pipeline
   */
  def buildPipeline(config:Config, estimator:Estimator[_]): Pipeline = {
    val vectorizerInput = if (config.features.useNGram) "ngrams" else "cleanedTokens"
    val useNgram = config.features.useNGram
    val useIDF = config.features.useIDF
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
        .setN(config.features.nGramSize)
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

    val features = config.features.vectorizer match {
      case "tf" => if (useIDF) Seq(tf, idf) else Seq(tf)
      case "countVectorizer" => if (useIDF) Seq(countVectorizer, idf) else Seq(countVectorizer)
      case "word2vec" => Seq(word2vec)
    }

    val transformers = Seq(tokenizer, cleaned) ++ nGramStage ++ features

    val stages = transformers.toArray :+ estimator

    new Pipeline().setStages(stages)
  }
}
