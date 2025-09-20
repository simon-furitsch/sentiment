package net.furitsch.sentiment
package run

import config.ConfigLoader

import net.furitsch.sentiment.filesystem.DirectoryManager

import java.nio.file.{Files, Path, Paths}

object RunManager {

  /**
   * TODO: Race Condition if parallelized
   * Initializes a new run.
   * Computes a new run ID
   * Creates a RunContext for the file system to create the corresponding directories
   * @return RunContext with File paths and id
   */
  def initNewRun():RunContext = {
    val path:Path = Paths.get(ConfigLoader.paths.runs)
    val maxID = getMaxRunId(DirectoryManager.getRunDirectories(path))
    val newID = computeNextRunId(maxID)
    val runRoot = path.resolve(newID.toString)
    RunContext(
      newID,
      runRoot,
      ConfigLoader.paths.configName,
      runRoot.resolve("model"),
      runRoot.resolve("evaluation"),
      runRoot.resolve("snapshots"),
      runRoot.resolve("logs"))
  }

  def resumeRun(id:Int):Unit = {
    
  }



  /**
   * Gets the highest run ID
   * @param folders Directory names
   * @return Highest run ID
   */
  def getMaxRunId(folders: List[String]) : Int = {
    val ids = folders.flatMap(_.toIntOption)
    if (ids.isEmpty) 0 else ids.max
  }

  /**
   * Gets a new run ID by incrementing the highest run ID by 1
   * @param maxRun highest existing run ID
   * @return next run ID
   */
  def computeNextRunId(maxRun:Int):Int ={
    maxRun+1
  }
}
