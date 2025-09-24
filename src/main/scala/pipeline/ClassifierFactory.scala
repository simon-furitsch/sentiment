package net.furitsch.sentiment
package pipeline

import config.Config

import org.apache.spark.ml.classification.{LogisticRegression, NaiveBayes}
import org.apache.spark.ml.Estimator

object ClassifierFactory {
  /**
   * Creates a classifier defined in the configuration file
   * @param config the configuration of the run
   * @return the classifier for the run
   */
  def make(config:Config):Estimator[_] = {
    config.model.algorithm match {
      case "logisticRegression" =>
        val threshold = config.model.threshold
        val iterations = config.model.iterations
        val regParam = config.model.regParam

        new LogisticRegression()
          .setFeaturesCol("features")
          .setLabelCol(config.dataset.binaryLabelColumn)
          .setThreshold(threshold)
          .setMaxIter(iterations)
          .setRegParam(regParam)

      case "naiveBayes" =>
        new NaiveBayes()
        .setFeaturesCol("features")
        .setLabelCol(config.dataset.binaryLabelColumn)
        .setModelType("multinomial")

      case _ => throw new IllegalArgumentException("Classifier type unknown. Please check the config!") //Todo Logging
    }
  }

}
