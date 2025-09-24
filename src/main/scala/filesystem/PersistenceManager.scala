package net.furitsch.sentiment
package filesystem

import config.Config
import run.RunContext
import evaluation.EvaluationMetrics

import org.apache.spark.ml.PipelineModel

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}

object PersistenceManager {
  /**
   * Saves the model in runs/{id}/model
   *
   * @param context the context of the run
   * @param model   the model to save
   * @return true if the save was successful, false if an exception occurred
   */
  def saveModel(context: RunContext, model: PipelineModel): Boolean = {
    try {
      model.write.overwrite().save(context.modelDir.toString)
      true
    } catch {
      //Todo: Logging
      case e: Exception => false
    }
  }

  def loadModel(context: RunContext): PipelineModel = {
    PipelineModel.load(context.modelDir.toString)
  }

  /**
   * Saves the run context in runs/{id}/context.yaml
   *
   * @param context the run context to save
   * @return true, if the save was successful, false if an exception occurred
   */
  def saveContext(context: RunContext): Boolean = {
    try {
      val input =
        s"""id: ${context.id}
           |root: ${context.root.toString}
           |configName: ${context.configName}
           |modelDir: ${context.modelDir.toString}
           |evalDir: ${context.evalDir.toString}
           |snapshotsDir: ${context.snapshotsDir.toString}
           |logsDir: ${context.logsDir.toString}""".stripMargin

      Files.write(context.root.resolve(
        s"context.yaml"),
        input.getBytes(StandardCharsets.UTF_8))

      true
    } catch {
      //Todo: Loggins
      case e: Exception => false
    }
  }

  /**
   * Loads the context of a run from the given path
   *
   * @param path the path to the run context
   * @return the RunContext of the run
   */
  def loadContext(path: Path): RunContext = {
    val lines = Files.readAllLines(path, StandardCharsets.UTF_8)
    val contextMap = lines.toArray(new Array[String](lines.size())).map { line =>
      val Array(k, v) = line.split(":", 2)
      k.trim -> v.trim
    }.toMap

    RunContext(
      contextMap("id").toInt,
      Paths.get(contextMap("root")),
      contextMap("configName"),
      Paths.get(contextMap("modelDir")),
      Paths.get(contextMap("evalDir")),
      Paths.get(contextMap("snapshotsDir")),
      Paths.get(contextMap("logsDir"))
    )
  }


  /**
   * Saves a snapshot of the configuration file under runs/{id}/snapshots/{configname}
   *
   * @param context the context of the run
   * @return true if it was successful, false if an exception occurred
   */
  def saveSnapshot(config: Config, context: RunContext): Boolean = {
    try {
      val snapshotPath = context.snapshotsDir
      val configPath = config.paths.conf
      val configName = context.configName

      val source = Paths.get(configPath + configName)
      val destination = snapshotPath.resolve(configName)

      Files.copy(source, destination)

      true
    } catch {
      //Todo Logging
      case e: Exception => false
    }
  }

  /**
   * Saves the metrics as a .yaml file into runs/{id}/metrics/metrics.yaml
   *
   * @param context the run context
   * @param metrics the evaluation metrics to save
   * @return true if the save was successful, false if an exception occurred
   */
  def saveMetrics(context: RunContext, metrics: EvaluationMetrics): Boolean = {
    try {
      val input =
        s"""${metrics.metrics.map { case (metric, value) => s"$metric = $value" }.mkString("\n")}"""

      Files.write(context.evalDir.resolve(
        s"metrics.yaml"),
        input.getBytes(StandardCharsets.UTF_8))

      true
    } catch {
      //Todo: Loggins
      case e: Exception => false
    }
  }
}
