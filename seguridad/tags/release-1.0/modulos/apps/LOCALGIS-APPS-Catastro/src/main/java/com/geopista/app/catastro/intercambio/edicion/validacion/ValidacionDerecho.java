package com.geopista.app.catastro.intercambio.edicion.validacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import com.geopista.app.catastro.intercambio.exception.DataException;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.Derecho;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.Titular;

public class ValidacionDerecho
{
	
	/**
	 * Método que comprueba que la persona con nif el que llega por parámetro exista.
	 * @param conn Conexión a la BBDD.
	 * @param nif Nif de la persona que se quiere validar.
	 * @return
	 * @throws Exception
	 */
	private boolean existePersona(Connection conn, String nif) throws Exception
	{
		String sSQL = "MCgetTitular"; 
		 //String sSQL= "select * from persona where nif=?;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, nif);
	            rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	return true;
	            }
	        }
	        catch (Exception e)
	        {
	            throw e;
	        }
	        finally
	        {
	            try{ps.close();}catch(Exception e){};
	            try{rs.close();}catch(Exception e){};
	        }
		return false;
	}
	
	
	/**
	 * Método que comprueba que el cargo esté asociado a otro titular a parte del que llega por parámetro.
	 * @param titular Titular a excluir de la búsqueda de titulares del Bieninmueble.
	 * @param bij BienInmueble del que se buscan los titulares.
	 * @return
	 */
	private boolean existeOtroTitularEnCargo(Titular titular, BienInmuebleJuridico bij)
	{
		ArrayList lstTitulares = bij.getLstTitulares();
		Derecho derechoTitular = titular.getDerecho();
			
		for (int i=0;i<lstTitulares.size();i++)
		{
			Titular tit = (Titular) lstTitulares.get(i);
			Derecho derechoTit = tit.getDerecho();
			
			if (!tit.getNif().equalsIgnoreCase(titular.getNif()) && (derechoTit.getCodDerecho().equalsIgnoreCase(derechoTitular.getCodDerecho())))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Método que comprueba que el cargo esté asociado a otros dos titulares a parte del que llega por parámetro.
	 * @param titular Titular a excluir de la búsqueda de titulares del Bieninmueble.
	 * @param bij BienInmueble del que se buscan los titulares.
	 * @return
	 */
	private boolean existeMasDeDosTitularEnCargo(Titular titular, BienInmuebleJuridico bij)
	{
		ArrayList lstTitulares = bij.getLstTitulares();
		Derecho derechoTitular = titular.getDerecho();
			
		for (int i=0;i<lstTitulares.size();i++)
		{
			Titular tit = (Titular) lstTitulares.get(i);
			Derecho derechoTit = tit.getDerecho();
			if (!titular.getNifConyuge().equals("")){
				if (!titular.getNif().equals(tit.getNifConyuge())){
					if (!tit.getNif().equalsIgnoreCase(titular.getNif()) && (derechoTit.getCodDerecho().equalsIgnoreCase(derechoTitular.getCodDerecho())))
						return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Método que comprueba que el BienInmueble tenga dos titulares, y que el nif del cónyuge de uno sea el nif del
	 * otro titular y viceversa.
	 * @param bij BienInmueble que contiene los titulares.
	 * @return
	 */
	private boolean existenDosTitulares(BienInmuebleJuridico bij)
	{
		ArrayList lstTitulares = bij.getLstTitulares();
		
		if ((lstTitulares != null)&&(lstTitulares.size() == 2))
		{
			Titular titular1 = (Titular) lstTitulares.get(0);
			Titular titular2 = (Titular) lstTitulares.get(1);
			
			if (titular1.getNifConyuge().equalsIgnoreCase(titular2.getNif()) && titular2.getNifConyuge().equalsIgnoreCase(titular1.getNif()))
				return true;
		}
		
		return false;		
	}
	
		
	/**
	 * Método que realiza la validación de un derecho.
	 * @param conn Conexión a la BBDD.
	 * @param titular Tiular que contiene el derecho a validar.
	 * @param bij Bieninmueble que contiene sal titular anterior.
	 * @return
	 * @throws DataException 
	 */
	private String validacionParcialDerecho(Connection conn, Titular titular, BienInmuebleJuridico bij) throws DataException{
		Derecho derecho = titular.getDerecho();
		

        if(derecho.getCodDerecho() == null)
            return "Error.T1";       

        //Tipo de derecho
        //JAVIER: deberia de crearse siempre el titular con valor de PR (propiedad) en el derecho
        if (!derecho.getCodDerecho().equalsIgnoreCase("CA") && !derecho.getCodDerecho().equalsIgnoreCase("DS") && !derecho.getCodDerecho().equalsIgnoreCase("US") && !derecho.getCodDerecho().equalsIgnoreCase("PR")
				&& !derecho.getCodDerecho().equalsIgnoreCase("NP") && !derecho.getCodDerecho().equalsIgnoreCase("DF"))
			return "Error.T1";
		
		//Porcentaje del derecho
		if (derecho.getPorcentajeDerecho() > 100)
			return "Error.T2";
		
		if (derecho.getPorcentajeDerecho() <= 0)
			return "Error.T9";
		
		//NIF del titular
        /*try {
			//JAVIER: creo que no hay que verificar que exista persona, pues esa tabla es
            //de catastro real y lo que estamos haciendo es añadir un titular a catastro temp
            //if (!existePersona(conn, titular.getNif()))
			//	return "Error.T6";
			
		} catch (Exception e) {throw new DataException(e);} */
		
		if (titular.getNif().startsWith("E")){
			//Debe figurar con el 100% del derecho
			if (derecho.getPorcentajeDerecho() != 100)
				return "Error.T20";
			
			if (titular.getNifCb()==null || !titular.getNifCb().equalsIgnoreCase(titular.getNif()))
				return "Error.T21";
		}
        else{
            if(titular.getNifCb() != null){
                if ((titular.getNifCb().length() > 0)&&(derecho.getPorcentajeDerecho() > 99) && (derecho.getPorcentajeDerecho() < 100)){
                    //es necesario que en el cargo haya otro titular con el mismo tipo de derecho
                    if (!existeOtroTitularEnCargo(titular, bij))
                        return "Error.T7";
                }
            }
        }
		
		//NIF del cónyuge
		if ((titular.getNifConyuge() != null) && (titular.getNifConyuge().length() > 0)){

            if (titular.getNif().equalsIgnoreCase(titular.getNifConyuge()))
				return "Error.T5";

            try {

                //JAVIER: creo que no hay que verificar que exista persona, pues esa tabla es
                //de catastro real y lo que estamos haciendo es añadir un titular a catastro temp
				//if (!existePersona(conn, titular.getNifConyuge()))
				//	return "Error.T3";
				
				if (existeMasDeDosTitularEnCargo(titular, bij))
					return "Error.T18";
				
				if (titular.getNifConyuge().length() > 0 && !existenDosTitulares(bij))
					return "Error.T10";
				
			}
            catch (Exception e) {
                throw new DataException(e);
            }
		}
		
		//NIF de la comunidad de bienes
		/*if ((titular.getNifCb() != null)&&(titular.getNifCb().length() > 0)){
			try {
                //JAVIER: creo que no hay que verificar que exista persona, pues esa tabla es
                //de catastro real y lo que estamos haciendo es añadir un titular a catastro temp
                //if (!existePersona(conn, titular.getNifCb()))
                //	return "Error.T4";
			}
			catch (Exception e) {throw new DataException(e);}
		}*/
		
		//Fecha Alteración
		if ((titular.getFechaAlteracion() == null)||(titular.getFechaAlteracion().length() < 1))
			return "Error.T8";
		
		return null;
	}
	
	/**
	 * Método que comprueba que si hay varios derechos del mismo tipo con el mismo NIF.
	 * @param titular Titular con el NIF.
	 * @param bij bienInmueble que contiene los derechos a validar.
	 * @return
	 */
	private boolean existeDerechosMismoTipoNif(Titular titular, BienInmuebleJuridico bij){
		//No puede haber varios derechos del mismo tipo con el mismo NIF
		ArrayList lstTitulares = bij.getLstTitulares();
		int contadorRepetidos = 0;
		
		for (int i=0;i<lstTitulares.size();i++){
			Titular tit = (Titular) lstTitulares.get(i);
			
			if (tit.getNif().equalsIgnoreCase(titular.getNif()) &&
                    (titular.getDerecho().getCodDerecho().equalsIgnoreCase(tit.getDerecho().getCodDerecho()))){
				contadorRepetidos += 1;
				if (contadorRepetidos > 1)
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Método que comprueba si un BienInmueble tiene varios derechos con el mismo tipo.
	 * @param titular titular que se está validando.
	 * @param bij bienInmueble que contiene los derechos.
	 * @return
	 */
	private boolean existenDerechosMismoTipo(Titular titular, BienInmuebleJuridico bij){
		ArrayList lstTitulares = bij.getLstTitulares();
		int contadorRepetidos = 0;
		
		for (int i=0;i<lstTitulares.size();i++){
			Titular tit = (Titular) lstTitulares.get(i);
			
			if (titular.getDerecho().getCodDerecho().equalsIgnoreCase(tit.getDerecho().getCodDerecho())){
				contadorRepetidos += 1;
				if (contadorRepetidos > 1)
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Se comprueba que si hay varios derechos con el mismo tipo que si un derecho tiene nif de la comunidad de 
	 * bienes, lo tengan todos,y que sean iguales.
	 * @param titular Titular sobre el que validar.
	 * @param bij BienInmueble que contiene los derechos a validar.
	 * @return
	 */
	private String validarMismosDerechos(Titular titular, BienInmuebleJuridico bij){
		//Si hay varios derechos del mismo tipo, no puede ocurrir que unos tengan el NIF de la comunidad de bienes y otros no, o que sea distinta comunidad de bienes.

        ArrayList lstTitulares = bij.getLstTitulares();
		int contadorMismoTipo = 0;
		int contadorNifCB = 0;
		
		for (int i=0;i<lstTitulares.size();i++){
			Titular tit = (Titular) lstTitulares.get(i);
			
			if (titular.getDerecho().getCodDerecho().equalsIgnoreCase(tit.getDerecho().getCodDerecho())){
				contadorMismoTipo += 1;
				
                if ((tit.getNifCb() != null)&&(tit.getNifCb().length() > 0))
					contadorNifCB += 1;

                if (tit.getNifCb() != null && !titular.getNifCb().equalsIgnoreCase(tit.getNifCb()))
					return "Error.T27";
			}
		}
		/*
		if ((contadorNifCB != 0)&&(contadorNifCB != lstTitulares.size()))
			return "Error.T27";*/
		
		return null;
	}
	
	/**
	 * Método que comprueba que la misma comunidad de bienes no tenga derechos distintos.
	 * @param titular Titular que se está validando
	 * @param bij BienInmueble que contiene los derechos.
	 * @return
	 */
	private String comprobarDerechosDistintoTipoMismoCB(Titular titular, BienInmuebleJuridico bij){
		//No puede haber varios derechos de distinto tipo con la misma comunidad de bienes, es decir, la misma comunidad de bienes no puede tener derechos distintos.

        ArrayList lstTitulares = bij.getLstTitulares();
		
		for (int i=0; i<lstTitulares.size();i++){
			Titular tit = (Titular) lstTitulares.get(i);

            if(!tit.getNif().equalsIgnoreCase(titular.getNif())){

                if (!tit.getDerecho().getCodDerecho().equalsIgnoreCase(titular.getDerecho().getCodDerecho())){
                    if(tit.getNifCb() != null && titular.getNifCb() != null &&
                       !tit.getNifCb().equals("") && !titular.getNifCb().equals("") &&
                            (tit.getNifCb().equalsIgnoreCase(titular.getNifCb())))
                        return "Error.T22";
                }
            }
        }
		
		return null;
	}
	
	/**
	 * Método que comprueba que exista el derecho.
	 * @param codDerecho Código del derecho a validar
	 * @param tiposDerecho la lista con los tipos de derechos
	 * @return
	 */
	private boolean existeTipoDerecho(String codDerecho, ArrayList tiposDerecho){
		for (int i=0;i < tiposDerecho.size();i++){
			String td = (String) tiposDerecho.get(i);
			
			if (codDerecho.equalsIgnoreCase(td))
				return true;
		}
		return false;
	}
	
	/**
	 * Método que se encarga de validar los tipos de derechos del BienInmueble.
	 * @param bij BienInmueble  con los derechos a validar.
	 * @return
	 */
	private String comprobarTiposDerecho(BienInmuebleJuridico bij){
		ArrayList lstTitulares = bij.getLstTitulares();
		ArrayList tiposDerechos = null;
		
		for (int i=0;i<lstTitulares.size();i++){
			Titular titular = (Titular) lstTitulares.get(i);
			
			if (tiposDerechos == null)
				tiposDerechos = new ArrayList();
			
			if (!existeTipoDerecho(titular.getDerecho().getCodDerecho(), tiposDerechos))
				tiposDerechos.add(titular.getDerecho().getCodDerecho());
		}
		if (tiposDerechos != null && tiposDerechos.size() > 3)
			return "Error.T12";
		
		return null;
	}
	
	
	/**
	 * Método que comprueba que la suma de porcentajes de los derechos no sea superior a 100, con un margen de 1.
	 * @param bij BienInmueble con los derechos.
	 * @return
	 */
	private boolean sumaPorcentajesSuperior100(BienInmuebleJuridico bij){
		float sumaDerechos = 0;

        ArrayList lstTitulares = bij.getLstTitulares();

		if (lstTitulares != null){

            for (int i=0;i<lstTitulares.size();i++){
				Titular titular = (Titular) lstTitulares.get(i);
				
				Derecho derecho = titular.getDerecho();
				
				sumaDerechos = sumaDerechos + derecho.getPorcentajeDerecho();
			}
		}
		
		//Por el margen de 1 --> 101
		if (sumaDerechos > 101)
			return true;
		
		return false;
	}

    /**
	 * Método que comprueba que la suma de porcentajes de los derechos no sea superior a 100, con un margen de 1.
	 * @param bij BienInmueble con los derechos.
	 * @return
	 */
    private boolean sumaPorcentajesProDerechoSuperior100(BienInmuebleJuridico bij){
		float sumaNudaPropiedad = 0; float sumaPropiedad = 0;
        float sumaUsufructo = 0; float sumaDerechoSuperficie = 0;
        float sumaConcesionAdministrativa = 0;
        float sumaDisfrutador = 0;

        ArrayList lstTitulares = bij.getLstTitulares();

		if (lstTitulares != null){

            for (int i=0;i<lstTitulares.size();i++){
				Titular titular = (Titular) lstTitulares.get(i);

				Derecho derecho = titular.getDerecho();

                if(derecho.getCodDerecho().equalsIgnoreCase("CA"))
                    sumaConcesionAdministrativa = sumaConcesionAdministrativa + derecho.getPorcentajeDerecho();
                else if(derecho.getCodDerecho().equalsIgnoreCase("DS"))
                    sumaDerechoSuperficie = sumaDerechoSuperficie + derecho.getPorcentajeDerecho();
                else if(derecho.getCodDerecho().equalsIgnoreCase("US"))
                    sumaUsufructo = sumaUsufructo + derecho.getPorcentajeDerecho();
                else if(derecho.getCodDerecho().equalsIgnoreCase("PR"))
                    sumaPropiedad = sumaPropiedad + derecho.getPorcentajeDerecho();
                else if(derecho.getCodDerecho().equalsIgnoreCase("NP"))
                    sumaNudaPropiedad = sumaNudaPropiedad + derecho.getPorcentajeDerecho();
                else if(derecho.getCodDerecho().equalsIgnoreCase("DS"))
                    sumaDisfrutador = sumaDisfrutador + derecho.getPorcentajeDerecho();

			}
		}

		//Por el margen de 1 --> 101
		if (sumaConcesionAdministrativa > 101  || sumaDerechoSuperficie > 101 ||
                sumaUsufructo > 101  || sumaPropiedad > 101  ||
                sumaNudaPropiedad > 101 || sumaDisfrutador > 101)
			return true;

		return false;
	}

    /**
	 * método que comprueba que la suma de porentajes de Propietarios y nuda propiedad sea 100
	 * con un margen de 1.
	 * @param bij BienInmueble con los derechos.
	 * @return
	 */
	private boolean sumaPorcentajesPRyNPIgual100(BienInmuebleJuridico bij){
		ArrayList lstTitulares = bij.getLstTitulares();
		float sumaDerechos = 0;
		
		if (lstTitulares != null){
			for (int i=0;i<lstTitulares.size();i++){
				Titular titular = (Titular) lstTitulares.get(i);
				
				Derecho derecho = titular.getDerecho();
				
				if (derecho.getCodDerecho().equalsIgnoreCase("PR") || derecho.getCodDerecho().equalsIgnoreCase("NP"))
					sumaDerechos = sumaDerechos + derecho.getPorcentajeDerecho();
			}
		}
		
		//Por el margen de 1 --> 101
		if ((sumaDerechos > 99) && (sumaDerechos < 101))
			return true;
		
		return false;
	}
	
	
	/**
	 * Método que comprueba que exista el repceptor del IBI
	 * @param bij BienInmueble con los titulares.
	 * @return
	 */
	private boolean existeReceptorIBI(BienInmuebleJuridico bij){
		ArrayList lstTitulares = bij.getLstTitulares();
		Titular receptorIBI = null;
		
		if (lstTitulares != null){
			for (int i=0;i<lstTitulares.size();i++){
				Titular titular = (Titular) lstTitulares.get(i);
				if (titular.getDerecho() != null && titular.getDerecho().getPorcentajeDerecho() >=0){		
					if (receptorIBI == null)
						receptorIBI = titular;
					else{
						if (titular.getDerecho().getPorcentajeDerecho() > receptorIBI.getDerecho().getPorcentajeDerecho())
							receptorIBI = titular;
						
						if (titular.getDerecho().getPorcentajeDerecho() == receptorIBI.getDerecho().getPorcentajeDerecho()){
							if (titular.getDerecho().getOrdinalDerecho() < receptorIBI.getDerecho().getOrdinalDerecho())
								receptorIBI = titular;
						}
					}
				}
			}
		}
		
		if (receptorIBI != null)
			return true;
		
		return false;
	}
	
	
	/**
	 * Método que comprueba que la sunma de los porcentajes sea menor que 99.
	 * @param bij BienInmueble con los derechos.
	 * @return
	 */
	private boolean sumaPorcentajesMenor99(BienInmuebleJuridico bij){
		ArrayList lstTitulares = bij.getLstTitulares();
		float sumaDerechosCA = 0;
		float sumaDerechosDS = 0;
		float sumaDerechosDF = 0;
		boolean existeDerechoCA = false;
		boolean existeDerechoDS = false;
		boolean existeDerechoDF = false;

        if (lstTitulares != null){
			for (int i=0;i<lstTitulares.size();i++){
				Titular titular = (Titular) lstTitulares.get(i);
				
				Derecho derecho = titular.getDerecho();
				
				if (derecho.getCodDerecho().equalsIgnoreCase("CA")){
					sumaDerechosCA += derecho.getPorcentajeDerecho();
					existeDerechoCA = true;
				}else if (derecho.getCodDerecho().equalsIgnoreCase("DS")){
					sumaDerechosDS += derecho.getPorcentajeDerecho();
					existeDerechoDS = true;
				}else if (derecho.getCodDerecho().equalsIgnoreCase("DF")){
					sumaDerechosDF += derecho.getPorcentajeDerecho();
					existeDerechoDF = true;
				}
			}
		}
		
		if (existeDerechoCA && sumaDerechosCA < 99)
			return true;
		if (existeDerechoDS && sumaDerechosCA < 99)
			return true;
		if (existeDerechoDF && sumaDerechosCA < 99)
			return true;
		
		return false;
	}
	
	
	/**
	 * Método que comprueba si existen derechos de nuda propiedad y de usufructo al mismo tiempo.
	 * @param bij Bieninmueble con los derechos.
	 * @return
	 */
	private boolean existeNPyUS(BienInmuebleJuridico bij){
		ArrayList lstTitulares = bij.getLstTitulares();
		boolean np = false;
		boolean us = false;
		
		if (lstTitulares != null){
			for (int i=0;i<lstTitulares.size();i++){
				Titular titular = (Titular) lstTitulares.get(i);
				
				Derecho derecho = titular.getDerecho();
				
				if (derecho.getCodDerecho().equalsIgnoreCase("NP") && (derecho.getPorcentajeDerecho() > 0))
					np = true;
				
				if (derecho.getCodDerecho().equalsIgnoreCase("US") && (derecho.getPorcentajeDerecho() > 0))
					us = true;
			}
		}
		
		if ((np && !us) || (!np && us))
			return false;
		
		return true;
	}
	
	/**
	 * Método que comprueba que los porcentajes de nuda propiedad  y ususfructo coincidan con un margen de 1.
	 * @param bij BienInmueble que contiene los derechos.
	 * @return
	 */
	private boolean igualesNPyUS(BienInmuebleJuridico bij)
	{
		ArrayList lstTitulares = bij.getLstTitulares();
		float np = 0;
		float us = 0;
		
		if (lstTitulares != null)
		{
			for (int i=0;i<lstTitulares.size();i++)
			{
				Titular titular = (Titular) lstTitulares.get(i);
				
				Derecho derecho = titular.getDerecho();
				
				if (derecho.getCodDerecho().equalsIgnoreCase("NP"))
					np = derecho.getPorcentajeDerecho();
								
				if (derecho.getCodDerecho().equalsIgnoreCase("US"))
					us = derecho.getPorcentajeDerecho();;
			}
		}
		
		if ((np - us) <= 1 && (np - us) >= -1)
			return true;
		
		return false;
	}
	
	/**
	 * Método que comprueba que la suma de porcentajes de propietarios sea menos que 99.
	 * @param bij
	 * @return
	 */
	private boolean porcentajePRMenor99(BienInmuebleJuridico bij)
	{
		ArrayList lstTitulares = bij.getLstTitulares();
		float pr = 0;
			
		if (lstTitulares != null)
		{
			for (int i=0;i<lstTitulares.size();i++)
			{
				Titular titular = (Titular) lstTitulares.get(i);
				
				Derecho derecho = titular.getDerecho();
				
				if (derecho.getCodDerecho().equalsIgnoreCase("PR"))
					pr += derecho.getPorcentajeDerecho();
			}
		}
		
		if (pr < 99)
			return true;
		
		return false;
	}
	
	/**
	 * Método que comprueba que el porcentaje de nuda propiedad sea 0.
	 * @param bij
	 * @return
	 */
	private boolean porcentajeNP0(BienInmuebleJuridico bij){
		ArrayList lstTitulares = bij.getLstTitulares();
		float np = 0;
			
		if (lstTitulares != null){
			for (int i=0;i<lstTitulares.size();i++){
				Titular titular = (Titular) lstTitulares.get(i);
				
				Derecho derecho = titular.getDerecho();
				
				if (derecho.getCodDerecho().equalsIgnoreCase("NP"))
					np += derecho.getPorcentajeDerecho();
			}
		}
		
		if (np == 0)
			return true;
		
		return false;
	}
	
	/**
	 * Método que comprueba que existan varios derechos de propietarios.
	 * @param bij
	 * @return
	 */
	private boolean existenTitularesPR(BienInmuebleJuridico bij){
		ArrayList lstTitulares = bij.getLstTitulares();
		int contadorPR = 0;
			
		if (lstTitulares != null){
			for (int i=0;i<lstTitulares.size();i++){
				Titular titular = (Titular) lstTitulares.get(i);
				
				Derecho derecho = titular.getDerecho();
				
				if (derecho.getCodDerecho().equalsIgnoreCase("PR"))
					contadorPR += 1;
			}
		}
		
		if (contadorPR > 1)
			return true;
		
		return false;
	}
	
	/**
	 * Método que se encarga de la validación genérica de los derechos del BienInmueble.
	 * @param conn Conexión a la BBDD.
	 * @param bij BienInmueble con los derechos.
	 * @return
	 */
	private String validacionesGenericasDerechos(Connection conn, BienInmuebleJuridico bij){
		String result = null;

        //No puede haber varios derechos del mismo tipo con el mismo NIF
		ArrayList lstTitulares = bij.getLstTitulares();
		
		for (int i=0;i<lstTitulares.size();i++){
			Titular titular = (Titular) lstTitulares.get(i);
			
			//NIF
			if (existeDerechosMismoTipoNif(titular, bij))
				return "Error.T15";
			
			
			//NIF de la comunidad de bienes
			if (existenDerechosMismoTipo(titular, bij)){
				//No puede ocurrir que unos tengan el NIF de la comunidad de bienes y otros no, o que sea distinta comunidad de bienes.
				result = validarMismosDerechos(titular, bij);
				if (result != null)
					return result;
			}
			
			result = comprobarDerechosDistintoTipoMismoCB(titular, bij);
			if (result != null)
				return result;
			
		}

        //Tipos de derecho
        result = comprobarTiposDerecho(bij);
        if (result != null)
            return result;

        //Porcentajes
        //if (sumaPorcentajesSuperior100(bij))
        if(sumaPorcentajesProDerechoSuperior100(bij))
            return "Error.T16";
		
		if (sumaPorcentajesMenor99(bij))
			return "Error.T13";
		
		if (!existeNPyUS(bij))
			return "Error.T23";
		
		if (!igualesNPyUS(bij))
			return "Error.T24";
		
		if (!sumaPorcentajesPRyNPIgual100(bij))
			return "Error.T25";
		
		if (porcentajePRMenor99(bij) && porcentajeNP0(bij)){
			if (!existenTitularesPR(bij))
				return "Error.T13";
		}

		//Receptor IBI
		if (!existeReceptorIBI(bij))
			return "Error.T14";
		
		return result;
	}
	
	/**
	 * Método que comprueba que exista el representante asociado al Bieninmueble.
	 * @param conn conexión a la BBDD.
	 * @param nif Nif del representante.
	 * @return
	 * @throws Exception
	 */
	private boolean existeRepresentanteEnCargo(Connection conn, String nif) throws Exception
	{
		 String sSQL = "MCgetRepresentante"; 
		 //String sSQL= "select * from representante where nifrepresentante=?;
	     PreparedStatement ps= null;
	     ResultSet rs= null;
	  	     
	     try
	        {
	            ps= conn.prepareStatement(sSQL);
	            ps.setString(1, nif);
	            rs= ps.executeQuery();
	            if(rs.next())
	            {
	            	return true;
	            }
	        }
	        catch (Exception e)
	        {
	            throw e;
	        }
	        finally
	        {
	            try{ps.close();}catch(Exception e){};
	            try{rs.close();}catch(Exception e){};
	        }
		return false;
	}

	/**
	 * Método que se encarga de la validación particular del BienInmueble que llega por parámetro.
	 * @param conn Conexión a la BBDD.
	 * @param bij BienInmueble a validar.
	 * @return
	 * @throws DataException 
	 */
	public String validacionParcialBI(Connection conn, BienInmuebleJuridico bij) throws DataException{
		//Ausencia de derechos
		if ((bij.getLstTitulares() == null)||(bij.getLstTitulares().size() < 1))
			return "Error.G145";

        String msgError = null;
        for (int i=0; i<bij.getLstTitulares().size();i++){
			Titular titular = (Titular) bij.getLstTitulares().get(i);
			msgError = validacionParcialDerecho(conn, titular, bij);
			if (msgError != null)
				return msgError;
		}
				
		return msgError;
	}


    /**
	 * método que se encarga de la validación de los derechos. Tanto particular como generalmente.
	 * @param conn Conexión a la BBDD.
	 * @param finca Finca con los Bienesinmuebles y derechos a validar.
	 * @return
	 * @throws DataException 
	 */
	public String validacionDerechos(Connection conn, FincaCatastro finca) throws DataException
	{
		String mensError = null;
		ArrayList lstBI = finca.getLstBienesInmuebles();
		
		if ((lstBI != null)&&(lstBI.size() > 0))
		{
			for (int i=0; i< lstBI.size();i++)
			{
				BienInmuebleJuridico bij = (BienInmuebleJuridico)lstBI.get(i);
				
                mensError = validacionParcialBI(conn, bij);
                if(mensError != null) return mensError;
				
                mensError = validacionesGenericasDerechos(conn, bij);
                if(mensError != null) return mensError;
								
				//Control del representante
				try {
					Persona representante = bij.getBienInmueble().getRepresentante();
					if ((representante != null)&&(representante.getNif() == null)||(representante.getNif().length() < 1))
						return "Error.G147";
					
					//if ((representante == null)||(!existeRepresentanteEnCargo(conn, representante.getNif())))
					if(representante == null)
							return "Error.G146M";
					
				} catch (Exception e) {
					throw new DataException(e);
				}
				
			}
		}
		
		return null;
	}

    //METODO JAVIER
    public String validacionDerechos(Connection conn, ArrayList lstBI) throws DataException{

        String mensError = null;

        if ((lstBI != null)&&(lstBI.size() > 0)) {

            for (int i=0; i< lstBI.size();i++){

                BienInmuebleJuridico bij = (BienInmuebleJuridico)lstBI.get(i);

                mensError = validacionParcialBI(conn, bij);
                if(mensError != null) return mensError;

                mensError = validacionesGenericasDerechos(conn, bij);
                if(mensError != null) return mensError;
                
                //Control del representante
                try {
                    Persona representante = bij.getBienInmueble().getRepresentante();
                    if ((representante != null)&&(representante.getNif() == null))
                        return "Error.G147";

                    if ((representante != null)&&(representante.getNif().length() < 1))
                        return "Error.G147";

                    //JAVIER: No hay que buscar el representante en cargo en cat real sino en cat temp.
                    //if ((representante == null)||(!existeRepresentanteEnCargo(conn, representante.getNif())))
                    if(representante == null)
                        return "Error.G146M";

                    boolean isRepresentanteEnCargo = false;

                    for(Iterator it = bij.getLstTitulares().iterator(); it.hasNext();){
                        Titular tit = (Titular)it.next();

                        if(tit.getNif().equals(representante.getNif())){
                            isRepresentanteEnCargo = true;
                            break;
                        }
                    }

                    if(!isRepresentanteEnCargo)
                        return "Error.G146M";

                }
                catch (Exception e) {
                    throw new DataException(e);
                }

            }
        }

        return null;
    }

}
