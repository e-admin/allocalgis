package com.geopista.app.catastro.model.beans;

import java.io.Serializable;


/**
 * Bean que encapsula todos los datos de las direcciones en la aplicacion.
 * */

public class DireccionLocalizacion implements Serializable{
	private long idLocalizacion;

    private String provinciaINE;
    private String municipioINE;
	private String codigoMunicipioDGC;
	private String nombreEntidadMenor;

	private int codigoVia;

	private int primerNumero = -1;
	private String primeraLetra;
	private int segundoNumero = -1;
    private String segundaLetra;

	private double kilometro = -1;
    private String bloque;
	private String puerta;
	private String escalera;
	private String planta;
	private String direccionNoEstructurada;

    //private int codigoPostal = -1;
    private String codigoPostal;
    private String nombreVia;
    private int apartadoCorreos = -1;
	private String tipoVia;

    private String nombreProvincia;
	private String nombreMunicipio;

    /**
     * Distrito
     */
    private String distrito;
    /**
     * Identificador de la vía
     */
    private int idVia;

    /**
     * Código del municipio origen en caso de agregación
     */
    private String codMunOrigenAgregacion;

    /**
     * Código de la zona de contración
     */
    private String codZonaConcentracion;

    /**
     * Código del polígono
     */
    private String codPoligono;

    /**
     * Código de la parcela
     */
    private String codParcela;

    /**
     * Código del paraje
     */
    private String codParaje;

    /**
     * Nombre del paraje
     */
    private String nombreParaje;

    public DireccionLocalizacion()
    {

    }

    public long getIdLocalizacion()
    {
        return idLocalizacion;
    }

    public void setIdLocalizacion(long idLocalizacion)
    {
        this.idLocalizacion = idLocalizacion;
    }

    public String getProvinciaINE()
    {
        return provinciaINE;
    }

    public void setProvinciaINE(String provinciaINE)
    {
        this.provinciaINE = provinciaINE;
    }

    public String getCodigoMunicipioDGC()
    {
        return codigoMunicipioDGC;
    }

    public void setCodigoMunicipioDGC(String codigoMunicipioDGC)
    {
        this.codigoMunicipioDGC = codigoMunicipioDGC;
    }

    public String getMunicipioINE()
    {
        return municipioINE;
    }

    public void setMunicipioINE(String municipioINE)
    {
        this.municipioINE = municipioINE;
    }

    public String getNombreEntidadMenor()
    {
        return nombreEntidadMenor;
    }

    public void setNombreEntidadMenor(String nombreEntidadMenor)
    {
        this.nombreEntidadMenor = nombreEntidadMenor;
    }

    public int getCodigoVia()
    {
        return codigoVia;
    }

    public void setCodigoVia(int codigoVia)
    {
        this.codigoVia = codigoVia;
    }

    public int getPrimerNumero()
    {
        return primerNumero;
    }

    public void setPrimerNumero(int primerNumero)
    {
        this.primerNumero = primerNumero;
    }

    public String getPrimeraLetra()
    {
        return primeraLetra;
    }

    public void setPrimeraLetra(String primeraLetra)
    {
        this.primeraLetra = primeraLetra;
    }

    public int getSegundoNumero()
    {
        return segundoNumero;
    }

    public void setSegundoNumero(int segundoNumero)
    {
        this.segundoNumero = segundoNumero;
    }

    public String getSegundaLetra()
    {
        return segundaLetra;
    }

    public void setSegundaLetra(String segundaLetra)
    {
        this.segundaLetra = segundaLetra;
    }

    public double getKilometro()
    {
        //Double.parseDouble(ConstantesRegExp.decimalFormatter.format(kilometro));
        return kilometro;
    }

    public void setKilometro(double kilometro)
    {
        this.kilometro = kilometro;
    }

    public String getBloque()
    {
        return bloque;
    }

    public void setBloque(String bloque)
    {
        this.bloque = bloque;
    }

    public String getPuerta()
    {
        return puerta;
    }

    public void setPuerta(String puerta)
    {
        this.puerta = puerta;
    }

