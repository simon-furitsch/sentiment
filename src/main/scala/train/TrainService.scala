package net.furitsch.sentiment
package train


import pipeline.{ClassifierFactory, PipelineBuilder}

import net.furitsch.sentiment.config.Config
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.DataFrame

object TrainService {
  /**
   * Fits a pipeline on a data frame
   * @param config the configuration file of the run
   * @param dataFrame the dataframe to fit
   * @return a trained pipeline model
   */
  def train(config:Config,dataFrame: DataFrame):PipelineModel = {
    val estimator = ClassifierFactory.make(config)
    val pipeline = PipelineBuilder.buildPipeline(config,estimator)
    pipeline.fit(dataFrame)
  }

}
