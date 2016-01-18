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
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ID de Via</name>
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
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
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
    <name>Codigo INE</name>
    <type>INTEGER</type>
    <column>
      <name>codigoine</name>
      <description>codigoine</description>
      <domain class="com.geopista.feature.NumberDomain">
        <name>?[0:99999]</name>
        <pattern>?[0:99999]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Codigo Catastro</name>
    <type>INTEGER</type>
    <column>
      <name>codigocatastro</name>
      <description>codigocatastro</description>
      <domain class="com.geopista.feature.NumberDomain">
        <name>?[0:99999]</name>
        <pattern>?[0:99999]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Tipo Via Normalizado Catastro</name>
    <type>STRING</type>
    <column>
      <name>tipovianormalizadocatastro</name>
      <description>tipovianormalizadocatastro</description>
      <domain class="com.geopista.feature.CodeBookDomain">
        <name>Tipos de via normalizados de Catastro</name>
        <pattern />
        <description>Tipos de via normalizados de Catastro</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>DS</name>
          <pattern>DS</pattern>
          <description>Diseminado</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>BO</name>
          <pattern>BO</pattern>
          <description>Barrio</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PZ</name>
          <pattern>PZ</pattern>
          <description>Plaza</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>AV</name>
          <pattern>AV</pattern>
          <description>Avenida</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CM</name>
          <pattern>CM</pattern>
          <description>Camino</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CL</name>
          <pattern>CL</pattern>
          <description>Calle</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CR</name>
          <pattern>CR</pattern>
          <description>Carretera</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>NE</name>
          <pattern>NE</pattern>
          <description>No Especificado</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>UR</name>
          <pattern>UR</pattern>
          <description>Urbanización</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RD</name>
          <pattern>RD</pattern>
          <description>Ronda</description>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>TR</name>
          <pattern>TR</pattern>
          <description>Travesía</description>
        </child>
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Tipo Via INE</name>
    <type>STRING</type>
    <column>
      <name>tipoviaine</name>
      <description>tipoviaine</description>
      <domain class="com.geopista.feature.TreeDomain">
        <name>Tipos de vias y abreviaturas del INE</name>
        <pattern null="true" />
        <description>Tipos de vias y abreviaturas del INE</description>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>VCTO</name>
          <pattern>VCTO</pattern>
          <description>Viaducto</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>VCTO</name>
            <pattern>VCTO</pattern>
            <description>Viaducto</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>VALLE</name>
          <pattern>VALLE</pattern>
          <description>Valle</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>VALLE</name>
            <pattern>VALLE</pattern>
            <description>Valle</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PSAJE</name>
          <pattern>PSAJE</pattern>
          <description>Pasaje</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PSAJE</name>
            <pattern>PSAJE</pattern>
            <description>Pasaje</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>URB</name>
          <pattern>URB</pattern>
          <description>Urbanizacion</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>URB</name>
            <pattern>URB</pattern>
            <description>Urbanizacion</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PROL</name>
          <pattern>PROL</pattern>
          <description>Prolongacion</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PROL</name>
            <pattern>PROL</pattern>
            <description>Prolongacion</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PRAZA</name>
          <pattern>PRAZA</pattern>
          <description>Plaza</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PRAZA</name>
            <pattern>PRAZA</pattern>
            <description>Plaza</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>TRVAL</name>
          <pattern>TRVAL</pattern>
          <description>Transversal</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>TRVAL</name>
            <pattern>TRVAL</pattern>
            <description>Transversal</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PRAJE</name>
          <pattern>PRAJE</pattern>
          <description>Paraje</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PRAJE</name>
            <pattern>PRAJE</pattern>
            <description>Paraje</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>TRVA</name>
          <pattern>TRVA</pattern>
          <description>Travesia</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>TRVA</name>
            <pattern>TRVA</pattern>
            <description>Travesia</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PQUE</name>
          <pattern>PQUE</pattern>
          <description>Parque</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PQUE</name>
            <pattern>PQUE</pattern>
            <description>Parque</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>TRAS</name>
          <pattern>TRAS</pattern>
          <description>Trasera</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>TRAS</name>
            <pattern>TRAS</pattern>
            <description>Trasera</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>POLIG</name>
          <pattern>POLIG</pattern>
          <description>Poligono</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>POLIG</name>
            <pattern>POLIG</pattern>
            <description>Poligono</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>TRANS</name>
          <pattern>TRANS</pattern>
          <description>Transito</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>TRANS</name>
            <pattern>TRANS</pattern>
            <description>Transito</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PNTE</name>
          <pattern>PNTE</pattern>
          <description>Puente</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PNTE</name>
            <pattern>PNTE</pattern>
            <description>Puente</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SENDA</name>
          <pattern>SENDA</pattern>
          <description>Senda</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>SENDA</name>
            <pattern>SENDA</pattern>
            <description>Senda</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PLZLA</name>
          <pattern>PLZLA</pattern>
          <description>Plazuela</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PLZLA</name>
            <pattern>PLZLA</pattern>
            <description>Plazuela</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SEND</name>
          <pattern>SEND</pattern>
          <description>Sendero</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>SEND</name>
            <pattern>SEND</pattern>
            <description>Sendero</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PLCET</name>
          <pattern>PLCET</pattern>
          <description>Placeta</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PLCET</name>
            <pattern>PLCET</pattern>
            <description>Placeta</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SEDRA</name>
          <pattern>SEDRA</pattern>
          <description>Sendera</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>SEDRA</name>
            <pattern>SEDRA</pattern>
            <description>Sendera</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PLAZA</name>
          <pattern>PLAZA</pattern>
          <description>Plaza</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PLAZA</name>
            <pattern>PLAZA</pattern>
            <description>Plaza</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>EXTRM</name>
          <pattern>EXTRM</pattern>
          <description>Extramuros</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>EXTRM</name>
            <pattern>EXTRM</pattern>
            <description>Extramuros</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>BARRO</name>
          <pattern>BARRO</pattern>
          <description>Barrio</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>BARRO</name>
            <pattern>BARRO</pattern>
            <description>Barrio</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PLAYA</name>
          <pattern>PLAYA</pattern>
          <description>Playa</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PLAYA</name>
            <pattern>PLAYA</pattern>
            <description>Playa</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>EXPLA</name>
          <pattern>EXPLA</pattern>
          <description>Explanada</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>EXPLA</name>
            <pattern>EXPLA</pattern>
            <description>Explanada</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>BARDA</name>
          <pattern>BARDA</pattern>
          <description>Barriada</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>BARDA</name>
            <pattern>BARDA</pattern>
            <description>Barriada</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ESCAL</name>
          <pattern>ESCAL</pattern>
          <description>Escalinata</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ESCAL</name>
            <pattern>ESCAL</pattern>
            <description>Escalinata</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>AVDA</name>
          <pattern>AVDA</pattern>
          <description>Avenida</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>AVDA</name>
            <pattern>AVDA</pattern>
            <description>Avenida</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ESCA</name>
          <pattern>ESCA</pattern>
          <description>Escalera</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ESCA</name>
            <pattern>ESCA</pattern>
            <description>Escalera</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>AUTO</name>
          <pattern>AUTO</pattern>
          <description>Autopista</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>AUTO</name>
            <pattern>AUTO</pattern>
            <description>Autopista</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CZADA</name>
          <pattern>CZADA</pattern>
          <description>Calzada</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CZADA</name>
            <pattern>CZADA</pattern>
            <description>Calzada</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ATAL</name>
          <pattern>ATAL</pattern>
          <description>Atallo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ATAL</name>
            <pattern>ATAL</pattern>
            <description>Atallo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CUSTA</name>
          <pattern>CUSTA</pattern>
          <description>Cuesta</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CUSTA</name>
            <pattern>CUSTA</pattern>
            <description>Cuesta</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ATAJO</name>
          <pattern>ATAJO</pattern>
          <description>Atajo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ATAJO</name>
            <pattern>ATAJO</pattern>
            <description>Atajo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CTRIN</name>
          <pattern>CTRIN</pattern>
          <description>Carreterin</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CTRIN</name>
            <pattern>CTRIN</pattern>
            <description>Carreterin</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ARRY</name>
          <pattern>ARRY</pattern>
          <description>Arroyo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ARRY</name>
            <pattern>ARRY</pattern>
            <description>Arroyo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CTRA</name>
          <pattern>CTRA</pattern>
          <description>Carretera</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CTRA</name>
            <pattern>CTRA</pattern>
            <description>Carretera</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ARRAL</name>
          <pattern>ARRAL</pattern>
          <description>Arrabal</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ARRAL</name>
            <pattern>ARRAL</pattern>
            <description>Arrabal</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CRRLO</name>
          <pattern>CRRLO</pattern>
          <description>Corralillo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CRRLO</name>
            <pattern>CRRLO</pattern>
            <description>Corralillo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ARB</name>
          <pattern>ARB</pattern>
          <description>Arboleda</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ARB</name>
            <pattern>ARB</pattern>
            <description>Arboleda</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CRRIL</name>
          <pattern>CRRIL</pattern>
          <description>Carril</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CRRIL</name>
            <pattern>CRRIL</pattern>
            <description>Carril</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>APTOS</name>
          <pattern>APTOS</pattern>
          <description>Apartamentos</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>APTOS</name>
            <pattern>APTOS</pattern>
            <description>Apartamentos</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SECT</name>
          <pattern>SECT</pattern>
          <description>Sector</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>SECT</name>
            <pattern>SECT</pattern>
            <description>Sector</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>SBIDA</name>
          <pattern>SBIDA</pattern>
          <description>Subida</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>SBIDA</name>
            <pattern>SBIDA</pattern>
            <description>Subida</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PL</name>
          <pattern>PL</pattern>
          <description>Placa</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PL</name>
            <pattern>PL</pattern>
            <description>Placa</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RUELA</name>
          <pattern>RUELA</pattern>
          <description>Ruela</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RUELA</name>
            <pattern>RUELA</pattern>
            <description>Ruela</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PISTA</name>
          <pattern>PISTA</pattern>
          <description>Pista</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PISTA</name>
            <pattern>PISTA</pattern>
            <description>Pista</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RUA</name>
          <pattern>RUA</pattern>
          <description>Rua</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RUA</name>
            <pattern>RUA</pattern>
            <description>Rua</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PINAR</name>
          <pattern>PINAR</pattern>
          <description>Pinar</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PINAR</name>
            <pattern>PINAR</pattern>
            <description>Pinar</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>NE</name>
          <pattern>NE</pattern>
          <description>No Especificado</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>NE</name>
            <pattern>NE</pattern>
            <description>No Especificado</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RTDA</name>
          <pattern>RTDA</pattern>
          <description>Rotonda</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RTDA</name>
            <pattern>RTDA</pattern>
            <description>Rotonda</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PASEO</name>
          <pattern>PASEO</pattern>
          <description>Passeig</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PASEO</name>
            <pattern>PASEO</pattern>
            <description>Passeig</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RONDA</name>
          <pattern>RONDA</pattern>
          <description>Ronda</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RONDA</name>
            <pattern>RONDA</pattern>
            <description>Ronda</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>MUELL</name>
          <pattern>MUELL</pattern>
          <description>Muelle</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>MUELL</name>
            <pattern>MUELL</pattern>
            <description>Muelle</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RIERA</name>
          <pattern>RIERA</pattern>
          <description>Riera</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RIERA</name>
            <pattern>RIERA</pattern>
            <description>Riera</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>MONTE</name>
          <pattern>MONTE</pattern>
          <description>Monte</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>MONTE</name>
            <pattern>MONTE</pattern>
            <description>Monte</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RCON</name>
          <pattern>RCON</pattern>
          <description>Rincon</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RCON</name>
            <pattern>RCON</pattern>
            <description>Rincon</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>MALEC</name>
          <pattern>MALEC</pattern>
          <description>Malecon</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>MALEC</name>
            <pattern>MALEC</pattern>
            <description>Malecon</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>LUGAR</name>
          <pattern>LUGAR</pattern>
          <description>Lugar</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>LUGAR</name>
            <pattern>LUGAR</pattern>
            <description>Lugar</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RCDA</name>
          <pattern>RCDA</pattern>
          <description>Rinconada</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RCDA</name>
            <pattern>RCDA</pattern>
            <description>Rinconada</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>LOMA</name>
          <pattern>LOMA</pattern>
          <description>Loma</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>LOMA</name>
            <pattern>LOMA</pattern>
            <description>Loma</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CRRDO</name>
          <pattern>CRRDO</pattern>
          <description>Corredor</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CRRDO</name>
            <pattern>CRRDO</pattern>
            <description>Corredor</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ANGTA</name>
          <pattern>ANGTA</pattern>
          <description>Angosta</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ANGTA</name>
            <pattern>ANGTA</pattern>
            <description>Angosta</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>LDERA</name>
          <pattern>LDERA</pattern>
          <description>Ladera</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>LDERA</name>
            <pattern>LDERA</pattern>
            <description>Ladera</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CRRDE</name>
          <pattern>CRRDE</pattern>
          <description>Corredera</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CRRDE</name>
            <pattern>CRRDE</pattern>
            <description>Corredera</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ALTO</name>
          <pattern>ALTO</pattern>
          <description>Alto</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ALTO</name>
            <pattern>ALTO</pattern>
            <description>Alto</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CRRCI</name>
          <pattern>CRRCI</pattern>
          <description>Corredorcillo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CRRCI</name>
            <pattern>CRRCI</pattern>
            <description>Corredorcillo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ALDEA</name>
          <pattern>ALDEA</pattern>
          <description>Aldea</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ALDEA</name>
            <pattern>ALDEA</pattern>
            <description>Aldea</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CRA</name>
          <pattern>CRA</pattern>
          <description>Carrera</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CRA</name>
            <pattern>CRA</pattern>
            <description>Carrera</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ALAM</name>
          <pattern>ALAM</pattern>
          <description>Alameda</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ALAM</name>
            <pattern>ALAM</pattern>
            <description>Alameda</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>COSTA</name>
          <pattern>COSTA</pattern>
          <description>Costa</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>COSTA</name>
            <pattern>COSTA</pattern>
            <description>Costa</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ACEQ</name>
          <pattern>ACEQ</pattern>
          <description>Acequia</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ACEQ</name>
            <pattern>ACEQ</pattern>
            <description>Acequia</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>COMPJ</name>
          <pattern>COMPJ</pattern>
          <description>Complejo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>COMPJ</name>
            <pattern>COMPJ</pattern>
            <description>Complejo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ACCE</name>
          <pattern>ACCE</pattern>
          <description>Acceso</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ACCES</name>
            <pattern>ACCES</pattern>
            <description>Acceso</description>
          </child>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ACCE</name>
            <pattern>ACCE</pattern>
            <description>Acceso</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>COL</name>
          <pattern>COL</pattern>
          <description>Colonia</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>COL</name>
            <pattern>COL</pattern>
            <description>Colonia</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CNADA</name>
          <pattern>CNADA</pattern>
          <description>Can</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CNADA</name>
            <pattern>CNADA</pattern>
            <description>Can</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CMNO</name>
          <pattern>CMNO</pattern>
          <description>Camino</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CMNO</name>
            <pattern>CMNO</pattern>
            <description>Camino</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CLLZO</name>
          <pattern>CLLZO</pattern>
          <description>Callizo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CLLZO</name>
            <pattern>CLLZO</pattern>
            <description>Callizo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RBRA</name>
          <pattern>RBRA</pattern>
          <description>Ribera</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RBRA</name>
            <pattern>RBRA</pattern>
            <description>Ribera</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RBLA</name>
          <pattern>RBLA</pattern>
          <description>Rambla</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RBLA</name>
            <pattern>RBLA</pattern>
            <description>Rambla</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RAMPA</name>
          <pattern>RAMPA</pattern>
          <description>Rampa</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RAMPA</name>
            <pattern>RAMPA</pattern>
            <description>Rampa</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RAMAL</name>
          <pattern>RAMAL</pattern>
          <description>Ramal</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RAMAL</name>
            <pattern>RAMAL</pattern>
            <description>Ramal</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RACO</name>
          <pattern>RACO</pattern>
          <description>Raco</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RACO</name>
            <pattern>RACO</pattern>
            <description>Raco</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>ZONA</name>
          <pattern>ZONA</pattern>
          <description>Zona</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>ZONA</name>
            <pattern>ZONA</pattern>
            <description>Zona</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RACDA</name>
          <pattern>RACDA</pattern>
          <description>Raconada</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RACDA</name>
            <pattern>RACDA</pattern>
            <description>Raconada</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>VREDA</name>
          <pattern>VREDA</pattern>
          <description>Vereda</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>VREDA</name>
            <pattern>VREDA</pattern>
            <description>Vereda</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>RABAL</name>
          <pattern>RABAL</pattern>
          <description>Rabal</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>RABAL</name>
            <pattern>RABAL</pattern>
            <description>Rabal</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>VIAL</name>
          <pattern>VIAL</pattern>
          <description>Vial</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>VIAL</name>
            <pattern>VIAL</pattern>
            <description>Vial</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PZTA</name>
          <pattern>PZTA</pattern>
          <description>Plazoleta</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PZTA</name>
            <pattern>PZTA</pattern>
            <description>Plazoleta</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>VIA</name>
          <pattern>VIA</pattern>
          <description>Via</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>VIA</name>
            <pattern>VIA</pattern>
            <description>Via</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PZO</name>
          <pattern>PZO</pattern>
          <description>Pasadizo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PZO</name>
            <pattern>PZO</pattern>
            <description>Pasadizo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>LAGO</name>
          <pattern>LAGO</pattern>
          <description>Lago</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>LAGO</name>
            <pattern>LAGO</pattern>
            <description>Lago</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>VEGA</name>
          <pattern>VEGA</pattern>
          <description>Vega</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>VEGA</name>
            <pattern>VEGA</pattern>
            <description>Vega</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>PTILL</name>
          <pattern>PTILL</pattern>
          <description>Portillo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>PTILL</name>
            <pattern>PTILL</pattern>
            <description>Portillo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>JDIN</name>
          <pattern>JDIN</pattern>
          <description>Jardin</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>JDIN</name>
            <pattern>JDIN</pattern>
            <description>Jardin</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CLLON</name>
          <pattern>CLLON</pattern>
          <description>Callejon</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CLLON</name>
            <pattern>CLLON</pattern>
            <description>Callejon</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>HOYA</name>
          <pattern>HOYA</pattern>
          <description>Hoya</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>HOYA</name>
            <pattern>HOYA</pattern>
            <description>Hoya</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CLLJA</name>
          <pattern>CLLJA</pattern>
          <description>Calleja</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CLLJA</name>
            <pattern>CLLJA</pattern>
            <description>Calleja</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>GTA</name>
          <pattern>GTA</pattern>
          <description>Glorieta</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>GTA</name>
            <pattern>GTA</pattern>
            <description>Glorieta</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CJTO</name>
          <pattern>CJTO</pattern>
          <description>Conjunto</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CJTO</name>
            <pattern>CJTO</pattern>
            <description>Conjunto</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>GRUP</name>
          <pattern>GRUP</pattern>
          <description>Grupo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>GRUP</name>
            <pattern>GRUP</pattern>
            <description>Grupo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CJLA</name>
          <pattern>CJLA</pattern>
          <description>Callejuela</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CJLA</name>
            <pattern>CJLA</pattern>
            <description>Callejuela</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>GALE</name>
          <pattern>GALE</pattern>
          <description>Galeria</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>GALE</name>
            <pattern>GALE</pattern>
            <description>Galeria</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CERRO</name>
          <pattern>CERRO</pattern>
          <description>Cerro</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CERRO</name>
            <pattern>CERRO</pattern>
            <description>Cerro</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>G.V.</name>
          <pattern>G.V.</pattern>
          <description>Gran Via</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>G.V.</name>
            <pattern>G.V.</pattern>
            <description>Gran Via</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CBTIZ</name>
          <pattern>CBTIZ</pattern>
          <description>Cobertizo</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CBTIZ</name>
            <pattern>CBTIZ</pattern>
            <description>Cobertizo</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>FINCA</name>
          <pattern>FINCA</pattern>
          <description>Finca</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>FINCA</name>
            <pattern>FINCA</pattern>
            <description>Finca</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CANAL</name>
          <pattern>CANAL</pattern>
          <description>Canal</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CANAL</name>
            <pattern>CANAL</pattern>
            <description>Canal</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>FALDA</name>
          <pattern>FALDA</pattern>
          <description>Falda</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>FALDA</name>
            <pattern>FALDA</pattern>
            <description>Falda</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>CALLE</name>
          <pattern>CALLE</pattern>
          <description>Calle</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>CALLE</name>
            <pattern>CALLE</pattern>
            <description>Calle</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>EXTRR</name>
          <pattern>EXTRR</pattern>
          <description>Extrarradio</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>EXTRR</name>
            <pattern>EXTRR</pattern>
            <description>Extrarradio</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>BRANC</name>
          <pattern>BRANC</pattern>
          <description>Barranco</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>BRANC</name>
            <pattern>BRANC</pattern>
            <description>Barranco</description>
          </child>
        </child>
        <child class="com.geopista.feature.CodedEntryDomain">
          <name>BJADA</name>
          <pattern>BJADA</pattern>
          <description>Bajada</description>
          <child class="com.geopista.feature.CodedEntryDomain">
            <name>BJADA</name>
            <pattern>BJADA</pattern>
            <description>Bajada</description>
          </child>
        </child>
        <levelName>tipoviaine</levelName>
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Posicion Tipo Via</name>
    <type>INTEGER</type>
    <column>
      <name>posiciontipovia</name>
      <description>posiciontipovia</description>
      <domain class="com.geopista.feature.NumberDomain">
        <name>?[0:9]</name>
        <pattern>?[0:9]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Nombre de Via INE</name>
    <type>STRING</type>
    <column>
      <name>nombreviaine</name>
      <description>nombreviaine</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?50[.*]</name>
        <pattern>?50[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Nombre de Via Corto INE</name>
    <type>STRING</type>
    <column>
      <name>nombreviacortoine</name>
      <description>nombreviacortoine</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?25[.*]</name>
        <pattern>?25[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Nombre Catastro</name>
    <type>STRING</type>
    <column>
      <name>nombrecatastro</name>
      <description>nombrecatastro</description>
      <domain class="com.geopista.feature.StringDomain">
        <name>?25[.*]</name>
        <pattern>?25[.*]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
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
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
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
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>ID de ALP</name>
    <type>INTEGER</type>
    <column>
      <name>idalp</name>
      <description>idalp</description>
      <domain class="com.geopista.feature.NumberDomain">
        <name>?[0:99999999]</name>
        <pattern>?[0:99999999]</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Fecha Grabacion Ayuntamiento</name>
    <type>DATE</type>
    <column>
      <name>fechagrabacionayto</name>
      <description>fechagrabacionayto</description>
      <domain class="com.geopista.feature.DateDomain">
        <name>?[*:*]dd-MM-yyyy</name>
        <pattern>?[*:*]dd-MM-yyyy</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Fecha Grabacion Cierre</name>
    <type>DATE</type>
    <column>
      <name>fechagrabacioncierre</name>
      <description>fechagrabacioncierre</description>
      <domain class="com.geopista.feature.DateDomain">
        <name>?[*:*]dd-MM-yyyy</name>
        <pattern>?[*:*]dd-MM-yyyy</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
  <attribute>
    <name>Fecha de Ejecucion</name>
    <type>DATE</type>
    <column>
      <name>fechaejecucion</name>
      <description>fechaejecucion</description>
      <domain class="com.geopista.feature.DateDomain">
        <name>?[*:*]dd-MM-yyyy</name>
        <pattern>?[*:*]dd-MM-yyyy</pattern>
        <description null="true" />
      </domain>
      <table>
        <name>vias</name>
        <description>vias</description>
        <geometryType>9</geometryType>
      </table>
    </column>
    <accessType>R</accessType>
    <category null="true" />
  </attribute>
</GeopistaSchema>

