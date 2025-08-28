package net.furitsch.sentiment
package cli

/**
 *  Command Line Interface
 *
 *  Used to identify the command, and extract its params.
 *  Then delegates to the corresponding class
 *  Example: sbt "runMain net.furitsch.sentiment.App download --out=data/sentiment140.csv"
 */
object Cli {
  def run(args: Array[String]): Unit = {
    args.toList match {
      case "download" :: params => println(s"Cmd: DOWNLOAD, Params: $params")
      case "eda" :: params => println(s"Cmd: EDA, Params: $params")
      case "train" :: params => println(s"Cmd: TRAIN, Params: $params")
      case "eval" :: params => println(s"Cmd: EVAL, Params: $params")
      case Nil => println(s"Usage: download|eda|train|eval --params")
      case _ => println("Unknown Command")
    }
  }
}
