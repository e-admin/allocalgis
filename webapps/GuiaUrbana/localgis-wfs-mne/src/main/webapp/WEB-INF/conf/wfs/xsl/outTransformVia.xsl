<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:wfs="http://www.opengis.net/wfs"
xmlns:app="http://www.deegree.org/app"
xmlns="http://www.idee.es/mne"
xmlns:gml="http://www.opengis.net/gml"
xmlns:xlink="http://www.w3.org/1999/xlink"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.idee.es/mne http://www.idee.es:80/wfs/IDEE-WFS-Nomenclator/wfs?request=DescribeFeatureType&amp;typename=Entidad
					http://www.opengis.net/wfs http://ogc.dmsolutions.ca/wfs/1.1.0/WFS-basic.xsd">
					
	<xsl:output method="xml" indent="yes"/> 
	
	<xsl:template match="wfs:FeatureCollection">
		<ResultCollection>
			<xsl:apply-templates />
		</ResultCollection>
	</xsl:template>
	<xsl:template match="gml:boundedBy">
	</xsl:template>
	<xsl:template match="gml:featureMember"> 
		<gml:featureMember>
			<xsl:for-each select="app:Via">
				<xsl:variable name = "ident" select='./app:fid'/>
				<xsl:text disable-output-escaping="yes">&lt;Entidad fid="</xsl:text>
				<xsl:value-of select='$ident'/>
				<xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
					<nombreEntidad>
						<nombre>
							<xsl:value-of select="./app:nombreEntidad_nombre"/>
						</nombre>
						<idioma>
							<xsl:text>spa</xsl:text>
						</idioma>
						<claseNombre>
							<xsl:text>Preferente</xsl:text>			
						</claseNombre>
						<estatus>
							<xsl:text>Normalizado</xsl:text>				
						</estatus>
						<fuente>
							<xsl:text>Catastro</xsl:text>				
						</fuente>
					</nombreEntidad>
				<tipoEntidad>
					<tipo>
						<xsl:value-of select="./app:tipoEntidad_tipo"/>
					</tipo>
					<catalogoEntidades>
						<xsl:text>http://www.ign.es/cartociudad/catalogo</xsl:text>
					</catalogoEntidades>
				</tipoEntidad>
				<posicionEspacial>		
                <xsl:if test="./app:posicionEspacial_coordinates != ''" >
					 <gml:MultiLineString srsName="EPSG:4230">
						<xsl:for-each select="./app:posicionEspacial_coordinates/node()/node()/node()/node()">
							<gml:LineString srsName="EPSG:4230">
								<xsl:copy-of select="."/>
							</gml:LineString>
						</xsl:for-each>
					</gml:MultiLineString>
				</xsl:if>
				</posicionEspacial>
					<!--<xsl:variable name = "coordinates" select="substring-before(substring-after(./app:posicionEspacial_coordinates,'('),')')"/>
					<xsl:variable name = "x" select="substring-before($coordinates,' ')"/>
					<xsl:variable name = "y" select="substring-after($coordinates,' ')"/>
					<gml:Point>
						<gml:coordinates cs="," decimal="." ts=""><xsl:value-of select="$x"/>,<xsl:text></xsl:text><xsl:value-of select="$y"/></gml:coordinates>
					</gml:Point>
					<gml:MultiLineString>
						<xsl:value-of select="./app:geom"/>
					</gml:MultiLineString>-->

				<EntidadRelacionada>
					<xsl:for-each select="./app:entidadRelacionada_idEntidad">
						<idEntidad>
							<xsl:value-of select="."/>
						</idEntidad>
						<descripcionRelacion>
							<xsl:text>Identificador de los portales que existen en una vía</xsl:text>				
						</descripcionRelacion>
						<tipoRelacion>
							<xsl:text>Jerárquica.padre</xsl:text>
						</tipoRelacion>
					</xsl:for-each>
				</EntidadRelacionada>
				<entidadLocal>
					<municipio>
						<xsl:value-of select="./app:entidadLocal_municipio"/>
					</municipio>
					<provincia>
						<xsl:value-of select="./app:entidadLocal_provincia"/>
					</provincia>
				</entidadLocal>



				<xsl:text disable-output-escaping="yes">&lt;/Entidad&gt;</xsl:text>
			</xsl:for-each>
		</gml:featureMember>
	</xsl:template>
</xsl:stylesheet>