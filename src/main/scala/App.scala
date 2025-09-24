package net.furitsch.sentiment
import cli.Cli

import org.apache.spark.sql.classic.SparkSession

object App {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Twitter Sentiment Analyse")
      .master("local[*]")
      .config("spark.driver.extraJavaOptions", "--add-opens=java.base/java.nio=ALL-UNNAMED")
      .config("spark.executor.extraJavaOptions",
        "--add-opens=java.base/java.nio=ALL-UNNAMED " +
          "--add-opens=java.base/java.lang.invoke=ALL-UNNAMED " +
          "--add-opens=java.base/java.lang=ALL-UNNAMED " +
          "--add-opens=java.base/java.util=ALL-UNNAMED")
      .getOrCreate()
    Cli.run(spark,args)
  }
}
