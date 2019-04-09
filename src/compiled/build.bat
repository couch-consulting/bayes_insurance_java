javac -d ./ -deprecation -classpath ..\..\NeticaJ_Win\NeticaJ_504\bin\NeticaJ.jar;..\..\commons-cli-1.4\commons-cli-1.4.jar  ..\app\Main.java -Xlint:unchecked
jar cfm ../../build/bayesInsurance.jar app.mf ./app/*.class

javac -d ./ -deprecation -classpath ..\..\NeticaJ_Win\NeticaJ_504\bin\NeticaJ.jar  ..\netviewer\NetViewer.java
jar cfm ../../build/netviewer.jar netviewer.mf ./netviewer/*
