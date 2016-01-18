package com.geopista.protocol.control;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 04-may-2004
 * Time: 15:04:05
 * To change this template use Options | File Templates.
 */
public class ListaSesiones implements Serializable {

	Hashtable<String, ISesion> lista = new Hashtable<String, ISesion>();

    public ListaSesiones() {
    }

    public Hashtable<String, ISesion> getLista() {
        return lista;
    }

    public void setLista(Hashtable<String, ISesion> lista) {
        this.lista = lista;
    }

    public boolean existeSesion(String sIdSesion)
    {
        return (lista.containsKey(sIdSesion));
    }
    public Sesion getSesion(String sIdSesion)
    {
        return (Sesion)lista.get(sIdSesion);
    }
    public void add(ISesion sSesion)
    {
        lista.put(sSesion.getIdSesion(),sSesion);
    }
    public void delete(Sesion sSesion)
    {
        lista.remove(sSesion.getIdSesion());
    }
    public Object delete(String sIdSesion)
    {
            return lista.remove(sIdSesion);
    }

    public Sesion getSesion(String sUserName, String sIdApp)
    {
        Sesion auxSesion= getSesion(sUserName);
        if ((auxSesion!=null)&&(auxSesion.getIdApp().equalsIgnoreCase(sIdApp))) return auxSesion;
        for (Enumeration<ISesion> e=lista.elements();e.hasMoreElements();)
        {
            auxSesion= (Sesion)e.nextElement();
            try
            {
                 System.out.println("Evaluando:"+auxSesion.getUserPrincipal().getName());
                 if ((auxSesion.getUserPrincipal().getName().equalsIgnoreCase(sUserName))
                   && auxSesion.getIdApp().equals(sIdApp))
                 {
                     return auxSesion;
                 }
            }catch(Exception ex)
            {
                System.out.println("Excepcion"+ex.toString());
            }
        }
        return null;
    }
    public ListaSesionesSimple getListaSesionesSimple(String idMunicipio)
    {
        Hashtable<String, SesionSimple> listaSimple= new Hashtable<String, SesionSimple>();
        for (Enumeration<ISesion> e= lista.elements();e.hasMoreElements();)
        {
            Sesion sesion = (Sesion)e.nextElement();
            if ((idMunicipio==null) || (sesion.getIdMunicipio()==null)){
            	listaSimple.put(sesion.getIdSesion(),new SesionSimple(sesion));
            }
            else if (sesion.getIdMunicipio().equals(idMunicipio))
            {
                listaSimple.put(sesion.getIdSesion(),new SesionSimple(sesion));
            }
        }

        return new ListaSesionesSimple(listaSimple);
    }
}
