set TOMCAT_HOME=C:\Apps\apache-tomcat-5.5.20

javac -deprecation -classpath .\WEB-INF\lib_copyToSharedFolder\NeticaJ.jar;%TOMCAT_HOME%\common\lib\servlet-api.jar  -d .\WEB-INF\classes *.java