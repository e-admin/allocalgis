<?xml version="1.0" encoding="UTF-8"?>
    <xsl:stylesheet version="2.0" 
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:ogc="http://www.opengis.net/ogc"  
        xmlns:wfs="http://www.opengis.net/wfs"
        xmlns:app="http://www.deegree.org/app"
        xmlns:gml="http://www.opengis.net/gml" 
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.1.0/wfs.xsd">
		<xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyzáéíóúñÁÉÍÓÚ</xsl:variable>
		<xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZAEIOUÑAEIOU</xsl:variable>        
        
        <xsl:output method="xml" indent="yes"/>	
        <xsl:template match="node()|@*">
            <xsl:choose>
                <xsl:when test=". =  //ogc:PropertyName" >
                    <ogc:PropertyName>
                        <xsl:text>app:</xsl:text><xsl:value-of select="translate(.,'/','_')" />
                    </ogc:PropertyName>
                </xsl:when>
                <xsl:when test=". =  //ogc:Literal" >
                    <ogc:Literal>
                        <xsl:value-of select="translate(.,$lcletters,$ucletters)" />
                    </ogc:Literal>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy>
                        <xsl:apply-templates select="@*|node()" />
                    </xsl:copy>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:template>
    </xsl:stylesheet>

