# This script creates a spatially enabled Postgis Database. You might have to adapt the paths to the
# Postgresql bin directory to your system.
# please specify 'deegreetest' for password if asked for it.
# After creation of user deegreetest and database deegreetest with this script, please follow up with
# by executing the scripts 02_shp2pgsql.sh and 03 DBtoFeaturetypeDef.sh
/usr/local/pgsql/bin/createuser -s -d deegreetest -P -U postgres
/usr/local/pgsql/bin/createdb deegreetest -U deegreetest -E UTF8 -T template0
/usr/local/pgsql/bin/createlang -U deegreetest plpgsql deegreetest
/usr/local/pgsql/bin/psql -U deegreetest -d deegreetest -f /usr/local/pgsql/share/contrib/lwpostgis.sql
/usr/local/pgsql/bin/psql -U deegreetest -d deegreetest -f /usr/local/pgsql/share/contrib/spatial_ref_sys.sql