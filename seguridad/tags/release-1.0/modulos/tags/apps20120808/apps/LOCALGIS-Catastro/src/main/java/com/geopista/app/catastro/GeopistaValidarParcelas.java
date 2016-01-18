/*
 * Created on 29-ago-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;

/**
 * @author ipequeño
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GeopistaValidarParcelas
{ 
    public boolean correcto;
    private int i=0;
    
    private static ApplicationContext app = AppContext.getApplicationContext();
    
    public GeopistaValidarParcelas()
    {
        
    }

	public boolean compruebaParcela(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
	{
	    int cuenta = 0;
        String nCampo;
        
	    for(i=0;i<esquemaTabla.getAttributeCount();i++)
	     {
	  	   
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DELEGACIO"))
	        {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	         {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " +"</b></font></p>");
	           
	         }
	         }
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("MUNICIPIO"))
	        {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	         {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " +"</b></font></p>");
	           
	         }
	        }
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("MASA"))
	        {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	         {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	           
	         }
	        }
	        
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("HOJA"))
	        {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	         {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	           
	         }
	        }
	        
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPO"))
	        {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	         {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	           
	         }
	        }
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PARCELA"))
	        {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	         if (correcto == false)
	         {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	           
	         }
	        }
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("VIA"))
	        {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	         if (correcto == false)
	         {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " +"</b></font></p>");
	           
	         }
	        }
	        
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAALTA"))
	        {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	         if (correcto == false)
	         {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " +"</b></font></p>");
	         }
	        }
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHABAJA"))
	        {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	         if (correcto == false)
	         {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ " " + app.getI18nString("tipoCampo")+ " " + esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " +"</b></font></p>");
	         }
	        }
	     }
	    if (cuenta != 9)
        {
            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"
                    + app.getI18nString("faltanCampos") + "</b></font></p>");

            correcto = false;
        }
        return correcto;
	}
	    
	    private boolean compruebaINTEGER(FeatureSchema esquemaCampo, String nombreAtributo)
	    {
	     if(esquemaCampo.getAttributeType(nombreAtributo)==AttributeType.INTEGER)
	     {
	      return true;
	     }
	     else
	     {
	      return false;
	     }
	    }

	  /**
	   * Este método comprueba que un campo del shapefile o del jml es de tipo String
	   * @param esquemaCampo, el esquema localizado en el shapefile o en el fichero jml
	   * @return true si el campo es de tipo String y false si no lo es
	   */

	   private boolean compruebaSTRING(FeatureSchema esquemaCampo, String nombreAtributo)
	    {
	     if(esquemaCampo.getAttributeType(nombreAtributo)==AttributeType.STRING)
	     {
	      return true;
	     }
	     else
	     {
	      return false;
	     }
	    }
	  /**
	   * Este método comprueba que un campo del shapefile o del jml es de tipo Fecha
	   * @param esquemaCampo, el esquema localizado en el shapefile o en el fichero jml
	   * @return true si el campo es de tipo Fecha y false si no lo es
	   */

	   private boolean compruebaDATE(FeatureSchema esquemaCampo, String nombreAtributo)
	    {
	     if(esquemaCampo.getAttributeType(nombreAtributo)==AttributeType.DATE)
	     {
	      return true;
	     }
	     else
	     {
	      return false;
	     }
	    }
	  /**
	   * Este método comprueba que un campo del shapefile o del jml es de tipo Doble
	   * @param esquemaCampo, el esquema localizado en el shapefile o en el fichero jml
	   * @return true si el campo es de tipo Doble y false si no lo es
	   */
	    private boolean compruebaDOUBLE(FeatureSchema esquemaCampo, String nombreAtributo)
	    {
	     if(esquemaCampo.getAttributeType(nombreAtributo)==AttributeType.DOUBLE)
	     {
	      return true;
	     }
	     else
	     {
	      return false;
	     }
	    }

}