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
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
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
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
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
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Código Provincia</name>
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
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
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
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
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
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Nombre Oficial</name>
    <type>INTEGER</type>
    <column>
      <name>nombre_oficial</name>
      <description>nombre_oficial</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>50[.*]</name>
        <pattern>50[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
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
        <name>4[.*]</name>
        <pattern>4[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Fecha de alta</name>
    <type>DATE</type>
    <column>
      <name>fecha_alta</name>
      <description>fecha_alta</description>
      <domain class="com.geopista.feature.DateDomain">
        <name>?[*:*]dd-MM-yyyy</name>
        <pattern>?[*:*]dd-MM-yyyy</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
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
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Fecha de revisión</name>
    <type>DATE</type>
    <column>
      <name>fecha_revision</name>
      <description>fecha_revision</description>
      <domain class="com.geopista.feature.DateDomain">
        <name>?[*:*]dd-MM-yyyy</name>
        <pattern>?[*:*]dd-MM-yyyy</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Estado de Revisión</name>
    <type>INTEGER</type>
    <column>
      <name>estado_revision</name>
      <description>estado_revision</description>
      <domain class="com.geopista.feature.CodeBookDomain">
        <name>eiel_Estado de revisión</name>
        <pattern null="true" />
        <description>Estado de revisión</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>2</name>
          <pattern>2</pattern>
          <description>Actualizado y revisado, pendiente cargar en servidor</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>1</name>
          <pattern>1</pattern>
          <description>Actualizado en campo</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>0</name>
          <pattern>0</pattern>
          <description>Sin actualizar</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>4</name>
          <pattern>4</pattern>
          <description>Información antigua 2007</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>3</name>
          <pattern>3</pattern>
          <description>Actualizado definitivo</description>
        </child>
      </domain>
      <table>
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
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
        <name>eiel_c_nucleo_poblacion</name>
        <description>eiel_c_nucleo_poblacion</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
</GeopistaSchema>

