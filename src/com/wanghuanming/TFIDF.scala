package com.wanghuanming

import java.io.File

import scala.Array.canBuildFrom
import scala.io.Source

import org.ansj.splitWord.analysis.BaseAnalysis

object TFIDF {
  val engStopwords = Source.fromFile("engStopwords.txt").getLines.map(_.trim).toList
  val zhStopwords = Source.fromFile("zhStopwords.txt").getLines.map(_.trim).toList
  val stopwords = engStopwords.union(zhStopwords)

  def main(args: Array[String]) = {
    val content = "hello world shit fuck cao chinese nobody main"
    val keywords = getKeywords(content)
    keywords.foreach(println)
  }

  def getKeywords(content: String, topN: Int = 10) = {
    val allWords = BaseAnalysis.parse(content).toArray.map(_.toString.split("/")(0)).toList
    val filtered = allWords.filter { word =>
      word.length >= 2 && !stopwords.contains(word)
    }
    val tf = TF(filtered)
    val idf = IDF("/tmp/shakespear")
    val tfidf = tf.map { item =>
      val word = item._1
      val freq = item._2
      word -> freq * idf.get(word).getOrElse(0.0)
    }.toList
    tfidf.sortBy(_._2).take(topN)
  }

  def IDF(dir: String) = {
    val files = new File(dir).listFiles
    val fileNum = files.length
    val fre = files.map { file =>
      BaseAnalysis.parse(Source.fromFile(file).getLines.reduce(_ + _))
        .toArray.map(_.toString.split("/")(0)).toList.distinct
    }
    fre.flatten.map(_ -> 1).groupBy(_._1).map { item => item._1 -> Math.log(item._2.map(_._2).reduce(_ + _) * 1.0 / (fileNum + 1)) }
  }

  def TF(article: List[String]) = {
    val sum = article.length
    article.map(_ -> 1).groupBy(_._1).map { item =>
      val word = item._1
      val list = item._2
      word -> list.map(_._2).reduce(_ + _) * 1.0 / sum
    }
  }
}
