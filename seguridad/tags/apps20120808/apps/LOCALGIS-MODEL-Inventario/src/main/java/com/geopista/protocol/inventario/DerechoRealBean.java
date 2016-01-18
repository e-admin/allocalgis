package com.geopista.protocol.inventario;

import java.io.Serializable;


/**
 * Creado por SATEC.
 * User: angeles
 * Date: 07-sep-2006
 * Time: 15:47:16
 */
public class DerechoRealBean  extends BienBean implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String destino;
    private String bien;
    private Double costeAdquisicion;
    private Double valorActual;
    private String clase;
    RegistroBean registro;
    
    public DerechoRealBean(){
   	 	super();
   	 	setTipo(Const.PATRON_DERECHOS_REALES);
    }

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public RegistroBean getRegistro() {
        return registro;
    }

    public void setRegistro(RegistroBean registro) {
        this.registro = registro;
    }

    public String getBien() {
        return bien;
    }

    public void setBien(String bien) {
        this.bien = bien;
    }

    public Double getCosteAdquisicion() {
        return costeAdquisicion;
    }

    public void setCosteAdquisicion(double costeAdquisicion) {
        this.costeAdquisicion = new Double(costeAdquisicion);
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }


    public Double getValorActual() {
        return valorActual;
    }

    public void setValorActual(double valorActual) {
        this.valorActual = new Double(valorActual);
    }
    public String getRegistroTomo() {
          return (registro==null?null:registro.getTomo());
      }

      public void setRegistroTomo(String registroTomo) {
          if (registro==null) this.registro=new RegistroBean();
          registro.setTomo(registroTomo);
      }

      public String getRegistroFolio() {
          return (registro==null?null:registro.getFolio());
      }

      public void setRegistroFolio(String registroFolio) {
          if (registro==null) this.registro=new RegistroBean();
          registro.setFolio(registroFolio);
      }

      public String getRegistroLibro() {
          return (registro==null?null:registro.getLibro());
      }

      public void setRegistroLibro(String registroLibro) {
          if (registro==null) this.registro=new RegistroBean();
           registro.setLibro(registroLibro);
      }

      public String getRegistroFinca() {
          return (registro==null?null:registro.getFinca());
      }

      public void setRegistroFinca(String registroFinca) {
          if (registro==null) this.registro=new RegistroBean();
               registro.setFinca(registroFinca);
      }

      public String getRegistroInscripcion() {
          return (registro==null?null:registro.getInscripcion());
      }

      public void setRegistroInscripcion(String registroInscripcion) {
          if (registro==null) this.registro=new RegistroBean();
                 registro.setInscripcion(registroInscripcion);
      }

      public String getRegistroProtocolo() {
          return (registro==null?null:registro.getProtocolo());
      }

      public void setRegistroProtocolo(String registroProtocolo) {
          if (registro==null) this.registro=new RegistroBean();
          registro.setProtocolo(registroProtocolo);
      }

      public String getRegistroNotario() {
          return (registro==null?null:registro.getNotario());
      }

      public void setRegistroNotario(String registroNotario) {
          if (registro==null) this.registro=new RegistroBean();
          registro.setNotario(registroNotario);
      }

      public String getRegistroPropiedad() {
          return (registro==null?null:registro.getPropiedad());
      }

      public void setRegistroPropiedad(String propiedad) {
          if (this.registro==null) this.registro=new RegistroBean();
          this.registro.setPropiedad(propiedad);

      }
      
      /**
       * Para que sea compatible con versiones anteriores
       * @return
       */
      public String getFormaAdquisicion() {
          return getAdquisicion();
      }

      public void setFormaAdquisicion(String forma) {
          setAdquisicion(forma);

      }

      public boolean isPatrimonioMunicipal() {
          return patrimonioMunicipal;
      }

      public void setPatrimonioMunicipal(boolean patrimonioMunicipal) {
          this.patrimonioMunicipal = patrimonioMunicipal;
      }

      public void setPatrimonioMunicipal(String patrimonioMunicipal) {
           try{this.patrimonioMunicipal= patrimonioMunicipal.equalsIgnoreCase("1")?true:false;}catch(Exception e){this.patrimonioMunicipal= false;}
      }

}
