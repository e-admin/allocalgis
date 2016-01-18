package com.geopista.server.database;

import java.io.File;
import java.io.FilenameFilter;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.filter.LCGNodoEIEL;
import com.geopista.app.plantillas.ConstantesLocalGISPlantillas;
import com.geopista.server.administrador.init.Constantes;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.SRID;

import com.geopista.server.document.DocumentoEnDisco;


public class COperacionesPlantillas {

	private SRID srid;
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(COperacionesPlantillas.class);

	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();
	
	public COperacionesPlantillas() {
	}

	public COperacionesPlantillas(SRID srid) {
		this.srid = srid;
	}

	
	
	
	/**
     * Retorna las plantillas para la generacion de informes
     * @param oos
     * @param path
     * @throws Exception
     */
    public void returnPlantillas(ObjectOutputStream oos, String path,String filtro,String patron,ArrayList patrones,String idEntidad, String idMunicipio) throws Exception{
           try{
        	   Collection plantillasGenerales=getPlantillas(path,filtro,patron,patrones,null, idMunicipio);
        	   Collection plantillasEspecificas=getPlantillas(path,filtro,patron,patrones,idEntidad, idMunicipio);
        	   
        	   Iterator it1=plantillasGenerales.iterator();
        	   Iterator it2=plantillasEspecificas.iterator();
        	   
        	   //Hay 3 elementos
        	   ArrayList imagenes1=(ArrayList)it1.next();
           	   ArrayList reports1=(ArrayList)it1.next();
           	   ArrayList subreports1=(ArrayList)it1.next();

        	   ArrayList imagenes2=(ArrayList)it2.next();
           	   ArrayList reports2=(ArrayList)it2.next();
           	   ArrayList subreports2=(ArrayList)it2.next();

           	   imagenes1.addAll(imagenes2);
           	   reports1.addAll(reports2);
           	   subreports1.addAll(subreports2);
        	   
     
              oos.writeObject(imagenes1);
              oos.writeObject(reports1);
              oos.writeObject(subreports1);
              
              
           }catch(Exception e){
               logger.error("returnPlantillas: "+ e.getMessage());
               oos.writeObject(new ACException(e));
               throw e;
           }
    }	
    
