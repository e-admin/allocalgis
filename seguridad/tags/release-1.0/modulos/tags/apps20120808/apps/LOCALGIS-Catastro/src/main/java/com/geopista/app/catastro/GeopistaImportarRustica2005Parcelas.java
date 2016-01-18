package com.geopista.app.catastro;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;

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

public class GeopistaImportarRustica2005Parcelas
{
    private static ApplicationContext app = AppContext.getApplicationContext();
    
    public Connection con = null;
    
    public GeopistaImportarRustica2005Parcelas()
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
        
        Connection conn = app.getConnection();
        conn.setAutoCommit(false);
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
        int ID_SujetoPasivo=0;
        String nif = "";
        boolean existe;
        
        String referenciaCatastral = "";
        String idMun = "";
        
        try
        {
            String[] columnNames2 = {"Tipo","Inicio","Fin"};
            Object[][] data2 =
            {
                    // Sujeto Pasivo 
                    {"1", "158","167"},	//nif
                    {"1", "168","228"},	//identificacion
                    {"0", "228","230"},	//codigo_delegacion_meh
                    {"0", "230","233"},	//codigo_municipio_dgc
                    {"0", "59","62"},   //codigo_municipio_ine
                    {"0", "238","243"},	//codigovia
                    {"1", "243","248"},	//tipovia
                    {"1", "248","273"},	//nombrevia
                    {"0", "273","277"},	//primer_numero
                    {"1", "277","278"},	//primera_letra
                    {"0", "278","282"},	//segundo_numero
                    {"1", "282","283"},	//segunda_letra
                    {"0", "283","288"},	//kilometro
                    {"1", "288","292"},	//bloque
                    {"1", "292","317"},	//direccion_no_estructurada
                    {"1", "317","319"},	//escalera
                    {"1", "319","322"},	//planta
                    {"1", "322","325"},	//puerta
                    {"0", "325","330"},	//codigo_postal
                    {"0", "330","335"},	//apartadocorreos
                    {"1", "35","37"}       //derechoprevalente
            };
            
            JTable sujetopasivo = new JTable(data2, columnNames2);            
            
            ResultSet r = null;
            
            PreparedStatement psInsert = con.prepareStatement("catpadron2005insertarSujeto");
            PreparedStatement psUpdate = con.prepareStatement("catpadron2005actualizarSujeto");
            
            int n = 0;
            String valor = "";           
            
            while(n<21)
            {
                
                
                n=n+1;
                int a = Integer.parseInt(sujetopasivo.getValueAt(n-1,1).toString());
                int b = Integer.parseInt(sujetopasivo.getValueAt(n-1,2).toString());
                valor = Linea.substring(a,b);
                switch (Integer.parseInt(sujetopasivo.getValueAt(n-1,0).toString()))
                {
                // Numérico
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
                        return false;
                    }
                    break;
                    
                    // Alfanuméricos   
                case 1:
                    if (a==158) //Se trata del NIF/DNI
                    {
                        nif=valor;
                        
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
            
            PreparedStatement psBuscarId = con.prepareStatement("idSujetoPasivo");
            psBuscarId.setString(1,nif);
            
            existe=false;
            try
            {
                if(psBuscarId.execute())
                {
                    r  = psBuscarId.getResultSet();
                    if( r.next())
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
            app.closeConnection(con, psBuscarId,null,r);
            
            if (existe)
            {   
                try
                {
                    psUpdate.setInt(22, ID_SujetoPasivo);
                    psUpdate.executeUpdate();
                    app.closeConnection(null, psUpdate,null,null);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return false;
                }
            }
            else
            {
                try
                {                    
                    psInsert.executeUpdate();
                    app.closeConnection(null, psInsert,null,null);
                    
                }
                catch(Exception e)
                {
                    e.printStackTrace();                   
                    return false;
                    
                }
            }            
            
            String[] columnNames = {"Tipo","Inicio","Fin"};
            Object[][] data =
            {
                    //Datos de la parcela
                    
                    {"1", "8","13"},		//idmunicipio (5 caracteres)
                    {"1", "8","22"},		//referencia_catastral
                    {"1", "22","26"},		//numerocargo
                    {"1", "26","27"},		//primercaractercontrol
                    {"1", "27","28"},		//segundocaractercontrol
                    
                    {"0", "62","65"},		//codigomunicipioagregacion
                    {"0", "65","67"},		//codigozonaconcentracion
                    {"0", "67","70"},		//codigopoligono
                    {"0", "70","75"},		//codigoparcela
                    {"1", "75","105"},		//nombreparaje
                    
                    {"0", "410","420"},	    //superficiesuelo (6 enteros y 4 decimales)
                    {"0", "420","432"},	    //valorcatastral
                    {"0", "444","456"},	    //valorcatastralbonificado
                    {"1", "456","457"},	    //clavebonificacion
                    
            };
            
            
            JTable parcela = new JTable(data, columnNames);
            
            r = null;
            
            psUpdate = null;
            psInsert = null;
            
            //si el sujeto pasivo no existía
            if (ID_SujetoPasivo==0)
            {
                psUpdate = con.prepareStatement("catfinrusactualizarparcelas2005nuevoSujeto");
                psInsert = con.prepareStatement("catfinrusinsertarparcelas2005nuevoSujeto");
            }
            //si el sujeto pasivo ya existía
            else
            {
                psInsert = con.prepareStatement("catpadroninsertarparcelasrusticas");
                psUpdate = con.prepareStatement("catfinrusactualizarparcelas");
            }
            
            
            n=0;
            valor="";
            
            while(n<14)
            {
                
                n=n+1;
                int a=Integer.parseInt(parcela.getValueAt(n-1,1).toString());
                int b=Integer.parseInt(parcela.getValueAt(n-1,2).toString());
                valor= Linea.substring(a,b);
                
                switch (Integer.parseInt(parcela.getValueAt(n-1,0).toString()))
                {
                case 0:
                    psInsert.setInt(n,Integer.parseInt(valor));
                    psUpdate.setInt(n,Integer.parseInt(valor));
                    break;
                case 1:
                    
                    if((a==8)&&(b==13))
                    {
                        idMun=valor;
                    }
                    if((a==8)&&(b==22))
                    {
                        referenciaCatastral=valor;
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
            
            psBuscarId = null;
            psBuscarId= con.prepareStatement("existePadron");
            psBuscarId.setString(1,"R");
            psBuscarId.setString(2,referenciaCatastral);
            psBuscarId.setString(3,idMun);
            
            existe=false;
            
            if(psBuscarId.execute())
            {
                r  = psBuscarId.getResultSet();
                while( r.next())
                {
                    if (r.getInt(1)!=0)
                    {
                        existe=true;
                    }
                }
            }
            
            app.closeConnection(con, psBuscarId,null,r);
            
            //La parcela rustica existe
            if (existe)
            {
                try
                {   
                    // si el sujeto pasivo existía
                    if (ID_SujetoPasivo!=0)
                    {
                        //catfinrusactualizarparcelas                        
                        psUpdate.setString(15,"U");
                        psUpdate.setInt(16,ID_SujetoPasivo);
                        psUpdate.setString(17,referenciaCatastral);
                    }
                    else
                    {
                        //catfinrusactualizarparcelas2005nuevoSujeto
                        psUpdate.setString(15,"U");
                        psUpdate.setString(16,referenciaCatastral);
                    }
                    
                    psUpdate.executeUpdate();
                    app.closeConnection(con, psUpdate, null, null);
                    return true;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return false;
                }
            }
            //La parcela rústica no existe
            else
            {   
                try
                {
                    Calendar cal=Calendar.getInstance();
                    
                    // si el sujeto pasivo existía
                    if (ID_SujetoPasivo!=0)
                    {
                        //catpadroninsertarparcelasrusticas 
                        psInsert.setInt(15,ID_SujetoPasivo);
                        psInsert.setString(16,"R");
                        psInsert.setDate(17, new java.sql.Date(cal.getTime().getTime()));
                        
                    }
                    else
                    {
                        //catfinrusinsertarparcelas2005nuevoSujeto 
                        psInsert.setString(15,"R");
                        psInsert.setDate(16, new java.sql.Date(cal.getTime().getTime()));
                        
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
    
}

