<?xml version="1.0" encoding="UTF-8"?>
<GeopistaSchema>
  <attribute>
    <name>GEOMETRIA</name>
    <type>GEOMETRY</type>
    <column>
      <name>GEOMETRY</name>
      <description>GEOMETRY</description>
      <domain null="true" />
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ID de Parcela</name>
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
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Referencia Catastral</name>
    <type>STRING</type>
    <column>
      <name>referencia_catastral</name>
      <description>referencia_catastral</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>14[.*]</name>
        <pattern>14[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Numero de Bienes</name>
    <type>INTEGER</type>
    <column>
      <name>id2</name>
      <description>id2</description>
      <domain null="true" />
      <table>
        <name>SYSTEM</name>
        <description>SYSTEM</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ID de Municipio</name>
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
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ID de Distrito</name>
    <type>STRING</type>
    <column>
      <name>id_distrito</name>
      <description>id_distrito</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?2[.*]</name>
        <pattern>?2[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Codigo Parcela</name>
    <type>STRING</type>
    <column>
      <name>codigoparcela</name>
      <description>codigoparcela</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?5[.*]</name>
        <pattern>?5[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Codigo Poligono</name>
    <type>STRING</type>
    <column>
      <name>codigopoligono</name>
      <description>codigopoligono</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?3[.*]</name>
        <pattern>?3[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ID de Via</name>
    <type>INTEGER</type>
    <column>
      <name>id_via</name>
      <description>id_via</description>
      <domain class="com.geopista.feature.NumberDomain">
        <name>[0:99999999]</name>
        <pattern>[0:99999999]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Primer Numero</name>
    <type>INTEGER</type>
    <column>
      <name>primer_numero</name>
      <description>primer_numero</description>
      <domain class="com.geopista.feature.NumberDomain">
        <name>?[0:9999]</name>
        <pattern>?[0:9999]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Primera Letra</name>
    <type>STRING</type>
    <column>
      <name>primera_letra</name>
      <description>primera_letra</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?1[.*]</name>
        <pattern>?1[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Segundo Numero</name>
    <type>INTEGER</type>
    <column>
      <name>segundo_numero</name>
      <description>segundo_numero</description>
      <domain class="com.geopista.feature.NumberDomain">
        <name>?[0:9999]</name>
        <pattern>?[0:9999]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Segunda Letra</name>
    <type>STRING</type>
    <column>
      <name>segunda_letra</name>
      <description>segunda_letra</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?1[.*]</name>
        <pattern>?1[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Kilometro</name>
    <type>DOUBLE</type>
    <column>
      <name>kilometro</name>
      <description>kilometro</description>
      <domain class="com.geopista.feature.NumberDomain">
        <name>?[0:99999]</name>
        <pattern>?[0:99999]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Bloque</name>
    <type>STRING</type>
    <column>
      <name>bloque</name>
      <description>bloque</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?4[.*]</name>
        <pattern>?4[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Direccion No Estructurada</name>
    <type>STRING</type>
    <column>
      <name>direccion_no_estructurada</name>
      <description>direccion_no_estructurada</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?25[.*]</name>
        <pattern>?25[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Codigo Postal</name>
    <type>INTEGER</type>
    <column>
      <name>codigo_postal</name>
      <description>codigo_postal</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?5[.*]</name>
        <pattern>?5[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Codigo Delegacion MEH</name>
    <type>INTEGER</type>
    <column>
      <name>codigodelegacionmeh</name>
      <description>codigodelegacionmeh</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?2[.*]</name>
        <pattern>?2[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Longitud</name>
    <type>DOUBLE</type>
    <column>
      <name>length</name>
      <description>length</description>
      <domain null="true" />
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Area</name>
    <type>DOUBLE</type>
    <column>
      <name>area</name>
      <description>area</description>
      <domain null="true" />
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Fecha Alta</name>
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
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Fecha Baja</name>
    <type>DATE</type>
    <column>
      <name>fecha_baja</name>
      <description>fecha_baja</description>
      <domain class="com.geopista.feature.DateDomain">
        <name>?[*:*]dd-MM-yyyy</name>
        <pattern>?[*:*]dd-MM-yyyy</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>parcelas</name>
        <description>parcelas</description>
        <geometryType>5</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
</GeopistaSchema>

