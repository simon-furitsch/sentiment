package net.furitsch.sentiment
package data
import run.RunContext
import org.knowm.xchart.{BitmapEncoder, XYChartBuilder}
import org.knowm.xchart.BitmapEncoder.BitmapFormat

object PlotManager {
  /**
   * Saves a PNG Image of the ROC curve plot into the evaluate folder defined in the configuration file
   * @param context the context of the run
   * @param fpr false positive rate
   * @param tpr true positive rate
   */
  def createRocPlot(context:RunContext, fpr: Array[Double],tpr: Array[Double]):Unit = {
    val chart = new XYChartBuilder()
      .width(750).height(540)
      .title("ROC Kurve")
      .xAxisTitle("False Positive Rate")
      .yAxisTitle("True Positive Rate")
      .build()

    chart.addSeries("ROC", fpr, tpr)
    chart.addSeries("Baseline", Array(0.0, 1.0), Array(0.0, 1.0))

    val outPath = context.evalDir.resolve("roc.png").toString

    BitmapEncoder.saveBitmap(chart, outPath, BitmapFormat.PNG)
  }

}
