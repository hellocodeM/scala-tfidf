scala-TFIDF（文章关键词提取）

## description
Keywords extraction using TFIDF implemented in Scala, supporting both Chinese and English. 

中英文关键词提取，使用TFIDF算法。

## example
```scala
val content = """在 Windows 平台下写游戏，相比 console 等其它平台，最麻烦之事莫过于让游戏窗口于其它窗口良好的相处。
             即使是全屏模式，其实也还是一个窗口。如果你不去跑窗口的消息循环，一个劲的刷新屏幕，
             我估计要被所有 Windows 用户骂死。那么怎样让你的游戏程序做一个 Windows 下的良好公民呢？
             最简单的方法是用循环用 PeekMessage 来处理 Windows 消息，一旦消息队列为空，就转去跑一帧游戏逻辑，
             这帧逻辑完成后游戏屏幕也被刷新了一帧。主循环代码大概看起来是这样的：for(;;) {
              """"
val keyWords = TFIDF.getKeywords(content, 3)
keyWords.foreach(println)
/*
  (窗口,0.25338828369839717)
  (游戏,0.22950758666561152)
  (一帧,0.21397986067501137)
*/
```

## install
```
// download the jars from my ftp.
wget "ftp://128.199.148.200/pub/resources/tfidf/ansj_seg-2.0.8.jar"
wget "ftp://128.199.148.200/pub/resources/tfidf/nlp-lang-0.3.jar"
wget "ftp://128.199.148.200/pub/resources/tfidf/tfidf_2.10-0.0.2.jar"
// pub these jars under the "lib/" directory

```
## usage
I computed the IDF for you in advance, but which may not fit for your requirements.

So, you'd better compute the IDF based on your own corpus like this.
Notice that, the parameter of constructCorpus is a directory containing a huge number of documents.

```scala
TFIDF.constructCorpus("/your/own/corpus/")
// the above command will produce a file called "custome-idf.cache", which is the serialized data of IDF.
val keywords = TFIDF.getKeywords(content, 5);
```

## requirements
[ansj_seg](https://github.com/NLPchina/ansj_seg)
