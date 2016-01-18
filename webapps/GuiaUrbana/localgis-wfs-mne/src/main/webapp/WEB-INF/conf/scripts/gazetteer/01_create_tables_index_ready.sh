/usr/local/pgsql/bin/shp2pgsql -s 26912 -c ../../../data/SGID024_Municipalities2004_edited imp_municipalities > ../../../data/sql/imp_municipalities.sql
/usr/local/pgsql/bin/psql -f ../../../data/sql/imp_municipalities.sql -d deegreetest -U deegreetest
/usr/local/pgsql/bin/shp2pgsql -s 26912  -c ../../../data/SGID100_CountyBoundaries_edited imp_counties > ../../../data/sql/imp_counties.sql
/usr/local/pgsql/bin/psql -f ../../../data/sql/imp_counties.sql -d deegreetest -U deegreetest
/usr/local/pgsql/bin/psql -d deegreetest -U deegreetest -f 02_deegree_postgis_counties_create-table.sql
/usr/local/pgsql/bin/psql -d deegreetest -U deegreetest -f 02_deegree_postgis_municipalities_create-table.sql
/usr/local/pgsql/bin/psql -d deegreetest -U deegreetest -f 03_deegree_postgis_counties_insert-and-update.sql
/usr/local/pgsql/bin/psql -d deegreetest -U deegreetest -f 03_deegree_postgis_municipalities_insert-and-update.sql
/usr/local/pgsql/bin/psql -d deegreetest -U deegreetest -f 04_deegree_postgis_counties_create-index.sql
/usr/local/pgsql/bin/psql -d deegreetest -U deegreetest -f 04_deegree_postgis_municipalities_create-index.sql