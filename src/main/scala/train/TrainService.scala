package net.furitsch.sentiment
package train


import pipeline.{ClassifierFactory, PipelineBuilder}

import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.DataFrame

object TrainService {

  def train(dataFrame: DataFrame):PipelineModel = {
    val estimator = ClassifierFactory.make()
    val pipeline = PipelineBuilder.buildPipeline(estimator)
    pipeline.fit(dataFrame)
  }

}
