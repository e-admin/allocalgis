package com.geopista.app.acteconomicas;



import java.util.Vector;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 06-jun-2006
 * Time: 13:08:43
 */
public class Via {
   int id;
   String tipoviaine;
   String nombreviaine;
   Vector numerosPolicia;

    public Via( int id, String tipoviaine, String nombreviaine) {
        this.nombreviaine = nombreviaine;
        this.id = id;
         this.tipoviaine = tipoviaine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreviaine() {
        return nombreviaine;
    }

    public void setNombreviaine(String nombreviaine) {
        this.nombreviaine = nombreviaine;
    }

    public Vector getNumerosPolicia() {
        return numerosPolicia;
    }

    public void setNumerosPolicia(Vector numerosPolicia) {
        this.numerosPolicia = numerosPolicia;
    }

    public String getTipoviaine() {
        return tipoviaine;
    }

    public void setTipoviaine(String tipoviaine) {
        this.tipoviaine = tipoviaine;
    }
    public void addNumeroPolicia(NumeroPolicia numeroPolicia)
    {
        if (numerosPolicia==null) numerosPolicia=new Vector();
        numerosPolicia.add(numeroPolicia);
    }
    public String toString()
    {
        return nombreviaine!=null?nombreviaine:"";
    }
}
