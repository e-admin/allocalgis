package com.geopista.app.catastro.intercambio.importacion.utils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.EntidadGeneradora;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.vividsolutions.jump.util.Blackboard;

public class ImportarResponsePruebas {
	
	public static void setExpediente(Blackboard blackboard){
		
		Expediente expediente = new Expediente();
		expediente.setAnnoExpedienteAdminOrigenAlteracion(2007);
		expediente.setAnnoExpedienteGerencia(new Integer(2007));
		expediente.setAnnoProtocoloNotarial("2007");
		expediente.setCodigoDescriptivoAlteracion("001");
		expediente.setCodigoEntidadRegistroDGCOrigenAlteracion(new Integer(901));
		expediente.setCodigoINEmunicipio("222");
		expediente.setCodNotaria("777");
		expediente.setCodPoblacionNotaria("6666");
		expediente.setCodProvinciaNotaria("44");
		expediente.setDescripcionAlteracion("DESCRIPCION");
		DireccionLocalizacion direccion = new DireccionLocalizacion();
		direccion.setApartadoCorreos(47010);
		direccion.setBloque("C");
		direccion.setCodigoMunicipioDGC("111");
		direccion.setCodigoPostal("47010");
		direccion.setCodigoVia(123);
		direccion.setCodMunOrigenAgregacion("999");
		direccion.setCodParaje("444");
		direccion.setCodParcela("456");
		direccion.setCodPoligono("345");
		direccion.setCodZonaConcentracion("777");
		direccion.setDireccionNoEstructurada("DIRECCION");
		direccion.setDistrito("13");
		direccion.setEscalera("C");
		direccion.setIdLocalizacion(1234);
		direccion.setIdVia(14);
		direccion.setKilometro(100);
		direccion.setMunicipioINE("222");
		direccion.setNombreEntidadMenor("NOMBRE");
		direccion.setNombreMunicipio("MUNICIPIO");
		direccion.setNombreParaje("PARAJE");
		direccion.setNombreProvincia("PROVINCIA");
		direccion.setNombreVia("VIA");
		direccion.setPlanta("2");
		direccion.setPrimeraLetra("C");
		direccion.setPrimerNumero(2);
		direccion.setProvinciaINE("22");
		direccion.setPuerta("C");
		direccion.setSegundaLetra("D");
		direccion.setSegundoNumero(3);
		direccion.setTipoVia("R");
		
		expediente.setDireccionPresentador(direccion);
		
		EntidadGeneradora entidadGeneradora = new EntidadGeneradora();
		entidadGeneradora.setCodigo(101);
		entidadGeneradora.setDescripcion("DESCRIPCION");
		entidadGeneradora.setIdEntidadGeneradora(12);
		entidadGeneradora.setNombre("NOMBRE");
		entidadGeneradora.setTipo("A");
		
		expediente.setEntidadGeneradora(entidadGeneradora);
		expediente.setExisteDeclaracionAlteracion(true);
		expediente.setExistenciaInformacionGrafica("S");
		Date fechaAlteracion = new Date(2007,04,25);
		Date fechaDeCierre = new Date(2007,04,25);
		Date fechaMovimiento = new Date(2007,04,25);
		Date fechaRegistro = new Date(2007,04,25);
		expediente.setFechaAlteracion(fechaAlteracion);
		expediente.setFechaDeCierre(fechaDeCierre);
		expediente.setFechaMovimiento(fechaMovimiento);
		expediente.setFechaRegistro(fechaRegistro);
		Time horaMovimiento =  new Time(3600);
		expediente.setHoraMovimiento("11:11:00");
		expediente.setIdEstado(123);
		expediente.setIdExpediente(111);
		expediente.setIdMunicipio(222);
		expediente.setIdTecnicoCatastro("555");
		expediente.setInfoDocumentoOrigenAlteracion("INFODOC");
		
		ArrayList listaReferencias = new ArrayList();
		
		FincaCatastro fincaCatastro1 = new FincaCatastro();
		fincaCatastro1.setBICE("BI");
		fincaCatastro1.setCodDelegacionMEH("11");
		fincaCatastro1.setCodMunicipioDGC("222");
		ArrayList cultivos1 = new ArrayList();
		fincaCatastro1.setCultivos(cultivos1);
		//fincaCatastro1.setDatosEconomicos(datosEconomicos);
		ReferenciaCatastral refFinca1 = new ReferenciaCatastral("11111111111111");
		fincaCatastro1.setRefFinca(refFinca1);
		
		FincaCatastro fincaCatastro2 = new FincaCatastro();
		fincaCatastro2.setBICE("BI");
		fincaCatastro2.setCodDelegacionMEH("11");
		fincaCatastro2.setCodMunicipioDGC("222");
		ArrayList cultivos2 = new ArrayList();
		fincaCatastro2.setCultivos(cultivos2);
		//fincaCatastro1.setDatosEconomicos(datosEconomicos);
		ReferenciaCatastral refFinca2 = new ReferenciaCatastral("22222222222222");
		fincaCatastro2.setRefFinca(refFinca2);
		
		listaReferencias.add(fincaCatastro1);
		listaReferencias.add(fincaCatastro2);
		
		expediente.setListaReferencias(listaReferencias);
		
		expediente.setM_Direccion(direccion);
		
		expediente.setNifPresentador("77777777A");
		expediente.setNombreCompletoPresentador("NOMBREPRESENTADOR");
		expediente.setNumBienesInmueblesCaractEsp(1);
		expediente.setNumBienesInmueblesRusticos(2);
		expediente.setNumBienesInmueblesUrbanos(3);
		expediente.setNumeroExpediente("333");
		expediente.setProtocoloNotarial("666666");
		expediente.setReferenciaExpedienteAdminOrigen("99999999");
		expediente.setReferenciaExpedienteGerencia("REFERENCIADMIN");
		expediente.setTipoDeIntercambio("I");
		expediente.setTipoDocumentoOrigenAlteracion("P");
		TipoExpediente tipoExpediente = new TipoExpediente();
		tipoExpediente.setCodigoTipoExpediente("CDOM");
		expediente.setTipoExpediente(tipoExpediente);
		
		blackboard.put("expediente", expediente);
		
		return;
	}

}
