package com.geopista.app.catastro.model.beans;

import java.io.Serializable;


public class NumeroFincaRegistral implements Serializable{
	/**
	 * Registro de la propiedad = Codigo de Provincia + Codigo del registro de la propiedad
	 * 5 dígitos.
	 */
	private RegistroPropiedad registroPropiedad;
	
	/**
	 * Seccion.
	 */
	private String seccion;
	
	/**
	 * Número de finca. 6 dígitos.
	 */
	private String numFinca;
	
	/**
	 * Número de subfinca registral.
	 */
	private String numSubFinca;
	
	/**
	 * Número de finca registral = registroPropiedad + seccion + numFinca + numSubFinca
	 */
	private String numFincaRegistral;
    
	/**
	 * Tamaño que debe tener el número de finca registral.
	 */
    private static int TAM_NUMFINCAREGISTRAL = 19;
    private static int TAM_REGPROPIEDAD = 5;
    private static int TAM_SECCION = 2;
    private static int TAM_NUMFINCA = 6;
    private static int TAM_NUMSUBFINCA = 6;
	
    /**
     * 
     * @param registroPropiedad
     * @param seccion
     * @param numFinca
     * @param numSubFinca
     */
	public NumeroFincaRegistral(RegistroPropiedad registroPropiedad, String seccion, String numFinca, String numSubFinca)
	{
		if(registroPropiedad!=null){
//			rellena el numero de registro de propiedad con 0's delante hasta completar su tamaño
            StringBuffer str = new StringBuffer(registroPropiedad.getRegistroPropiedad());
            int strLength  = str.length();
            for ( int i = 0; i <= TAM_REGPROPIEDAD ; i ++ ) {
                if (i < TAM_REGPROPIEDAD - strLength) 
                    str.insert( 0, '0' );
            }
            registroPropiedad = new RegistroPropiedad(str.toString());
		}
		this.registroPropiedad = registroPropiedad;
		
		if(seccion!=null){
//			rellena el numero de sección con 0's delante hasta completar su tamaño
            StringBuffer str = new StringBuffer(seccion);
            int strLength  = str.length();
            for ( int i = 0; i <= TAM_SECCION ; i ++ ) {
                if (i < TAM_SECCION - strLength) 
                    str.insert( 0, '0' );
            }
            seccion = str.toString();
		}
		this.seccion = seccion;
		
		if(numFinca!=null){
//			rellena el numero de finca con 0's delante hasta completar su tamaño
            StringBuffer str = new StringBuffer(numFinca);
            int strLength  = str.length();
            for ( int i = 0; i <= TAM_NUMFINCA ; i ++ ) {
                if (i < TAM_NUMFINCA - strLength) 
                    str.insert( 0, '0' );
            }
            numFinca = str.toString();
		}
		this.numFinca = numFinca;
		
		if(numSubFinca!=null){
//			rellena el numero de subfinca con 0's delante hasta completar su tamaño
            StringBuffer str = new StringBuffer(numSubFinca);
            int strLength  = str.length();
            for ( int i = 0; i <= TAM_NUMSUBFINCA ; i ++ ) {
                if (i < TAM_NUMSUBFINCA - strLength) 
                    str.insert( 0, '0' );
            }
            numSubFinca = str.toString();
		}
		this.numSubFinca = numSubFinca;
		
		this.numFincaRegistral = 
            registroPropiedad.getRegistroPropiedad() + seccion + numFinca + numSubFinca;
	}

    public NumeroFincaRegistral (String numFincaRegistral)
    {
        if (numFincaRegistral!=null){
            //rellena el numero de finca registral con 0's delante hasta completar su tamaño
            StringBuffer str = new StringBuffer(numFincaRegistral);
            int strLength  = str.length();
            for ( int i = 0; i <= TAM_NUMFINCAREGISTRAL ; i ++ ) {
                if (i < TAM_NUMFINCAREGISTRAL - strLength) 
                    str.insert( 0, '0' );
            }
            numFincaRegistral = str.toString();
            //Registro de la propiedad 5 dígitos, sección 2 dígitos, número finca 6 dígitos, 
            //subfinca 6 dígitos
            this.registroPropiedad = new RegistroPropiedad(numFincaRegistral.substring(0, 5));
            this.seccion= numFincaRegistral.substring(5, 7);
            this.numFinca = numFincaRegistral.substring(7, 13);
            this.numSubFinca= numFincaRegistral.substring(13, 19);
            this.numFincaRegistral = numFincaRegistral;
        }
    }
    
    public NumeroFincaRegistral (){
    	
    }
    
    /**
     * Devuelve el número de finca.
     * @return String con el número de finca.
     */
	public String getNumFinca() {
		return numFinca;
	}

	/**
	 * Guarda el número de finca. 
	 * @param numFinca Número de finca a guardar.
	 */
	public void setNumFinca(String numFinca) {
		this.numFinca = numFinca;
	}

	/**
	 * Devuelve el número de subfinca.
	 * @return String con el número de Subfinca.
	 */
	public String getNumSubFinca() {
		return numSubFinca;
	}
	
	/**
	 * Guarda el número de subfinca.
	 * @param numSubFinca String con el número de subfinca.
	 */
	public void setNumSubFinca(String numSubFinca) {
		this.numSubFinca = numSubFinca;
	}

	/**
	 * Devuelve el número del registro de la propiedad.
	 * @return String con el registro de la propiedad.
	 */
	public RegistroPropiedad getRegistroPropiedad() {
		return registroPropiedad;
	}

	/**
	 * Guarda el registro de la propiedad.
	 * @param registroPropiedad String con el registro de la propiedad.
	 */
	public void setRegistroPropiedad(RegistroPropiedad registroPropiedad) {
		this.registroPropiedad = registroPropiedad;
	}

	/**
	 * Devuelve la seccion.
	 * @return String con la seccion.
	 */
	public String getSeccion() {
		return seccion;
	}

	/**
	 * Guarda la seccion.
	 * @param seccion String con la seccion a cargar.
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	/**
	 * Devuelve el número de la finca registral.
	 * @return String con el número de finca registral.
	 */
	public String getNumFincaRegistral() {
		return numFincaRegistral;
	}

	/**
	 * Guarda el número de finca registral.
	 * @param numFincaRegistral String con el número de finca registral.
	 */
	public void setNumFincaRegistral(String numFincaRegistral) {
		this.numFincaRegistral = numFincaRegistral;
	}
	

}
