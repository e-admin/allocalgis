package com.geopista.protocol.licencias.actividad;

/**
 * @author SATEC
 * @version $Revision: 1.3 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2009/09/11 09:53:24 $
 *          $Name:  $
 *          $RCSfile: DatosActividad.java,v $
 *          $Revision: 1.3 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class DatosActividad {
	private long idActividad;
    private long idSolicitud;
    private String numExpedienteObra;

	private int numOperarios;
    private int aforo;
    private double alturaTechos;
	private String descripcionVentilacion;
    private String descripcionAlmacenaje;
    private int alquiler= 0;

	public DatosActividad() {
	}

	public long getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(long idActividad) {
		this.idActividad = idActividad;
	}
	
    public String getIdActividadString()
    {
        return String.valueOf(idActividad);
    }
    public void setIdActividadString(String str)
    {
        if (str==null || str.length()==0) return;
        idActividad = Long.parseLong(str);
    }

    public long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getNumExpedienteObra() {
        return numExpedienteObra;
    }

    public void setNumExpedienteObra(String numExpediente) {
        this.numExpedienteObra = numExpediente;
    }

	public int getNumeroOperarios() {
		return numOperarios;
	}

	public void setNumeroOperarios(int n) {
		this.numOperarios= n;
	}

	public int getAforo() {
		return aforo;
	}

	public void setAforo(int n) {
		this.aforo= n;
	}

	public double getAlturaTechos() {
		return alturaTechos;
	}

	public void setAlturaTechos(double altura) {
		this.alturaTechos= altura;
	}

    public String getDescripcionAlmacenaje() {
        return descripcionAlmacenaje;
    }

    public void setDescripcionAlmacenaje(String descripcion) {
        this.descripcionAlmacenaje= descripcion;
    }

    public String getDescripcionVentilacion() {
        return descripcionVentilacion;
    }

    public void setDescripcionVentilacion(String descripcion) {
        this.descripcionVentilacion= descripcion;
    }

    public int getAlquiler(){
        return alquiler;
    }

    public void setAlquiler(int i){
        this.alquiler= i;
    }

    public String getAforoString(){
    	return String.valueOf(aforo);
    }
    public void setAforoString(String str){
        if (str==null || str.length()==0) return;
        aforo = Integer.parseInt(str);
    }
    public String getNumeroOperariosString(){
    	return String.valueOf(numOperarios);
    }
    public void setNumeroOperariosString(String str){
        if (str==null || str.length()==0) return;
        numOperarios = Integer.parseInt(str);
    }
    public String getAlturaTechosString(){
    	return String.valueOf(alturaTechos);
    }
    public void setAlturaTechosString(String str){
        if (str==null || str.length()==0) return;
        alturaTechos = Double.parseDouble(str);
    }
    
}
