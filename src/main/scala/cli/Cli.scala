package net.furitsch.sentiment
package cli

import filesystem.DirectoryManager
import run.{RunManager, RunService}

import org.apache.spark.sql.classic.SparkSession

/**
 *  Command Line Interface
 *
 *  Used to identify the command, and extract its params.
 *  Then delegates to the corresponding class
 *  Example: sbt "runMain net.furitsch.sentiment.App download --out=data/sentiment140.csv"
 */
object Cli {
  def run(spark:SparkSession, args: Array[String]): Unit = {
    args.toList match {
      case command :: params =>
        val parameters = splitArgs(params)
        command match {
          case "init-run" => {
            val context = RunManager.initNewRun()
            DirectoryManager.createDirectories(context)
          }
          case "full-run" => RunService.fullRun(spark)
          case "download" => println(s"Cmd: DOWNLOAD, Params: $parameters")
          case "eda" => println(s"Cmd: EDA, Params: $parameters")
          case "train" => println(s"Cmd: TRAIN, Params: $parameters")
          case "eval" => println(s"Cmd: EVAL, Params: $parameters")
          case _ => println("Unknown Command")
        }
      case Nil => println(s"Usage: download|eda|train|eval --params")
    }
  }

  /**
   * Splits the command parameters and builds a Map [param,value]
   * @param params List of parameters
   * @return Map[param,value]
   */
  def splitArgs(params: List[String]): Map[String,String] = {
    if(params.isEmpty) Map.empty[String,String]
    else params.
      filter(_.startsWith("--"))
      .map(_.stripPrefix("--"))
      .map(_.split("="))
      .map{case Array(k,v) => k -> v}
      .toMap
  }
}
