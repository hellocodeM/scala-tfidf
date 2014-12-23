package com.wanghuanming

import scala.io.Source

object test {

  def main(args: Array[String]) = {
    val content = Source.fromURL("http://wanghuanming.com/2014/12/thread-and-process").getLines.reduce(_ + _)
    				.replaceAll("<script.*?script>", "").replaceAll("<[^>]*>", "")
    val article = """学习kernel的时候，听说这么一个概念：在Linux中没有线程，所谓的线程只是特殊的进程，由于UNIX中进程是比较轻量级的，所以线程只是共享进程空间以及一些其他资源的特殊进程而已。觉得神奇，但又无迹可寻。今天使用htop的时候，偶然发现了一个佐证。</p>
<h2 id="htop">htop</h2>
<p>htop是任务管理工具，可以看到系统中运行的所有进程，比如说，我电脑上看到的：</p>
<p><img src="http://huanming-blog.qiniudn.com/thread-and-process.png" alt=""></p>
<p>可以看到，它以树形结构列出了所有进程。</p>
<h2 id="多进程？">多进程？</h2>
<p>可以看到，有许多mongod的“进程”，它们的pid不一样，而且还有父子关系，乍一看，还真像是多进程的。但是注意到一点，<strong>它们占有的资源是完全一样的</strong>，包括内存，CPU。</p>
<p>在多进程的情况下，怎么会发生这种事情呢，难道它们做的事情完全一致？</p>
<p>据我推测，它们不过是多个对等线程而已，而线程又是使用相同的进程地址空间的，所以使用的资源是一样的。但由于Linux的特殊性，它们作为独立的进程被调度，造成了我们看到的不同的PID的情况。</p>
<p>比较好奇的是，搜狗输入法为什么要这么多线程。""".replaceAll("<script.*?script>", "").replaceAll("<[^>]*>", "")
    val keywords = TFIDF.getKeywords(content)
    println(article)
    TFIDF.getKeywords(article).foreach(println)
  }
}