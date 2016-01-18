/**
 * CargarFicheroPadron.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Clase encargada de importar un único fichero de padrón, tras su validación previa
 */

package com.geopista.app.catastro.intercambio.importacion;


import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.model.beans.PadronFile;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;


public class CargarFicheroPadron {
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    PrintWriter log ;
  
    private long numeroRegistros=0L;
    private int totalRows=0;
    private int insertedRows=0;//filas insertadas
    private int notInsertedRows=0;//filas no insertadas
    
    
    private PadronFile pf;
    
    
    public CargarFicheroPadron(String nombreFichero){
    	 try {
    		 FileWriter fstream = new FileWriter("log-padron-"+nombreFichero);
    		 log= new PrintWriter(fstream);
    	 } catch (IOException e) {
    		 log.write(">>Carga padron: Se ha producido la siguiente excepción: "+e.getMessage()+"\r\n");
    		 e.printStackTrace(log);//volcamos la pila al fichero de log
    		 log.flush();
         	 e.printStackTrace();//mostramos la pila en la consola
 		}
    }//fin constructor
    
    
    
    /**Valida el fichero de entrada
     * 
     */
    private boolean validate(InputStream is, boolean multilinea, long numRegistros) throws IOException{
    	boolean valido=true;
    	
    	if(multilinea){
    		if (is.available()%ImportarPadronMunicipal.LONGITUD_REGISTRO!=numRegistros*2-2
                    && is.available()%ImportarPadronMunicipal.LONGITUD_REGISTRO!=numRegistros*2)
            {
    			System.out.println("Es multilinea y falla en la validacion");
    			log.write("Es multilinea y falla en la validacion\r\n");
    			return false;
            }
    	}
    	
    	 byte[] bytes_linea = new byte[ImportarPadronMunicipal.LONGITUD_REGISTRO];                                                
         int cuenta_lineas = 0;
         int i=0;
         int leidos_linea=0;
         
         while (is.available()>0){   
        	 bytes_linea[i]=(byte) is.read();
          	 i++;	
          	leidos_linea++;
         	
         	
         	if(leidos_linea==ImportarPadronMunicipal.LONGITUD_REGISTRO){//hemos terminado de leer una línea
         		//1.tratamos la línea
        	 
             cuenta_lineas ++;
             
           //---//validación de la primera línea//---//
             if (cuenta_lineas==1){
                 if( bytes_linea[0]!=ImportarPadronMunicipal.inicioCabecera[0] || bytes_linea[1]!=ImportarPadronMunicipal.inicioCabecera[1]){
                	 System.out.println("Falla en la cabecera");
          			 log.write("Falla en la cabecera\r\n");
                	 return false; //El registro de cabecera no comienza x 01                    
                 }
                     
             }//fin if
             
             
           //---//validación de la última línea//---//
             else if (cuenta_lineas == numRegistros){
                 if ( bytes_linea[0]!=ImportarPadronMunicipal.inicioCola[0] || bytes_linea[1]!=ImportarPadronMunicipal.inicioCola[1]){
                	 System.out.println("Falla en la cola");
          			 log.write("Falla en la cola\r\n");
                    return false;  //No ha encontrado el registro de Cola   
                 }
             }//fin else if
             
             
             //---validación de las líneas bien inmueble y titular//---//
             else if ((bytes_linea[0]!=ImportarPadronMunicipal.inicioBienInmueble[0] || bytes_linea[1]!=ImportarPadronMunicipal.inicioBienInmueble[1])
                     && (bytes_linea[0]!=ImportarPadronMunicipal.inicioTitular[0] || bytes_linea[1]!=ImportarPadronMunicipal.inicioTitular[1])){                                                  
            	 System.out.println("Falla en un registro");
      			 log.write("Falla en un registro\r\n");
            	 return false;
             }//fin else if
             
           
           //hemos terminado con la línea 
     		//2. reseteamos contadores
     		bytes_linea=new byte[ImportarPadronMunicipal.LONGITUD_REGISTRO];
     		i=0;   
     		leidos_linea=0;
     		
     		  
            if(multilinea)
                is.skip(2);
            
             
         	}//fin del tratamiento de la línea
         }//fin while
         
    	
    	return valido;
    }
    
    
	
