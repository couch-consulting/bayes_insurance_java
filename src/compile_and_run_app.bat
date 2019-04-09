javac -d compiled -deprecation -classpath ..\NeticaJ_Win\NeticaJ_504\bin\NeticaJ.jar;..\commons-cli-1.4\commons-cli-1.4.jar  app\Main.java -Xlint:unchecked
set PATH=..\NeticaJ_Win\NeticaJ_504\bin;%PATH%
java  -classpath compiled\;..\NeticaJ_Win\NeticaJ_504\bin\NeticaJ.jar;..\commons-cli-1.4\commons-cli-1.4.jar  -Djava.library.path=..\NeticaJ_Win\NeticaJ_504\bin app.Main --trainCSV=../data/versicherung_a.csv --testCSV=../data/versicherung_a_classify.csv --connectionInput=../data/connectionSetup/netConnections.csv --output=../results/
