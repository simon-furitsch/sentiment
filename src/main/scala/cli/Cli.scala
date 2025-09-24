package net.furitsch.sentiment
package cli

import filesystem.DirectoryManager
import run.{RunManager, RunService}

import org.apache.spark.sql.classic.SparkSession

import java.nio.file.Paths

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
        val configName = parameters.getOrElse("config","default.conf")
        command match {
          case "init-run" => RunService.initRun(configName)
          case "full-run" => RunService.fullRun(configName,spark)
          //TODO: case "download" => println(s"Cmd: DOWNLOAD, Params: $parameters")
          //TODO: case "eda" => println(s"Cmd: EDA, Params: $parameters")
          case "train" => val runID = parameters
            .getOrElse("id",throw new IllegalArgumentException("No RunID set! Usage: train --id=\"id\""))
            .toInt

            RunService.continueWithTraining(runID,spark)
          case "eval" => val runID = parameters
            .getOrElse("id",throw new IllegalArgumentException("No RunID set! Usage: eval --id=\"id\""))
            .toInt

            RunService.continueWithEvaluation(runID,spark)
          case _ => println("Unknown Command")
        }
      case Nil => println(s"Usage: init-run|full-run|train|eval --params")
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
