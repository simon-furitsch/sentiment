package net.furitsch.sentiment
package filesystem

import config.ConfigLoader
import run.RunContext
import org.apache.spark.ml.PipelineModel

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

object PersistenceManager {
  /**
   * Saves the model in runs/{id}/model
   * @param context Context of the Run
   * @param model Model to save
   * @return true if the save worked, false if an exception occurred
   */
  def saveModel(context:RunContext, model: PipelineModel): Boolean = {
    try{
      model.write.overwrite().save(context.modelDir.toString)
      true
    } catch {
      //Todo: Logging
      case e: Exception => false
    }
  }

  /**
   * Saves the run context in runs/context.yaml
   * @param context RunContext to save
   * @return true, if it worked, false if an exception occurred
   */
  def saveContext(context:RunContext):Boolean = {
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
   * Saves a snapshot of the configuration file under runs/{id}/snapshots/{configname}
   * @param context the context of the run
   * @return true if it worked, false if an exception occurred
   */
  def saveSnapshot(context:RunContext): Boolean ={
    try {
      val snapshotPath = context.snapshotsDir
      val configPath = ConfigLoader.paths.conf
      val configName = context.configName

      val source = Paths.get(configPath+configName)
      val destination = snapshotPath.resolve(configName)

      Files.copy(source,destination)

      true
    } catch {
      //Todo Logging
      case e:Exception => false
    }
  }

}