	/**
     * Realiza el proceso de importación del padrón de habitantes
     */    
    public void importar(Map dataMap){
    	System.out.println(">>Carga padron: Comenzamos la carga del mismo" );
		log.write(">>Carga padron: Comenzamos la carga del mismo\r\n");
       
         try{
        	 boolean valido=false;
                   				                   				
             pf = new PadronFile();
             String linea;
             InputStream is = (InputStream) dataMap.get("is"); //flujo para la lectura del fichero de padrón
             InputStream is2 = (InputStream) dataMap.get("is2");
             int length=is.available();           
             
             
             //número de registros del fichero de entrada
             long numRegistros = length/ImportarPadronMunicipal.LONGITUD_REGISTRO;
             this.numeroRegistros=numRegistros;
             System.out.println(">>Carga padron: Hay "+numRegistros+" regitros" );
     		 log.write(">>Carga padron: Hay "+numRegistros+" regitros\r\n");
     		 
     		 
     		 
     		 //una linea
             if(length%ImportarPadronMunicipal.LONGITUD_REGISTRO == 0){
            	 System.out.println(">>Carga padron: No es multilinea");
                 blackboard.put(ImportarUtils.FILE_TXT_MULTILINE, false); 
             }
             
             //varias lineas
             else{
            	 System.out.println(">>Carga padron: Es multilinea");
                 blackboard.put(ImportarUtils.FILE_TXT_MULTILINE, true);
             }
             
             
             //validamos el fichero de entrada
             valido= validate(is,(new Boolean(blackboard.get(ImportarUtils.FILE_TXT_MULTILINE).toString())).booleanValue(),numRegistros);
             is.close();
     		 
             if(valido){   
            	 System.out.println("El fichero es VALIDO");
            	 log.write("El fichero es VALIDO\r\n");
             byte bytes_linea[]=new byte[ImportarPadronMunicipal.LONGITUD_REGISTRO];
             int i=0;
             int leidos_linea=0;
             
             while(is2.available()>0){
            	bytes_linea[i]=(byte) is2.read();
             	i++;	
             	leidos_linea++;
             	
             	
             	if(leidos_linea==ImportarPadronMunicipal.LONGITUD_REGISTRO){//hemos terminado de leer una línea
             		//1.tratamos la línea
            		
             		linea = new String(bytes_linea);
                    log.write(">>Carga padron: Registros insertados: "+(++totalRows)+"de: "+numRegistros+"\r\n");
                    
                    //Comiezo de la carga
                    if (totalRows==1 &&
                    		bytes_linea[0]==ImportarPadronMunicipal.inicioCabecera[0] && 
                    		bytes_linea[1]==ImportarPadronMunicipal.inicioCabecera[1]){
                         pf.setHeaderInformation(linea);
                         insertedRows++;
                         System.out.println("Comienzo de la carga");
                         log.write("Comienzo de la carga\r\n\r\n");
                    }//fin if
                         
                    
                    //final de la carga                        
                    else if (totalRows == numRegistros && 
                    		bytes_linea[0]==ImportarPadronMunicipal.inicioCola[0] && 
                    		bytes_linea[1]==ImportarPadronMunicipal.inicioCola[1]){
                             pf.setTailInformation(linea);
                             insertedRows++;
                             System.out.println("Fin de la carga");
                             log.write("Fin de la carga\r\n\r\n");
                    }
                    
                    
                    //insertamos un bienInmueble
                    else if (bytes_linea[0]==ImportarPadronMunicipal.inicioBienInmueble[0] && 
                    		bytes_linea[1]==ImportarPadronMunicipal.inicioBienInmueble[1]){      
                    	try{    
                    	  log.write("Cargamos un bien inmueble\r\n\r\n");
                    		
                           ImportacionOperations oper = new ImportacionOperations();
                           oper.insertarRegistroBI(linea);
                           insertedRows++;
                           
                           }catch(Exception e){
                           System.out.println("Linea: "+linea);
                           System.out.println(">>Carga padron: Se ha producido la siguiente excepción: "+e.getMessage()+"\r\n");
                           e.printStackTrace();//volcamos la pila en la consola
                           log.write(">>Carga padron: Se ha producido la siguiente excepción: "+e.getMessage()+"\r\n");
                           e.printStackTrace(log);//volcamos la pila en el log
                           log.flush();
                                      			
                           notInsertedRows++;
                           }
                           }//fin else
                    
                    //insertamos un titular
                    else if (bytes_linea[0]==ImportarPadronMunicipal.inicioTitular[0] && 
                    		bytes_linea[1]==ImportarPadronMunicipal.inicioTitular[1]){  
                               try{                                            
                            	   log.write("Cargamos un titular\r\n\r\n");
                            	   
                            	   ImportacionOperations oper = new ImportacionOperations();
                                   oper.insertarRegistroTitular(linea);
                                   insertedRows++;
                                   
                               }catch(Exception e){
                                   System.out.println("Linea: "+linea);
                                   System.out.println(">>Carga padron: Se ha producido la siguiente excepción: "+e.getMessage()+"\r\n");
                                   e.printStackTrace();//volcamos la pila en la consola
                                   log.write(">>Carga padron: Se ha producido la siguiente excepción: "+e.getMessage()+"\r\n");
                                   e.printStackTrace(log);//volcamos la pila en el log
                                   log.flush(); 
                                              			
                                   notInsertedRows++;
                                   }
                               }//fin else
                    
                    else{
                    	log.write("----------------------------------No insertamos nada\r\n");
                        notInsertedRows++;
                    }
                    
                                		
            		//hemos terminado con la línea 
            		//2. reseteamos contadores
            		bytes_linea=new byte[ImportarPadronMunicipal.LONGITUD_REGISTRO];
            		i=0;   
            		leidos_linea=0;
            		
            		 //si tiene retornos de carro, se salta la lectura de estos 2 bytes
                    if((new Boolean(blackboard.get(ImportarUtils.FILE_TXT_MULTILINE).toString())).booleanValue())
                                                 is2.skip(2);
             		
             	}
             }//fin while de lectura
             

         is2.close();   
         printFinalMessage();
             }
             else{
            	 System.out.println("El fichero NO es VALIDO");
            	 log.write("El fichero NO es VÁLIDO\r\n");
            	 log.flush();
            	 log.close();
             }
                         
        }catch(Exception e){
        	 System.out.println(">>Carga padron: Se ha producido la siguiente excepción: "+e.getMessage()+"\r\n");
             e.printStackTrace();//volcamos la pila en la consola
             log.write(">>Carga padron: Se ha producido la siguiente excepción: "+e.getMessage()+"\r\n");
             e.printStackTrace(log);//volcamos la pila en el log
             log.flush();
        	 log.close();
        }
        }//fin del método
	
	
	
	
    
