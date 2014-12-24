package com.wanghuanming

import java.io.File
import java.io.PrintWriter

import scala.Array.canBuildFrom
import scala.io.Source

import org.ansj.splitWord.analysis.BaseAnalysis

object TFIDF {
  val engStopwords = Source.fromFile("resource/engStopwords.txt").getLines.map(_.trim).toList
  val zhStopwords = Source.fromFile("resource/zhStopwords.txt").getLines.map(_.trim).toList
  val stopwords = engStopwords.union(zhStopwords)

  def getKeywords(content: String, topN: Int = 10) = {
    val allWords = BaseAnalysis.parse(content).toArray.map(_.toString.split("/")).filter(_.length >= 1).map(_(0)).toList
    val tf = TF(allWords.filter { word => word.length >= 2 && !stopwords.contains(word) })
    val idf = IDF("/tmp/shakespear")
    val tfidf = tf.map { item =>
      val word = item._1
      val freq = item._2
      word -> freq * idf.getOrElse(word, 0.0)
    }.toList
    tfidf.sortBy(_._2).reverse.take(topN)
  }

  def IDF(dir: String): Map[String, Double] = {
    if (new File("resource/IDF.cache").exists) {
      return Source.fromFile("resource/IDF.cache").getLines().map(_.split(" ")).filter(_.length == 2)
    		  	.map { item => item(0) -> item(1).toDouble }.toMap
    }
    val files = new File(dir).listFiles
    val fileNum = files.length
    val fre = files.map { file =>
      BaseAnalysis.parse(Source.fromFile(file).getLines.reduce(_ + _))
        .toArray.map(_.toString.split("/")(0)).toList.distinct
    }
    val res = fre.flatten.map(_ -> 1).groupBy(_._1).map { item => item._1 -> Math.log(fileNum * 1.0 / item._2.length + 1)) }
    val writer = new PrintWriter("resource/IDF.cache")
    res.foreach { item => writer.println(item._1 + " " + item._2) }
    writer.close
    return res
  }

  def TF(article: List[String]) = {
    val sum = article.length
    article.map(_ -> 1).groupBy(_._1).map { item =>
      item._1 -> item._2.length * 1.0 / sum
    }
  }
}
