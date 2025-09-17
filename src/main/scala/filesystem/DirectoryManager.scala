package net.furitsch.sentiment
package filesystem

import run.RunContext

import java.nio.file.Files

object DirectoryManager {

  def createDirectories(runContext: RunContext): Unit = {
    Files.createDirectories(runContext.root)
    Files.createDirectories(runContext.evalDir)
    Files.createDirectories(runContext.modelDir)
    Files.createDirectories(runContext.logsDir)
    Files.createDirectories(runContext.snapshotsDir)
  }

}
