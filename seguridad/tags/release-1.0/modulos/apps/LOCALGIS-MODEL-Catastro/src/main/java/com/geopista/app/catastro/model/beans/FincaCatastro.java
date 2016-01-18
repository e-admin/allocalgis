package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.ArrayList;

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
    
    public String TIPO_MOVIMIENTO;

    public String TIPO_MOVIMIENTO_DELETE;
    
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
		return "F";
	}



	public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
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
