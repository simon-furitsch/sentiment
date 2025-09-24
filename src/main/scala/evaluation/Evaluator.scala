package net.furitsch.sentiment
package evaluation

import config.ConfigLoader

import net.furitsch.sentiment.data.PlotManager
import net.furitsch.sentiment.run.RunContext
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.evaluation.{BinaryClassificationEvaluator, MulticlassClassificationEvaluator}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.sql.DataFrame

object Evaluator {

  def evaluate(context:RunContext, model: PipelineModel, testData:DataFrame):EvaluationMetrics = {

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

    val scoreAndLabels = predictions.rdd.map { row =>
      val prob  = row.getAs[org.apache.spark.ml.linalg.Vector]("probability")
      val label = row.getAs[java.lang.Number](ConfigLoader.dataset.binaryLabelColumn).doubleValue()
      (prob(1), label)
    }

    val rocMetrics = new BinaryClassificationMetrics(scoreAndLabels)

    val roc = rocMetrics.roc().collect().sortBy(_._1)
    val fpr = roc.map(_._1)
    val tpr = roc.map(_._2)

    PlotManager.createRocPlot(context,fpr,tpr)

    val evaluationMap:Map[String,Double] = Map(
      "accuracy" -> accuracy,
      "f1" -> f1,
      "areaUnderROC" -> auc
    ).filterNot(_._2 == -1)

    EvaluationMetrics(evaluationMap)

  }

}
