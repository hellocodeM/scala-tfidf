scala-TFIDF

(中英文关键词提取，使用TFIDF算法）

## description
Keywords extraction using TFIDF implemented in Scala, supporting both Chinese and English. 

## example
```scala
// article to be extracted
val content = Source.io.fromURL("http::/wanghuanming.com/2014/12/thread-and-process").getLines.reduce(_ + _)
              .replaceAll("<script.*?script>", "").replaceAll("<[^>]*>", "")
// param: content, topN, corpus
val keyWords = TFIDF.getKeywords(content, 5, "/tmp/shakespear")
keyWords.foreach(println)
```

## usage
First, you need to download the code, I don't provide a jar since the program is really tiny.

Then, you have to download the [ansj_seg](https://github.com/NLPchina/ansj_seg) as dependency, if you are a Chinese user. If you are using English, you could change it to your own splitting method, it's really easy.

To start the program, you need to provide a directory of corpus in which there are thousands of articles. Of course, you need to choose Chinese corpus if you want to extract Chinese keywords or you will get frustrating results. 

If you want to process a web page, HTML tags like <script>, <span> must be stripped. In test example, I have written the function so you could use it directly.

To speed up the IDF, I cache the results in the directory, so if you want to change to corpus, you have to delete the cache "IDF.cache".

## requirements
[ansj_seg](https://github.com/NLPchina/ansj_seg)
