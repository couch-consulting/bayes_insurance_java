javac -deprecation -classpath ..\bin\NeticaJ.jar  *.java
set PATH=..\bin;%PATH%
java  -classpath .;..\bin\NeticaJ.jar -Djava.library.path=..\bin  Main