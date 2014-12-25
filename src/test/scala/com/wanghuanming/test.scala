package com.wanghuanming

import scala.io.Source
import com.wanghuanming

object test {
  def main(args: Array[String]) = {
    val URLs = List("http://wanghuanming.com/2014/12/thread-and-process", 
                    "http://wanghuanming.com/2014/12/mesos-deploy",
                    "http://www.cnblogs.com/jasonkoo/articles/2834727.html")
    URLs.foreach{ url =>
      println(url)
      val content = Source.fromURL(url).getLines.reduce(_ + _)
      val keywords = TFIDF.getKeywords(stripTags(content), 5, "/tmp/shakespear")
      keywords.foreach(println)
    }
  }
  
  def stripTags(article: String) = {
    article.replaceAll("<script.*?script>", "").replaceAll("<[^>]*?>", "")
  }
}
