@echo off
rem ***** Builds the SCPOWEB project *****
rem
rem   Author:         Avinash Shanbhag
rem 
rem
rem   The script expects the following environment variables to be set prior
rem   to execution:
rem
rem       JAVA_HOME        Home directory for current version of JDK (also 
rem                           used by ANT)
rem       WEBLOGIC_HOME    Home directory of weblogic (i.e. d:\bea\weblogic81)                
rem       BUILD_ROOT       Root of build tree (default = d:\release\v70\webworksforecast)
rem       BUILD_PROPS      location of build substitution values
rem 
rem   Optional Env vars  
rem       BUILD_VERSION
rem       PATCH_VERSION
rem 
rem

@setlocal
SET PATH=E:\misc\apache-maven-3.3.9\apache-maven-3.3.9\bin;E:\java\jdk1.8.0_66\bin;%PATH%;
set ANT_HOME=E:\release\GIT\SCPO\vendor\apache-ant-1.9.6
SET JAVA_HOME=E:\java\jdk1.8.0_66
set BUILD_ROOT=E:\misc\gcp\java-docs-samples-master\compute\forecast
REM Set path to include ant & java
SET PATH=%ANT_HOME%\bin;%PATH%
SET PATH=%JAVA_HOME%\bin;%PATH%

set CLASSPATH=E:\java\jdk1.8.0_66\jre\lib\rt.jar;E:\java\jdk1.8.0_66\lib\dt.jar;E:\java\jdk1.8.0_66\lib\tools.jar;E:\misc\gcp\java-docs-samples-master\compute\forecast\src\main\webapp\WEB-INF\lib\mae.jar;E:\misc\gcp\java-docs-samples-master\compute\forecast\src\main\webapp\WEB-INF\lib\mysql-connector-java-5.1.39-bin.jar;E:\misc\gcp\java-docs-samples-master\compute\forecast\src\main\webapp\WEB-INF\lib\mae_client.jar;E:\misc\gcp\java-docs-samples-master\compute\forecast\src\main\webapp\WEB-INF\lib\ojdbc7.jar;%CLASSPATH%;

echo %ANT_HOME%\bin\ant -emacs %ANT_VARS% -buildfile %BUILD_ROOT%\build.xml %*
call %ANT_HOME%\bin\ant -emacs %ANT_VARS% -buildfile %BUILD_ROOT%\build.xml %*
goto finish



:finish
@endlocal
