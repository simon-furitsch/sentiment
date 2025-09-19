package net.furitsch.sentiment
package pipeline

import config.ConfigLoader
import org.apache.spark.ml.classification.{LogisticRegression, NaiveBayes}
import org.apache.spark.ml.Estimator

object ClassifierFactory {

  def make():Estimator[_] = {
    ConfigLoader.model.algorithm match {
      case "logisticRegression" =>
        val threshold = ConfigLoader.model.threshold
        val iterations = ConfigLoader.model.iterations
        val regParam = ConfigLoader.model.regParam

        new LogisticRegression()
          .setFeaturesCol("features")
          .setLabelCol("binary_label")
          .setThreshold(threshold)
          .setMaxIter(iterations)
          .setRegParam(regParam)

      case "naiveBayes" =>
        new NaiveBayes()
        .setFeaturesCol("features")
        .setLabelCol("binary_label")
        .setModelType("multinomial")

      case _ => throw new IllegalArgumentException("Classifier type unknown. Please check the config!")
    }
  }

}
