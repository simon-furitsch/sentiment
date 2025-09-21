package net.furitsch.sentiment
package evaluation

import config.ConfigLoader
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.evaluation.{BinaryClassificationEvaluator, MulticlassClassificationEvaluator}
import org.apache.spark.sql.DataFrame

object Evaluator {

  def evaluate(model: PipelineModel, testData:DataFrame):EvaluationMetrics = {

    val predictions = model.transform(testData)

    val accuracy =
      if(ConfigLoader.evaluation.useAccuracy)
        new MulticlassClassificationEvaluator()
        .setLabelCol(ConfigLoader.dataset.binaryLabelColumn)
        .setPredictionCol("prediction")
        .setMetricName("accuracy")
        .evaluate(predictions)
      else -1

    val f1 = {
      if(ConfigLoader.evaluation.useF1)
        new MulticlassClassificationEvaluator()
        .setLabelCol(ConfigLoader.dataset.binaryLabelColumn)
        .setPredictionCol("prediction")
        .setMetricName("f1")
        .evaluate(predictions)
      else -1
    }

    val auc =
      if(ConfigLoader.evaluation.useAreaUnderROC)
        new BinaryClassificationEvaluator()
        .setLabelCol(ConfigLoader.dataset.binaryLabelColumn)
        .setRawPredictionCol("rawPrediction")
        .setMetricName("areaUnderROC")
        .evaluate(predictions)
      else -1

    val evaluationMap:Map[String,Double] = Map(
      "accuracy" -> accuracy,
      "f1" -> f1,
      "areaUnderROC" -> auc
    ).filterNot(_._2 == -1)

    EvaluationMetrics(evaluationMap)

  }

}
