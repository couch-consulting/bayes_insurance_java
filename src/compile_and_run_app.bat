javac -d compiled -deprecation -classpath ..\NeticaJ_Win\NeticaJ_504\bin\NeticaJ.jar  app\Main.java
set PATH=..\NeticaJ_Win\NeticaJ_504\bin;%PATH%
java  -classpath compiled\;..\NeticaJ_Win\NeticaJ_504\bin\NeticaJ.jar  -Djava.library.path=..\NeticaJ_Win\NeticaJ_504\bin  Main
