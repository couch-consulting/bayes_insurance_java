@echo off
set PATH=libs/NeticaJ_Win/NeticaJ_504/bin;%PATH%
@echo on
java -Djava.library.path=libs/NeticaJ_Win/NeticaJ_504/bin -jar bayesInsurance.jar --trainCSV=../data/versicherung_a.csv --testCSV=../data/versicherung_a_classify.csv --connectionInput=../data/connectionSetup/netConnections.csv --output=../results/
@echo off
pause