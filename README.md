> 整个过程学习自：[制作Kindle mobi书籍完美教程](https://www.douban.com/note/194849341/)

#txt转mobi
- 用来将粗txt文件转成带目录，有一点格式的mobi文件。需要用到[Calibre](https://calibre-ebook.com/download)软件
- 首先使用PreprocessText文件夹中的java代码对txt文件进行预处理，它做的工作有：删除段前空格并用两个全角空格代替、对章和卷名用markdown标记
- 然后使用Calibre软件进行转换，需要用到的CSS文件在下面附上了
 
##txt文件预处理
- PreprocessText包中包含三个java文件，提供一个具有简单UI的小工具对txt文件进行预处理
- txt文件最好是utf-8格式的
- 用java读取utf-8格式的时候会产生乱码，所以用到了[UnicodeReader.java](http://akini.mbnet.fi/java/unicodereader/UnicodeReader.java.txt)文件进行乱码的处理

##calibre处理
这个软件比较简单好用，要注意的就是添加如下CSS样式

MyCSS:
<pre>
<code>
body { margin: 0; }
p {margin-top: 0pt; margin-bottom: 0pt; padding: 0pt; text-indent: 15pt; text-align: justify;}
h1 { text-align: center; padding-top: 12pt; page-break-before: always;}
h2 {text-align: center; font-weight: 700; font-size: xx-large; padding-bottom: 20pt; page-break-before: always;}
</code>
</pre>
> http://amalthia.mediawood.net/tutorials/ebooks/look.html


# TXT2MOBI
- This is a program to convert .txt to .mobi,and [Calibre](https://calibre-ebook.com/download) is recommended.
- First,use java to preprocessing the text files,including deleting spaces and replace them with full-width spaces, add markdown identifies to vol and chapter names.
- Then use Calibre to convert,the CSS is attached below.

##txt pre-processing
- Package PreprocessText includes three java files which provide a tool to pre-processing txt files.
- And txt files are recommended to be saved as utf-8.
- Use [UnicodeReader.java](http://akini.mbnet.fi/java/unicodereader/UnicodeReader.java.txt) to solve the messy code while reading file in java.
- Simple UI.

##calibre 
MyCSS:
<pre>
<code>
body { margin: 0; }
p {margin-top: 0pt; margin-bottom: 0pt; padding: 0pt; text-indent: 15pt; text-align: justify;}
h1 { text-align: center; padding-top: 12pt; page-break-before: always;}
h2 {text-align: center; font-weight: 700; font-size: xx-large; padding-bottom: 20pt; page-break-before: always;}
</code>
</pre>
> http://amalthia.mediawood.net/tutorials/ebooks/look.html
