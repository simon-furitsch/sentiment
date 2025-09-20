package net.furitsch.sentiment
package filesystem

import run.RunContext
import scala.jdk.CollectionConverters._

import java.nio.file.{Files, Path}

object DirectoryManager {

  def createDirectories(runContext: RunContext): Unit = {
    Files.createDirectories(runContext.root)
    Files.createDirectories(runContext.evalDir)
    Files.createDirectories(runContext.modelDir)
    Files.createDirectories(runContext.logsDir)
    Files.createDirectories(runContext.snapshotsDir)
  }

  /**
   * Gets the folder names within the runs folder as a List of Strings
   * The path is set up within the run config File
   * (!)Assumes that there are no external files present within the directories
   * @param path Path to the run folder
   * @return All directory names within the runs folder
   */
  def getRunDirectories(path: Path): List[String] = {
    val stream = Files.list(path)
    try {
      stream
        .iterator()
        .asScala
        .map(_.getFileName.toString)
        .toList
    } finally {
      stream.close()
    }
  }

}
