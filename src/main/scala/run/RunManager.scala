package net.furitsch.sentiment
package run

import config.Config

import net.furitsch.sentiment.filesystem.{DirectoryManager, PersistenceManager}

import java.nio.file.{Files, Path, Paths}

object RunManager {

  /**
   * (!)Race Condition if parallelized
   * Initializes a new run.
   * Generates a new run ID
   * Creates a RunContext for the file system to create the corresponding directories
   * @return a run context containing file paths and the run id
   */
  def initNewRun(config:Config):RunContext = {
    val path:Path = Paths.get(config.paths.runs)
    val maxID = getMaxRunId(DirectoryManager.getRunDirectories(path))
    val newID = computeNextRunId(maxID)
    val runRoot = path.resolve(newID.toString)
    RunContext(
      newID,
      runRoot,
      config.paths.configName,
      runRoot.resolve("model"),
      runRoot.resolve("evaluation"),
      runRoot.resolve("snapshots"),
      runRoot.resolve("logs"))
  }

  /**
   * Resumes a run saved inside the runs folder
   * @param id the id of the run to load
   * @return the run context of the run
   */
  def resumeRun(id:Int):RunContext = {
    PersistenceManager.loadContext(Paths.get(s"runs/$id/context.yaml"))
  }

  /**
   * Gets the highest run ID
   * @param folders directory names
   * @return the highest run ID
   */
  def getMaxRunId(folders: List[String]) : Int = {
    val ids = folders.flatMap(_.toIntOption)
    if (ids.isEmpty) 0 else ids.max
  }

  /**
   * Gets a new run ID by incrementing the highest run ID by 1
   * @param maxRun the highest existing run ID
   * @return the next run ID
   */
  def computeNextRunId(maxRun:Int):Int ={
    maxRun+1
  }
}
