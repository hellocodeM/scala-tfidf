package com.wanghuanming

object test {

  def main(args: Array[String]) = {
    val content = "hello world shit fuck cao chinese nobody main"
    val keywords = TFIDF.getKeywords(content)
    keywords.foreach(println)
  }
}