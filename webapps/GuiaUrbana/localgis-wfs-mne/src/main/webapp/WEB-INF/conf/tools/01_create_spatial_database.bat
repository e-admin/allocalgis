rem  This script creates a spatially enabled Postgis Database. You might have to adapt the paths to the
rem  Postgresql bin directory to your system.
rem  please specify 'deegreetest' for password if asked for it.
rem  After creation of user deegreetest and database deegreetest with this script, please follow up with
rem  by executing the scripts 02_shp2pgsql.sh and 03 DBtoFeaturetypeDef.sh
createuser -s -d deegreetest -P -U postgres
createdb deegreetest -U deegreetest -E UTF8 -T template0
createlang -U deegreetest plpgsql deegreetest
psql -U deegreetest -d deegreetest -f C:\Programme\PostgreSQL\8.1\share\contrib\lwpostgis.sql
psql -U deegreetest -d deegreetest -f C:\Programme\PostgreSQL\8.1\share\contrib\spatial_ref_sys.sql