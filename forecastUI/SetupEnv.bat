@echo on
REM
SET JAVA_HOME=E:\java\jdk1.8.0_66
SET M2_HOME=E:\misc\apache-maven-3.3.9\apache-maven-3.3.9
SET PATH=E:\misc\apache-maven-3.3.9\apache-maven-3.3.9\bin;E:\java\jdk1.8.0_66\bin;%PATH%;
SET CLASSPATH=.;E:\java\jdk1.8.0_66\jre\lib\rt.jar;E:\java\jdk1.8.0_66\lib\dt.jar;E:\java\jdk1.8.0_66\lib\tools.jar;E:\misc\gcp\java-docs-samples-master\compute\forecastUI\src\main\webapp\WEB-INF\lib\mae.jar;E:\misc\gcp\java-docs-samples-master\compute\forecastUI\src\main\webapp\WEB-INF\lib\mae_client.jar;E:\misc\gcp\java-docs-samples-master\compute\forecastUI\src\main\webapp\WEB-INF\lib\ojdbc7.jar;E:\misc\wildfly-10.0.0.Final\modules\system\layers\base\org\wildfly\extension\batch\jberet\main\wildfly-jberet-10.0.0.Final.jar;E:\misc\wildfly-10.0.0.Final\modules\system\layers\base\org\jboss\as\jaxrs\main\wildfly-jaxrs-10.0.0.Final.jar;E:\misc\wildfly-10.0.0.Final\modules\system\layers\base\javax\enterprise\api\main\cdi-api-1.2.jar;E:\misc\glassfish-4.1.1\glassfish4\glassfish\modules;%CLASSPATH%;

set BUILD_ROOT=E:\misc\gcp\java-docs-samples-master\compute\forecastUI

set ANT_HOME=E:\release\GIT\SCPO\vendor\apache-ant-1.9.6


cd E:\misc\gcp\java-docs-samples-master\compute\forecastUI