package net.furitsch.sentiment
package utils

import config.ConfigLoader
import org.apache.spark.sql.Dataset

object DatasetSplitter {

  private val splitWeights = Array(
    ConfigLoader.split.train,
    ConfigLoader.split.test,
    ConfigLoader.split.validate)

  /**
   * Splits a Dataset into train, validation and test sets
   * Seed & weights set up in the configuration file
   * @param dataSet dataset to split
   * @tparam T Type of dataset entries
   * @return Array(train,valid,test)
   * */
  def split[T](dataSet: Dataset[T]):Array[Dataset[T]] = {
    dataSet.randomSplit(splitWeights,ConfigLoader.split.seed)
  }
}
