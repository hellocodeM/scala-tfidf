scala-TFIDF（文章关键词提取）

## description
Keywords extraction using TFIDF implemented in Scala, supporting both Chinese and English. 

中英文关键词提取，使用TFIDF算法。

## example
```scala
val content = """《偶然》
                  徐志摩
                  我是天空里的一片云，偶尔投影在你的波心,
                  你不必压抑，
                  更无须欢喜，
                  在转瞬间消灭了踪影。
                  你我相逢在黑夜的海上，
                  你有你的，我有我的方向；
                  你记得也好，
                  最好你忘掉，
                  在这交会时互放的光亮！
                  """
TFIDF.getKeywords(content, 2).foreach(println)
/*
    (交会,0.4707520694552998)
    (光亮,0.4707520694552998)
*/
```

## install
You could download it from the [release](https://github.com/HelloCodeMing/scala-tfidf/releases/) link directly, or from my ftp:
```
// download the jars from my ftp.
wget "ftp://128.199.148.200/pub/resources/tfidf/ansj_seg-2.0.8.jar"
wget "ftp://128.199.148.200/pub/resources/tfidf/nlp-lang-0.3.jar"
wget "ftp://128.199.148.200/pub/resources/tfidf/tfidf_2.10-0.0.2.jar"
// pub these jars under the "lib/" directory

```
## usage
I have computed the IDF for you in advance, but which may not fit for your requirements.

So, you'd better compute the IDF based on your own corpus like this.
Notice that, the parameter of constructCorpus is a directory containing a huge number of documents.

```scala
TFIDF.constructCorpus("/your/own/corpus/")
// the above command will produce a file called "custome-idf.cache", which is the serialized data of IDF.
val keywords = TFIDF.getKeywords(content, 5);
```

## requirements
[ansj_seg](https://github.com/NLPchina/ansj_seg)
