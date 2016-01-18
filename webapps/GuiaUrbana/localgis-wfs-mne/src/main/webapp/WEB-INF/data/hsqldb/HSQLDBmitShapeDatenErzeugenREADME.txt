java -cp hsqldb.jar org.hsqldb.util.DatabaseManager

Beim sich öffnenden Fenster einen Datenbankname vergeben. 

In das Eingabefenster folgendes hineinkopieren:

CREATE TABLE TAB_DEEGREE_IDX ( 
	ID integer NOT NULL,
	column_name varchar(50) NOT NULL,
	table_name varchar(50) NOT NULL,
	owner varchar(50) NOT NULL,
	index_name varchar(50) NOT NULL,
	FK_INDEXTREE int NOT NULL
) 
;

CREATE TABLE TAB_Quadtree ( 
	ID integer NOT NULL,
	FK_ROOT varchar(150),
	DEPTH int
) 
;



ALTER TABLE TAB_DEEGREE_IDX
ADD CONSTRAINT UQ_TAB_DEEGREE_IDX_id UNIQUE (ID)
;
ALTER TABLE TAB_DEEGREE_IDX
ADD CONSTRAINT UQ_TAB_DEEGREE_IDX_index_name UNIQUE (index_name)
;
ALTER TABLE TAB_Quadtree
ADD CONSTRAINT UQ_TAB_Quadtree_ID UNIQUE (ID)
;


ALTER TABLE TAB_DEEGREE_IDX ADD CONSTRAINT FK_TAB_DEEGREE_IDX_TAB_Quadtree 
	FOREIGN KEY (FK_INDEXTREE) REFERENCES TAB_Quadtree (ID)
;

ALTER TABLE TAB_DEEGREE_IDX ADD CONSTRAINT PK_TAB_DEEGREE_IDX 
	PRIMARY KEY (ID) 
;

ALTER TABLE TAB_Quadtree ADD CONSTRAINT PK_TAB_Quadtree 
	PRIMARY KEY (ID) 
;

_______________________________________________

Mit Shape-Dateien befüllen. Dringend beachten dass table und index UPPERCASE geschrieben werden:

#/usr/local/java/j2sdk1.5-sun/bin/java -Xms100m -Xmx300m -cp .:/home/hanko/workspace/deegreebase/classes:/home/hanko/workspace/deegreebase/lib/jts/jts-1.8.jar:/home/hanko/workspace/deegreebase/lib/xml/jaxen-1.1-beta-8.jar:/home/hanko/workspace/deegreebase/lib/log4j/log4j-1.2.9.jar:hsqldb.jar org.deegree.tools.shape.GenericSQLShapeImporter -driver org.hsqldb.jdbcDriver -url jdbc:hsqldb:administration -user sa -password "" -indexName SGID024_MUNICIPALITIES2004_EDITED_IDX -table SGID024_MUNICIPALITIES2004_EDITED -shapeFile /home/hanko/GIS-Daten/utah/adminstration/SGID024_Municipalities2004_edited -maxTreeDepth 3 -idType INTEGER

/usr/local/java/j2sdk1.5-sun/bin/java -Xms100m -Xmx300m -cp .:/home/hanko/workspace/deegreebase/classes:/home/hanko/workspace/deegreebase/lib/jts/jts-1.8.jar:/home/hanko/workspace/deegreebase/lib/xml/jaxen-1.1-beta-8.jar:/home/hanko/workspace/deegreebase/lib/log4j/log4j-1.2.9.jar:hsqldb.jar org.deegree.tools.shape.GenericSQLShapeImporter -driver org.hsqldb.jdbcDriver -url jdbc:hsqldb:administration -user sa -password "" -indexName SGID100_COUNTYBOUNDARIES_EDITED_IDX -table SGID100_COUNTYBOUNDARIES_EDITED -shapeFile ../SGID100_CountyBoundaries_edited -maxTreeDepth 3 -idType INTEGER

/usr/local/java/j2sdk1.5-sun/bin/java -Xms100m -Xmx300m -cp .:/home/hanko/workspace/deegreebase/classes:/home/hanko/workspace/deegreebase/lib/jts/jts-1.8.jar:/home/hanko/workspace/deegreebase/lib/xml/jaxen-1.1-beta-8.jar:/home/hanko/workspace/deegreebase/lib/log4j/log4j-1.2.9.jar:hsqldb.jar org.deegree.tools.shape.GenericSQLShapeImporter -driver org.hsqldb.jdbcDriver -url jdbc:hsqldb:administration -user sa -password "" -indexName SGID500_ZIPCODES_IDX -table SGID500_ZIPCODES -shapeFile ../SGID500_ZipCodes -maxTreeDepth 3 -idType INTEGER

/usr/local/java/j2sdk1.5-sun/bin/java -Xms100m -Xmx300m -cp .:/home/hanko/workspace/deegreebase/classes:/home/hanko/workspace/deegreebase/lib/jts/jts-1.8.jar:/home/hanko/workspace/deegreebase/lib/xml/jaxen-1.1-beta-8.jar:/home/hanko/workspace/deegreebase/lib/log4j/log4j-1.2.9.jar:hsqldb.jar org.deegree.tools.shape.GenericSQLShapeImporter -driver org.hsqldb.jdbcDriver -url jdbc:hsqldb:administration -user sa -password "" -indexName SGID024_STATEBOUNDARY_IDX -table SGID024_STATEBOUNDARY -shapeFile ../SGID024_StateBoundary -maxTreeDepth 3 -idType INTEGER


