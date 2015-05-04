package com.wanghuanming.TFIDF

import java.io.{File, PrintWriter}

import org.ansj.splitWord.analysis.ToAnalysis

import scala.io.Source

/**
 * Keywords extracting using TFIDF algorithm.
 * author: HelloCode
 * email: huanmingwong@163.com
 */

object TFIDF {
  val pathPrefix = "src/main/resources/"
  val stopwords = List("engStopwords.txt", "zhStopwords.txt")
    .flatMap(file => Source.fromFile(pathPrefix + file)
    .getLines.map(_.trim))
  var defaultIDF = 0.0

  /**
   * @param content: the article to be extracted.
   * @param topN: how many keywords to be extracted.
   * @param corpus: the path of corpus
   * @return : list of keywords
   */

  def getKeywords(content: String, topN: Int = 10, corpus: String) = {
    val terms = segment(content)
    val tf = TF(filterStopwords(terms))
    val idf = IDF(corpus)
    val tfidf = tf.map { case (word, freq) =>
      word -> freq * idf.getOrElse(word, defaultIDF)
    }.toList
    tfidf.sortBy(_._2).reverse.take(topN)
  }

  private def filterStopwords(terms: List[String]) = {
    terms.filter { word =>
      (word.length >= 2 && !stopwords.contains(word))
    }
  }

  private def segment(content: String) = {
    ToAnalysis.parse(content)
      .toArray
      .map(_.toString.split("/")(0))
      .filter(_.length >= 1)
      .toList
  }

  private def IDF(corpus: String): Map[String, Double] = {
    loadCorpus(corpus)
  }

  private def TF(article: List[String]) = {
    val sum = article.length
    // word count
    article.groupBy(x => x).map { case (word, list) =>
      word -> list.length.toDouble / sum
    }
  }

  private def loadCorpus(corpus: String) = {
    if (Cache.exists(corpus)) {
      Cache.read(corpus).split("\n")
        .map(_.split("\\s+"))
        .filter(_.length == 2)
        .map { item => item(0) -> item(1).toDouble }
        .toMap
    } else {
      constructCorpus(corpus)
    }
  }

  private def constructCorpus(corpus: String) = {
    val files = (new File(corpus)).listFiles
    val fileCount = files.size
    val writer = new PrintWriter(corpus)
    defaultIDF = Math.log(fileCount)
    val cor = files.map { file =>
      segment(Source.fromFile(file).mkString) .distinct
    }.flatten
      .groupBy(x => x)
      .map { case(word, list) => word -> Math.log(fileCount * 1.0 / list.length+1) }

    cor.foreach { case(word, freq) => writer.println(word + " " + freq)}

    writer.close
    cor
  }



  object Cache {
    val prefix = "src/main/resources/"
    val postfix = ".cache"
    
    private def constructPath(path: String) = prefix + path + postfix

    def exists(path: String) = (new File(constructPath(path))).exists

    def read(path: String) = Source.fromFile(constructPath(path)).mkString

    def write(path: String, content: String) = {
      val writer = new PrintWriter(constructPath(path))
      writer.println(content)
      writer.close
    }
  }
}
