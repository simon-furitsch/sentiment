package net.furitsch.sentiment
package data

import config.Config
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.classic.SparkSession

object DataLoader {
  /**
   * Loads a dataset, defined in the configuration file
   * @param config the config of the run
   * @param spark the spark context
   * @return a Dataframe containing the data
   */
  def loadDataset(config:Config, spark: SparkSession): DataFrame = {
    val dataPath = config.paths.data
    val fileName = config.dataset.fileName
    val fileExtension = config.dataset.fileExtension
    val path = s"$dataPath/$fileName.$fileExtension"
    val df = spark.read
      .option("header", config.dataset.hasHeader.toString)
      .csv(path)
    if (!config.dataset.hasHeader)
      df.toDF("target", "id", "date", "flag", "user", "text")
    else df
  }
}