    private Collection getPlantillas(String path,String filtro,String patron,ArrayList patrones,String idEntidad, String idMunicipio) throws Exception{

        ArrayList aList= new ArrayList();
        ArrayList plantillas= new ArrayList();
        Object[] plantilla;
        ArrayList imagenes= new ArrayList();
        Object[] imagen;
       
        File dir=null;
        String[] arrayPlantillas = null;         
        String pathPlantillas = "";
       	String subPath = "";

        if (path.contains(ConstantesLocalGISPlantillas.PATH_IMPRESION)){
        	pathPlantillas = ConstantesLocalGISPlantillas.PATH_IMPRESION;
        	arrayPlantillas = path.split(ConstantesLocalGISPlantillas.PATH_INVENTARIO);        	
        }else{
        	if (path.contains(ConstantesLocalGISPlantillas.PATH_ESPACIOPUBLICO)){
        		pathPlantillas = ConstantesLocalGISPlantillas.PATH_ESPACIOPUBLICO;
        		arrayPlantillas = path.split(ConstantesLocalGISPlantillas.PATH_ESPACIOPUBLICO);
        	}else{
            	if (path.contains(ConstantesLocalGISPlantillas.PATH_OCUPACION)){
            		pathPlantillas = ConstantesLocalGISPlantillas.PATH_OCUPACION;
            		arrayPlantillas = path.split(ConstantesLocalGISPlantillas.PATH_OCUPACION);
            	}else{
                	if (path.contains(ConstantesLocalGISPlantillas.PATH_LICENCIAS)){
                		pathPlantillas = ConstantesLocalGISPlantillas.PATH_LICENCIAS;
                		arrayPlantillas = path.split(ConstantesLocalGISPlantillas.PATH_LICENCIAS);
                	}else{
                    	if (path.contains(ConstantesLocalGISPlantillas.PATH_INVENTARIO)){
                    		pathPlantillas = ConstantesLocalGISPlantillas.PATH_INVENTARIO;
                    		arrayPlantillas = path.split(ConstantesLocalGISPlantillas.PATH_INVENTARIO);
                    	}else{
                        	if (path.contains(ConstantesLocalGISPlantillas.PATH_EIEL)){
                        		pathPlantillas = ConstantesLocalGISPlantillas.PATH_EIEL;
                        		arrayPlantillas = path.split(ConstantesLocalGISPlantillas.PATH_EIEL);
                        	}else{
                            	if (path.contains(ConstantesLocalGISPlantillas.PATH_CONTAMINANTES)){
                            		pathPlantillas = ConstantesLocalGISPlantillas.PATH_CONTAMINANTES;
                            		arrayPlantillas = path.split(ConstantesLocalGISPlantillas.PATH_CONTAMINANTES);
                            	}else{
                                	if (path.contains(ConstantesLocalGISPlantillas.PATH_ACTIVIDAD)){
                                		pathPlantillas = ConstantesLocalGISPlantillas.PATH_ACTIVIDAD;
                                		arrayPlantillas = path.split(ConstantesLocalGISPlantillas.PATH_ACTIVIDAD);
                                	}
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if ((arrayPlantillas != null) && (arrayPlantillas.length > 1))
       		subPath = arrayPlantillas[1];
        
        if (idEntidad==null)
        	dir = new File(ConstantesLocalGISPlantillas.PATH_PLANTILLAS+File.separator+pathPlantillas + 
        			File.separator + ConstantesLocalGISPlantillas.PATH_IMG);
        else
        	dir = new File(Constantes.DIR_PLANTILLAS+File.separator+idEntidad+File.separator+pathPlantillas+File.separator 
        			+ ConstantesLocalGISPlantillas.PATH_IMG);
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();

            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (int i = 0; i < children.length; i++) {
                    // Get filename of file or directory
                    File file= children[i];
                    // Si la carpeta img contiene la subcarpeta escudos nos quedamos con la imagen del municipio
                    if (file.toString().endsWith(ConstantesLocalGISPlantillas.PATH_ESCUDOS) && file.isDirectory()){
                    	File[] children2 = file.listFiles();
                    	 if (children2 == null) {
                             // Either dir does not exist or is not a directory
                         } else {
                             for (int j = 0; j < children2.length; j++) {
                                 // Get filename of file or directory
                                 File file2= children2[j];
                                 imagen= new Object[2];
                                 if (file2.getName().contains(idMunicipio)){
                                	 imagen[0]= ConstantesLocalGISPlantillas.PATH_ESCUDOS+File.separator+file2.getName();
                                 	imagen[1]= DocumentoEnDisco.getContenido(file2);
                                 	imagenes.add(imagen);
                                 }
                             }
                         }
                    }else{
                    	imagen= new Object[2];
                    	imagen[0]= file.getName();
                    	imagen[1]= DocumentoEnDisco.getContenido(file);
                    	imagenes.add(imagen);
                    }
                }
            }
        }
        
        /** El primer elemento del array correspode a las imagenes, el segundo a las plantillas .jrxml */
        aList.add(imagenes);
        
        /** Leemos las plantillas */
        /** filtramos por ficheros con extension .jrxml */
        FilenameFilter filter=null;
        if (filtro==null){
        	if (patron!=null){
        		filter = new MatchJRXMLFilterPatrones(patron,patrones);        		
        	}
        	else{
		        filter = new FilenameFilter() {
		            public boolean accept(File dir, String name) {
		                return ((name.endsWith("."+ConstantesLocalGISPlantillas.EXTENSION_JRXML)) && (!name.startsWith(ConstantesLocalGISPlantillas.PATRON_FICHA_MUNICIPAL)));
		            }
		        };
        	}
        }
        //filtramos por ficheros con extension .jmp
        else if(filtro.contains(ConstantesLocalGISPlantillas.EXTENSION_JMP)){
        	filter = new FilenameFilter() {
	            public boolean accept(File dir, String name) {
	                return name.endsWith("."+ConstantesLocalGISPlantillas.EXTENSION_JMP);
	            }
	        };
        }
        else{
        	filter = new MatchJRXMLFilter(filtro);
        }
        
        if (idEntidad==null)
        	dir = new File(ConstantesLocalGISPlantillas.PATH_PLANTILLAS+File.separator+pathPlantillas+subPath);
        else
        	dir = new File(Constantes.DIR_PLANTILLAS+File.separator+idEntidad+File.separator+pathPlantillas+subPath);
        
        if (dir.isDirectory()) {
            File[] children = dir.listFiles(filter);
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (int i = 0; i < children.length; i++) {
                    // Get filename of file or directory
                    File file= children[i];
                    plantilla= new Object[2];
                    plantilla[0]= file.getName();
                    plantilla[1]= DocumentoEnDisco.getContenido(file);
                    plantillas.add(plantilla);
                }
            }
        }

        /** Insertamos las plantillas en el segundo elemento del array */
        aList.add(plantillas);
        
       
        /** Insertamos los subReports */
     	aList.add(getSubReports(idEntidad, pathPlantillas));
        
        return aList;

    }
    
    private Collection getSubReports(String idEntidad, String pathPlantillas) throws Exception{

        ArrayList subreports= new ArrayList();
        Object[] subreport;

        /** filtramos por ficheros con extension .jrxml */
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("."+ConstantesLocalGISPlantillas.EXTENSION_JRXML);
            }
        };

        /** Leemos los subreports. TODOS los subreports se encuentran en el mismo directorio */
        File dir=null;
        if (idEntidad==null)
        	dir = new File(ConstantesLocalGISPlantillas.PATH_PLANTILLAS+File.separator+pathPlantillas +
        			File.separator + ConstantesLocalGISPlantillas.PATH_SUBREPORTS);
        else
        	dir = new File(Constantes.DIR_PLANTILLAS+File.separator+idEntidad+File.separator+pathPlantillas +
        			File.separator + ConstantesLocalGISPlantillas.PATH_SUBREPORTS);

        if (dir.isDirectory()) {
            File[] children = dir.listFiles(filter);
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (int i = 0; i < children.length; i++) {
                    // Get filename of file or directory
                    File file= children[i];
                    subreport= new Object[2];

                    subreport[0]= file.getName();
                    subreport[1]= DocumentoEnDisco.getContenido(file);
                    subreports.add(subreport);
                }
            }
        }

        return subreports;
    }    
 
    class MatchJRXMLFilter implements FilenameFilter 
    {
        private String filter;
        
        public MatchJRXMLFilter( String filter ) 
        {           
            this.filter = filter;
            
        }
        public boolean accept(File dir, String name) 
        {              	
        	 return (name.endsWith("."+ConstantesLocalGISPlantillas.EXTENSION_JRXML) && (name.contains(filter)));           
        }
    }
    
    class MatchJRXMLFilterPatrones implements FilenameFilter 
    {
        private String patron;
        private ArrayList patrones;
        
        public MatchJRXMLFilterPatrones(String patron,ArrayList patrones) 
        {           
            this.patron = patron;
            this.patrones = patrones;
            
        }
        public boolean accept(File dir, String name) 
        {              	
        	boolean encontrado=false;
        	Iterator it=patrones.iterator();
        	while (it.hasNext()){
        		LCGNodoEIEL nodoEIEL=(LCGNodoEIEL)it.next();
        		String clave=nodoEIEL.getClave();
        		String categoria=nodoEIEL.getCategoria();
        		if (name!=null && name.contains(clave) && (name.endsWith("."+ConstantesLocalGISPlantillas.EXTENSION_JRXML)))
        			return true;
        		if (name!=null && name.contains(categoria) && (name.endsWith("."+ConstantesLocalGISPlantillas.EXTENSION_JRXML)))
        			return true;
        	}
        	
        	 return (name.endsWith("."+ConstantesLocalGISPlantillas.EXTENSION_JRXML) && (name.contains(patron)));           
        }
    }    
    
}