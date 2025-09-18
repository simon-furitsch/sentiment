package net.furitsch.sentiment
package data

import config.ConfigLoader

import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.classic.SparkSession

object DataLoader {
  private val dataPath = ConfigLoader.paths.data
  private val fileName = ConfigLoader.dataset.fileName
  private val fileExtension = ConfigLoader.dataset.fileExtension
  private val path = s"$dataPath/$fileName.$fileExtension"

  def loadDataset(spark:SparkSession):DataFrame = {
    import spark.implicits._
    spark.read
      .option("header",ConfigLoader.dataset.hasHeader.toString)
      .csv(path)
  }
}
