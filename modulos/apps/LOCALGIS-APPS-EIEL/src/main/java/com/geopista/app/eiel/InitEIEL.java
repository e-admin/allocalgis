/**
 * InitEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.GenericFieldEIEL;
import com.geopista.protocol.inventario.CampoFiltro;
import com.geopista.security.GeopistaPrincipal;

public class InitEIEL {
	
	
	public static LocalGISEIELClient clienteLocalGISEIEL = null;
	
	public static GeopistaPrincipal principal;
	
	public static void init(){
		
	}
	
	public static Hashtable<String,String> modelosPatrones;
	public static Hashtable<String,String> modelosCapas;
	public static Hashtable<String,String> modelosActivarSelectorMunicipio;
	public static Hashtable<String,String> modelosActivarSelectorNucleo;
	public static HashMap<String,String> traduccionesEspeciales;
	public static HashMap<String,String> tablasAlfanumericas;
	public static HashMap<String,ArrayList> camposTablaEspecificos;
	

	public static HashMap<String,String> filtroTablasAlfanumericasTR;

	

	
	public static HashMap<String,Boolean> loadedLayers;


	static{
		
		//Capas con tratamiento especial
		//Definidas en ConstantesLocalGISEIEL
		
		
		
		//Asociacion de capas a modelos
		loadedLayers=new HashMap<String,Boolean>();
		
		modelosPatrones=new Hashtable<String,String>();
		modelosPatrones.put(ConstantesLocalGISEIEL.CAPTACIONES,ConstantesLocalGISEIEL.CAPTACIONES_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.DATOS_VERTEDEROS,ConstantesLocalGISEIEL.VERTEDEROS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.DEPOSITOS,ConstantesLocalGISEIEL.DEPOSITOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.PUNTOS_VERTIDO,ConstantesLocalGISEIEL.PUNTOS_VERTIDO_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION,ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.EMISARIOS,ConstantesLocalGISEIEL.EMISARIOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.TCOLECTOR,ConstantesLocalGISEIEL.TCOLECTOR_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.TCONDUCCION,ConstantesLocalGISEIEL.TCONDUCCION_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.ENCUESTADOS2,ConstantesLocalGISEIEL.ENCUESTADOS2_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS,ConstantesLocalGISEIEL.SERVICIOS_ABASTECIMIENTOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO,ConstantesLocalGISEIEL.SERVICIOS_SANEAMIENTO_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES,ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.CEMENTERIOS,ConstantesLocalGISEIEL.CEMENTERIOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES,ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA,ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION,ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.CENTROS_SANITARIOS,ConstantesLocalGISEIEL.CENTROS_SANITARIOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.MATADEROS,ConstantesLocalGISEIEL.MATADEROS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.LONJAS_MERCADOS,ConstantesLocalGISEIEL.LONJAS_MERCADOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.TANATORIOS,ConstantesLocalGISEIEL.TANATORIOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.PARQUES_JARDINES,ConstantesLocalGISEIEL.PARQUES_JARDINES_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES,ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS,ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.CENTROS_CULTURALES,ConstantesLocalGISEIEL.CENTROS_CULTURALES_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO,ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.RECOGIDA_BASURAS,ConstantesLocalGISEIEL.RECOGIDA_BASURAS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.AGRUPACIONES6000,ConstantesLocalGISEIEL.AGRUPACIONES6000_MODEL_COMPLETO);
		modelosPatrones.put(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA,ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO,ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO,ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.POBLAMIENTO,ConstantesLocalGISEIEL.POBLAMIENTO_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS,ConstantesLocalGISEIEL.PADRON_MUNICIPIOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.PADRON_NUCLEOS,ConstantesLocalGISEIEL.PADRON_NUCLEOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.NUCLEOS_POBLACION,ConstantesLocalGISEIEL.NUCLEOS_POBLACION_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.CABILDO,ConstantesLocalGISEIEL.CABILDO_CONSEJO_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES,ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS,ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.DISEMINADOS,ConstantesLocalGISEIEL.DISEMINADOS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.DEPURADORAS1,ConstantesLocalGISEIEL.DEPURADORAS1_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.DEPURADORAS2,ConstantesLocalGISEIEL.DEPURADORAS2_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.NUCLEO_ENCT_7,ConstantesLocalGISEIEL.INFO_TERMINOS_MUNICIPALES_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.ENCUESTADOS1,ConstantesLocalGISEIEL.ENCUESTADOS1_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS,ConstantesLocalGISEIEL.CARRETERAS_MODEL);
		modelosPatrones.put(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO,ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_MODEL);
		
		
		modelosPatrones.put("IV",ConstantesLocalGISEIEL.GENERIC_MODEL); //Red de Carreteras->Infraestructura viaria
		modelosPatrones.put("PL",ConstantesLocalGISEIEL.GENERIC_MODEL); //Alumbrado->Puntos de Luz
		modelosPatrones.put("CMP",ConstantesLocalGISEIEL.GENERIC_MODEL); //Alumbrado->Cuadro de mandos
		modelosPatrones.put("eea",ConstantesLocalGISEIEL.GENERIC_MODEL); //Alumbrado->Estabilizador
		modelosPatrones.put("PR",ConstantesLocalGISEIEL.GENERIC_MODEL); //Saneamiento->Elementos puntuales
		modelosPatrones.put("AR",ConstantesLocalGISEIEL.GENERIC_MODEL); //Abastecimiento->Elementos puntuales
		modelosPatrones.put("RD",ConstantesLocalGISEIEL.GENERIC_MODEL); //Abastecimiento->Red de distribución domiciliaria
		modelosPatrones.put("RS",ConstantesLocalGISEIEL.GENERIC_MODEL); //Saneamiento->Red de ramales
		modelosPatrones.put("CBS",ConstantesLocalGISEIEL.GENERIC_MODEL); //Saneamiento->Caseta de Bombeo
		
		
		
		modelosActivarSelectorMunicipio=new Hashtable<String,String>();
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.CAPTACIONES,"S");
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.DATOS_VERTEDEROS,"S");
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.DEPOSITOS,"S");
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.PUNTOS_VERTIDO,"S");
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.EMISARIOS,"S");
		
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.TCONDUCCION,"S");
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.DEPURADORAS1,"S");
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.DEPURADORAS2,"S");
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION,"S");
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER,"S");
		modelosActivarSelectorMunicipio.put(ConstantesLocalGISEIEL.TCOLECTOR,"S");
		
		//Estos los activamos para que funcione el selector de nucleo
		modelosActivarSelectorNucleo=new Hashtable<String,String>();
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.CAPTACIONES,"S");
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.DATOS_VERTEDEROS,"S");
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.DEPOSITOS,"S");
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.PUNTOS_VERTIDO,"S");
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.EMISARIOS,"S");
		
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.TCONDUCCION,"S");
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.DEPURADORAS1,"S");
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.DEPURADORAS2,"S");
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION,"S");
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER,"S");
		modelosActivarSelectorNucleo.put(ConstantesLocalGISEIEL.TCOLECTOR,"S");
		

		
		
		
		modelosCapas=new Hashtable<String,String>();
		modelosCapas.put(ConstantesLocalGISEIEL.CAPTACIONES,ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.DATOS_VERTEDEROS,ConstantesLocalGISEIEL.VERTEDERO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.DEPOSITOS,ConstantesLocalGISEIEL.DEPOSITOS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.PUNTOS_VERTIDO,ConstantesLocalGISEIEL.PUNTOS_VERTIDO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION,ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.EMISARIOS,ConstantesLocalGISEIEL.EMISARIOS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.TCOLECTOR,ConstantesLocalGISEIEL.COLECTOR_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.TCONDUCCION,ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.ENCUESTADOS2,ConstantesLocalGISEIEL.ENCUESTADOS2_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS,ConstantesLocalGISEIEL.DATOS_SERVICIO_ABASTECIMIENTO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO,ConstantesLocalGISEIEL.DATOS_SERVICIO_SANEAMIENTO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES,ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.CEMENTERIOS,ConstantesLocalGISEIEL.CEMENTERIOS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES,ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA,ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION,ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.CENTROS_SANITARIOS,ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.MATADEROS,ConstantesLocalGISEIEL.MATADEROS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.LONJAS_MERCADOS,ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.TANATORIOS,ConstantesLocalGISEIEL.TANATORIO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.PARQUES_JARDINES,ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES,ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS,ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.CENTROS_CULTURALES,ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO,ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.RECOGIDA_BASURAS,ConstantesLocalGISEIEL.RECOGIDA_BASURAS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.AGRUPACIONES6000, ConstantesLocalGISEIEL.AGRUPACIONES6000_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA,ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO,ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO,ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.POBLAMIENTO,ConstantesLocalGISEIEL.POBLAMIENTO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS,ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.PADRON_NUCLEOS,ConstantesLocalGISEIEL.PADRON_NUCLEOS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.NUCLEOS_POBLACION,ConstantesLocalGISEIEL.NUCLEOS_POBLACION_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.CABILDO,ConstantesLocalGISEIEL.CABILDO_CONSEJO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES,ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS,ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.DISEMINADOS,ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.DEPURADORAS1,ConstantesLocalGISEIEL.DEPURADORAS1_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.DEPURADORAS2,ConstantesLocalGISEIEL.DEPURADORAS2_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.NUCLEO_ENCT_7,ConstantesLocalGISEIEL.NUCLEO_ENCT_7_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.ENCUESTADOS1,ConstantesLocalGISEIEL.ENCUESTADOS1_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS,ConstantesLocalGISEIEL.CARRETERA_LAYER);
		modelosCapas.put(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO,ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
		

		//Capas que no tienen relacion alfanumerica/grafica (Solo grafica)
		modelosCapas.put("IV",ConstantesLocalGISEIEL.INF_VIARIA_LAYER); //Red de Carreteras->Infraestructura viaria
		modelosCapas.put("PL","PL"); //Alumbrado->Puntos de Luz
		modelosCapas.put("CMP","CMP"); //Alumbrado->Cuadro de mandos
		modelosCapas.put("eea","eea"); //Alumbrado->Estabilizador
		modelosCapas.put("PR","PR"); //Saneamiento->Elementos puntuales
		modelosCapas.put("AR","AR"); //Abastecimiento->Elementos puntuales
		modelosCapas.put("RD","RD"); //Abastecimiento->Red de distribución domiciliaria
		modelosCapas.put("RS","RS"); //Saneamiento->Red de ramales
		modelosCapas.put("CBS","CBS"); //Saneamiento->Caseta de Bombeo
		
		
		
		traduccionesEspeciales=new HashMap<String,String>();
        traduccionesEspeciales.put("TCN","CN");
    	traduccionesEspeciales.put("TEM","EM");
    	traduccionesEspeciales.put("TCL","CL");
    	traduccionesEspeciales.put("ASL","AS");
    	traduccionesEspeciales.put("SA","CSAN");
    	traduccionesEspeciales.put("TN","TA");
    	traduccionesEspeciales.put("AB","AU");
    	traduccionesEspeciales.put("carreteras","TC");
    	traduccionesEspeciales.put("edificiosing","SU");
    	traduccionesEspeciales.put("ED","D2");
    	
    	tablasAlfanumericas=new HashMap<String,String>();
    	tablasAlfanumericas.put("CA","eiel_t_abast_ca");
    	tablasAlfanumericas.put("DE","eiel_t_abast_de");
    	tablasAlfanumericas.put("CN","eiel_t_abast_tcn");
    	tablasAlfanumericas.put("TP","eiel_t_abast_tp");

    	tablasAlfanumericas.put("CC","eiel_t_cc");
    	tablasAlfanumericas.put("CE","eiel_t_ce");
    	tablasAlfanumericas.put("AS","eiel_t_as");
    	tablasAlfanumericas.put("CU","eiel_t_cu");
    	tablasAlfanumericas.put("EN","eiel_t_en");
    	tablasAlfanumericas.put("IP","eiel_t_ip");
    	tablasAlfanumericas.put("CSAN","eiel_t_sa");
    	tablasAlfanumericas.put("SU","eiel_t_su");
    	tablasAlfanumericas.put("ID","eiel_t_id");
    	tablasAlfanumericas.put("LM","eiel_t_lm");
    	tablasAlfanumericas.put("MT","eiel_t_mt");
    	tablasAlfanumericas.put("PJ","eiel_t_pj");
    	tablasAlfanumericas.put("TA","eiel_t_ta");
    	tablasAlfanumericas.put("A6","eiel_t_entidades_agrupadas");
    	
    	tablasAlfanumericas.put("TC","eiel_t_carreteras");
    	tablasAlfanumericas.put("CL","eiel_t_saneam_tcl");
    	tablasAlfanumericas.put("D1","eiel_t1_saneam_ed");
    	tablasAlfanumericas.put("D2","eiel_t2_saneam_ed");    	
    	tablasAlfanumericas.put("EM","eiel_t_saneam_tem");
    	tablasAlfanumericas.put("PV","eiel_t_saneam_pv");
    	tablasAlfanumericas.put("SN","eiel_t_saneam_au");
    	
    	tablasAlfanumericas.put("VT","eiel_t_vt");

    	//Lo metemos en alfanumericos aunque sean graficas
    	tablasAlfanumericas.put("IV","eiel_c_redviaria_tu");
    	tablasAlfanumericas.put("PL","eiel_c_alum_pl");
    	tablasAlfanumericas.put("CMP","eiel_c_alum_cmp");
    	tablasAlfanumericas.put("eea","eiel_c_alum_eea");
    	tablasAlfanumericas.put("PR","eiel_c_saneam_pr");
    	tablasAlfanumericas.put("AR","eiel_c_abast_ar");
    	tablasAlfanumericas.put("RD","eiel_c_abast_rd");
    	tablasAlfanumericas.put("RS","eiel_c_saneam_rs");
    	tablasAlfanumericas.put("CBS","eiel_c_saneam_cb");
    	
    	
    	filtroTablasAlfanumericasTR=new HashMap<String,String>();
    	filtroTablasAlfanumericasTR.put("CA"," and (eiel_t_abast_ca.orden_ca,eiel_t_abast_ca.codprov,eiel_t_abast_ca.codmunic) in (select eiel_tr_abast_ca_pobl.orden_ca,eiel_tr_abast_ca_pobl.codprov_ca,eiel_tr_abast_ca_pobl.codmunic_ca from eiel_tr_abast_ca_pobl where eiel_tr_abast_ca_pobl.codprov_pobl='$COD_PROV$' and eiel_tr_abast_ca_pobl.codmunic_pobl='$COD_MUN$' and eiel_tr_abast_ca_pobl.codentidad_pobl='$COD_ENTIDAD$' and eiel_tr_abast_ca_pobl.codpoblamiento_pobl='$COD_POBL$' and eiel_tr_abast_ca_pobl.revision_expirada=9999999999)");
    	filtroTablasAlfanumericasTR.put("DE"," and (eiel_t_abast_de.orden_de,eiel_t_abast_de.codprov,eiel_t_abast_de.codmunic) in (select eiel_tr_abast_de_pobl.orden_de,eiel_tr_abast_de_pobl.codprov_de,eiel_tr_abast_de_pobl.codmunic_de from eiel_tr_abast_de_pobl where eiel_tr_abast_de_pobl.codprov_pobl='$COD_PROV$' and eiel_tr_abast_de_pobl.codmunic_pobl='$COD_MUN$' and eiel_tr_abast_de_pobl.codentidad_pobl='$COD_ENTIDAD$' and eiel_tr_abast_de_pobl.codpoblamiento_pobl='$COD_POBL$' and eiel_tr_abast_de_pobl.revision_expirada=9999999999)");
    	filtroTablasAlfanumericasTR.put("CN"," and (eiel_t_abast_tcn.tramo_cn,eiel_t_abast_tcn.codprov,eiel_t_abast_tcn.codmunic) in (select eiel_tr_abast_tcn_pobl.tramo_tcn,eiel_tr_abast_tcn_pobl.codprov_tcn,eiel_tr_abast_tcn_pobl.codmunic_tcn from eiel_tr_abast_tcn_pobl where eiel_tr_abast_tcn_pobl.codprov_pobl='$COD_PROV$' and eiel_tr_abast_tcn_pobl.codmunic_pobl='$COD_MUN$' and eiel_tr_abast_tcn_pobl.codentidad_pobl='$COD_ENTIDAD$' and eiel_tr_abast_tcn_pobl.codpoblamiento_pobl='$COD_POBL$' and eiel_tr_abast_tcn_pobl.revision_expirada=9999999999)");
    	filtroTablasAlfanumericasTR.put("TP"," and (eiel_t_abast_tp.orden_tp,eiel_t_abast_tp.codprov,eiel_t_abast_tp.codmunic) in (select eiel_tr_abast_tp_pobl.orden_tp,eiel_tr_abast_tp_pobl.codprov_tp,eiel_tr_abast_tp_pobl.codmunic_tp from eiel_tr_abast_tp_pobl where eiel_tr_abast_tp_pobl.codprov_pobl='$COD_PROV$' and eiel_tr_abast_tp_pobl.codmunic_pobl='$COD_MUN$' and eiel_tr_abast_tp_pobl.codentidad_pobl='$COD_ENTIDAD$' and eiel_tr_abast_tp_pobl.codpoblamiento_pobl='$COD_POBL$' and eiel_tr_abast_tp_pobl.revision_expirada=9999999999)");
    	filtroTablasAlfanumericasTR.put("D1"," and (eiel_t1_saneam_ed.orden_ed,eiel_t1_saneam_ed.codprov,eiel_t1_saneam_ed.codmunic) in (select eiel_tr_saneam_ed_pobl.orden_ed,eiel_tr_saneam_ed_pobl.codprov_ed,eiel_tr_saneam_ed_pobl.codmunic_ed from eiel_tr_saneam_ed_pobl where eiel_tr_saneam_ed_pobl.codprov_pobl='$COD_PROV$' and eiel_tr_saneam_ed_pobl.codmunic_pobl='$COD_MUN$' and eiel_tr_saneam_ed_pobl.codentidad_pobl='$COD_ENTIDAD$' and eiel_tr_saneam_ed_pobl.codpoblamiento_pobl='$COD_POBL$' and eiel_tr_saneam_ed_pobl.revision_expirada=9999999999)");
    	filtroTablasAlfanumericasTR.put("D2"," and (eiel_t2_saneam_ed.orden_ed,eiel_t2_saneam_ed.codprov,eiel_t2_saneam_ed.codmunic) in (select eiel_tr_saneam_ed_pobl.orden_ed,eiel_tr_saneam_ed_pobl.codprov_ed,eiel_tr_saneam_ed_pobl.codmunic_ed from eiel_tr_saneam_ed_pobl where eiel_tr_saneam_ed_pobl.codprov_pobl='$COD_PROV$' and eiel_tr_saneam_ed_pobl.codmunic_pobl='$COD_MUN$' and eiel_tr_saneam_ed_pobl.codentidad_pobl='$COD_ENTIDAD$' and eiel_tr_saneam_ed_pobl.codpoblamiento_pobl='$COD_POBL$' and eiel_tr_saneam_ed_pobl.revision_expirada=9999999999)");
    	filtroTablasAlfanumericasTR.put("PV"," and (eiel_t_saneam_pv.orden_pv,eiel_t_saneam_pv.codprov,eiel_t_saneam_pv.codmunic) in (select eiel_tr_saneam_pv_pobl.orden_pv,eiel_tr_saneam_pv_pobl.codprov_pv,eiel_tr_saneam_pv_pobl.codmunic_pv from eiel_tr_saneam_pv_pobl where eiel_tr_saneam_pv_pobl.codprov_pobl='$COD_PROV$' and eiel_tr_saneam_pv_pobl.codmunic_pobl='$COD_MUN$' and eiel_tr_saneam_pv_pobl.codentidad_pobl='$COD_ENTIDAD$' and eiel_tr_saneam_pv_pobl.codpoblamiento_pobl='$COD_POBL$' and eiel_tr_saneam_pv_pobl.revision_expirada=9999999999)");
    	filtroTablasAlfanumericasTR.put("CL"," and (eiel_t_saneam_tcl.tramo_cl,eiel_t_saneam_tcl.codprov,eiel_t_saneam_tcl.codmunic)  in (select eiel_tr_saneam_tcl_pobl.tramo_cl,eiel_tr_saneam_tcl_pobl.codprov_tcl,eiel_tr_saneam_tcl_pobl.codmunic_tcl from eiel_tr_saneam_tcl_pobl where eiel_tr_saneam_tcl_pobl.codprov_pobl='$COD_PROV$' and eiel_tr_saneam_tcl_pobl.codmunic_pobl='$COD_MUN$' and eiel_tr_saneam_tcl_pobl.codentidad_pobl='$COD_ENTIDAD$' and eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl='$COD_POBL$' and eiel_tr_saneam_tcl_pobl.revision_expirada=9999999999)");
    	filtroTablasAlfanumericasTR.put("EM"," and (eiel_t_saneam_tem.tramo_em,eiel_t_saneam_tem.codprov,eiel_t_saneam_tem.codmunic)  in (select eiel_tr_saneam_tem_pobl.tramo_em,eiel_tr_saneam_tem_pobl.codprov_tem,eiel_tr_saneam_tem_pobl.codmunic_tem from eiel_tr_saneam_tem_pobl where eiel_tr_saneam_tem_pobl.codprov_pobl='$COD_PROV$' and eiel_tr_saneam_tem_pobl.codmunic_pobl='$COD_MUN$' and eiel_tr_saneam_tem_pobl.codentidad_pobl='$COD_ENTIDAD$' and eiel_tr_saneam_tem_pobl.codpoblamiento_pobl='$COD_POBL$' and eiel_tr_saneam_tem_pobl.revision_expirada=9999999999)");
    	filtroTablasAlfanumericasTR.put("VT"," and (eiel_t_vt.orden_vt,eiel_t_vt.codprov,eiel_t_vt.codmunic)  in (select eiel_tr_vt_pobl.orden_vt,eiel_tr_vt_pobl.codprov_vt,eiel_tr_vt_pobl.codmunic_vt from eiel_tr_vt_pobl where eiel_tr_vt_pobl.codprov='$COD_PROV$' and eiel_tr_vt_pobl.codmunic='$COD_MUN$' and eiel_tr_vt_pobl.codentidad='$COD_ENTIDAD$' and eiel_tr_vt_pobl.codpoblamiento='$COD_POBL$' and eiel_tr_vt_pobl.revision_expirada=9999999999)");
    
    
  
    	//********************************************
    	//Campos especificos a recuperar en las tablas
    	//********************************************
    	camposTablaEspecificos=new HashMap<String, ArrayList>();
    	ArrayList iv=new ArrayList();
    	iv.add(new GenericFieldEIEL("tipo",CampoFiltro.VARCHAR_CODE));
    	iv.add(new GenericFieldEIEL("denominacion",CampoFiltro.VARCHAR_CODE));
    	camposTablaEspecificos.put("IV",iv);
    	
    	ArrayList pl=new ArrayList();
    	pl.add(new GenericFieldEIEL("clave",CampoFiltro.VARCHAR_CODE));
    	pl.add(new GenericFieldEIEL("estado",CampoFiltro.VARCHAR_CODE));
    	camposTablaEspecificos.put("PL",pl);
    	
    	ArrayList cmp=new ArrayList();
    	cmp.add(new GenericFieldEIEL("potencia_inst",CampoFiltro.NUMERIC_CODE));
    	cmp.add(new GenericFieldEIEL("potencia_contratada",CampoFiltro.NUMERIC_CODE));
    	camposTablaEspecificos.put("CMP",cmp);
    	
    	
    	ArrayList eea=new ArrayList();
    	eea.add(new GenericFieldEIEL("n_circuitos",CampoFiltro.NUMERIC_CODE));
    	eea.add(new GenericFieldEIEL("estabilizador",CampoFiltro.NUMERIC_CODE));
    	camposTablaEspecificos.put("eea",eea);
    	
    	ArrayList pr=new ArrayList();
    	pr.add(new GenericFieldEIEL("clave",CampoFiltro.VARCHAR_CODE));
    	pr.add(new GenericFieldEIEL("estado",CampoFiltro.VARCHAR_CODE));
    	camposTablaEspecificos.put("PR",pr);

    	ArrayList ar=new ArrayList();
    	ar.add(new GenericFieldEIEL("clave",CampoFiltro.VARCHAR_CODE));
    	ar.add(new GenericFieldEIEL("estado",CampoFiltro.VARCHAR_CODE));
    	camposTablaEspecificos.put("AR",ar);

    	ArrayList rd=new ArrayList();
    	rd.add(new GenericFieldEIEL("estado",CampoFiltro.VARCHAR_CODE));
    	rd.add(new GenericFieldEIEL("material",CampoFiltro.VARCHAR_CODE));
    	camposTablaEspecificos.put("RD",rd);
    	
    	ArrayList rs=new ArrayList();
    	rs.add(new GenericFieldEIEL("clave",CampoFiltro.VARCHAR_CODE));
    	rs.add(new GenericFieldEIEL("estado",CampoFiltro.VARCHAR_CODE));
    	camposTablaEspecificos.put("RS",rs);
  
      	ArrayList cbs=new ArrayList();
      	cbs.add(new GenericFieldEIEL("clave",CampoFiltro.VARCHAR_CODE));
      	cbs.add(new GenericFieldEIEL("orden_cb",CampoFiltro.VARCHAR_CODE));
    	camposTablaEspecificos.put("CBS",cbs);
    	
	}
}
