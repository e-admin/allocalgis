<?xml version="1.0" encoding="UTF-8"?>
<GeopistaSchema>
  <attribute>
    <name>Geometria</name>
    <type>GEOMETRY</type>
    <column>
      <name>GEOMETRY</name>
      <description>GEOMETRY</description>
      <domain null="true" />
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>id</name>
    <type>INTEGER</type>
    <column>
      <name>id</name>
      <description>id</description>
      <domain class="com.geopista.feature.AutoFieldDomain">
        <name>ID</name>
        <pattern>ID</pattern>
        <description>Autonumérico incremental</description>
      </domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>IdMunicip</name>
    <type>INTEGER</type>
    <column>
      <name>id_municipio</name>
<description>id_municipio</description>
<domain class="com.geopista.feature.TreeDomain">
<name>municipio_domain</name>
<pattern null="true" />
<description>municipio_domain</description>
<child class="com.geopista.feature.CodedEntryDomain">
<name>33001</name>
<pattern>33001</pattern>
<description>Allande</description>
<child class="com.geopista.feature.CodedEntryDomain">
<name>33001</name>
<pattern>33001</pattern>
<description>Allande</description>
</child>
</child>
</domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Clave</name>
    <type>STRING</type>
    <column>
      <name>clave</name>
      <description>clave</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>2[.*]</name>
        <pattern>2[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Código provincia</name>
    <type>STRING</type>
    <column>
      <name>codprov</name>
      <description>codprov</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>2[.*]</name>
        <pattern>2[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Código Municipio</name>
    <type>STRING</type>
    <column>
      <name>codmunic</name>
      <description>codmunic</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>3[.*]</name>
        <pattern>3[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Código INE Entidad</name>
    <type>STRING</type>
    <column>
      <name>codentidad</name>
      <description>codentidad</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>4[.*]</name>
        <pattern>4[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Código INE Núcleo</name>
    <type>STRING</type>
    <column>
      <name>codpoblamiento</name>
      <description>codpoblamiento</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>2[.*]</name>
        <pattern>2[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Código Orden</name>
    <type>STRING</type>
    <column>
      <name>orden_ce</name>
      <description>orden_ce</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>3[.*]</name>
        <pattern>3[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Precisión digitalización</name>
    <type>STRING</type>
    <column>
      <name>precision_dig</name>
      <description>precision_dig</description>
      <domain class="com.geopista.feature.CodeBookDomain">
        <name>eiel_precision_dig</name>
        <pattern null="true" />
        <description>Precision digitalización</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>DC</name>
          <pattern>DC</pattern>
          <description>Desconocido</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>AP</name>
          <pattern>AP</pattern>
          <description>Punto aproximado</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SC</name>
          <pattern>SC</pattern>
          <description>Escaneado</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PY</name>
          <pattern>PY</pattern>
          <description>Proyecto</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>OF</name>
          <pattern>OF</pattern>
          <description>Digitalizado sobre ortofoto</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>GP</name>
          <pattern>GP</pattern>
          <description>GPS</description>
        </child>
      </domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Observaciones</name>
    <type>STRING</type>
    <column>
      <name>observ</name>
      <description>observ</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?100[.*]</name>
        <pattern>?100[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Revisión actual</name>
    <type>INTEGER</type>
    <column>
      <name>revision_actual</name>
      <description>revision_actual</description>
      <domain null="true" />
      <table>
        <name>eiel_c_ce</name>
        <description>eiel_c_ce</description>
        <geometryType>11</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
</GeopistaSchema>

