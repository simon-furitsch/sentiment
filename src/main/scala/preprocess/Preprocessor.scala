package net.furitsch.sentiment
package preprocess

import config.ConfigLoader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, when}

object Preprocessor {
  /**
   * Adds a binary label column to the dataset
   * If the target was negative, it maps it to 1
   * If the target is anything else, it maps it to 0
   * @param df whole dataframe
   * @return dataframe with binary label
   */
  def toBinary(df: DataFrame): DataFrame = {
    df.withColumn(
      ConfigLoader.dataset.binaryLabelColumn,
      when(col("target").cast("int")===0,1).otherwise(0))
  }

}
