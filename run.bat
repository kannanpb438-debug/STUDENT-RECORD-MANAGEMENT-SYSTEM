@echo off
echo ========================================================================
echo    Student Record Management System (SRMS) - Campus7 ERP Web App
echo ========================================================================
echo.

set MVN_BIN=mvn
if exist "%USERPROFILE%\.maven\apache-maven-3.9.6\bin\mvn.cmd" (
    set "MVN_BIN=%USERPROFILE%\.maven\apache-maven-3.9.6\bin\mvn.cmd"
)

if defined JAVA_HOME if "%JAVA_HOME:~-1%"=="\" set "JAVA_HOME=%JAVA_HOME:~0,-1%"

echo Using Maven executable: %MVN_BIN%
echo Access URL after startup: http://localhost:8080/srms/
echo.
call "%MVN_BIN%" compile exec:java "-Dexec.mainClass=com.srms.ServerRunner"
pause
