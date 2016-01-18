package com.geopista.app.catastro;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.geopista.sql.GEOPISTAConnection;

import java.lang.Integer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Calendar;

import javax.swing.JTable;

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla parcelas.
 *  Contiene métodos que Insertan/Eliminan datos de parcelas dependiendo de que la
 *  información en el fichero de texto sobre parcelas sea correcta o incorrecta.
 */

public class GeopistaImportarUrbana2005Parcelas
{
    private static ApplicationContext app = AppContext.getApplicationContext();
    
    public Connection con = null;
    
    public GeopistaImportarUrbana2005Parcelas()
    {
        try
        {
            con = getDBConnection();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Realiza la conexión con la base de datos
     * @return Devuelve la conexión establecida con la base de datos
     */
    public static Connection getDBConnection () throws SQLException
    {
        
        Connection conn=  app.getConnection();
       // conn.setAutoCommit(false);
        return conn;
    }
    
    /**
     * Inserta en la tabla parcelas los datos encontrados en el fichero de texto que el usuario ha seleccionado
     * para importar.
     * @param Linea - Línea del fichero que contiene una entrada sobre parcelas
     * @return El identificador de la Parcela que ha insertado en la base de datos. Devuelve 0 cuando se han producido errores y la parcela no se ha insertado
     */
    
    public boolean IncluirParcela (String Linea)
    {     
        try
        {                     
            String nif="";
            ResultSet r = null;
            
            String referenciaCatastral="";
            boolean existe;
            int idMun = 0;
            
            // SUJETO PASIVO 
            
            // Preparación de las Tablas                 
            String[] columnNames2 = {"Tipo","Inicio","Fin"};
            Object[][] data2 =
            {
                    {"1", "158","167"},     //nif + caracter de control DNI/NIF
                    {"1", "168","228"},     //identificaciontitular
                    {"0", "228","230"},     //codigo_delegacion_meh
                    {"0", "230","233"},     //codigo_municipio_dgc
                    {"0", "235","238"},     //codigo_municipio_ine
                    {"0", "238","243"},     //codigo_via
                    {"1", "243","248"},     //tipovia
                    {"1", "248","273"},     //nombrevia
                    {"0", "273","277"},     //primer_numero
                    {"1", "277","278"},     //primera_letra
                    {"0", "278","282"},     //segundo_numero
                    {"1", "282","283"},     //segunda_letra
                    {"0", "283","288"},     //kilometro
                    {"1", "288","292"},     //bloque
                    {"1", "292","317"},     //direccion_no_estructurada
                    {"1", "317","319"},     //escalera
                    {"1", "319","322"},     //planta
                    {"1", "322","325"},     //puerta
                    {"0", "325","330"},     //codigo_postal
                    {"0", "330","335"}      //apartado_correos                        
            };
            
            
            JTable tblSujetoPasivo = new JTable(data2, columnNames2);
                           
            PreparedStatement psInsert = con.prepareStatement("catpadron2005insertarSujeto");
            PreparedStatement psUpdate = con.prepareStatement("catpadron2005actualizarSujeto");
            
            int n=0;
            String valor="";
            while(n<20)
            {
                n=n+1;
                int a=Integer.parseInt(tblSujetoPasivo.getValueAt(n-1,1).toString());
                int b=Integer.parseInt(tblSujetoPasivo.getValueAt(n-1,2).toString());
                valor= Linea.substring(a,b);
                switch (Integer.parseInt(tblSujetoPasivo.getValueAt(n-1,0).toString()))
                {
                case 0:
                    try
                    {
                        if(a==283) //Se trata del kilometro
                        {
                            if (valor.length()==5)
                            {
                                String parte_1 = valor.substring(0,3);
                                String parte_2= valor.substring(3,5);
                                String cadena_num = parte_1 + "." + parte_2;
                                Float c = Float.valueOf(cadena_num);
                                psInsert.setFloat(n, c.floatValue());
                                psUpdate.setString(n, cadena_num);                                  
                            }
                        }
                        else
                        {
                            psInsert.setInt(n,Integer.parseInt(valor));
                            psUpdate.setInt(n,Integer.parseInt(valor));                               
                        }
                    }
                    catch(Exception excep)
                    {
                        excep.printStackTrace();
                    }
                    break;
                    
                case 1:
                    if(a==158) //Se trata del NIF
                    {
                        nif = valor; 
                        
                        if (nif.trim().equals(""))
                        {
                            int numSujetosSinDNI = new Integer(app.getBlackboard().get("SujetoSinDNI").toString()).intValue();
                            app.getBlackboard().put("SujetoSinDNI", ++numSujetosSinDNI);
                            return false;
                        }
                    }
                    psInsert.setString(n,valor);
                    psUpdate.setString(n,valor);
                    
                    break;
                    
                case 2:
                    if (valor.equals("S"))
                    {
                        psInsert.setBoolean(n,true);
                        psUpdate.setBoolean(n,true);
                        
                    }
                    else
                    {
                        psInsert.setBoolean(n,false);
                        psUpdate.setBoolean(n,false);
                        
                    }
                    break;
                    
                default:
                    break;
                }//switch
                
            }  //while            
            
            //Derecho prevalente (sólo en rústica)
            psInsert.setString(21,"");
            psUpdate.setString(21,"");
            
            PreparedStatement psBuscarId = con.prepareStatement("idSujetoPasivo");
            psBuscarId.setString(1,nif);
            
            int ID_SujetoPasivo = 0;
            
            existe=false;
            try
            {
                if(psBuscarId.executeQuery()!=null)
                {
                    r=null;
                    r  = psBuscarId.getResultSet();
                    if(r.next())
                    {                            
                        if (r.getInt(1)!=0)
                        {
                            ID_SujetoPasivo = r.getInt(1);
                            existe=true;
                        }
                    }
                }
            }
            catch (Exception excep)
            {
                excep.printStackTrace();
                return false;
            }                
            finally
            {
                app.closeConnection(con, psBuscarId,null,r);   
            }
            
            //El sujeto pasivo existe
            if (existe)
            {
                try
                {
                    psUpdate.setString(22, nif);
                    psUpdate.executeUpdate();
                    
                    app.closeConnection(con, psUpdate,null,null);
                    
                }
                catch(Exception es)
                {
                    es.printStackTrace();
                    return false;
                }                
            }
            else
            {
                try
                {
                    psInsert.executeUpdate();
                    app.closeConnection(con, psInsert,null,null);
                    
                }
                catch(Exception es)
                {
                    es.printStackTrace();
                    return false;
                }
            }  
            
            
            // PARCELAS            
            
            //Preparación de la tabla
            String[] columnNames = {"Tipo","Inicio","Fin"};
            Object[][] data =
            {
                    // Parcelas 
                    {"1", "8","22"},		//referencia_catastral
                    {"1", "22","26"},		//numero_cargo
                    {"1", "26","27"},		//primer_caracter_control
                    {"1", "27","28"},		//segundo_caracter_control
                    {"1", "28","36"},		//numero_fijo
                    {"1", "37","52"},		//identificador_ayto
                    {"0", "57","62"},		//id_municipio
                    {"1", "62","64"},		//distrito_municipal
                    {"1", "64","66"},		//codigo_entidad_menor
                    {"1", "71","76"},		//tipo_via
                    {"1", "76","101"},	    //nombre_via
                    {"0", "101","105"},	    //primer_numero
                    {"1", "105","106"},	    //primera_letra
                    {"0", "106","110"},	    //segundo_numero
                    {"1", "110","111"},	    //segunda_letra
                    {"0", "111","116"},	    //kilometro
                    {"1", "116","120"},	    //bloque
                    {"1", "120","145"},	    //direccion_no_estructurada
                    {"0", "145","150"},	    //codigo_postal
                    {"1", "150","152"},	    //escalera
                    {"1", "152","155"},	    //planta
                    {"1", "155","158"},	    //puerta
                    {"0", "410","414"},	    //anio_valor_catastral
                    {"0", "414","426"},	    //valor_catastral
                    {"0", "426","438"},	    //valor_catastral_suelo
                    {"0", "438","450"},	    //valor_catastral_construccion
                    {"0", "450","462"},	    //baseliquidable
                    {"1", "462","463"},	    //clavegrupobienes
                    {"0", "477","484"},	    //superficieconstruccionescargos
                    {"0", "484","491"},	    //superficiesuelocargos
                    {"0", "491","498"}	    //coeficientepropiedad
            };
            
            
            JTable parcela = new JTable(data, columnNames);           
            
            psUpdate = null;
            psInsert = null;
            
            //si el sujeto pasivo no existía
            if (ID_SujetoPasivo==0)
            {
                psUpdate = con.prepareStatement("catfinurbactualizarparcelas2005nuevoSujeto");
                psInsert = con.prepareStatement("catfinurinsertarparcelas2005nuevoSujeto");
            }
            //si el sujeto pasivo ya existía
            else
            {
                psUpdate = con.prepareStatement("catfinurbactualizarparcelas");
                psInsert = con.prepareStatement("catfinurinsertarparcelas2005");
            }
            
            
            n=0;
            valor="";            
            while(n<31)
            {
                n=n+1;
                int a=Integer.parseInt(parcela.getValueAt(n-1,1).toString());
                int b=Integer.parseInt(parcela.getValueAt(n-1,2).toString());
                valor= Linea.substring(a,b);
                switch (Integer.parseInt(parcela.getValueAt(n-1,0).toString()))
                {
                case 0:
                    try
                    {
                        if(a==57)
                        {
                            idMun=Integer.parseInt(valor);
                        }                        
                        if(a==491)
                        {
                            if (valor.length()==7)
                            {
                                String parte_1 = valor.substring(0,3);
                                String parte_2= valor.substring(3,7);
                                String cadena_num = parte_1 + "." + parte_2;
                                Float c = Float.valueOf(cadena_num);
                                psInsert.setFloat(n, c.floatValue());
                                psUpdate.setFloat(n, c.floatValue());                                
                            }                            
                        }
                        else if(a==111)
                        {
                            if (valor.length()==5)
                            {
                                String parte_1 = valor.substring(0,3);
                                String parte_2= valor.substring(3,5);
                                String cadena_num = parte_1 + "." + parte_2;
                                Float c = Float.valueOf(cadena_num);
                                psInsert.setFloat(n, c.floatValue());
                                psUpdate.setFloat(n, c.floatValue());                                
                            }
                        }
                        else
                        {
                            psInsert.setInt(n,Integer.parseInt(valor));
                            psUpdate.setInt(n,Integer.parseInt(valor));                            
                        }
                    }
                    catch(Exception excep)
                    {
                        excep.printStackTrace();
                    }
                    break;
                    
                case 1:
                    if(a==8)
                    {
                        referenciaCatastral = valor;
                    }                    
                    psInsert.setString(n,valor);
                    psUpdate.setString(n,valor);                    
                    break;
                    
                case 2:
                    if (valor.equals("S"))
                    {
                        psInsert.setBoolean(n,true);
                        psUpdate.setBoolean(n,true);                        
                    }
                    else
                    {
                        psInsert.setBoolean(n,false);
                        psUpdate.setBoolean(n,false);                        
                    }
                    break;
                default:
                    break;
                }//switch
                
            }  //while
            
            //Antes compruebo si ya existe esa parcela:            
            psBuscarId = null;
            psBuscarId= con.prepareStatement("existePadron");
            psBuscarId.setString(1,"U");
            psBuscarId.setString(2,referenciaCatastral);
            psBuscarId.setInt(3,idMun);
            
            existe=false;
            
            try
            {
                if(psBuscarId.executeQuery()!=null)
                {
                    r= null;
                    r  = psBuscarId.getResultSet();
                    while( r.next())
                    {                        
                        if (r.getInt(1)!=0)
                        {
                            existe=true;
                        }
                    }
                }
            }
            catch (Exception excep)
            {
                excep.printStackTrace();
                return false;
            }
            finally
            {
                app.closeConnection(con, psBuscarId,null,r);
            }           
            
            //La parcela existe
            if (existe)
            {
                //se actualizan los datos de la parcela con los de la linea del fichero 
                try
                {                    
                    // si el sujeto pasivo existía
                    if (ID_SujetoPasivo!=0)
                    {
                        //catfinurbactualizarparcelas                        
                        psUpdate.setString(32,"U");
                        psUpdate.setInt(33,ID_SujetoPasivo);
                        psUpdate.setString(34,referenciaCatastral);
                    }
                    else
                    {
                        //catfinurbactualizarparcelas2005nuevoSujeto
                        psUpdate.setString(32,"U");
                        psUpdate.setString(33,referenciaCatastral);
                    }
                    
                    psUpdate.executeUpdate();
                    app.closeConnection(con, psUpdate,null,null);  
                    return true;
                    
                }
                catch(Exception es)
                {
                    es.printStackTrace();
                    return false;
                }
                
            }
            else
            {
                try
                {
                    
                    Calendar cal=Calendar.getInstance();
                    
                    // si el sujeto pasivo existía
                    if (ID_SujetoPasivo!=0)
                    {
                        //catfinurinsertarparcelas2005 
                        psInsert.setInt(32,ID_SujetoPasivo);
                        psInsert.setString(33,"U");
                        psInsert.setDate(34, new java.sql.Date(cal.getTime().getTime()));
                        
                    }
                    else
                    {
                        //catfinurinsertarparcelas2005nuevoSujeto 
                        psInsert.setString(32,"U");
                        psInsert.setDate(33, new java.sql.Date(cal.getTime().getTime()));
                        
                    }    
                    
                    psInsert.executeUpdate();                     
                    app.closeConnection(con, psInsert,null,null);
                     
                    return true;
                    
                }
                catch(Exception es)
                {
                    es.printStackTrace();
                    return false;
                }
            }
            
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }//catch
        
        
    }
    /**
     * Elimina una parcela de la base de datos
     * @param idparcela - Identificador de la parcela a eliminar
     */
    
    public void eliminarParcela (int idparcela)
    {
        if(idparcela!=0)
        {
            try{
                PreparedStatement ps = con.prepareStatement("catfinurbeliminarparcelas");
                ps.setInt(1,idparcela);
                ps.executeUpdate();
                app.closeConnection(con, ps,null,null);
            }
            catch(Exception es)
            {
                es.printStackTrace();
            }
        }
    }

    void setAutoCommit(boolean valor)
    {
      try{((GEOPISTAConnection) con).setAutoCommit1(valor);}catch(Exception e){};
    }

    void commit()
    {
        try
        {
            con.commit();
        }catch(Exception e)
        {
            //logger.error(e);
        }
    }
}