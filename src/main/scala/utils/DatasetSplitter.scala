package net.furitsch.sentiment
package utils

import config.ConfigLoader

import org.apache.spark.sql.{DataFrame, Dataset}

object DatasetSplitter {

  private val splitWeights = Array(
    ConfigLoader.split.train,
    ConfigLoader.split.test,
    ConfigLoader.split.validate)

  /**
   * Splits a Dataset into train, validation and test sets
   * Seed & weights set up in the configuration file
   * @param df dataframe to split
   * @return Array(train,test,valid)
   * */
  def split(df: DataFrame):Array[DataFrame] = {
    df.randomSplit(splitWeights,ConfigLoader.split.seed)
  }
}
