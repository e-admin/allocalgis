c:\Programme\PostgreSQL\bin\shp2pgsql -s 26912 -c ..\..\..\data\SGID024_Municipalities2004_edited imp_municipalities > ..\..\..\data\sql\imp_municipalities.sql
c:\Programme\PostgreSQL\bin\psql -f ..\..\..\data\sql\imp_municipalities.sql -d deegreetest -U deegreetest
c:\Programme\PostgreSQL\bin\shp2pgsql -s 26912  -c ..\..\..\data\SGID100_CountyBoundaries_edited imp_counties > ..\..\..\data\sql\imp_counties.sql
c:\Programme\PostgreSQL\bin\psql -f ..\..\..\data\sql\imp_counties.sql -d deegreetest -U deegreetest
c:\Programme\PostgreSQL\bin\psql -d deegreetest -U deegreetest -f 02_deegree_postgis_counties_create-table.sql
c:\Programme\PostgreSQL\bin\psql -d deegreetest -U deegreetest -f 02_deegree_postgis_municipalities_create-table.sql
c:\Programme\PostgreSQL\bin\psql -d deegreetest -U deegreetest -f 03_deegree_postgis_counties_insert-and-update.sql
c:\Programme\PostgreSQL\bin\psql -d deegreetest -U deegreetest -f 03_deegree_postgis_municipalities_insert-and-update.sql
c:\Programme\PostgreSQL\bin\psql -d deegreetest -U deegreetest -f 04_deegree_postgis_counties_create-index.sql
c:\Programme\PostgreSQL\bin\psql -d deegreetest -U deegreetest -f 04_deegree_postgis_municipalities_create-index.sql