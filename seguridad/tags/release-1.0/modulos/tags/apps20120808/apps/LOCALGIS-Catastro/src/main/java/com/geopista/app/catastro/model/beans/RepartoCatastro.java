package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.ArrayList;



public class RepartoCatastro implements Serializable
{
	 public String TIPO_MOVIMIENTO;
	
    /**
     * Identificador del reparto
     */
    //private String idReparto;
    /**
     * Tipo de registro
     */
    //private String tipoMovimiento;
    /**
     * Datos de expediente
     */
    //private Expediente datosExpediente;
    
    /**
     * Identificador de la construcción o cultivo origen
     */
    private ReferenciaCatastral idOrigen;
    /**
     * IDentificador de la construcción destino
     */
    //private String idConstruccionDestino;    
    
    /**
     * Numero de cargo destino
     */
    //private String numCargoDestino;    
    
    /**
     * Número de orden del elemento de construccion a repartir
     */
    private String numOrdenConsRepartir;
    /**
     * Número de orden del elemento repartido
     */
    //private String numOrdenRepartido;
    /**
     * Código de subparcela de elemento a repartir
     */
    private String codSubparcelaElementoRepartir;
    /**
     * Calificacion catastral del cultivo a repartir
     */
    private String califCatastralElementoRepartir;
    
    /**
     * tipo de reparto: construcciones, cultivos o construccion en bienes
     */
    private String tipoReparto;
    
    /**
     * Porcentaje de reparto
     */
    private float porcentajeReparto;
    
    /**
     * Código de la delegación
     */
    private String codDelegacion;
    
    /**
     * Código del municipio
     */
    private String codMunicipio;
    
    
    /**
     * Elemento repartido (sólo se rellena si se encuentra el elemento
     * en la finca). Puede ser un cargo o un local/cultivo
     */
    //private Object elemRepartido;
    
    private ArrayList lstBienes;
    
    private ArrayList lstLocales;
    
    /**
     * Constructor por defecto
     *
     */
    public RepartoCatastro()
    {
        
    }
    
    
    
    
   


	public ArrayList getLstBienes() {
		return lstBienes;
	}







	public void setLstBienes(ArrayList lstBienes) {
		this.lstBienes = lstBienes;
	}







	public ArrayList getLstLocales() {
		return lstLocales;
	}







	public void setLstLocales(ArrayList lstLocales) {
		this.lstLocales = lstLocales;
	}







	public String getTIPO_MOVIMIENTO() {
		return "F";
	}




