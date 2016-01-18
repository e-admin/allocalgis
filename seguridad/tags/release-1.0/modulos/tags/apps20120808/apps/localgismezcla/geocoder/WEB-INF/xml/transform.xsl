<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:gml="http://www.opengis.net/gml" version="1.0">
  <xsl:template match="Collection">
    <Result xmlns:gml="http://www.opengis.net/gml">
      <xsl:apply-templates select="./gml:boundedBy"/>
      <xsl:apply-templates select="./gml:featureMember"/>
    </Result>
  </xsl:template>
  
  <xsl:template match="gml:boundedBy">
  </xsl:template>
  
  <xsl:template match="gml:featureMember/Europe">
    <EuropeanCountry>
      <ID>
	<xsl:value-of select="ID"/>
      </ID>
      <Name>
	<xsl:value-of select="NAME"/>
      </Name>
      <Area>
	<xsl:value-of select="AREA"/>
      </Area>
    </EuropeanCountry>
  </xsl:template>
</xsl:stylesheet>
