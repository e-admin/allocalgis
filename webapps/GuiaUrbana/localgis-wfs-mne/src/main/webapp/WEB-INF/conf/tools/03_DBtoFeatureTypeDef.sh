# You have to set several parameters to create a featuretypedefinition:
# the following lines are just comments
# each row starts with a java call including several libraries
# -tables tablename -> if more than one table than table_1,table_2
# -user DB user must be specified
# -password '' passwort in single quote
# -driver org.postgresql.Driver the needed driver
# -url  jdbc:postgresql://localhost:5432/deegreetest the url to the DB server with port number an DB name
# -output ../../conf/wms/featuretypes/sgid024_countyboundariespre2003.xsd output directory 
#         and featuretype denfinition file name.

# SCRIPT EXAMPLE

# java -classpath .:../../classes:../../lib/postgresql-8.0-311.jdbc3.jar:../../lib/log4j-1.2.9.jar:../../lib/deegree2.jar org.deegree.tools.datastore.DBSchemaToDatastoreConf -tables table_1,table_2 -user someuser -password '' -driver org.postgresql.Driver -url  jdbc:postgresql://server:5432/someDB -output ../../conf/wfs/featuretypes/featuretypedefinition.xsd

# SCRIPT BEGIN

java -classpath .:../../classes:../../lib/postgresql-8.0-311.jdbc3.jar:../../lib/log4j-1.2.9.jar:../../lib/deegree2.jar org.deegree.tools.datastore.DBSchemaToDatastoreConf -tables sgid024_springs -user deegreetest -password 'deegreetest' -driver org.postgresql.Driver -url  jdbc:postgresql://localhost:5432/deegreetest -output ../../conf/wfs/featuretypes/sgid024_springs.xsd
java -classpath .:../../classes:../../lib/postgresql-8.0-311.jdbc3.jar:../../lib/log4j-1.2.9.jar:../../lib/deegree2.jar org.deegree.tools.datastore.DBSchemaToDatastoreConf -tables sgid100_railroadsdlg100 -user deegreetest -password 'deegreetest' -driver org.postgresql.Driver -url  jdbc:postgresql://localhost:5432/deegreetest -output ../../conf/wfs/featuretypes/sgid100_railroadsdlg100.xsd





#java -classpath .:../classes:../lib/postgresql-8.0-311.jdbc3.jar:../lib/log4j-1.2.9.jar:/lib/deegree2.jar org.deegree.tools.datastore.DBSchemaToDatastoreConf -tables table_1,table_2 -user someuser -password '' -driver org.postgresql.Driver -url  jdbc:postgresql://server:5432/someDB -output /tmp/datastore.xsd
