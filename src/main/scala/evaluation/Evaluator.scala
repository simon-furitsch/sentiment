package net.furitsch.sentiment
package evaluation

import config.Config
import data.PlotManager
import run.RunContext

import org.apache.spark.sql.functions.col
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.evaluation.{BinaryClassificationEvaluator, MulticlassClassificationEvaluator}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.linalg.Vector

object Evaluator {
  /**
   * Evaluates the model using the metrics defined in the configuration file.
   * @param config the configuration of the run
   * @param context the execution context of the run
   * @param model the trained model to evaluate
   * @param testData the dataset used to evaluate
   * @return EvaluationMetrics containing a map with the metrics
   */
  def evaluate(config:Config, context:RunContext, model: PipelineModel, testData:DataFrame):EvaluationMetrics = {

    val predictions = model.transform(testData)

    val accuracy =
      if(config.evaluation.useAccuracy)
        new MulticlassClassificationEvaluator()
        .setLabelCol(config.dataset.binaryLabelColumn)
        .setPredictionCol("prediction")
        .setMetricName("accuracy")
        .evaluate(predictions)
      else -1

    val f1 = {
      if(config.evaluation.useF1)
        new MulticlassClassificationEvaluator()
        .setLabelCol(config.dataset.binaryLabelColumn)
        .setPredictionCol("prediction")
        .setMetricName("f1")
        .evaluate(predictions)
      else -1
    }

    val labelCol = config.dataset.binaryLabelColumn
    val pred = predictions.withColumn(labelCol, col(labelCol).cast("double"))

    val scoreAndLabels = pred.select(col("probability").as("prob"), col(labelCol).as("label"))
      .rdd
      .map { row =>
        val prob  = row.getAs[Vector]("prob")
        val label = row.getAs[Double]("label")
        (prob(1), label)
      }

    val rocMetrics = new BinaryClassificationMetrics(scoreAndLabels)

    val aucFromMetrics = rocMetrics.areaUnderROC()
    val roc = rocMetrics.roc().collect().sortBy(_._1)
    val fpr = roc.map(_._1)
    val tpr = roc.map(_._2)

    val auc =
      if (config.evaluation.useAreaUnderROC) aucFromMetrics else -1


    PlotManager.createRocPlot(context,fpr,tpr)

    val evaluationMap:Map[String,Double] = Map(
      "accuracy" -> accuracy,
      "f1" -> f1,
      "areaUnderROC" -> auc
    ).filterNot(_._2 == -1)

    EvaluationMetrics(evaluationMap)

  }

}
