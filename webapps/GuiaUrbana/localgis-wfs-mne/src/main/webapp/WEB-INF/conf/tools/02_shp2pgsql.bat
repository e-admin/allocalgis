rem this script creates an sql file which gets inserted into DB afterwards 
rem shp2pgsql.exe -d -i -I -s 26912 ..\..\data\SGID024_Springs sgid024_springs > ..\..\data\sql\sgid024_springs.sql
rem shp2pgsql.exe -d -i -I -s 26912 ..\..\data\SGID100_RailroadsDLG100 sgid100_railroadsdlg100 > ..\..\data\sql\sgid100_railroadsdlg100.sql
rem psql.exe -f ..\..\data\sql\sgid024_springs.sql -U deegreetest -d deegreetest
rem psql.exe -f ..\..\data\sql\sgid100_railroadsdlg100.sql -U deegreetest -d deegreetest

