1.22：
在主方法中实现了从“src/main/resource/original”对文件的读取，修改后存入另一个文件夹“src/main/resources/modified”，将除了总体的location，name，rating以及各个顾客的评论内容以外的信息全部去除。
一些比较特殊的文件例子放在“special hotel”文档中。
1.33：
在主方法中实现了对已经Modified的文档的进一步处理，textss的每一个元素为texts，每个texts对应于一个文件，texts的每个元素对应该文件中的每一段评论。

TODO: 
1.清除含有乱码/非英文的评论+清除过短的评论；
2.将读取和修改文件模块整个移动到Reader文件里（等待确定功能没有问题后再refactor）