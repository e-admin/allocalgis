<?xml version="1.0" encoding="UTF-8"?>
<GeopistaSchema>
  <attribute>
    <name>TIPO_FECHA</name>
    <type>DATE</type>
    <column>
      <name>TIPO_FECHA</name>
      <description>TIPO_FECHA</description>
      <domain class="com.geopista.feature.DateDomain">
        <name>?[*:*]dd-MM-yyyy</name>
        <pattern>?[*:*]dd-MM-yyyy</pattern>
        <description>?[*:*]dd-MM-yyyy</description>
      </domain>
      <table>
        <name>TIPO_FECHA</name>
        <description>TIPO_FECHA</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_Obra ejecutada</name>
    <type>STRING</type>
    <column>
      <name>eiel_Obra ejecutada</name>
      <description>eiel_Obra ejecutada</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_Obra ejecutada</name>
        <pattern null="true" />
        <description>eiel_Obra ejecutada</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SI</name>
          <pattern>SI</pattern>
          <description>Si</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>NO</name>
          <pattern>NO</pattern>
          <description>No</description>
        </child>
      </domain>
      <table>
        <name>eiel_Obra ejecutada</name>
        <description>eiel_Obra ejecutada</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_Acceso con silla de ruedas</name>
    <type>STRING</type>
    <column>
      <name>eiel_Acceso con silla de ruedas</name>
      <description>eiel_Acceso con silla de ruedas</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_Acceso con silla de ruedas</name>
        <pattern null="true" />
        <description>eiel_Acceso con silla de ruedas</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SI</name>
          <pattern>SI</pattern>
          <description>Si</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>NO</name>
          <pattern>NO</pattern>
          <description>No</description>
        </child>
      </domain>
      <table>
        <name>eiel_Acceso con silla de ruedas</name>
        <description>eiel_Acceso con silla de ruedas</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_Existencia de crematorio</name>
    <type>STRING</type>
    <column>
      <name>eiel_Existencia de crematorio</name>
      <description>eiel_Existencia de crematorio</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_Existencia de crematorio</name>
        <pattern null="true" />
        <description>eiel_Existencia de crematorio</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SI</name>
          <pattern>SI</pattern>
          <description>Si</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>NO</name>
          <pattern>NO</pattern>
          <description>No</description>
        </child>
      </domain>
      <table>
        <name>eiel_Existencia de crematorio</name>
        <description>eiel_Existencia de crematorio</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_saturación</name>
    <type>STRING</type>
    <column>
      <name>eiel_saturación</name>
      <description>eiel_saturación</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_saturación</name>
        <pattern null="true" />
        <description>eiel_saturación</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SI</name>
          <pattern>SI</pattern>
          <description>Si</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>NO</name>
          <pattern>NO</pattern>
          <description>No</description>
        </child>
      </domain>
      <table>
        <name>eiel_saturación</name>
        <description>eiel_saturación</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_Posibilidad de ampliación</name>
    <type>STRING</type>
    <column>
      <name>eiel_Posibilidad de ampliación</name>
      <description>eiel_Posibilidad de ampliación</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_Posibilidad de ampliación</name>
        <pattern null="true" />
        <description>eiel_Posibilidad de ampliación</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>NO</name>
          <pattern>NO</pattern>
          <description>No</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SI</name>
          <pattern>SI</pattern>
          <description>Si</description>
        </child>
      </domain>
      <table>
        <name>eiel_Posibilidad de ampliación</name>
        <description>eiel_Posibilidad de ampliación</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_Capilla</name>
    <type>STRING</type>
    <column>
      <name>eiel_Capilla</name>
      <description>eiel_Capilla</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_Capilla</name>
        <pattern null="true" />
        <description>eiel_Capilla</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>NO</name>
          <pattern>NO</pattern>
          <description>No existe capilla</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>MO</name>
          <pattern>MO</pattern>
          <description>Monoconfesional</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>EC</name>
          <pattern>EC</pattern>
          <description>Ecuménica</description>
        </child>
      </domain>
      <table>
        <name>eiel_Capilla</name>
        <description>eiel_Capilla</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_Estado del acceso</name>
    <type>STRING</type>
    <column>
      <name>eiel_Estado del acceso</name>
      <description>eiel_Estado del acceso</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_Estado del acceso</name>
        <pattern null="true" />
        <description>eiel_Estado del acceso</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>R</name>
          <pattern>R</pattern>
          <description>Regular</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>M</name>
          <pattern>M</pattern>
          <description>Malo</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>E</name>
          <pattern>E</pattern>
          <description>Ejecución</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>B</name>
          <pattern>B</pattern>
          <description>Bueno</description>
        </child>
      </domain>
      <table>
        <name>eiel_Estado del acceso</name>
        <description>eiel_Estado del acceso</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_Estado del acceso</name>
    <type>STRING</type>
    <column>
      <name>eiel_Estado del acceso</name>
      <description>eiel_Estado del acceso</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_Estado del acceso</name>
        <pattern null="true" />
        <description>eiel_Estado del acceso</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>R</name>
          <pattern>R</pattern>
          <description>Regular</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>M</name>
          <pattern>M</pattern>
          <description>Malo</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>E</name>
          <pattern>E</pattern>
          <description>Ejecución</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>B</name>
          <pattern>B</pattern>
          <description>Bueno</description>
        </child>
      </domain>
      <table>
        <name>eiel_Estado del acceso</name>
        <description>eiel_Estado del acceso</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_Titularidad de cementerio</name>
    <type>STRING</type>
    <column>
      <name>eiel_Titularidad de cementerio</name>
      <description>eiel_Titularidad de cementerio</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_Titularidad de cementerio</name>
        <pattern null="true" />
        <description>eiel_Titularidad de cementerio</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PV</name>
          <pattern>PV</pattern>
          <description>Privado</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>OT</name>
          <pattern>OT</pattern>
          <description>Otros</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>MU</name>
          <pattern>MU</pattern>
          <description>Municipal</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CR</name>
          <pattern>CR</pattern>
          <description>Confesiones Religiosas</description>
        </child>
      </domain>
      <table>
        <name>eiel_Titularidad de cementerio</name>
        <description>eiel_Titularidad de cementerio</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>eiel_Estado de revisión</name>
    <type>STRING</type>
    <column>
      <name>eiel_Estado de revisión</name>
      <description>eiel_Estado de revisión</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>eiel_Estado de revisión</name>
        <pattern null="true" />
        <description>eiel_Estado de revisión</description>
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
      </domain>
      <table>
        <name>eiel_Estado de revisión</name>
        <description>eiel_Estado de revisión</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>TIPO_FECHA</name>
    <type>DATE</type>
    <column>
      <name>TIPO_FECHA</name>
      <description>TIPO_FECHA</description>
      <domain class="com.geopista.feature.DateDomain">
        <name>?[*:*]dd-MM-yyyy</name>
        <pattern>?[*:*]dd-MM-yyyy</pattern>
        <description>?[*:*]dd-MM-yyyy</description>
      </domain>
      <table>
        <name>TIPO_FECHA</name>
        <description>TIPO_FECHA</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
</GeopistaSchema>

