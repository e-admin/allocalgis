/**
 * FincaCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosFinca;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosFinca;

public class FincaCatastro implements Serializable {
    
    private int idFinca;
    /**
     * Identificador único de la finca = Referencia catastral
     */
    private ReferenciaCatastral refFinca;
    private DireccionLocalizacion dirParcela = new DireccionLocalizacion();
    private DatosFisicosFinca datosFisicos = new DatosFisicosFinca();
    private DatosEconomicosFinca datosEconomicos = new DatosEconomicosFinca();
    
   
    public static final String TIPO_MOVIMIENTO_FINAL ="F";
    public static final String TIPO_MOVIMIENTO_ALTA ="A";
    public static final String TIPO_MOVIMIENTO_BAJA ="B";
    public static final String TIPO_MOVIMIENTO_MODIF ="M";

    public String TIPO_MOVIMIENTO = TIPO_MOVIMIENTO_FINAL;
    public String TIPO_MOVIMIENTO_DELETE = TIPO_MOVIMIENTO_FINAL;
    // marca si el elemento ha sido modificado,borrado o es nuevo
    // para activar los campos de edicion en la informacion catastral
    private boolean elementoModificado;
    
    private String identificadorDialogo;
    private boolean actualizadoOVC;
    
	/**
     * BICE
     */
    private String BICE;
    
    /**
     * Datos del expediente catastral
     */
    private Expediente datosExpediente = new Expediente();
    
    /**
     * Cultivos
     */
    private ArrayList cultivos = new ArrayList();
    /**
     * Lista de elementos entre los que se realiza el reparto
     */
    private ArrayList lstReparto =  new ArrayList();
    
    
    /**
     * Código de la delegación del MEH
     */
    private String codDelegacionMEH;
    
    /**
     * Código del municipio según la DGC
     */
    private String codMunicipioDGC;
    
    /**
     * Lista de bienes inmuebles
     */
    private ArrayList lstBienesInmuebles = new ArrayList();
    private ArrayList lstBienesInmueblesAltaModif = new ArrayList();
	private ArrayList lstBienesInmueblesBaja = new ArrayList();
	private ArrayList lstBienesInmueblesAltaModifConTitular = new ArrayList();
	private ArrayList lstBienesInmueblesAltaModifSinTitular = new ArrayList();
    
    /**
     * Lista de cultivos
     */
    private ArrayList lstCultivos = new ArrayList();
    
    /**
     * Lista de locales
     */
    private ArrayList lstConstrucciones = new ArrayList();
    
    /**
     * Lista de Suelos
     */
    private ArrayList lstSuelos = new ArrayList();
    
    /**
     * Lista de Unidades Constructivas
     */
    private ArrayList lstUnidadesConstructivas = new ArrayList();
    
    /**
     * FX-CC: Información gráfica asociada a la finca catastral
     */
    private FX_CC fxcc;
    
    /**
     * lstImagenes: Lista de Imágenes asociadas a la finca catastral
     */
    private ArrayList lstImagenes = null;
    
    
    /**
     * Booleano que indica si la parcela se ha eliminado del expediente
     * en la pantalla de edición.
     */
    private boolean delete = false;
    
    /**
     * Constructor por defecto
     *
     */
    public FincaCatastro()
    {
        
    }   
    
    
    
    public String getTIPO_MOVIMIENTO() {
		return TIPO_MOVIMIENTO;
		//return "F";
	}



	public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
		
		if(!(Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
			//expediente de variaciones
			if(TIPO_MOVIMIENTO. equals(TIPO_MOVIMIENTO_ALTA) || 
					TIPO_MOVIMIENTO. equals(TIPO_MOVIMIENTO_MODIF)){
				//se marca que el elemento esta modificado, por lo tanto es editable
				setElementoModificado(true);
				
			}
			else if(TIPO_MOVIMIENTO. equals(TIPO_MOVIMIENTO_BAJA)){
				setElementoModificado(false);
			}
		}
	}



	/**
     * @return Returns the datosEconomicos.
     */
    public DatosEconomicosFinca getDatosEconomicos()
    {
        return datosEconomicos;
    }
    
    /**
     * @param datosEconomicos The datosEconomicos to set.
     */
    public void setDatosEconomicos(DatosEconomicosFinca datosEconomicos)
    {
        this.datosEconomicos = datosEconomicos;
    }
    
    /**
     * @return Returns the datosFisicos.
     */
    public DatosFisicosFinca getDatosFisicos()
    {
        return datosFisicos;
    }
    
    /**
     * @param datosFisicos The datosFisicos to set.
     */
    public void setDatosFisicos(DatosFisicosFinca datosFisicos)
    {
        this.datosFisicos = datosFisicos;
    }
    
    /**
     * @return Returns the dirParcela.
     */
    public DireccionLocalizacion getDirParcela()
    {
        return dirParcela;
    }
    
    /**
     * @param dirParcela The dirParcela to set.
     */
    public void setDirParcela(DireccionLocalizacion dirParcela)
    {
        this.dirParcela = dirParcela;
    }
    
    /**
     * @return Returns the refFinca.
     */
    public ReferenciaCatastral getRefFinca()
    {
        return refFinca;
    }
    
    /**
     * @param refFinca The refFinca to set.
     */
    public void setRefFinca(ReferenciaCatastral refFinca)
    {
        this.refFinca = refFinca;
    }
    
    /**
     * @return Returns the datosExpediente.
     */
    public Expediente getDatosExpediente()
    {
        return datosExpediente;
    }
    
    /**
     * @param datosExpediente The datosExpediente to set.
     */
    public void setDatosExpediente(Expediente datosExpediente)
    {
        this.datosExpediente = datosExpediente;
    }
    
    /**
     * @return Returns the cultivos.
     */
    public ArrayList getCultivos()
    {
        return cultivos;
    }
    
    /**
     * @param cultivos The cultivos to set.
     */
    public void setCultivos(ArrayList cultivos)
    {
        this.cultivos = cultivos;
    }
    
    /**
	 * @return Returns the lstReparto.
	 */
	public ArrayList getLstReparto() {
		return lstReparto;
	}

	/**
	 * @param lstReparto The lstReparto to set.
	 */
	public void setLstReparto(ArrayList lstReparto) {
		this.lstReparto = lstReparto;
	}
	
	public void addLstUnidadConstructiva(UnidadConstructivaCatastro uc)
	{
		lstUnidadesConstructivas.add(uc);
	}

	public void addCultivo(Cultivo cultivo)
    {
        lstCultivos.add(cultivo);
    }
    
   
    /**
     * @return Returns the bICE.
     */
    public String getBICE() {
        return BICE;
    }
    /**
     * @param bice The bICE to set.
     */
    public void setBICE(String bice) {
        BICE = bice;
    }
    
    
    
    /**
     * @return Returns the codDelegacionMEH.
     */
    public String getCodDelegacionMEH()
    {
        return codDelegacionMEH;
    }
    /**
     * @param codDelegacionMEH The codDelegacionMEH to set.
     */
    public void setCodDelegacionMEH(String codDelegacionMEH)
    {
        this.codDelegacionMEH = codDelegacionMEH;
    }
    /**
     * @return Returns the codMunicipioDGC.
     */
    public String getCodMunicipioDGC()
    {
        return codMunicipioDGC;
    }
    /**
     * @param codMunicipioDGC The codMunicipioDGC to set.
     */
    public void setCodMunicipioDGC(String codMunicipioDGC)
    {
        this.codMunicipioDGC = codMunicipioDGC;
    }
    /**
     * @return Returns the lstBienesInmuebles.
     */
    public ArrayList getLstBienesInmuebles()
    {
        return lstBienesInmuebles;
    }

    /**
     * @param lstBienes The lstBienesInmuebles to set.
     */
    public void setLstBienesInmuebles(ArrayList lstBienes)
    {
        this.lstBienesInmuebles = lstBienes;
    }

    public void addBienInmueble(BienInmuebleCatastro bien)
    {
    	this.lstBienesInmuebles.add(bien);
    }
    
    /**
     * @return Returns the lstCultivos.
     */
    public ArrayList getLstCultivos()
    {
        return lstCultivos;
    }

    /**
     * @param lstCultivos The lstCultivos to set.
     */
    public void setLstCultivos(ArrayList lstCultivos)
    {
        this.lstCultivos = lstCultivos;
    }

    /**
     * @return Returns the lstConstrucciones.
     */
    public ArrayList getLstConstrucciones()
    {
        return lstConstrucciones;
    }

    /**
     * @param lstLocales The lstConstrucciones to set.
     */
    public void setLstConstrucciones(ArrayList lstLocales)
    {
        this.lstConstrucciones = lstLocales;
    }

    /**
     * @return Returns the lstSuelos.
     */
    public ArrayList getLstSuelos()
    {
        return lstSuelos;
    }

    /**
     * @param lstSuelos The lstSuelos to set.
     */
    public void setLstSuelos(ArrayList lstSuelos)
    {
        this.lstSuelos = lstSuelos;
    }

    /**
     * @return Returns the lstUnidadesConstructivas.
     */
    public ArrayList getLstUnidadesConstructivas()
    {
        return lstUnidadesConstructivas;
    }

    /**
     * @param lstUnidadesConstructivas The lstUnidadesConstructivas to set.
     */
    public void setLstUnidadesConstructivas(ArrayList lstUnidadesConstructivas)
    {
        this.lstUnidadesConstructivas = lstUnidadesConstructivas;
    }
    
    public void addUnidadConstructiva(ConstruccionCatastro construccion)
    {
    	lstConstrucciones.add(construccion);
    }

    /**
	 * @return Returns the fxcc.
	 */
	public FX_CC getFxcc() {
		return fxcc;
	}

	/**
	 * @param fxcc The fxcc to set.
	 */
	public void setFxcc(FX_CC fxcc) {
		this.fxcc = fxcc;
	}

	/**
     * @return Returns the idFinca.
     */
    public int getIdFinca()
    {
        return idFinca;
    }

    /**
     * @param idFinca The idFinca to set.
     */
    public void setIdFinca(int idFinca)
    {
        this.idFinca = idFinca;
    }

    /**
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }

    
    public FincaCatastro clone(FincaCatastro f)
    {
    	FincaCatastro fincaClone = new FincaCatastro();
    	
    	fincaClone.setBICE(f.getBICE());
    	fincaClone.setCodDelegacionMEH(f.getCodDelegacionMEH());
    	fincaClone.setCodMunicipioDGC(f.getCodMunicipioDGC());
    	fincaClone.setCultivos(f.getCultivos());
    	
    	fincaClone.setDatosEconomicos(new DatosEconomicosFinca());
    	fincaClone.getDatosEconomicos().setAnioAprobacion(f.getDatosEconomicos().getAnioAprobacion());
    	fincaClone.getDatosEconomicos().setCodigoCalculoValor(f.getDatosEconomicos().getCodigoCalculoValor());
    	fincaClone.getDatosEconomicos().setIndInfraedificabilidad(f.getDatosEconomicos().getIndInfraedificabilidad());
    	fincaClone.getDatosEconomicos().setIndModalidadReparto(f.getDatosEconomicos().getIndModalidadReparto());
    	fincaClone.getDatosEconomicos().setPoligonoCatastralValor(f.getDatosEconomicos().getPoligonoCatastralValor());
    	
    	fincaClone.setDatosFisicos(f.getDatosFisicos());
    	fincaClone.setDirParcela(f.getDirParcela());
    	fincaClone.setFxcc(f.getFxcc());
    	fincaClone.setIdFinca(f.getIdFinca());
    	
    	if (f.getLstBienesInmuebles() != null)
    	{
	    	ArrayList lstBI = new ArrayList();
	    	for (int i=0; i<f.getLstBienesInmuebles().size();i++)
	    	{
	    		BienInmuebleJuridico bij = (BienInmuebleJuridico) f.getLstBienesInmuebles().get(i);
	    		BienInmuebleJuridico bijNuevo = bij.clone(bij);
	    		
	    		lstBI.add(bijNuevo);
	       	}
	    	fincaClone.setLstBienesInmuebles(lstBI);
    	}
    	    	
    	if (f.getLstConstrucciones() != null)
    	{
    		ArrayList lstConstrucciones = new ArrayList();
    		for (int i=0;i<f.getLstConstrucciones().size();i++)
    		{
    			ConstruccionCatastro construccion = (ConstruccionCatastro) f.getLstConstrucciones().get(i);
    			ConstruccionCatastro construccionNueva = construccion.clone(construccion);
    			
    			lstConstrucciones.add(construccionNueva);
    		}
    		fincaClone.setLstConstrucciones(lstConstrucciones);
    	}
    	
    	if (f.getLstCultivos() != null)
    	{
    		ArrayList lstCultivos = new ArrayList();
    		for (int i=0;i<f.getLstCultivos().size();i++)
    		{
    			Cultivo cultivo = (Cultivo) f.getLstCultivos().get(i);
    			Cultivo cultivoNuevo = cultivo.clone(cultivo);
    			
    			lstCultivos.add(cultivoNuevo);
    		}
    		fincaClone.setLstCultivos(lstCultivos);
    	}
    	
    	if (f.getLstReparto() != null)
    	{
    		ArrayList lstRepartos = new ArrayList();
    		for (int i=0;i<f.getLstReparto().size();i++)
    		{
    			RepartoCatastro reparto = (RepartoCatastro) f.getLstReparto().get(i);
    			RepartoCatastro repartoNuevo = reparto.clone(reparto);
    			
    			lstRepartos.add(repartoNuevo);
    		}
    		fincaClone.setLstReparto(lstReparto);
    	}
   
    	
    	fincaClone.setLstSuelos(f.getLstSuelos());
      	fincaClone.setLstUnidadesConstructivas(f.getLstUnidadesConstructivas());
   
      	fincaClone.setRefFinca(new ReferenciaCatastral(f.getRefFinca().getRefCatastral()));
    	fincaClone.setTIPO_MOVIMIENTO(f.getTIPO_MOVIMIENTO());
    	
    	return fincaClone;
    }
    
    
    public Boolean isNotDelete()
    {
        return new Boolean(!delete);
    }

    public Boolean getDelete() {
		return new Boolean(isDelete());
	}
    
    public boolean isDelete() {
    	
    	if (TIPO_MOVIMIENTO_DELETE!=null){
    		if( TIPO_MOVIMIENTO_DELETE.equals("B")){
    			delete = true;
    		}
    		else{
    			delete = false;
    		}
    	}
		return delete;
	}



	public void setDelete(boolean delete) {
		this.delete = delete;
	}



	public void setData(FincaCatastro fc)
    {
        this.setBICE(fc.getBICE());
        this.setCodDelegacionMEH(fc.getCodDelegacionMEH());
        this.setCodMunicipioDGC(fc.getCodMunicipioDGC());
        this.setDatosEconomicos(fc.getDatosEconomicos());
        this.setDatosExpediente(fc.getDatosExpediente());
        this.setDatosFisicos(fc.getDatosFisicos());
        this.setDirParcela(fc.getDirParcela());
        this.setRefFinca(fc.getRefFinca());
        
    }

    public void setTIPO_MOVIMIENTO_DELETE(String TIPO_MOVIMIENTO_DELETE)
    {
        this.TIPO_MOVIMIENTO_DELETE = TIPO_MOVIMIENTO_DELETE;
    }

    public String getTIPO_MOVIMIENTO_DELETE()
    {
        return TIPO_MOVIMIENTO_DELETE;
    }



	public ArrayList getLstImagenes() {
		return lstImagenes;
	}

	public void setLstImagenes(ArrayList lstImagenes) {
		this.lstImagenes = lstImagenes;
	}
	
	
    public boolean isElementoModificado() {
		return elementoModificado;
	}
	public void setElementoModificado(boolean elementoModificado) {
		this.elementoModificado = elementoModificado;
	}
	
	public boolean isCatastroTemporal(){
		
		if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
			boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
			if(isCatTemporal){
				// se ecribe el xml en catastro temporal tanto para expedientes de situaciones finales
				//como expedientes de variaciones
				return true;
			}
			else{
				// se genera el fin de entrada
				if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
					// si el expediente es de situaciones finales, se utilizan los tag de situaciones finales
					return true;
				}
				else{
					// para generar el fin de entrada con los elementos modificados ya que el expediente
					// es de variaciones
					return false;
				}
			}
		}
		else{
			return false;
		}
		//return true;
	}
	public boolean isNotCatastroTemporal(){
		if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
			boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
			if(isCatTemporal){
				// al escribir en catastro_temporal no se usan los tags que identifican a las variaciones
				return false;
			}
			else{
				// se escribe el fin de entrada en un expediente de variaciones
				if((Boolean)AppContext.getApplicationContext().getBlackboard().get("expSitFinales")){
					// el expediente es de situaciones finales, no se incluyen los tags de variaciones
					return false;
				}
				else{
					// el expediente es de variaciones, se incluyen los tags de variaciones
					return true;
				}

			}
		}
		else{
			return true;
		}
		
		//return false;
	}
	
	public Boolean isElemModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean elementoModificado = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) || 
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF) ||
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			elementoModificado = true;
		}
		else{
			elementoModificado = false;
		}
		elementoModificado = true;
		return elementoModificado;
	}
	
	public Boolean isElemSueloModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		// si hay algun suelo modificacado o dado de alta o de baja
		Boolean algunElementoModificado = false;
		if(getLstSuelos() != null && !getLstSuelos().isEmpty()){
			for(int i=0; i<getLstSuelos().size() && !algunElementoModificado; i++){
				SueloCatastro suelo = (SueloCatastro)getLstSuelos().get(i);
				if(suelo.getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_ALTA) ||
						suelo.getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_MODIF) ||
						suelo.getTIPO_MOVIMIENTO().equals(SueloCatastro.TIPO_MOVIMIENTO_BAJA)){
					
					algunElementoModificado = true;
					
				}
			}
			
		}
		return algunElementoModificado;
	}
	
	public Boolean isElemCultivoModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean algunElementoModificado = false;
		if(getLstCultivos() != null && !getLstCultivos().isEmpty()){
			for(int i=0; i<getLstCultivos().size() && !algunElementoModificado; i++){
				Cultivo cultivo = (Cultivo)getLstCultivos().get(i);
				if(cultivo.getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_ALTA) ||
						cultivo.getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_MODIF) ||
						cultivo.getTIPO_MOVIMIENTO().equals(Cultivo.TIPO_MOVIMIENTO_BAJA)){
					
					algunElementoModificado = true;
					
				}
			}
			
		}
		return algunElementoModificado;
	}
	
	public Boolean isElemConstruccionModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean algunElementoModificado = false;
		if(getLstConstrucciones() != null && !getLstConstrucciones().isEmpty()){
			for(int i=0; i<getLstConstrucciones().size() && !algunElementoModificado; i++){
				ConstruccionCatastro construccion = (ConstruccionCatastro)getLstConstrucciones().get(i);
				if(construccion.getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_ALTA) ||
						construccion.getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_MODIF) ||
						construccion.getTIPO_MOVIMIENTO().equals(ConstruccionCatastro.TIPO_MOVIMIENTO_BAJA)){
					
					algunElementoModificado = true;
					
				}
			}
			
		}
		return algunElementoModificado;
	}
	
	public Boolean isElemUCModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean algunElementoModificado = false;
		if(getLstUnidadesConstructivas() != null && !getLstUnidadesConstructivas().isEmpty()){
			for(int i=0; i<getLstUnidadesConstructivas().size() && !algunElementoModificado; i++){
				UnidadConstructivaCatastro uc = (UnidadConstructivaCatastro)getLstUnidadesConstructivas().get(i);
				if(uc.getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_ALTA) ||
						uc.getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_MODIF) ||
						uc.getTIPO_MOVIMIENTO().equals(UnidadConstructivaCatastro.TIPO_MOVIMIENTO_BAJA)){
					
					algunElementoModificado = true;
					
				}
			}
			
		}
		return algunElementoModificado;

	}
	
	public Boolean isElemRepartoModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean algunElementoModificado = false;
		if(getLstReparto() != null && !getLstReparto().isEmpty()){
			for(int i=0; i<getLstReparto().size() && !algunElementoModificado; i++){
				RepartoCatastro reparto = (RepartoCatastro)getLstReparto().get(i);
				if(reparto.getTIPO_MOVIMIENTO().equals(RepartoCatastro.TIPO_MOVIMIENTO_ALTA) ||
						reparto.getTIPO_MOVIMIENTO().equals(RepartoCatastro.TIPO_MOVIMIENTO_MODIF) ||
						reparto.getTIPO_MOVIMIENTO().equals(RepartoCatastro.TIPO_MOVIMIENTO_BAJA)){
					
					algunElementoModificado = true;
					
				}
			}
			
		}
		return algunElementoModificado;

	}
	
	public Boolean isBienInmuebleJurElim(){
		Boolean algunElementoEliminado = false;
		if(getLstBienesInmuebles() != null && !getLstBienesInmuebles().isEmpty()){
			for(int i=0; i<getLstBienesInmuebles().size(); i++){
				BienInmuebleJuridico bien = (BienInmuebleJuridico)getLstBienesInmuebles().get(i);
				if(bien.getBienInmueble().getTipoMovimiento().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_BAJA)){
					lstBienesInmueblesBaja.add(bien);
					algunElementoEliminado = true;
				}
			}
		}
		return algunElementoEliminado;
	}
	
	public Boolean isBienInmuebleJurAltaModifSinTitularidad(){
		//si se ha modificado solo el bien
		Boolean algunElementoAltaModif = false;
		
		if(getLstBienesInmuebles() != null && !getLstBienesInmuebles().isEmpty()){
			for(int i=0; i<getLstBienesInmuebles().size() ; i++){
				BienInmuebleJuridico bien = (BienInmuebleJuridico)getLstBienesInmuebles().get(i);
				Boolean algunTitularModif = false;
				if(	bien.getBienInmueble().getTipoMovimiento().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_MODIF)){
				
					if(bien.getLstTitulares() != null && !bien.getLstTitulares().isEmpty()){
						for(int j=0; j< bien.getLstTitulares().size(); j++){
							Titular titular = (Titular)bien.getLstTitulares().get(j);
							if(titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) ||
									titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF) ||
									titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_BAJA)){
								// no hay ningun titular modificado
								algunTitularModif = true;
								
							}
						}
						
					}
					
					
					if(!algunTitularModif){
						lstBienesInmueblesAltaModifSinTitular.add(bien);
					}
				}
				algunTitularModif = false;
			}
			if(!lstBienesInmueblesAltaModifSinTitular.isEmpty()){
				algunElementoAltaModif = true;
			}
		}
		return algunElementoAltaModif;
	}
	
	public Boolean isBienInmuebleJurAltaModifConTitularidad(){
		Boolean algunElementoAltaModif = false;
		Boolean algunTitularModif = false;
		if(getLstBienesInmuebles() != null && !getLstBienesInmuebles().isEmpty()){
			for(int i=0; i<getLstBienesInmuebles().size() ; i++){
				BienInmuebleJuridico bien = (BienInmuebleJuridico)getLstBienesInmuebles().get(i);

				if(bien.getLstTitulares() != null && !bien.getLstTitulares().isEmpty()){
					for(int j=0; j< bien.getLstTitulares().size(); j++){
						Titular titular = (Titular)bien.getLstTitulares().get(j);
						if(titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) ||
								titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF) ||
								titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_BAJA)){
							// no hay ningun titular modificado
							algunTitularModif = true;
						}
					}
					
				}

				if(algunTitularModif){
					lstBienesInmueblesAltaModifConTitular.add(bien);
				}
				algunTitularModif= false;
			}
			if(!lstBienesInmueblesAltaModifConTitular.isEmpty()){
				algunElementoAltaModif = true;
			}
		}
		return algunElementoAltaModif;
	}
	
	public Boolean isElemBienModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean algunElementoModificado = false;
		if(getLstBienesInmuebles() != null && !getLstBienesInmuebles().isEmpty()){
			for(int i=0; i<getLstBienesInmuebles().size() ; i++){
				BienInmuebleJuridico bien = (BienInmuebleJuridico)getLstBienesInmuebles().get(i);
				if(bien.getBienInmueble().getTipoMovimiento() != null){
					if(bien.getBienInmueble().getTipoMovimiento().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_ALTA) ||
							bien.getBienInmueble().getTipoMovimiento().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_MODIF) ||
							bien.getBienInmueble().getTipoMovimiento().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_BAJA)){
						
						algunElementoModificado = true;
						
					}
				}
				if(bien.getLstTitulares() != null && !bien.getLstTitulares().isEmpty()){
					for(int j=0; j<bien.getLstTitulares().size();j++){
						Titular titular = (Titular)bien.getLstTitulares().get(j);
						if(titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) ||
								titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF) ||
								titular.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_BAJA)){
							if(!bien.getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_ALTA) &&
									!bien.getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_BAJA)){
								bien.setTIPO_MOVIMIENTO(BienInmuebleJuridico.TIPO_MOVIMIENTO_MODIF);
								
							}
							algunElementoModificado = true;
						}
					}
				}
				
				if(bien.getLstComBienes()!= null && !bien.getLstComBienes().isEmpty()){
					for(int h=0; h<bien.getLstComBienes().size();h++){
						ComunidadBienes comunidad = (ComunidadBienes)bien.getLstComBienes().get(h);
						if(comunidad.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_ALTA) ||
								comunidad.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_MODIF) ||
								comunidad.getTIPO_MOVIMIENTO().equals(Titular.TIPO_MOVIMIENTO_BAJA)){
							if(!bien.getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_ALTA) &&
									!bien.getTIPO_MOVIMIENTO().equals(BienInmuebleJuridico.TIPO_MOVIMIENTO_BAJA)){
								bien.setTIPO_MOVIMIENTO(BienInmuebleJuridico.TIPO_MOVIMIENTO_MODIF);
								
							}
							algunElementoModificado = true;
							
						}
					}
				}
			}
		}
		return algunElementoModificado;

	}
	
	public Boolean isElemFxccModificado() {
		
		return false;
		
	}
	
	public Boolean isElemImagenModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		/*Boolean algunElementoModificado = false;
		if(getLstImagenes() != null && !getLstImagenes().isEmpty()){
			for(int i=0; i<getLstImagenes().size() && !algunElementoModificado; i++){
				ImagenCatastro imagen = (ImagenCatastro)getLstImagenes().get(i);
				
				
			}
		}*/
		return false;
	}
	
	public Boolean isFincaAltaModif() {
		Boolean fincaAltaModif = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) ||
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF)){
			fincaAltaModif = true;
		}
		return fincaAltaModif;
	}
	
	public Boolean isFincaElim() {
		Boolean fincaEliminada = false;
		if (TIPO_MOVIMIENTO_DELETE != null){
			if(TIPO_MOVIMIENTO_DELETE.equals(TIPO_MOVIMIENTO_BAJA)){
				fincaEliminada = true;
			}
		}	
		return fincaEliminada;
	}
	
	public ArrayList getLstBienesInmueblesAltaModif() {
		return lstBienesInmueblesAltaModif;
	}



	public void setLstBienesInmueblesAltaModif(ArrayList lstBienesInmueblesAltaModif) {
		this.lstBienesInmueblesAltaModif = lstBienesInmueblesAltaModif;
	}



	public ArrayList getLstBienesInmueblesBaja() {
		return lstBienesInmueblesBaja;
	}



	public void setLstBienesInmueblesBaja(ArrayList lstBienesInmueblesBaja) {
		this.lstBienesInmueblesBaja = lstBienesInmueblesBaja;
	}

	public ArrayList getLstBienesInmueblesAltaModifSinTitular() {
		return lstBienesInmueblesAltaModifSinTitular;
	}



	public void setLstBienesInmueblesAltaModifSinTitular(
			ArrayList lstBienesInmueblesAltaModifSinTitular) {
		this.lstBienesInmueblesAltaModifSinTitular = lstBienesInmueblesAltaModifSinTitular;
	}

	public ArrayList getLstBienesInmueblesAltaModifConTitular() {
		return lstBienesInmueblesAltaModifConTitular;
	}



	public void setLstBienesInmueblesAltaModifConTitular(
			ArrayList lstBienesInmueblesAltaModifConTitular) {
		this.lstBienesInmueblesAltaModifConTitular = lstBienesInmueblesAltaModifConTitular;
	}

	public Boolean esRustica(){
		return dirParcela.esRustica();
    }

    public Boolean esUrbana(){
    	return dirParcela.esUrbana();
    }

    public String getIdentificadorDialogo() {
		return identificadorDialogo;
	}

	public void setIdentificadorDialogo(String identificadorDialogo) {
		this.identificadorDialogo = identificadorDialogo;
	}
	
	public boolean isActualizadoOVC() {
		return actualizadoOVC;
	}
	
	public void setActualizadoOVC(boolean actualizadoOVC) {
		this.actualizadoOVC = actualizadoOVC;
	}

}
