package net.furitsch.sentiment
import cli.Cli

import org.apache.spark.sql.classic.SparkSession

object App {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Twitter Sentiment Analyse")
      .master("local[*]")
      .getOrCreate()
    Cli.run(spark,args)
  }
}
