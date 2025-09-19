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
          .setLabelCol("label_binary")
          .setThreshold(threshold)
          .setMaxIter(iterations)
          .setRegParam(regParam)

      case "naiveBayes" =>
        new NaiveBayes()
        .setFeaturesCol("features")
        .setLabelCol("label_binary")
        .setModelType("multinomial")

      case _ => throw new IllegalArgumentException("Classifier type unknown. Please check the config!")
    }
  }

}