    private void printFinalMessage(){ 
        
    	//fecha
    	String cadFecha ="";
        if (pf.getFecha()!=null){
            if(pf.getFecha().length()==8)
                cadFecha = pf.getFecha().substring(6,8) + "/" + pf.getFecha().substring(4,6) 
                + "/" +pf.getFecha().substring(0,4);
            else
                cadFecha = pf.getFecha();
        }
        
      
        
        if (pf.getHora()!=null){
            if (pf.getHora().length()==6)
                cadFecha = cadFecha + " --> " +  pf.getHora().substring(0,2)+":"+
                pf.getHora().substring(2,4)+":"+ pf.getHora().substring(4,6);
            else
                cadFecha = cadFecha + " --> "+ pf.getHora();
        }
        
        NumberFormat formatter = new DecimalFormat("#.##");
        
        
        log.write("Fecha: "+cadFecha+"\r\n");
        log.write("Código entidad: "+pf.getCodigo()+"\r\n");
        log.write("Descripción: "+pf.getDescripcion()+"\r\n");
        log.write("Año: "+pf.getAnio()+"\r\n");
        
        System.out.println("Fecha: "+cadFecha);
        System.out.println("Código entidad: "+pf.getCodigo());
        System.out.println("Descripción: "+pf.getDescripcion());
        System.out.println("Año: "+pf.getAnio());
        
        

        if (pf.getValorTotal()!=0 && pf.getValorSuelo()!=0 && pf.getValorConstruccion()!=0 && pf.getBaseLiquidable()!=0){
        	
        	 log.write("Valor catastral total: "+formatter.format((new Double(pf.getValorTotal()).doubleValue() /100))+"\r\n");
             log.write("Valor catastral suelo: "+formatter.format((new Double(pf.getValorSuelo()).doubleValue()/100))+"\r\n");
             log.write("Valor catastral construcción: "+formatter.format((new Double(pf.getValorConstruccion()).doubleValue()/100))+"\r\n");
             log.write("Base liquidable total: "+formatter.format((new Double(pf.getBaseLiquidable()).doubleValue()/100))+"\r\n");
             
             System.out.println("Valor catastral total: "+formatter.format((new Double(pf.getValorTotal()).doubleValue() /100)));
             System.out.println("Valor catastral suelo: "+formatter.format((new Double(pf.getValorSuelo()).doubleValue()/100)));
             System.out.println("Valor catastral construcción: "+formatter.format((new Double(pf.getValorConstruccion()).doubleValue()/100)));
             System.out.println("Base liquidable total: "+formatter.format((new Double(pf.getBaseLiquidable()).doubleValue()/100)));
        }//fin if
  
    
       
        log.write("FIN DE LA CARGA: RESUMEN: \r\n"+
        		"Número total de columnas calculadas: "+numeroRegistros+"\r\n"+
        		"Columnas tratadas: "+totalRows+"\r\n"+
        		"Columnas insertadas: "+insertedRows+"\r\n"+
        		"Columnas NO insertadas: "+notInsertedRows+"\r\n");
        log.flush();
   		log.close();
        
        System.out.println("FIN DE LA CARGA: RESUMEN: \r\n"+
        		"Número total de columnas calculadas: "+numeroRegistros+"\r\n"+
        		"Columnas tratadas: "+totalRows+"\r\n"+
        		"Columnas insertadas: "+insertedRows+"\r\n"+
        		"Columnas NO insertadas: "+notInsertedRows+"\r\n");
        
    }//fin método
  
    
}//fin clase
