package com.geopista.app.infraestructuras;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;

public class GeopistaValidarImportacion 
{
  private int i=0;
  private String nombreTabla;
  private String nombreCampo;
  private String tipoInfraestructura;
  private boolean correcto;
  private static ApplicationContext app = AppContext.getApplicationContext();  
  private DateFormat targetDateFormat = new SimpleDateFormat("dd-MM-yyyy");

  public GeopistaValidarImportacion()
  {
  
  }


 public boolean compruebaCaptaciones(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
  int cuenta=0;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
       if (tipoFichero == 0)
       {    
           
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DENOMINACION"))
	       {
	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	        }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOCAPTACION"))
	       {
	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOTITULAR"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
	       {
	        correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1;  
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");   
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOGESTION"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("SISTEMACAPTACION"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	       }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOESTADO"))
	      {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	      }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().toUpperCase().equals("ID_SUBCUENCA"))
	      {
	        correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");   
	        }
	      }
	    }
       else
       {
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DENOMINA"))
	       {
	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	        }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPCAP"))
	       {
	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
	       {
	         correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");   
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TITCAP"))
	       {
	        correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1;  
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("GESCAP"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("SISCAP"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	       }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTCAP"))
	      {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
	        }
	      }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDSUB"))
	      {
	        correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");   
	        }
	      }
   
       }
    }//for

    if(cuenta != 8)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
      correcto = false;
    }
   return correcto; 
  }

 public boolean compruebaPiezas(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
 {
 int cuenta=0;
   for(i=0;i<esquemaTabla.getAttributeCount();i++)
   {
       
   	if (tipoFichero == 0)
	{    
          
		if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ID_NUCLEO"))
	        {
	        	correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        	cuenta = cuenta + 1; 
	        	if (correcto == false)
	        	{
	        	    textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        	}
	        }
	        
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("SECCION"))
	       	{
	        	correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        	cuenta = cuenta + 1; 
	        	 if (correcto == false)
	        	{
	        	     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	        	}
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPO"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
	       {
	        correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1;  
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
	        }
	       }
	       
	}
	else
	{
		if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDNUC"))
		        {
		        	correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
		        	cuenta = cuenta + 1; 
		        	if (correcto == false)
		        	{
		        	    textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
		        	}
		        }
		        
		        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("SECCION"))
		       	{
		        	correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
		        	cuenta = cuenta + 1; 
		        	 if (correcto == false)
		        	{
		        	     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
		        	}
		       }
		       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPO"))
		       {
		         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
		         cuenta = cuenta + 1; 
		          if (correcto == false)
		        {
		              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
		        }
		       }
		       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
		       {
		        correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
		        cuenta = cuenta + 1;  
		         if (correcto == false)
		        {
		             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
		        }
	       }
	}
	
   }//for
  
   if(cuenta != 4)
   {
     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
     correcto = false;
   }
  return correcto; 
 }
 public boolean compruebaDepositos(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
     int cuenta=0;

     for(i=0;i<esquemaTabla.getAttributeCount();i++)
     {
     	if (tipoFichero == 0)
     	{
 	      
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOUBICACION"))
 	       {
 		 correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		  if (correcto == false)
 		{
 		     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
 		}
 	       }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOTITULAR"))
 	       {
 		 correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		  if (correcto == false)
 		{
 		     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
 		}
 	       }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOGESTION"))
 	       {
 		 correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		  if (correcto == false)
 		{
 		     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
 		}
 	       }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
 	       {
 		correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		cuenta = cuenta + 1;  
 		 if (correcto == false)
 		{
 		    textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");   
 		}

 	       }
 		if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOESTADO"))
 	       {
 		 correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		  if (correcto == false)
 		{
 		     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
 		}
 	       }
 		if(esquemaTabla.getAttributeName(i).toUpperCase().equals("CAPACIDAD"))
 	       {
 		 correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		  if (correcto == false)
 		{
 		     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");   
 		}
 	       }
 	}
 	else
 	{
 	   if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPDEP"))
 	   {
 		 correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		 if (correcto == false)
 		 {
 		    textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
 		 }
 	   }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TITDEP"))
 	       {
 		 correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		  if (correcto == false)
 		{
 		     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
 		}
 	       }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
 	       {
 		 correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		  if (correcto == false)
 		{
 		     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");   
 		}
 	       }
 	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("GESDEP"))
 	       {
 		correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		cuenta = cuenta + 1;  
 		 if (correcto == false)
 		{
 		    textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
 		}

 	       }
 		if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTDEP"))
 	       {
 		 correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		  if (correcto == false)
 		{
 		  break;
 		}
 	       }
 		if(esquemaTabla.getAttributeName(i).toUpperCase().equals("CAPDEP"))
 	       {
 		 correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
 		 cuenta = cuenta + 1; 
 		  if (correcto == false)
 		  {
 		     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");   
 		  }
 	       }		
 	}
     }
     if(cuenta != 6)
     {
         textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
       correcto = false;
    
     }

     return correcto;
   
  }

 public boolean compruebaPotabilizadoras(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
     int cuenta=0;

     for(i=0;i<esquemaTabla.getAttributeCount();i++)
     {
     	
     	if (tipoFichero == 0)
     	{
     	   if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOTRATAMIENTO"))
	       {
			 correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
			 cuenta = cuenta + 1; 
			 if (correcto == false)
			 {
			     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
			 }
	       }
     	  if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOUBICACION"))
	       {
			 correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
			 cuenta = cuenta + 1; 
			 if (correcto == false)
			 {
			     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");   
			 }
	       }
     	  if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
	      {
     	      correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
     	      cuenta = cuenta + 1;  
     	      if (correcto == false)
     	      {
     	         textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");   
     	      }
	       }
     	  if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FLOCULACION"))
	      {
     	      correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
     	      cuenta = cuenta + 1; 
     	      if (correcto == false)
     	      {
     	         textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");   
     	      }
	       }
     	  if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DECANTACION"))
	      {
     	      correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
     	      cuenta = cuenta + 1; 
     	      if (correcto == false)
     	      {
     	         textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
     	      }
	       }
	     	 if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FILTRACION"))
		     {
				correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
				cuenta = cuenta + 1; 
				 if (correcto == false)
				{
				     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
				}
		      }
	     	if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTERILIZACION"))
		    {
				correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
				cuenta = cuenta + 1; 
				 if (correcto == false)
				 {
				     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
				 }
		    }
	     	if(esquemaTabla.getAttributeName(i).toUpperCase().equals("OTROS"))
		    {
	     	    correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
				cuenta = cuenta + 1; 
				if (correcto == false)
				{
				    textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
				}
		    }
	     	if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOPERIODICIDAD"))
		    {
	     	    correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	     	    cuenta = cuenta + 1; 
	     	    if (correcto == false)
	     	    {
	     	       textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	     	    }
		    }
	     	if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOORGANISMOCONTROL"))
		    {
	     	    correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	     	    cuenta = cuenta + 1; 
	     	    if (correcto == false)
	     	    {
	     	       textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	     	    }
		    }
	     	if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOESTADO"))
		    {
	     	    correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	     	    cuenta = cuenta + 1; 
	     	    if (correcto == false)
	     	    {
	     	       textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	     	    }
		     }
     	}//if
     	else
     	{
     	  
     	   if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TRAPOT"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	         if (correcto == false)
		 {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
		 }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPPOT"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
	       {
	         correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1;  
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FLOCUL"))
	       {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DECANT"))
	       {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FILTRAC"))
	       {
	        correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	        cuenta = cuenta + 1; 
	         if (correcto == false)
	        {
	             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTERIL"))
	       {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("OTROS"))
	       {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PERPOT"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ORGPOT"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	      if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTPOT"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
 	    }//else
     }//for

     if(cuenta != 11)
     {
         textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
       correcto = false;
     }

     return correcto;
  
  }

 public boolean compruebaTramosAbastecimiento(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
    int cuenta=0;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
       if(tipoFichero == 0)
       {
           if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ID_NUCLEO"))
	       {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOESTADO"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOMATERIAL"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOTITULAR"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
	       {
	          correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOGESTION"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
	       {
	          correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	          cuenta = cuenta + 1;
	           if (correcto == false)
	        {
	               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
	       {
	           correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
	          cuenta = cuenta + 1;
	           if (correcto == false)
	        {
	               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESIONTRABAJO"))
	       {
	           correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
	          cuenta = cuenta + 1;
	           if (correcto == false)
	        {
	               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	        }
	       }
	       
       }//if
       else
       {
           if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDNUC"))
	       {
	         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTDIS"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("MATDIS"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
	       {
	         correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TITDIS"))
	       {
	          correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("GESDIS"))
	       {
	         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	         cuenta = cuenta + 1; 
	          if (correcto == false)
	        {
	              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
	        }
	       }
	        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
	       {
	          correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
	          cuenta = cuenta + 1;
	           if (correcto == false)
	        {
	               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
	       {
	          correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
	          cuenta = cuenta + 1;
	           if (correcto == false)
	        {
	               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	        }
	       }
	       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESION"))
	       {
	           correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
	          cuenta = cuenta + 1;
	           if (correcto == false)
	        {
	               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
	        }
	       }
       }//else
       
      }
    if(cuenta != 9)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
      correcto = false;
    }

      return correcto;
  }
 public boolean compruebaConducciones(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
    int cuenta=0;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
      if(tipoFichero==0)
      {
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOESTADO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOMATERIAL"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOTITULAR"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
       {
          correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOGESTION"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
        {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
        }
       }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
       {
          correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1;
           if (correcto == false)
        {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
       {
          correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1;
           if (correcto == false)
        {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
        }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESIONTRABAJO"))
       {
          correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1;
           if (correcto == false)
        {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
        }
       }
       
      }//if
      else
      {
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTCON"))
          {
            correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
             if (correcto == false)
           {
                 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           }
          }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("MATCON"))
          {
            correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
             if (correcto == false)
           {
                 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           }
          }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TITCON"))
          {
            correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
             if (correcto == false)
           {
                 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           }
          }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
          {
             correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
             if (correcto == false)
           {
                 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
           }
          }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("GESCON"))
          {
            correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
             if (correcto == false)
           {
                 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           }
          }
           if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
          {
             correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
             cuenta = cuenta + 1;
              if (correcto == false)
           {
                  textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
           }
          }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
          {
             correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
             cuenta = cuenta + 1;
              if (correcto == false)
           {
                  textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
           }
          }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESION"))
          {
             correcto = compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
             cuenta = cuenta + 1;
              if (correcto == false)
           {
                  textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
           }
          }
 
      }//else
      }
    if(cuenta < 8)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
      correcto = false;
    }

      return correcto;
  }

 public boolean compruebaElementosSaneamiento(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
    int cuenta=0;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
        if (tipoFichero==0)
        {
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ID_NUCLEO"))
       {
          correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
           if (correcto == false)
          {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("COTA"))
       {
           correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
          }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("SECCION"))
       {
           correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
          }
       }
     
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
       {
          correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
          }
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
         if (correcto == false)
         {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
         }
       }
        }//if
        else
        {
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDNUC"))
            {
               correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
               cuenta = cuenta + 1; 
                if (correcto == false)
               {
                    textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
               }
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("COTA"))
            {
              correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
               }
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("SECCION"))
            {
                correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
               }
            }
          
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
            {
               correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
               }
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPO"))
            {
              correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
              if (correcto == false)
              {
                  textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
              }
            }
        }//else
      }
    if(cuenta != 5)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
      correcto = false;
    }

      return correcto;

  }

 public boolean compruebaDepuradoras(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
    int cuenta=0;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
    if(tipoFichero==0)
    {
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOTITULAR"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
         if (correcto == false)
          {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOGESTION"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
       }
     
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
       {
          correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOSISTEMADEPURADORA"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
         if(esquemaTabla.getAttributeName(i).toUpperCase().equals("CAPACIDAD"))
       {
         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
         if (correcto == false)
          {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          }
      
       }
    }//if
    else
    {
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TITDEP"))
        {
          correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
          if (correcto == false)
           {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           }
       
        }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("GESDEP"))
        {
          correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
           if (correcto == false)
           {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           }
        }
      
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
        {
           correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
           cuenta = cuenta + 1; 
           if (correcto == false)
           {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
           }
       
        }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("SISDEP"))
        {
          correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
           if (correcto == false)
           {
               textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           }
       
        }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("CAPDEP"))
        {
              correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1; 
          if (correcto == false)
           {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
           }
       
        }

    }//else
      }
    if(cuenta != 5)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
      correcto = false;
    }

      return correcto;
  }

 public boolean compruebaTramosSaneamiento(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
    int cuenta=0;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
     if (tipoFichero==0)
     {
         if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ID_NUCLEO"))
         {
           correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
           cuenta = cuenta + 1; 
            if (correcto == false)
            {
                textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
            }
        
         }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOMATERIAL"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
       {
         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          }
      
       }
     
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
       {
          correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOESTADO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOTITULAR"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
         if (correcto == false)
          {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }

       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOGESTION"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
       {
           correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESIONTRABAJO"))
       {
           correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
          }
      
       }
       

     }//if
     else
     {
         if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDNUC"))
         {
           correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
           cuenta = cuenta + 1; 
            if (correcto == false)
            {
                textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
            }
        
         }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("MATSAN"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
       {
         correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          }
      
       }
     
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
       {
          correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTSAN"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TITSAN"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
         if (correcto == false)
          {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }

       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("GESSAN"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
       {
           correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESION"))
       {
           correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
          }
      
       }
     }
      }
    if(cuenta != 9)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
      correcto = false;
    }

      return correcto;
  }

 public boolean compruebaEmisarios(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
       int cuenta=0;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
        if(tipoFichero==0)
        {
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DENOMINACION"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
         if (correcto == false)
          {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOMATERIAL"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOESTADO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }

       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOVERTIDO"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
         if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOTITULAR"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOGESTION"))
       {
         correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
         cuenta = cuenta + 1; 
         if (correcto == false)
          {
             textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
       {
        correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
        cuenta = cuenta + 1;  
        if (correcto == false)
          {
            textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
          }
      
       }

       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("LONGITUDTERRESTRE"))
       {
          correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("LONGITUDMARITIMA"))
       {
          correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
       {
          correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
       {
          correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
          }
      
       }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DISTANCIAVERTIDO"))
       {
          correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
          }
      
       }
       if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESIONTRABAJO"))
       {
          correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
          cuenta = cuenta + 1;
          if (correcto == false)
          {
              textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
          }
      
       }
        
    }//if
        
        else
        {
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DNVEMI"))
            {
              correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
              if (correcto == false)
               {
                  textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("MATEMI"))
            {
              correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTEMI"))
            {
              correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }

            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TVREMI"))
            {
              correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
              if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TITEMI"))
            {
              correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("GESEMI"))
            {
              correcto = compruebaSTRING(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
              if (correcto == false)
               {
                  textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
            {
             correcto = compruebaDATE(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
             cuenta = cuenta + 1;  
             if (correcto == false)
               {
                 textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
               }
           
            }

            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("LONEMI"))
            {
               correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("LNMEMI"))
            {
               correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
            {
               correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
            {
               correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
               }
           
            }
             if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DVREMI"))
            {
               correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESION"))
            {
               correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla, esquemaTabla.getAttributeName(i).toString()));
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
               }
           
            }

        }
  }
    
    if(cuenta != 13)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
      correcto = false;
    }

      return correcto;
  }

 public boolean compruebaColectores(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
    int cuenta=0;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
        if(tipoFichero==0)
        {
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ID_NUCLEO"))
            {
               correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
               cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
               }
           
            }
          
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOMATERIAL"))
            {
              correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
            {
              correcto = compruebaINTEGER(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
            {
              correcto = compruebaDATE(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                       textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOESTADO"))
            {
              correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOTITULAR"))
            {
               correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
            {
                correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla,  esquemaTabla.getAttributeName(i).toString()));
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
               }
           
            }

            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESIONTRABAJO"))
            {
               correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla,  esquemaTabla.getAttributeName(i).toString()));
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
               }
           
            }
           
           
        }
        else
        {
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("IDNUC"))
            {
               correcto = compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString());
               cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
               }
           
            }
          
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("MATCOL"))
            {
              correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIRECCION"))
            {
              correcto = compruebaINTEGER(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
            {
              correcto = compruebaDATE(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                       textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTCOL"))
            {
              correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
              cuenta = cuenta + 1; 
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TITCOL"))
            {
               correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("GESCOL"))
            {
               correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
               }
           
            }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("DIAMETRO"))
            {
                correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla,  esquemaTabla.getAttributeName(i).toString()));
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
               }
           
            }

            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("PRESION"))
            {
                correcto = (compruebaINTEGER(esquemaTabla, esquemaTabla.getAttributeName(i).toString())||compruebaDOUBLE(esquemaTabla,  esquemaTabla.getAttributeName(i).toString()));
               cuenta = cuenta + 1;
               if (correcto == false)
               {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDouble") + " " +"</b></font></p>");
               }
           
            }

        }
   
    }
    if (tipoFichero == 0)
    {
        cuenta = cuenta + 1;//porque en los jml, hay 1 campo menos a validar que en los shapefiles
    }
    if(cuenta != 9)
    {
      textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
      correcto = false;
    }
      return correcto;
    
     
  }

 public boolean compruebaSaneamientoAutonomo(StringBuffer textoEditor, FeatureSchema esquemaTabla, int tipoFichero)
  {
    int cuenta=0;
    for(i=0;i<esquemaTabla.getAttributeCount();i++)
    {
        if (tipoFichero==0)
        {
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHAINSTALACION"))
          {
             correcto = compruebaDATE(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
                 if (correcto == false)
             {
                     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
             }
         
          }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOESTADO"))
          {
            correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
                 if (correcto == false)
             {
                     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
             }
         
          }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOMOTIVODEFICIT"))
          {
            correcto = compruebaINTEGER(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
                 if (correcto == false)
             {
                     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
             }
         
          }
             if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPOSANEAAUTO"))
          {
            correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
                 if (correcto == false)
             {
                     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
             }
         
          }
            if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESADECUADO"))
          {
            correcto = compruebaINTEGER(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
            cuenta = cuenta + 1; 
                 if (correcto == false)
             {
                     textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
             }
         
          }
     }
    else
    {
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("FECHA"))
        {
           correcto = compruebaDATE(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
               if (correcto == false)
           {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoDate") + " " +"</b></font></p>");
           }
       
        }
        if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ESTSAU"))
        {
          correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
               if (correcto == false)
           {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           }
       
        }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("MOTIVO"))
        {
          correcto = compruebaINTEGER(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
               if (correcto == false)
           {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
           }
       
        }
           if(esquemaTabla.getAttributeName(i).toUpperCase().equals("TIPO"))
        {
          correcto = compruebaSTRING(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
               if (correcto == false)
           {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoString") + " " +"</b></font></p>");
           }
       
        }
          if(esquemaTabla.getAttributeName(i).toUpperCase().equals("ADECUADO"))
        {
          correcto = compruebaINTEGER(esquemaTabla,  esquemaTabla.getAttributeName(i).toString());
          cuenta = cuenta + 1; 
               if (correcto == false)
           {
                   textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("tipoCampo")+ esquemaTabla.getAttributeName(i).toUpperCase() + " " + app.getI18nString("tipoCampoInteger") + " " + "</b></font></p>");
           }
       
        }
    }//else
    }//for
    if(cuenta != 5)
    {
        textoEditor.append("<p><font face=SansSerif size=3 color=#ff0000><b>"+ app.getI18nString("faltanCampos")+ "</b></font></p>");
      correcto = false;
    }

      return correcto;

  }
 public boolean compruebaINTEGER(FeatureSchema esquemaCampo, String nombreAtributo)
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
 public boolean compruebaSTRING(FeatureSchema esquemaCampo, String nombreAtributo)
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