    public String getEscalera()
    {
        return escalera;
    }

    public void setEscalera(String escalera)
    {
        this.escalera = escalera;
    }

    public String getPlanta()
    {
        return planta;
    }

    public void setPlanta(String planta)
    {
        this.planta = planta;
    }

    public String getDireccionNoEstructurada()
    {
        return direccionNoEstructurada;
    }

    public void setDireccionNoEstructurada(String direccionNoEstructurada)
    {
        this.direccionNoEstructurada = direccionNoEstructurada;
    }

    public String getCodigoPostal()
    {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal){
        if(codigoPostal != null)
            this.codigoPostal = completarConCeros(codigoPostal, 5);
    }

    public String getNombreVia()
    {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia)
    {
        this.nombreVia = nombreVia;
    }

    public int getApartadoCorreos()
    {
        return apartadoCorreos;
    }

    public void setApartadoCorreos(int apartadoCorreos)
    {
        this.apartadoCorreos = apartadoCorreos;
    }

    public String getTipoVia()
    {
        return tipoVia;
    }

    public void setTipoVia(String tipoVia)
    {
        this.tipoVia = tipoVia;
    }

    public String getNombreProvincia()
    {
        return nombreProvincia;
    }

    public void setNombreProvincia(String nombreProvincia)
    {
        this.nombreProvincia = nombreProvincia;
    }

    public String getNombreMunicipio()
    {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio)
    {
        this.nombreMunicipio = nombreMunicipio;
    }

    public String getDistrito()
    {
        return distrito;
    }

    public void setDistrito(String distrito)
    {
        this.distrito = distrito;
    }

    public int getIdVia()
    {
        return idVia;
    }

    public void setIdVia(int idVia)
    {
        this.idVia = idVia;
    }

    public String getCodMunOrigenAgregacion()
    {
        return codMunOrigenAgregacion;
    }

    public void setCodMunOrigenAgregacion(String codMunOrigenAgregacion)
    {
        this.codMunOrigenAgregacion = codMunOrigenAgregacion;
    }

    public String getCodZonaConcentracion()
    {
        return codZonaConcentracion;
    }

    public void setCodZonaConcentracion(String codZonaConcentracion)
    {
        this.codZonaConcentracion = codZonaConcentracion;
    }

    public String getCodPoligono()
    {
        return codPoligono;
    }

    public void setCodPoligono(String codPoligono)
    {
        this.codPoligono = codPoligono;
    }

    public String getCodParcela()
    {
        return codParcela;
    }

    public void setCodParcela(String codParcela)
    {
        this.codParcela = codParcela;
    }

    public String getCodParaje()
    {
        return codParaje;
    }

    public void setCodParaje(String codParaje)
    {
        this.codParaje = codParaje;
    }

    public String getNombreParaje()
    {
        return nombreParaje;
    }

    public void setNombreParaje(String nombreParaje)
    {
        this.nombreParaje = nombreParaje;
    }

    public Boolean esRustica(){
        return new Boolean(!esUrbana().booleanValue() || (codPoligono!=null&&!codPoligono.equalsIgnoreCase("")
        && codParcela!=null&&!codParcela.equalsIgnoreCase("")));
    }

    public Boolean esUrbana(){
        //return new Boolean(codigoVia!=0);
        return new Boolean(codigoVia!=-1);
    }

