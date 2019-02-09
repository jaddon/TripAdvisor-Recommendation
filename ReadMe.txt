1.22：
在主方法中实现了从“src/main/resource/original”对文件的读取，修改后存入另一个文件夹“src/main/resources/modified”，将除了总体的location，name，rating以及各个顾客的评论内容以外的信息全部去除。
一些比较特殊的文件例子放在“special hotel”文档中。
1.30：
在主方法中实现了对已经Modified的文档的进一步处理，textss的每一个元素为texts，每个texts对应于一个文件，texts的每个元素对应该文件中的每一段评论。
1.31：
实现对所有文档的所有term的tfidf计算：
运行前需将word_index文件放置在resources/term文件夹下！
tfIdfForAll的每一个元素对应于单个文件的所有段落对全部term的tfidf值，其中tfidf的每个数字对应于当前文件的当前段落对当前term的tfidf数值。
2.8：
使用修改过的VectorWritter可以获取适合svm的数据集。WingOfVictory及WingOfVictoryTest分别为从该数据集中小部分截取的训练和测试用数据集，这两个文件放在dkpro-tc-ml-libsvm\src\main\resources的文件夹下即可，然后将SvmPipeline放在dkpro-tc-ml-libsvm\src\main\java\org\dkpro\tc\ml\libsvm文件夹下（和TestTask一起）。注意这里使用的dkpro为0.9版本。

TODO: 
1.清除含有乱码/非英文的评论+清除过短的评论；
2.将读取和修改文件模块整个移动到Reader文件里（等待确定功能没有问题后再refactor）
