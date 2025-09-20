package net.furitsch.sentiment
package run

import java.nio.file.Path

case class RunContext(
                       id: Int,
                       root: Path,
                       configName: String,
                       modelDir: Path,
                       evalDir: Path,
                       snapshotsDir: Path,
                       logsDir: Path,
                     )
