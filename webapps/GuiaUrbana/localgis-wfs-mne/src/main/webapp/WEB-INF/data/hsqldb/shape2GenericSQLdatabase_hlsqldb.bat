set lib_path="D:\java\projekte\deegree2\lib"


C:\Programme\Java\jdk1.5.0_06\bin\java -Xms100m -Xmx300m -classpath .;D:\java\projekte\deegree2\classes;%lib_path%\jts\jts-1.8.jar;%lib_path%\xml\jaxen-1.1-beta-8.jar;%lib_path%\log4j\log4j-1.2.9.jar;hsqldb.jar org.deegree.tools.shape.GenericSQLShapeImporter -driver org.hsqldb.jdbcDriver -url jdbc:hsqldb:shape -user sa -password "" -indexName Municipalities2004_idx -table Municipalities2004 -shapeFile D:\data\shapes\utah\adminstration\SGID024_Municipalities2004 -maxTreeDepth 3 -idType INTEGER
pause