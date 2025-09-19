package net.furitsch.sentiment
package train

import run.{RunContext, RunManager}

import net.furitsch.sentiment.data.DataLoader
import net.furitsch.sentiment.pipeline.{ClassifierFactory, PipelineBuilder}
import net.furitsch.sentiment.preprocess.Preprocessor
import net.furitsch.sentiment.utils.DatasetSplitter
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.classic.SparkSession

import scala.:+

object TrainService {

  def train(dataFrame: DataFrame):Unit = {
    val estimator = ClassifierFactory.make()
    val pipeline = PipelineBuilder.buildPipeline(estimator)
    pipeline.fit(dataFrame)
  }

}
