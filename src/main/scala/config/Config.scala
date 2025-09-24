package net.furitsch.sentiment
package config

import com.typesafe.config.ConfigFactory
import java.io.File

/**
 * Configuration of a run
 */
class Config(path:String) {
  private val config = ConfigFactory.parseFile(new File(path)).resolve()

  object paths {
    val data:String = config.getString("paths.data")
    val logs:String = config.getString("paths.logs")
    val runs:String = config.getString("paths.runs")
    val conf:String = config.getString("paths.config")
    val configName:String = config.getString("paths.configName")
  }

  object dataset {
    val fileName:String = config.getString("dataset.fileName")
    val fileExtension:String = config.getString("dataset.fileExtension")
    val hasHeader:Boolean = config.getBoolean("dataset.hasHeader")
    val labelColumn:String = config.getString("dataset.labelColumn")
    val binaryLabelColumn:String = config.getString("dataset.binaryLabelColumn")
  }

  object model {
    val algorithm:String = config.getString("model.algorithm")
    val threshold:Double = config.getDouble("model.threshold")
    val iterations:Int = config.getInt("model.iterations")
    val regParam:Double = config.getDouble("model.regParam")
  }

  object split {
    val train:Double = config.getDouble("split.train")
    val test:Double = config.getDouble("split.test")
    val validate:Double = config.getDouble("split.validate")
    val seed:Long = config.getLong("split.seed")
  }

  object features {
    val useNGram:Boolean = config.getBoolean("features.useNGram")
    val nGramSize:Int = config.getInt("features.nGramSize")
    val vectorizer:String = config.getString("features.vectorizer")
    val useIDF:Boolean = config.getBoolean("features.useIDF")
  }

  object evaluation {
    val useAccuracy:Boolean = config.getBoolean("evaluation.useAccuracy")
    val useF1:Boolean = config.getBoolean("evaluation.useF1")
    val useAreaUnderROC:Boolean = config.getBoolean("evaluation.useAreaUnderROC")
  }
}