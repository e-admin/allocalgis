#You have to set several parameters to create a featuretypedefinition:
#each row starts with a java call including several library references and method calls
#-driver SHAPE
#-tables ..\..\data\shapename -> path to shapefile without the extension .shp
#-output ..\..\conf\wms\featuretypes\SGID500_Contours500Ft_shp.xsd -> output directory 
#        and featuretype denfinition file name.
#Have a look at the  Commented_FeaturetypeDefiniton.xsd.txt in the outputdirectory 
#$deegree-wms$/WEB-INF/conf/wms/featuretypes/ and the Philosopher example under $deegree-wms$/
# WEB-INF/conf/wms/featuretypes/philosopher/Philosopher.xsd for details about the featuretype definition. 

#SCRIPT EXAMPLE

#java -classpath .:../../classes:../../lib/log4j-1.2.9.jar:../../lib/deegree2.jar org.deegree.tools.datastore.DBSchemaToDatastoreConf -driver SHAPE -tables ../../data/SGID500_Contours500Ft -output ../../conf/wms/featuretypes/SGID500_Contours500Ft_shp.xsd

#Be aware that by executing this commands the existing featuretype definitions will be overwritten as long as you dont change the -output filename
#SCRIPT BEGIN
#java -classpath .:../../classes:../../lib/log4j-1.2.9.jar:../../lib/deegree2.jar org.deegree.tools.datastore.DBSchemaToDatastoreConf -driver SHAPE -tables ../../data/SGID024_Springs -output ../../conf/wms/featuretypes/SGID024_Springs_shp.xsd
#java -classpath .:../../classes:../../lib/log4j-1.2.9.jar:../../lib/deegree2.jar org.deegree.tools.datastore.DBSchemaToDatastoreConf -driver SHAPE -tables ../../data/SGID100_RailroadsDLG100 -output ../../conf/wms/featuretypes/SGID100_RailroadsDLG100_shp.xsd
#SCRIPT END