	public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
	}




	/**
     * @return Returns the datosExpediente.
     */
    /*public Expediente getDatosExpediente() {
        return datosExpediente;
    }*/
    
    
    /**
     * @param datosExpediente The datosExpediente to set.
     */
    /*public void setDatosExpediente(Expediente datosExpediente) {
        this.datosExpediente = datosExpediente;
    }*/
    
    
    
    
    /**
     * @return Returns the idOrigen.
     */
    public ReferenciaCatastral getIdOrigen() {
        return idOrigen;
    }
    
    
    /**
     * @param idOrigen The idOrigen to set.
     */
    public void setIdOrigen(ReferenciaCatastral idOrigen) {
        this.idOrigen = idOrigen;
    }
    
    
    /**
     * @return Returns the idReparto.
     */
    /*public String getIdReparto() {
        return idReparto;
    }*/
    
    
    /**
     * @param idReparto The idReparto to set.
     */
    /*public void setIdReparto(String idReparto) {
        this.idReparto = idReparto;
    }*/
    
    
    /**
     * @return Returns the porcentajeReparto.
     */
    public float getPorcentajeReparto() {
        return porcentajeReparto;
    }
    
    
    /**
     * @param porcentajeReparto The porcentajeReparto to set.
     */
    public void setPorcentajeReparto(float porcentajeReparto) {
        this.porcentajeReparto = porcentajeReparto;
    }
    
    
    /**
     * @return Returns the tipoMovimiento.
     */
    /*public String getTipoMovimiento() {
        return tipoMovimiento;
    }*/
    
    
    /**
     * @param tipoMovimiento The tipoMovimiento to set.
     */
    /*public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }*/
    
    /**
     * @return Returns the califCatastralElementoRepartir.
     */
    public String getCalifCatastralElementoRepartir()
    {
        return califCatastralElementoRepartir;
    }
    
    /**
     * @param califCatastralElementoRepartir The califCatastralElementoRepartir to set.
     */
    public void setCalifCatastralElementoRepartir(String califCatastralElementoRepartir)
    {
        this.califCatastralElementoRepartir = califCatastralElementoRepartir;
    }
    
    /**
     * @return Returns the codSubparcelaElementoRepartir.
     */
    public String getCodSubparcelaElementoRepartir()
    {
        return codSubparcelaElementoRepartir;
    }
    
    /**
     * @param codSubparcelaElementoRepartir The codSubparcelaElementoRepartir to set.
     */
    public void setCodSubparcelaElementoRepartir(String codSubparcelaElementoRepartir)
    {
        this.codSubparcelaElementoRepartir = codSubparcelaElementoRepartir;
    }
    
    /**
     * @return Returns the numOrdenConsRepartir.
     */
    public String getNumOrdenConsRepartir()
    {
        return numOrdenConsRepartir;
    }
    
    /**
     * @param numOrdenConsRepartir The numOrdenConsRepartir to set.
     */
    public void setNumOrdenConsRepartir(String numOrdenConsRepartir)
    {
        this.numOrdenConsRepartir = numOrdenConsRepartir;
    }
    
    /**
     * @return Returns the tipoReparto.
     */
    public String getTipoReparto()
    {
        return tipoReparto;
    }
    
    /**
     * @param tipoReparto The tipoReparto to set.
     */
    public void setTipoReparto(String tipoReparto)
    {
        this.tipoReparto = tipoReparto;
    }
    
    /**
     * @return Returns the numOrdenRepartido.
     */
    /*public String getNumOrdenRepartido()
    {
        return numOrdenRepartido;
    }*/
    
    /**
     * @param numOrdenRepartido The numOrdenRepartido to set.
     */
    /*public void setNumOrdenRepartido(String numOrdenRepartido)
    {
        this.numOrdenRepartido = numOrdenRepartido;
    }*/	
    
    
    /**
     * @return Returns the idConstruccionDestino.
     */
    /*public String getIdConstruccionDestino()
    {
        return idConstruccionDestino;
    }*/
    
    /**
     * @param idConstruccionDestino The idConstruccionDestino to set.
     */
    /*public void setIdConstruccionDestino(String idConstruccionDestino)
    {
        this.idConstruccionDestino = idConstruccionDestino;
    }*/
    
    
    /**
     * @return Returns the numCargoDestino.
     */
    /*public String getNumCargoDestino()
    {
        return numCargoDestino;
    }*/
    
    /**
     * @param numCargoDestino The numCargoDestino to set.
     */
    /*public void setNumCargoDestino(String numCargoDestino)
    {
        this.numCargoDestino = numCargoDestino;
    }*/
  
    public String getCodDelegacion() {
		return codDelegacion;
	}


	public void setCodDelegacion(String codDelegacion) {
		this.codDelegacion = codDelegacion;
	}




	public String getCodMunicipio() {
		return codMunicipio;
	}

	public void setCodMunicipio(String codMunicipio) {
		this.codMunicipio = codMunicipio;
	}

    public Boolean esLocal()
    {
        return new Boolean((numOrdenConsRepartir!=null&& !numOrdenConsRepartir.equalsIgnoreCase("")) &&
                (codSubparcelaElementoRepartir==null||(codSubparcelaElementoRepartir!=null &&codSubparcelaElementoRepartir.equalsIgnoreCase(""))
                        && (califCatastralElementoRepartir==null||(califCatastralElementoRepartir!=null&&califCatastralElementoRepartir.equalsIgnoreCase("")))));
    }

    public Boolean esCultivo()
    {
        return new Boolean(!esLocal().booleanValue());
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

    /**
     * @return Returns the elemRepartido.
     */
    /*public Object getElemRepartido()
    {
        return elemRepartido;
    }*/

    /**
     * @param elemRepartido The elemRepartido to set.
     */
    /*public void setElemRepartido(Object elemRepartido)
    {
        this.elemRepartido = elemRepartido;
    }*/
    
    public RepartoCatastro clone(RepartoCatastro reparto)
    {
    	RepartoCatastro repartoNuevo = new RepartoCatastro();
    	
    	repartoNuevo.setCalifCatastralElementoRepartir(reparto.getCalifCatastralElementoRepartir());
    	repartoNuevo.setCodDelegacion(reparto.getCodDelegacion());
    	repartoNuevo.setCodMunicipio(reparto.getCodMunicipio());
    	repartoNuevo.setCodSubparcelaElementoRepartir(reparto.getCodSubparcelaElementoRepartir());
    	//repartoNuevo.setDatosExpediente(reparto.getDatosExpediente());
    	//repartoNuevo.setElemRepartido(reparto.getElemRepartido());
    	//repartoNuevo.setIdConstruccionDestino(reparto.getIdConstruccionDestino());
    	repartoNuevo.setIdOrigen(reparto.getIdOrigen());
    	//repartoNuevo.setIdReparto(reparto.getIdReparto());
    	//repartoNuevo.setNumCargoDestino(reparto.getNumCargoDestino());
    	repartoNuevo.setNumOrdenConsRepartir(repartoNuevo.getNumOrdenConsRepartir());
    	//repartoNuevo.setNumOrdenRepartido(reparto.getNumOrdenRepartido());
    	repartoNuevo.setPorcentajeReparto(reparto.getPorcentajeReparto());
    	repartoNuevo.setTIPO_MOVIMIENTO(reparto.getTIPO_MOVIMIENTO());
    	
    	return repartoNuevo;
    }
}
