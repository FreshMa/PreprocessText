# TXT2MOBI
-This is a program to convert .txt to .mobi,and Calibre is recommended.
-First,use java to preprocessing the text files
-Then use Calibre to convert,the CSS is attached below.

##txt pre-processing
-Package PreprocessText includes three java files which provide a tool to pre-processing txt files.
-And txt files are recommended to be saved as utf-8.
-Simple UI.

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
>http://amalthia.mediawood.net/tutorials/ebooks/look.html
