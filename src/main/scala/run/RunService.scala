package net.furitsch.sentiment
package run

import data.DataLoader
import filesystem.DirectoryManager
import preprocess.Preprocessor
import train.TrainService
import utils.DatasetSplitter
import org.apache.spark.sql.classic.SparkSession

object RunService {
  /**
   * Creates a run context and the directories,
   * Makes the config persistent in snapshots/configname
   * splits the dataset and binarized the labels,
   * trains a model and makes it persistent in runid/model
   * evaluates the model and makes the metrics persistent
   * @param spark spark session
   */
  def fullRun(spark:SparkSession):Unit = {
    val context = RunManager.initNewRun()
    DirectoryManager.createDirectories(context)
    val dataset = DataLoader.loadDataset(spark)
    val binarizedDataset = Preprocessor.toBinary(dataset)
    val Array(trainSet,testSet,validateSet) = DatasetSplitter.split(binarizedDataset)
    val model = TrainService.train(trainSet)
    //Todo: Eval, Persistence
  }

}
