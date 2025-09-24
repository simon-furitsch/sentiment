package net.furitsch.sentiment
package run

import data.DataLoader
import filesystem.{DirectoryManager, PersistenceManager}
import preprocess.Preprocessor
import train.TrainService
import utils.DatasetSplitter

import org.apache.spark.ml.Pipeline
import evaluation.Evaluator

import net.furitsch.sentiment.config.Config
import org.apache.spark.sql.classic.SparkSession

object RunService {
  /**
   * Creates a run context and the directories,
   * Makes the config persistent in snapshots/configname
   * splits the dataset and binarizes the labels,
   * trains a model and makes it persistent in runs/runid/model
   * evaluates the model and persists the metrics
   *
   * @param spark spark session
   */
  def fullRun(configName: String, spark: SparkSession): Unit = {
    val configPath = s"config/$configName"
    val config: Config = new Config(configPath)
    val context = RunManager.initNewRun(config)
    DirectoryManager.createDirectories(context)
    val dataset = DataLoader.loadDataset(config, spark)
    val binarizedDataset = Preprocessor.toBinary(config, dataset)
    val Array(trainSet, testSet, validateSet) = DatasetSplitter.split(config, binarizedDataset)
    val model = TrainService.train(config, trainSet)
    PersistenceManager.saveSnapshot(config, context)
    PersistenceManager.saveModel(context, model)
    PersistenceManager.saveContext(context)
    val metrics = Evaluator.evaluate(config, context, model, testSet)
    PersistenceManager.saveMetrics(context, metrics)
  }

  /**
   * Loads a run and continues with the training process
   * @param runId the id of the run to continue
   * @param spark the spark context
   */
  def continueWithTraining(runId: Int, spark: SparkSession): Unit = {
    val context = RunManager.resumeRun(runId)
    val config = new Config(s"${context.snapshotsDir}/${context.configName}")
    val dataset = DataLoader.loadDataset(config, spark)
    val binarizedDataset = Preprocessor.toBinary(config, dataset)
    val Array(trainSet, testSet, validateSet) = DatasetSplitter.split(config, binarizedDataset)
    val model = TrainService.train(config, trainSet)
    PersistenceManager.saveModel(context, model)
  }

  /**
   * Loads a trained model and evaluates it with the metrics defined in the configuration file
   * @param runId the run id to evaluate
   * @param spark the spark context
   */
  def continueWithEvaluation(runId: Int, spark:SparkSession):Unit = {
    val context = RunManager.resumeRun(runId)
    val config = new Config(s"${context.snapshotsDir}/${context.configName}")
    val dataset = DataLoader.loadDataset(config, spark)
    val binarizedDataset = Preprocessor.toBinary(config, dataset)
    val Array(trainSet, testSet, validateSet) = DatasetSplitter.split(config, binarizedDataset)
    val model = PersistenceManager.loadModel(context)

    val metrics = Evaluator.evaluate(config, context, model, testSet)
    PersistenceManager.saveMetrics(context, metrics)
  }

  /**
   * Initializes a new run
   * @param configName the configuration name
   */
  def initRun(configName: String): Unit = {
    val config = new Config(s"config/$configName")
    val context = RunManager.initNewRun(config)
    DirectoryManager.createDirectories(context)
    PersistenceManager.saveContext(context)
    PersistenceManager.saveSnapshot(config,context)
  }

}
