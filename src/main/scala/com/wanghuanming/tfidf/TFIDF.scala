package com.wanghuanming.tfidf

import java.io._
import scala.io.Source

import org.ansj.splitWord.analysis.ToAnalysis


/**
  * Keywords extracting using TFIDF algorithm.
  * author: HelloCode
  * email: huanmingwong@163.com
  */

object TFIDF {
  val stopwords = List("/engStopwords.txt", "/zhStopwords.txt").flatMap { file =>
    Source.fromInputStream(getClass.getResourceAsStream(file)).getLines().map(_.trim)
  }
  final val idfPath = "/idf.cache"
  final val customIDFPath = "idf.cache"
  final val defaultIDF = Math.PI

  /**
    * @param content: the article to be extracted.
    * @param topN: how many keywords to be extracted.
    * @return : list of keywords
    */

  def getKeywords(content: String, topN: Int): List[(String, Double)] = {
    val terms = segment(content)
    val tf = TF(filterTrivialWord(terms))
    val idf = IDF()
    val tfidf = tf.map { case (word, freq) =>
      word -> freq * idf.getOrElse(word, defaultIDF)
    }.toList
    tfidf.sortBy(_._2).reverse.take(topN)
  }

  /**
    * Construct your own IDF with a corpus.
    * @param corpusPath the corpusPath must be a directory containing a huge number of documents.
    */
  def constructCorpus(corpusPath: String) = {
    assert(new File(corpusPath).isDirectory)
    val files = new File(corpusPath).listFiles
    val fileCount = files.size

    val corpus = files.flatMap { file =>
      segment(Source.fromFile(file).mkString).distinct
    }.groupBy(x => x)
      .map { case (word, list) => word -> Math.log(fileCount * 1.0 / list.length + 1) }

    // serialize the corpus
    val writer = new ObjectOutputStream(new FileOutputStream(new File(customIDFPath)))
    writer.writeObject(corpus)
    writer.close()
  }

  private def TF(article: List[String]) = {
    val sum = article.length
    // word count
    article.groupBy(x => x).map { case (word, list) =>
      word -> list.length.toDouble / sum
    }
  }

  private def IDF(): Map[String, Double] = {
    val cacheIS = {
      if (new File(customIDFPath).exists)
        new FileInputStream(new File(customIDFPath))
      else
        getClass.getResourceAsStream(idfPath)
    }
    // deserialize
    val reader = new ObjectInputStream(cacheIS)
    val corpus = reader.readObject.asInstanceOf[Map[String, Double]]
    reader.close()
    corpus
  }

  /**
    * Not a short symbol which length less that 2, not a stopword, not a number.
    * @param terms list of words
    * @return
    */
  private def filterTrivialWord(terms: List[String]) = {
    terms.filter { word =>
      word.length >= 2 && !stopwords.contains(word) && !isNumber(word)
    }
  }

  private def isNumber(term: String): Boolean = {
    term.forall { x =>
      ('0' <= x && x <= '9') || x == '.'
    }
  }

  /**
    * article segmentation method for ansj_seg library
    * @param content the article to be segmented.
    * @return terms segmented.
    */

  private def segment(content: String): List[String] = {
    ToAnalysis.parse(content)
      .toArray
      .map(_.toString.split("/"))
      .filter(_.length >= 2)
      .map(_ (0))
      .toList
  }
}
