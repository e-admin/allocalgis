/**
 * BienInmuebleCatastro.java
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
import com.geopista.app.catastro.model.datos.economicos.DatosBaseLiquidableBien;
import com.geopista.app.catastro.model.datos.economicos.DatosEconomicosBien;

public class BienInmuebleCatastro implements Serializable
{
    public static final String TIPO_MOVIMIENTO_VARIACIONES_TITULARIDAD = "I";
    public static final String TIPO_MOVIMIENTO_FINAL = "F";
    public static final String TIPO_MOVIMIENTO_ALTA = "A";
    public static final String TIPO_MOVIMIENTO_BAJA = "B";
    public static final String TIPO_MOVIMIENTO_MODIFICADO = "M";
    

    /**
     * Referencia Catastral + Número de Cargo + DígitosControl
     */
    private IdBienInmueble idBienInmueble = new IdBienInmueble();
    private Expediente datosExpediente = new Expediente();
    
    private String tipoMovimiento = TIPO_MOVIMIENTO_FINAL;
    private String claseBienInmueble;
    
    private String identificadorDialogo;
    private boolean actualizadoOVC = true;
    
    /**
     * Asignado por la Gerencia Territorial
     */
    private Integer numFijoInmueble;
    
    /**
     * Campo para la identificación del Bien Inmueble asignado por el Ayuntamiento.
     */
    private String idAyuntamientoBienInmueble;
    
    /**
     * Número de finca registral, incluyendo registro de la propiedad (Registro de la
     * propiedad 5 dígitos, sección 2 dígitos, número finca 6 dígitos, subfinca 6
     * dígitos).
     */
    private NumeroFincaRegistral numFincaRegistral;
    
    /**
     * Registro de la propiedad (max 3 dígitos)
     */
    private Integer regPropiedad;
           
    /**
     * Código de municipio
     */
    private String codMunicipioDGC;
    
    /**
     * Nombre de la entidad menor
     */
    private String nombreEntidadMenor;
    
    /**
     * Domicilio tributario del elemento.
     */
    private DireccionLocalizacion domicilioTributario = new DireccionLocalizacion();
        
    /**
     * Código municipio origen en caso de agregación (DGC).
     */
    private String codMunicipioOrigenAgregacion;
        
    private DatosBaseLiquidableBien datosBaseLiquidable = new DatosBaseLiquidableBien();
    private DatosEconomicosBien datosEconomicosBien = new DatosEconomicosBien();
    private FincaCatastro finca;
    
    private RepartoCatastro reparto;
    private ArrayList lstCultivos = new ArrayList();
    private ArrayList lstConstrucciones = new ArrayList();    

    
    private Persona representante= new Persona();
    
 // marca si el elemento ha sido modificado,borrado o es nuevo
    // para activar los campos de edicion en la informacion catastral
    private boolean elementoModificado;
    /**
     * Constructor por defecto
     *
     */
    public BienInmuebleCatastro(){
        
    }
   

	/**
     * @return Returns the claseBienInmueble.
     */
    public String getClaseBienInmueble() {
        return claseBienInmueble;
    }
    
    /**
     * @param claseBienInmueble The claseBienInmueble to set.
     */
    public void setClaseBienInmueble(String claseBienInmueble) {
        this.claseBienInmueble = claseBienInmueble;
    }
    
    /**
     * @return Returns the codMunicipioDGC.
     */
    public String getCodMunicipioDGC() {
        return codMunicipioDGC;
    }
    
    /**
     * @param codMunicipioDGC The codMunicipioDGC to set.
     */
    public void setCodMunicipioDGC(String codMunicipioDGC) {
        this.codMunicipioDGC = codMunicipioDGC;
    }
    
    /**
     * @return Returns the codMunicipioOrigenAgregacion.
     */
    public String getCodMunicipioOrigenAgregacion() {
        return codMunicipioOrigenAgregacion;
    }
    
    /**
     * @param codMunicipioOrigenAgregacion The codMunicipioOrigenAgregacion to set.
     */
    public void setCodMunicipioOrigenAgregacion(String codMunicipioOrigenAgregacion) {
        this.codMunicipioOrigenAgregacion = codMunicipioOrigenAgregacion;
    }
    
    
    
    /**
     * @return Returns the cultivos.
     */
    public ArrayList getLstCultivos() {
        return lstCultivos;
    }
    
    /**
     * @param cultivos The cultivos to set.
     */
    public void addLstCultivo(Cultivo cultivos) {
        this.lstCultivos.add(cultivos);
    }
    
    public void setLstCultivos(ArrayList lstCultivo)
    { 
    	this.lstCultivos = lstCultivo; 
    }
    
    /**
     * @return Returns the domicilioTributario.
     */
    public DireccionLocalizacion getDomicilioTributario() {
        return domicilioTributario;
    }
    
    /**
     * @param domicilioTributario The domicilioTributario to set.
     */
    public void setDomicilioTributario(DireccionLocalizacion domicilioTributario) {
        this.domicilioTributario = domicilioTributario;
    }
    
    /**
     * @return Returns the datosExpediente.
     */
    public Expediente getDatosExpediente() {
        return datosExpediente;
    }
    
    /**
     * @param expediente The datosExpediente to set.
     */
    public void setDatosExpediente(Expediente expediente) {
        this.datosExpediente = expediente;
    }
    
    /**
     * @return Returns the finca.
     */
    public FincaCatastro getFinca() {
        return finca;
    }
    
    /**
     * @param finca The finca to set.
     */
    public void setFinca(FincaCatastro finca) {
        this.finca = finca;
    }
    
    /**
     * @return Returns the idAyuntamientoBienInmueble.
     */
    public String getIdAyuntamientoBienInmueble() {
        return idAyuntamientoBienInmueble;
    }
    
    /**
     * @param idAyuntamientoBienInmueble The idAyuntamientoBienInmueble to set.
     */
    public void setIdAyuntamientoBienInmueble(String idAyuntamientoBienInmueble) {
        this.idAyuntamientoBienInmueble = idAyuntamientoBienInmueble;
    }
    
    /**
     * @return Returns the idBienInmueble.
     */
    public IdBienInmueble getIdBienInmueble() {
        return idBienInmueble;
    }
    
    /**
     * @param idBienInmueble The idBienInmueble to set.
     */
    public void setIdBienInmueble(IdBienInmueble idBienInmueble) {
        this.idBienInmueble = idBienInmueble;
    }
    
    /**
     * @return Returns the nombreEntidadMenor.
     */
    public String getNombreEntidadMenor() {
        return nombreEntidadMenor;
    }
    
    /**
     * @param nombreEntidadMenor The nombreEntidadMenor to set.
     */
    public void setNombreEntidadMenor(String nombreEntidadMenor) {
        this.nombreEntidadMenor = nombreEntidadMenor;
    }
    
    
    
    /**
     * @return Returns the numFijoInmueble.
     */
    public Integer getNumFijoInmueble() {
        return numFijoInmueble;
    }
    
    /**
     * @param numFijoInmueble The numFijoInmueble to set.
     */
    public void setNumFijoInmueble(Integer numFijoInmueble) {
        this.numFijoInmueble = numFijoInmueble;
    }
    
    /**
     * @return Returns the numFincaRegistral.
     */
    public NumeroFincaRegistral getNumFincaRegistral() {
        return numFincaRegistral;
    }
    
    /**
     * @param numFincaRegistral The numFincaRegistral to set.
     */
    public void setNumFincaRegistral(NumeroFincaRegistral numFincaRegistral) {
        this.numFincaRegistral = numFincaRegistral;
    }
    
    
    /**
     * @return Returns the reparto.
     */
    public RepartoCatastro getReparto() {
        return reparto;
    }
    
    /**
     * @param reparto The reparto to set.
     */
    public void setReparto(RepartoCatastro reparto) {
        this.reparto = reparto;
    }
    
    /**
     * @return Returns the representante.
     */
    public Persona getRepresentante() {
        return representante;
    }
    
    /**
     * @param representante The representante to set.
     */
    public void setRepresentante(Persona representante) {
        this.representante = representante;
    }
    
    /**
     * @return Returns the tipoMovimiento.
     */
    public String getTipoMovimiento() {
        return tipoMovimiento;
    }
    
    /**
     * @param tipoMovimiento The tipoMovimiento to set.
     */
    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
 
    
    /**
     * @return Returns the datosEconomicosBien.
     */
    public DatosEconomicosBien getDatosEconomicosBien() {
        return datosEconomicosBien;
    }
    
    /**
     * @param datosEconomicosBien The datosEconomicosBien to set.
     */
    public void setDatosEconomicosBien(DatosEconomicosBien datosEconomicosBien) {
        this.datosEconomicosBien = datosEconomicosBien;
    }
    
    
    
    /**
     * @return Returns the datosBaseLiquidable.
     */
    public DatosBaseLiquidableBien getDatosBaseLiquidable()
    {
        return datosBaseLiquidable;
    }
    
    /**
     * @param datosBaseLiquidable The datosBaseLiquidable to set.
     */
    public void setDatosBaseLiquidable(DatosBaseLiquidableBien datosBaseLiquidable)
    {
        this.datosBaseLiquidable = datosBaseLiquidable;
    }    
   
    
    /**
     * @param lstComunidadBienes The lstComunidadBienes to set.
     */
 /*   public void setLstComunidadBienes(ArrayList lstComunidadBienes)
    {
        this.lstComunidadBienes = lstComunidadBienes;
    }*/
    
    /**
     * @return Returns the lstConstrucciones.
     */
    public ArrayList getLstConstrucciones()
    {
        return lstConstrucciones;
    }
    
    /**
     * @param construccion la construccion a añadir.
     */
    public void addLstConstruccion(ConstruccionCatastro construccion)
    {
        this.lstConstrucciones.add(construccion);
    }
    
    public void setLstConstrucciones(ArrayList lstConstruccion)
    { 
    	this.lstConstrucciones = lstConstruccion; 
    }
   
    /**
     * @return Returns the regPropiedad.
     */
    public Integer getRegPropiedad()
    {
        return regPropiedad;
    }

    /**
     * @param regPropiedad The regPropiedad to set.
     */
    public void setRegPropiedad(Integer regPropiedad)
    {
        this.regPropiedad = regPropiedad;
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
    
    public BienInmuebleCatastro clone(BienInmuebleCatastro bi)
    {
    	BienInmuebleCatastro biNuevo = new BienInmuebleCatastro();
    
    	IdBienInmueble idBienInmueble = new IdBienInmueble();
    	idBienInmueble.setDigControl1(bi.getIdBienInmueble().getDigControl1());
    	idBienInmueble.setDigControl2(bi.getIdBienInmueble().getDigControl2());
    	idBienInmueble.setNumCargo(bi.getIdBienInmueble().getNumCargo());
    	idBienInmueble.setParcelaCatastral(bi.getIdBienInmueble().getParcelaCatastral().getRefCatastral());
    	biNuevo.setIdBienInmueble(new IdBienInmueble(idBienInmueble));
    	
    	biNuevo.setClaseBienInmueble(bi.getClaseBienInmueble());
    	biNuevo.setCodMunicipioDGC(bi.getCodMunicipioDGC());
    	biNuevo.setCodMunicipioOrigenAgregacion(bi.getCodMunicipioOrigenAgregacion());
    	
    	biNuevo.setDatosBaseLiquidable(new DatosBaseLiquidableBien());
    	biNuevo.getDatosBaseLiquidable().setEjercicioIBI(bi.getDatosBaseLiquidable().getEjercicioIBI());
    	biNuevo.getDatosBaseLiquidable().setEjercicioRevParcial(bi.getDatosBaseLiquidable().getEjercicioRevParcial());
    	biNuevo.getDatosBaseLiquidable().setEjercicioRevTotal(bi.getDatosBaseLiquidable().getEjercicioRevTotal());
    	biNuevo.getDatosBaseLiquidable().setPeriodoTotal(bi.getDatosBaseLiquidable().getPeriodoTotal());
    	biNuevo.getDatosBaseLiquidable().setProcedenciaValorBase(bi.getDatosBaseLiquidable().getProcedenciaValorBase());
    	biNuevo.getDatosBaseLiquidable().setValorBase(bi.getDatosBaseLiquidable().getValorBase());
    	biNuevo.getDatosBaseLiquidable().setValorCatastralIBI(bi.getDatosBaseLiquidable().getValorCatastralIBI());
    	
    	biNuevo.setDatosEconomicosBien(new DatosEconomicosBien());
    	biNuevo.getDatosEconomicosBien().setAnioAntiguedad(bi.getDatosEconomicosBien().getAnioAntiguedad());
    	biNuevo.getDatosEconomicosBien().setAnioFinValoracion(bi.getDatosEconomicosBien().getAnioFinValoracion());
    	biNuevo.getDatosEconomicosBien().setAnioValorCat(bi.getDatosEconomicosBien().getAnioValorCat());
    	biNuevo.getDatosEconomicosBien().setBaseLiquidable(bi.getDatosEconomicosBien().getBaseLiquidable());
    	biNuevo.getDatosEconomicosBien().setClaveBonificacionRustica(bi.getDatosEconomicosBien().getClaveBonificacionRustica());
    	biNuevo.getDatosEconomicosBien().setCoefParticipacion(bi.getDatosEconomicosBien().getCoefParticipacion());
    	biNuevo.getDatosEconomicosBien().setImporteBonificacionRustica(bi.getDatosEconomicosBien().getImporteBonificacionRustica());
    	biNuevo.getDatosEconomicosBien().setIndTipoPropiedad(bi.getDatosEconomicosBien().getIndTipoPropiedad());
    	biNuevo.getDatosEconomicosBien().setNumOrdenHorizontal(bi.getDatosEconomicosBien().getNumOrdenHorizontal());
    	biNuevo.getDatosEconomicosBien().setOrigenPrecioDeclarado(bi.getDatosEconomicosBien().getOrigenPrecioDeclarado());
    	biNuevo.getDatosEconomicosBien().setPrecioDeclarado(bi.getDatosEconomicosBien().getPrecioDeclarado());
    	biNuevo.getDatosEconomicosBien().setPrecioVenta(bi.getDatosEconomicosBien().getPrecioVenta());
    	biNuevo.getDatosEconomicosBien().setProcedenciaValorBase(bi.getDatosEconomicosBien().getProcedenciaValorBase());
    	biNuevo.getDatosEconomicosBien().setSuperficieCargoFincaConstruida(bi.getDatosEconomicosBien().getSuperficieCargoFincaConstruida());
    	biNuevo.getDatosEconomicosBien().setSuperficieCargoFincaRustica(bi.getDatosEconomicosBien().getSuperficieCargoFincaRustica());
    	biNuevo.getDatosEconomicosBien().setUso(bi.getDatosEconomicosBien().getUso());
    	biNuevo.getDatosEconomicosBien().setValorBase(bi.getDatosEconomicosBien().getValorBase());
    	biNuevo.getDatosEconomicosBien().setValorCatastral(bi.getDatosEconomicosBien().getValorCatastral());
    	biNuevo.getDatosEconomicosBien().setValorCatastralConstruccion(bi.getDatosEconomicosBien().getValorCatastralConstruccion());
    	biNuevo.getDatosEconomicosBien().setValorCatastralSuelo(bi.getDatosEconomicosBien().getValorCatastralSuelo());
    	
    	biNuevo.setDomicilioTributario(new DireccionLocalizacion());
    	biNuevo.getDomicilioTributario().setApartadoCorreos(bi.getDomicilioTributario().getApartadoCorreos());
    	biNuevo.getDomicilioTributario().setBloque(bi.getDomicilioTributario().getBloque());
    	biNuevo.getDomicilioTributario().setCodigoMunicipioDGC(bi.getDomicilioTributario().getCodigoMunicipioDGC());
    	biNuevo.getDomicilioTributario().setCodigoPostal(bi.getDomicilioTributario().getCodigoPostal());
    	biNuevo.getDomicilioTributario().setCodigoVia(bi.getDomicilioTributario().getCodigoVia());
    	biNuevo.getDomicilioTributario().setCodMunOrigenAgregacion(bi.getDomicilioTributario().getCodMunOrigenAgregacion());
    	biNuevo.getDomicilioTributario().setCodParaje(bi.getDomicilioTributario().getCodParaje());
    	biNuevo.getDomicilioTributario().setCodParcela(bi.getDomicilioTributario().getCodParcela());
    	biNuevo.getDomicilioTributario().setCodPoligono(bi.getDomicilioTributario().getCodPoligono());
    	biNuevo.getDomicilioTributario().setCodZonaConcentracion(bi.getDomicilioTributario().getCodZonaConcentracion());
    	biNuevo.getDomicilioTributario().setDireccionNoEstructurada(bi.getDomicilioTributario().getDireccionNoEstructurada());
    	biNuevo.getDomicilioTributario().setDistrito(bi.getDomicilioTributario().getDistrito());
    	biNuevo.getDomicilioTributario().setEscalera(bi.getDomicilioTributario().getEscalera());
    	biNuevo.getDomicilioTributario().setIdLocalizacion(bi.getDomicilioTributario().getIdLocalizacion());
    	biNuevo.getDomicilioTributario().setIdVia(bi.getDomicilioTributario().getIdVia());
    	biNuevo.getDomicilioTributario().setKilometro(bi.getDomicilioTributario().getKilometro());
    	biNuevo.getDomicilioTributario().setMunicipioINE(bi.getDomicilioTributario().getMunicipioINE());
    	biNuevo.getDomicilioTributario().setNombreEntidadMenor(bi.getDomicilioTributario().getNombreEntidadMenor());
    	biNuevo.getDomicilioTributario().setNombreMunicipio(bi.getDomicilioTributario().getNombreMunicipio());
    	biNuevo.getDomicilioTributario().setNombreParaje(bi.getDomicilioTributario().getNombreParaje());
    	biNuevo.getDomicilioTributario().setNombreProvincia(bi.getDomicilioTributario().getNombreProvincia());
    	biNuevo.getDomicilioTributario().setNombreVia(bi.getDomicilioTributario().getNombreVia());
    	biNuevo.getDomicilioTributario().setPlanta(bi.getDomicilioTributario().getPlanta());
    	biNuevo.getDomicilioTributario().setPrimeraLetra(bi.getDomicilioTributario().getPrimeraLetra());
    	biNuevo.getDomicilioTributario().setPrimerNumero(bi.getDomicilioTributario().getPrimerNumero());
    	biNuevo.getDomicilioTributario().setProvinciaINE(bi.getDomicilioTributario().getProvinciaINE());
    	biNuevo.getDomicilioTributario().setPuerta(bi.getDomicilioTributario().getPuerta());
    	biNuevo.getDomicilioTributario().setSegundaLetra(bi.getDomicilioTributario().getSegundaLetra());
    	biNuevo.getDomicilioTributario().setSegundoNumero(bi.getDomicilioTributario().getSegundoNumero());
    	biNuevo.getDomicilioTributario().setTipoVia(bi.getDomicilioTributario().getTipoVia());
    	    	
    	biNuevo.setIdAyuntamientoBienInmueble(bi.getIdAyuntamientoBienInmueble());
    	biNuevo.setTipoMovimiento(bi.getTipoMovimiento());
    	
    	if (bi.getLstConstrucciones() != null)
    	{
	    	ArrayList lstConstrucciones = new ArrayList();
	    	for (int i=0;i<bi.getLstConstrucciones().size();i++)
	    	{
	    		ConstruccionCatastro construccion = (ConstruccionCatastro) bi.getLstConstrucciones().get(i);
	    		ConstruccionCatastro construccionNueva = construccion.clone(construccion);
	    		
	    		lstConstrucciones.add(construccionNueva);
	    	}
	    	biNuevo.setLstConstrucciones(lstConstrucciones);
    	}
    	
    	if (bi.getLstCultivos() != null)
    	{
    		ArrayList lstCultivos = new ArrayList();
    		for (int i=0;i<bi.getLstCultivos().size();i++)
    		{
    			Cultivo cultivo = (Cultivo) bi.getLstCultivos().get(i);
    			Cultivo cultivoNuevo = cultivo.clone(cultivo);
    			
    			lstCultivos.add(cultivoNuevo);
    		}
    		biNuevo.setLstCultivos(lstCultivos);
    	}
    	
    	
    	biNuevo.setNombreEntidadMenor(bi.getNombreEntidadMenor());
    	biNuevo.setNumFijoInmueble(bi.getNumFijoInmueble());
    	biNuevo.setNumFincaRegistral(bi.getNumFincaRegistral());
    //	biNuevo.setRegPropiedad(bi.getRegPropiedad());
    	if (bi.getReparto() != null)
    		biNuevo.setReparto(bi.getReparto().clone(bi.getReparto()));
    	biNuevo.setRepresentante(bi.getRepresentante());
    	biNuevo.setTipoMovimiento(bi.getTipoMovimiento());
    	
    	return biNuevo;
    }
    
    public boolean isElementoModificado() {
		return elementoModificado;
	}

	public void setElementoModificado(boolean elementoModificado) {
		this.elementoModificado = elementoModificado;
	}
	
	
	public boolean isNotElementoEliminado(){
		boolean isElemEliminado = false;
		
		if(!getTipoMovimiento().equals(TIPO_MOVIMIENTO_BAJA)){
			isElemEliminado = true;
		}
		return isElemEliminado;
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
	
	
	public Boolean esRustica(){
//      return new dBoolean(!esUrbana().booleanValue() || (codPoligono!=null&&!codPoligono.equalsIgnoreCase("")
//      && codParcela!=null&&!codParcela.equalsIgnoreCase("")));
		return domicilioTributario.esRustica();
  }

  public Boolean esUrbana(){
      //return new Boolean(codigoVia!=0);
//      return new Boolean(codigoVia!=-1);
  	return domicilioTributario.esUrbana();
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
