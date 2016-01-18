@ECHO OFF
REM "c:\Program Files\apache-maven-3.2.2\bin\mvn"  -s settings_lcg3.xml package -Dmaven.test.skip=true
call "c:\Program Files\apache-maven-3.2.2\bin\mvn" -s settings_lcg3.xml clean package -Dmaven.test.skip=true
