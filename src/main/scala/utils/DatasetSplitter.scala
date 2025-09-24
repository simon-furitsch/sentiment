package net.furitsch.sentiment
package utils

import config.Config

import net.furitsch.sentiment.config
import org.apache.spark.sql.{DataFrame, Dataset}

object DatasetSplitter {

  /**
   * Splits a data frame into train, validation and test sets
   * The seed & weights are defined in the configuration file
   * @param df the dataframe to split
   * @return an array containing train, validation and test splits
   * */
  def split(config:Config, df: DataFrame):Array[DataFrame] = {
    val splitWeights = Array(
      config.split.train,
      config.split.test,
      config.split.validate)
    df.randomSplit(splitWeights,config.split.seed)
  }
}
