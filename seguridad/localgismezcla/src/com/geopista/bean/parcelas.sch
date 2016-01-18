<?xml version="1.0" encoding="UTF-8"?>
<GeopistaSchema>
  <attribute>
    <name>ID_Parcela</name>
    <type>INTEGER</type>
    <column>
      <name>ID</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Tipo_Movimiento</name>
    <type>STRING</type>
    <column>
      <name>Referencia catastral</name>
      <description />
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Referencia_Catastral</name>
    <type>STRING</type>
    <column>
      <name>Referencia catastral</name>
      <description />
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ID_Municipio</name>
    <type>INTEGER</type>
    <column>
      <name>ID Municipio</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ID_Distrito</name>
    <type>INTEGER</type>
    <column>
      <name>ID Distrito</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Tipo</name>
    <type>STRING</type>
    <column>
      <name>Tipo</name>
      <description />
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Codigo_Entidad_Menor</name>
    <type>STRING</type>
    <column>
      <name>Entidad menor</name>
      <description />
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ID_Via</name>
    <type>INTEGER</type>
    <column>
      <name>ID Via</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Primer_Numero</name>
    <type>INTEGER</type>
    <column>
      <name>Primer número</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Primera_Letra</name>
    <type>STRING</type>
    <column>
      <name>Primera Letra</name>
      <description />
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Segundo_Numero</name>
    <type>INTEGER</type>
    <column>
      <name>Segundo número</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Segunda_Letra</name>
    <type>STRING</type>
    <column>
      <name>Segunda letra</name>
      <description />
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Kilometro</name>
    <type>INTEGER</type>
    <column>
      <name>Kilómetro</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Bloque</name>
    <type>STRING</type>
    <column>
      <name>Bloque</name>
      <description />
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Direccion_No_Estructurada</name>
    <type>STRING</type>
    <column>
      <name>Texto de dirección no estructurada</name>
      <description />
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Codigo_Postal</name>
    <type>INTEGER</type>
    <column>
      <name>Código postal</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Superficie_Solar</name>
    <type>INTEGER</type>
    <column>
      <name>Superficie del solar</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Superficie_Construida_Total</name>
    <type>INTEGER</type>
    <column>
      <name>Superficie construida total</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Superficie_Const_SobreRasante</name>
    <type>INTEGER</type>
    <column>
      <name>Superficie construida sobre rasante</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Superficie_Const_BajoRasante</name>
    <type>INTEGER</type>
    <column>
      <name>Superficie construida bajo rasante</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Superficie_Cubierta</name>
    <type>INTEGER</type>
    <column>
      <name>Superficie cubierta</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Anio_Aprobacion</name>
    <type>INTEGER</type>
    <column>
      <name>Año aprobación</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Codigo_Calculo_Valor</name>
    <type>INTEGER</type>
    <column>
      <name>Código forma de cálculo</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Modalidad_Reparto</name>
    <type>STRING</type>
    <column>
      <name>Indicador de modalidad de reparto</name>
      <description />
      <domain class="com.geopista.feature.CodeBookDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
        <child class="com.geopista.feature.CodedEntryDomain">
          <name />
          <pattern>?1</pattern>
          <description>Por partes iguales</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name />
          <pattern>?2</pattern>
          <description>Por superficie</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name />
          <pattern>?3</pattern>
          <description>Por coeficientes</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name />
          <pattern>?A</pattern>
          <description>Se valora por repercusión</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name />
          <pattern>?B</pattern>
          <description>Anula el vuelo indicado en ponencia</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name />
          <pattern>?C</pattern>
          <description>Fincas infraedificadas o ruinosas</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name />
          <pattern>?N</pattern>
          <description>Reparto efectuado según forma de cálculo</description>
        </child>
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>AnnoExpediente</name>
    <type>INTEGER</type>
    <column>
      <name>Año del expediente</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ReferenciaExpediente</name>
    <type>STRING</type>
    <column>
      <name>Referencia del expediente</name>
      <description />
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern>?[.*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Fecha_Alta</name>
    <type>DATE</type>
    <column>
      <name>Fecha de alta</name>
      <description />
      <domain class="com.geopista.feature.DateDomain">
        <name />
        <pattern>?[*:*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Fecha_Baja</name>
    <type>DATE</type>
    <column>
      <name>Fecha de baja</name>
      <description />
      <domain class="com.geopista.feature.DateDomain">
        <name />
        <pattern>?[*:*]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Area</name>
    <type>INTEGER</type>
    <column>
      <name>Area</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Length</name>
    <type>INTEGER</type>
    <column>
      <name>Perímetro</name>
      <description />
      <domain class="com.geopista.feature.NumberDomain">
        <name />
        <pattern>?[-INF:INF]</pattern>
        <description />
      </domain>
      <table>
        <name>parcelas</name>
        <description />
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>GEOMETRY</name>
    <type>GEOMETRY</type>
    <column>
      <name>Dummy</name>
      <description>Dummy</description>
      <domain class="com.geopista.feature.StringDomain">
        <name />
        <pattern />
        <description />
      </domain>
      <table>
        <name>Dummy</name>
        <description>Dummy</description>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
</GeopistaSchema>

