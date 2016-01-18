setting up a simple deegree2 gazeteer - containing two FeatureTypes

You find two  shape files containing the required data for WFS-G demo in the 
directory $WMS_HOME$/WEB-INF/data/

SGID100_CountyBoundaries_edited.shp
SGID024_Municipalities2004_edited.shp

These will be used to set up a POSTGIS database.

Please adapt the scripts (paths) 01_create_spatial_database.bat/.sh and 01_create_tables_index_ready.bat/.sh
to your system and execute them. These scripts will set up the database, the required DB user and tables.
This readme can't give you full POSTGIS support, why you should consult the PostgreSQL 
and POSTGIS documentations (http://www.postgis.org/documentation/ ) if you experience any problems
 with the creation of the DB-user and database 01_create_spatial_database.bat/.sh script. 
 Script 01_create_tables_index_ready.bat/.sh sums up as well the insert of shape files into database 
 as the sql statements 02_*.sql to 04_.sql for creating the tables. 
 
 Now the database is ready for use but the WFS still needs some adaption. Just copy the two featuretype 
 definitions under $WMS_HOME$/WEB-INF/conf/wfs/featuretypes/examplefeaturetypes/gazeteer into 
 $WMS_HOME$/WEB-INF/conf/wfs/featuretypes/ and restart tomcat.
 
 You are now ready to use the WFS-gazeteer. 
 Go to your internet browser and start the generic client:
 http://localhost:8080/deegree-wfs/client/client.html Check out the WFS-G with the example requests. 
 All gazeteer requests start with Gaz_*.