    public boolean comparaDireccionesBI(DireccionLocalizacion dir) {
        boolean iguales;
        iguales = dir.getCodigoVia()==this.codigoVia && ((dir.getTipoVia()==null &&this.tipoVia==null)||
        		(dir.getTipoVia()!=null&&this.tipoVia!=null&&dir.getTipoVia().equalsIgnoreCase(this.tipoVia))) &&
        		((dir.getNombreVia()==null && this.nombreVia==null)||(dir.getNombreVia()!=null && this.nombreVia!=null &&
        				dir.getNombreVia().equalsIgnoreCase(this.nombreVia)));
        if(iguales)
        {
//      	iguales = (((dir.getEscalera()==null || dir.getEscalera().equalsIgnoreCase("")) && (this.escalera==null || this.escalera.equalsIgnoreCase("")))||
//      	(dir.getEscalera()!=null && this.escalera!=null && dir.getEscalera().equalsIgnoreCase(this.escalera)))
//      	&&(((dir.getPuerta()==null || dir.getPuerta().equalsIgnoreCase("")) && (this.puerta==null || this.puerta.equalsIgnoreCase("")))||
//      	(dir.getPuerta()!=null && this.puerta!=null && dir.getPuerta().equalsIgnoreCase(this.puerta)))
//      	&&(((dir.getBloque()==null || dir.getBloque().equalsIgnoreCase("")) && (this.bloque==null || this.bloque.equalsIgnoreCase("")))
//      	||(dir.getBloque()!=null && this.bloque!=null && dir.getBloque().equalsIgnoreCase(this.bloque)))
//      	&&(((dir.getPlanta()==null || dir.getPlanta().equalsIgnoreCase("")) && (this.planta==null || this.planta.equalsIgnoreCase("")))||
//      	(dir.getPlanta()!=null && this.planta!=null && dir.getPlanta().equalsIgnoreCase(this.planta)))
//      	&&(dir.getKilometro() == this.kilometro);
        	if(iguales)
        	{
        		iguales = (dir.getPrimerNumero()==this.primerNumero) && (dir.getSegundoNumero() == this.segundoNumero)
        		&&(((dir.getPrimeraLetra()==null || dir.getPrimeraLetra().equalsIgnoreCase(""))&& (this.primeraLetra==null || this.primeraLetra.equalsIgnoreCase("")))||
        				(dir.getPrimeraLetra()!=null && this.primeraLetra!=null && dir.getPrimeraLetra().equalsIgnoreCase(this.primeraLetra)))
        				&&(((dir.getSegundaLetra()==null || dir.getSegundaLetra().equalsIgnoreCase(""))&& (this.segundaLetra==null || this.segundaLetra.equalsIgnoreCase("")))
        						||(dir.getSegundaLetra()!=null && this.segundaLetra!=null && dir.getSegundaLetra().equalsIgnoreCase(this.segundaLetra)));
        		if(iguales)
        		{
//      			iguales = (dir.getCodigoPostal()==this.codigoPostal)&&(dir.getApartadoCorreos()==this.apartadoCorreos);
//      			if(iguales)
//      			{
        			iguales = (((dir.getCodPoligono()==null || dir.getCodPoligono().equalsIgnoreCase(""))&&(this.codPoligono==null || this.codPoligono.equalsIgnoreCase("")))||
        					(dir.getCodPoligono()!=null && this.codPoligono!=null && dir.getCodPoligono().equalsIgnoreCase(this.codPoligono)))
        					&&(((dir.getCodParcela()==null || dir.getCodParcela().equalsIgnoreCase(""))&& (this.codParcela==null || this.codParcela.equalsIgnoreCase("")))||
        							(dir.getCodParcela()!=null&& this.codParcela!=null && dir.getCodParcela().equalsIgnoreCase(this.codParcela)))
        							&&(((dir.getCodParaje()==null || dir.getCodParaje().equalsIgnoreCase(""))&& (this.codParaje==null || this.codParaje.equalsIgnoreCase("")))||
        									(dir.getCodParaje()!=null&& this.codParaje!=null && dir.getCodParaje().equalsIgnoreCase(this.codParaje)))
        									&&(((dir.getNombreParaje()==null || dir.getNombreParaje().equalsIgnoreCase(""))&&(this.nombreParaje==null || this.nombreParaje.equalsIgnoreCase("")) )||
        											(dir.getNombreParaje()!=null && this.nombreParaje!=null && dir.getNombreParaje().equalsIgnoreCase(this.nombreParaje)));
//      			}
        		}
        	}
        }
        return iguales;
    }
    
    /**
     * 
     * @param cadena 
     * @param longitud 
     * @return 
     */
	private static String completarConCeros(String cadena, int longitud){

		while (cadena.length()< longitud){
			cadena = '0' + cadena;
		}
		return cadena;
	}

}
