<?xml version="1.0" encoding="UTF-8"?>
<GeopistaSchema>
  <attribute>
    <name>tiposBienes</name>
    <type>STRING</type>
    <column>
      <name>tiposBienes</name>
      <description>tiposBienes</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>tiposBienes</name>
        <pattern null="true" />
        <description>tiposBienes</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>3</name>
          <pattern>3</pattern>
          <description>Patrimonio Municipal del Suelo</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>2</name>
          <pattern>2</pattern>
          <description>Bienes Revertibles</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>1</name>
          <pattern>1</pattern>
          <description>Bienes</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>4</name>
          <pattern>4</pattern>
          <description>Lotes</description>
        </child>
      </domain>
      <table>
        <name>tiposBienes</name>
        <description>tiposBienes</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>subTipoBienes</name>
    <type>STRING</type>
    <column>
      <name>subTipoBienes</name>
      <description>subTipoBienes</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>subTipoBienes</name>
        <pattern null="true" />
        <description>subTipoBienes</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>3</name>
          <pattern>3</pattern>
          <description>Inmuebles Rusticos</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>5</name>
          <pattern>5</pattern>
          <description>Vías Públicas Rústicas</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>4</name>
          <pattern>4</pattern>
          <description>Vías Públicas Urbanas</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>2</name>
          <pattern>2</pattern>
          <description>Inmuebles Urbanos</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>13</name>
          <pattern>13</pattern>
          <description>Muebles no comprendidos en los Epígrafes Anteriores</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>12</name>
          <pattern>12</pattern>
          <description>Semovientes</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>11</name>
          <pattern>11</pattern>
          <description>Vehículos</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>10</name>
          <pattern>10</pattern>
          <description>Créditos y Derechos Personales</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>9</name>
          <pattern>9</pattern>
          <description>Valores mobiliarios</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>7</name>
          <pattern>7</pattern>
          <description>Histórico/Artísticos</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>6</name>
          <pattern>6</pattern>
          <description>Derechos Reales</description>
        </child>
      </domain>
      <table>
        <name>subTipoBienes</name>
        <description>subTipoBienes</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>usoJuridico</name>
    <type>STRING</type>
    <column>
      <name>usoJuridico</name>
      <description>usoJuridico</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>usoJuridico</name>
        <pattern null="true" />
        <description>usoJuridico</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>0</name>
          <pattern>0</pattern>
          <description />
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>1</name>
          <pattern>1</pattern>
          <description>PATRIMONIAL</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>2</name>
          <pattern>2</pattern>
          <description>DOMINIO PÚBLICO, SERVICIO PÚBLICO</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>3</name>
          <pattern>3</pattern>
          <description>DOMINIO PÚBLICO, USO PÚBLICO</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>4</name>
          <pattern>4</pattern>
          <description>DOMINIO PÚBLICO, BIEN COMUNAL</description>
        </child>
      </domain>
      <table>
        <name>usoJuridico</name>
        <description>usoJuridico</description>
        <geometryType>0</geometryType>
      </table>
    </column>
    <accessType>R/W</accessType>
    <category null="true" />
  </attribute>
</GeopistaSchema>

