package net.furitsch.sentiment
package config

import com.typesafe.config.ConfigFactory
import java.io.File

/**
 * Used to load the default configs
 */
object ConfigLoader {
  private val config = ConfigFactory.parseFile(new File("conf/default.conf")).resolve()

  object paths {
    val data:String = config.getString("paths.data")
    val logs:String = config.getString("paths.logs")
    val runs:String = config.getString("paths.runs")
  }

  object dataset {
    val fileName:String = config.getString("dataset.fileName")
    val fileExtension:String = config.getString("dataset.fileExtension")
    val hasHeader:Boolean = config.getBoolean("dataset.hasHeader")
    val labelColumn:String = config.getString("dataset.labelColumn")
  }

  object model {
    val algorithm:String = config.getString("model.algorithm")
    val threshold:Double = config.getDouble("model.threshold")
    val iterations:Long = config.getLong("model.iterations")
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
    val useCountVectorizer:Boolean = config.getBoolean("features.useCountVectorizer")
    val useTFIDF:Boolean = config.getBoolean("features.useTFIDF")
    val useWord2Vec:Boolean = config.getBoolean("features.useWord2Vec")
  }
}