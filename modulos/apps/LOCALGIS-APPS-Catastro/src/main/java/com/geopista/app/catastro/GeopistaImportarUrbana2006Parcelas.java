/**
 * GeopistaImportarUrbana2006Parcelas.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JTable;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;


/** Esta clase, contiene los métodos necesarios para trabajar con la tabla parcelas.
 *  Contiene métodos que Insertan/Eliminan datos de parcelas dependiendo de que la
 *  información en el fichero de texto sobre parcelas sea correcta o incorrecta.
 */

public class GeopistaImportarUrbana2006Parcelas
{
    private static ApplicationContext app = AppContext.getApplicationContext();
    
    public Connection con = null;
    
    public GeopistaImportarUrbana2006Parcelas()
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
        int ID_SujetoPasivo = 0;
        int ID_Parcela = 0;
              
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        
        try
        {
            String[] columnNames2 = {"Tipo","Inicio","Fin"};
            Object[][] data2 =
            {
                    // Sujeto Pasivo
                    
                    {"1", "426","428"},	//derechoprevalente
                    {"0", "428","432"},	//numerotitulares
                    {"0", "432","433"},	//tipotitulares
                    {"1", "433","453"},	//complementotitularidad
                    {"1", "453","462"},	//nif
                    {"1", "462","522"},	//identificacion: primer apellido, segundo apellido y nombre, o razón social
                    {"0", "522","530"},	//claveinterna  
                    {"0", "530","532"},	//codigo_delegacion_meh
                    {"0", "557","560"},	//codigo_municipio_dgc             
                    {"0", "633","638"},	//codigovia
                    {"1", "638","643"},	//tipovia
                    {"1", "643","668"},	//nombrevia
                    {"0", "668","672"},	//primer_numero
                    {"1", "672","673"},	//primera_letra
                    {"0", "673","677"},	//segundo_numero
                    {"1", "677","678"},	//segunda_letra
                    {"3", "678","683"},	//kilometro
                    {"1", "683","687"},	//bloque
                    {"1", "695","720"},	//direccion_no_estructurada
                    {"1", "687","689"},	//escalera
                    {"1", "689","692"},	//planta
                    {"1", "692","695"},	//puerta
                    {"1", "720","725"},	//codigo_postal
                    {"1", "725","730"},	//apartadocorreos
                    {"1", "730","739"},	//nifconyugue
                    {"1", "739","799"},	//identificacionconyugue
                    
            };
            
            JTable sujetopasivo = new JTable(data2, columnNames2);
            
            //Comprobar si existe el sujeto pasivo
            boolean existeSujetoPasivo = false;
            PreparedStatement ps = con.prepareStatement("idSujetoPasivo");
            ResultSet r = null;
            
            String nif = Linea.substring(453,462);
            
            if (nif.trim().equals(""))
            {
                int numSujetosSinDNI = new Integer(app.getBlackboard().get("SujetoSinDNI").toString()).intValue();
                app.getBlackboard().put("SujetoSinDNI", ++numSujetosSinDNI);
                return false;
            }
            
            ps.setString(1,nif);
            
            if(ps.execute())
            {
                r  = ps.getResultSet();
                if(r.next())
                {
                    ID_SujetoPasivo = r.getInt(1);  
                    existeSujetoPasivo = true;
                }
            }
            
            app.closeConnection(con, ps,null,r);
            
            
            if (ID_SujetoPasivo==0){
                
                //Si el sujeto pasivo no existe, se busca el máximo identificador
                r=null;
                ps = con.prepareStatement("catpadronmaxsujetopasivo");
                
                if(ps.execute())
                {
                    r  = ps.getResultSet();
                    while(r.next())
                    {
                        ID_SujetoPasivo= r.getInt(1);
                    }
                }
                app.closeConnection(con, ps,null,r);               
            }          
            
            
            if (existeSujetoPasivo){
                ps = con.prepareStatement("catpadronactualizarsujetopasivo");
            }
            else{
                ps = con.prepareStatement("catpadroninsertarsujetopasivo");
            }
            
            
            int n=0;
            String valor="";
            while(n<data2.length)
            {
                n=n+1;
                int a=Integer.parseInt(sujetopasivo.getValueAt(n-1,1).toString());
                int b=Integer.parseInt(sujetopasivo.getValueAt(n-1,2).toString());
                valor= Linea.substring(a,b);
                switch (Integer.parseInt(sujetopasivo.getValueAt(n-1,0).toString()))
                {
                case 0:
                    ps.setInt(n,Integer.parseInt(valor.trim()));
                    
                    break;
                    
                case 1:
                    
                    ps.setString(n,valor);
                    
                    break;
                    
                case 2:
                    if (valor.equals("S"))
                    {
                        ps.setBoolean(n,true);                        
                    }
                    else
                    {
                        ps.setBoolean(n,false);                        
                    }
                    break;
                    
                case 3:
                    
                    if (valor.length()==5)
                    {
                        String parte_1 = valor.substring(0,3);
                        String parte_2= valor.substring(3,5);
                        String cadena_num = parte_1 + "." + parte_2;
                        Float c = Float.valueOf(cadena_num);
                        
                        
                        if (existeSujetoPasivo){
                            ps.setFloat(n, c.floatValue());
                        }
                        else{
                            ps.setString(n, cadena_num);
                        }
                        
                    }
                    else
                    {
                        ps.setFloat(n, 0);
                    }
                    
                    break;
                    
                default:
                    break;
                }//switch
                
            }  //while
            
            ps.setInt(data2.length+1, ID_SujetoPasivo);
            
            ps.executeUpdate();
            //app.closeConnection(con, ps,null,null);
            
            
            String[] columnNames = {"Tipo","Inicio","Fin"};
            Object[][] data =
            {
                    // Parcelas 
                    {"1", "9","23"},		//referencia_catastral x
                    {"1", "23","27"},		//numerocargo x
                    {"1", "27","28"},		//primercaractercontrol x
                    {"1", "28","29"},		//segundocaractercontrol x
                    {"1", "29","37"},		//numerofijo x
                    {"1", "37","52"},		//identificadorayuntamiento x
                    {"1", "247","249"},	//id_distrito x
                    {"1", "160","165"},	//tipovia x
                    {"1", "165","190"},	//nombre_via x
                    {"0", "190","194"},	//primer_numero n
                    {"1", "194","195"},	//primera_letra x 
                    {"0", "195","199"},	//segundo_numero n
                    {"1", "199","200"},	//segunda_letra x
                    {"3", "200","205"},	//kilometro f
                    {"1", "205","209"},	//bloque x
                    {"1", "217","242"},	//direccion_no_estructurada x
                    {"0", "242","247"},	//codigo_postal n
                    {"1", "209","211"},	//escalera x
                    {"1", "211","214"},	//planta x 
                    {"1", "214","217"},	//puerta x  
                    {"0", "292","296"},	//aniovalorcatastral n
                    {"10", "296","308"},	//valorcatastral n
                    {"10", "308","320"},	//valorcatastralsuelo n
                    {"10", "320","332"},	//valorcatastralconstruccion n
                    {"10", "332","344"},	//baseliquidable n
                    {"1", "344","345"},	//clavegrupobienes x
                    {"1", "249","252"},	//codigomunicipioagregacion x
                    {"1", "252","254"},	//codigozonaconcentracion x
                    {"1", "254","257"},	//codigopoligono x
                    {"1", "257","262"},	//codigoparcela x
                    {"1", "262","292"},	//nombreparaje x 
                    {"10", "345","357"},	//valorcatastralbonificado n 
                    {"1", "357","358"},	//clavebonificacion x
                    {"8", "358","368"},	//superficieconstruccionescargos n
                    {"8", "368","378"},	//superficiesuelo n	          
                    {"3", "378","387"},	//coeficientepropiedad (3 enteros y 6 decimales) f	          
                    {"10", "387","399"},	//importevalorbase n
                    {"1", "399","400"},	//codigovalorbase x
                    {"0", "400","404"},	//ejercicioefectosibi n
                    {"10", "404","416"},	//valorcatastralefectosibi n
                    {"0", "416","420"},	//ejerciciorevisiontotal n
                    {"0", "420","424"},	//ejerciciorevisionparcial n
                    {"0", "424","426"},	//periodovigencia n
                    {"1", "2", "4"},		//codigodelegacionmeh x
                    {"1", "4", "7"},		//codigomunicipiomeh x
                    {"1", "7", "9"},		//codigonaturalezabien x
                    {"0", "2", "7"},		//id_municipio n5
                    {"1", "125", "155"}	//nombreentidadmenor x
                    
                    
            };
            
            JTable parcela = new JTable(data, columnNames);
            
            //Comprobar si existe la parcela
            boolean existeParcela = false;
            ps = con.prepareStatement("idParcela");
            r = null;
            
            ps.setString(1,Linea.substring(9,23));
            
            if(ps.execute())
            {
                r  = ps.getResultSet();
                while(r.next())
                {
                    ID_Parcela = r.getInt(1);  
                    existeParcela = true;
                }
            }
            
            app.closeConnection(con, ps,null,r);
            
            if (ID_Parcela==0){
                
                //Si la parcela no existe, se busca el siguiente identificador
                r=null;
                ps = con.prepareStatement("catfinurbmaxparcelas");
                
                if(ps.execute())
                {
                    r  = ps.getResultSet();
                    while(r.next())
                    {
                        ID_Parcela= Integer.parseInt(r.getString(1).toString());
                    }
                }
                app.closeConnection(con, ps,null,r);               
            }             
            
            if (existeParcela)
            {
                ps = con.prepareStatement("catpadronactualizarparcelas");                
            }
            else
            {
                ps = con.prepareStatement("catpadroninsertarparcelas");
            }
            
            
            
            //String query="insert into parcelas (referencia_catastral, numerocargo, primercaractercontrol, segundocaractercontrol, numerofijo,identificadorayuntamiento,id_distrito,tipovia,nombre_via,primer_numero,primera_letra,segundo_numero,segunda_letra,kilometro,bloque,direccion_no_estructurada,codigo_postal,escalera,planta,puerta,aniovalorcatastral,valorcatastral,valorcatastralsuelo,valorcatastralconstruccion,baseliquidable,clavegrupobienes,codigomunicipioagregacion,codigozonaconcentracion,codigopoligono,codigoparcela,nombreparaje,valorcatastralbonificado,clavebonificacion,superficieconstruccionescargos,superficiesuelo,coficientepropiedad,importevalorbase,ejercicioefectosibi,valorcatastralefectosibi,ejerciciovisiontotal,ejerciciovisionparcial,periodovigencia,codigodelegacionmeh,codigomunicipiomeh,codigonaturalezabien,id_municipio,nombreentidadmenor,id_sujetopasivo,tipo, fecha_alta, id) values (";
            
            n=0;
            valor="";
            while(n<data.length)
            {
                n=n+1;
                int a=Integer.parseInt(parcela.getValueAt(n-1,1).toString());
                int b=Integer.parseInt(parcela.getValueAt(n-1,2).toString());
                valor= Linea.substring(a,b);
                int tipo= Integer.parseInt(parcela.getValueAt(n-1,0).toString());
                
                switch (tipo)
                {
                case 0:		                
                    ps.setInt(n,Integer.parseInt(valor.trim()));
                    //query=query+valor+",";
                    break;
                    
                case 1:
                    
                    ps.setString(n,valor);
                    //query=query+"'"+valor+"',";
                    break;
                    
                case 3:
                case 4:
                case 8:
                case 10:
                    
                    if (valor.length()>=5)
                    {
                        String parte_1 = valor.substring(0,tipo);
                        String parte_2= valor.substring(tipo);
                        String cadena_num = parte_1 + "." + parte_2;
                        //Float c = Float.valueOf(cadena_num);
                        //ps.setFloat(n, c.floatValue()); 
                        ps.setString(n, cadena_num);
                        
                        //query=query+cadena_num+",";
                    }
                    else
                    {
                        ps.setFloat(n, 0);
                    }
                    
                    break;
                    
                    
                default: 
                    break;
                
                
                }//switch
                
            }  //while
            
            
            ps.setInt(data.length+1, ID_SujetoPasivo);
            //query=query+ID_SujetoPasivo+" ,";
            
            String tipo = Linea.substring(7, 9);
            //Urbana
            if (tipo.equals("UR")){
                ps.setString(data.length+2,"U");
            }
            //Rustica
            else if(tipo.equals("RU"))
            {
                ps.setString(data.length+2,"R");
            }
            //Bien especial
            else if(tipo.equals("BI"))
            {
                ps.setString(data.length+2,"B");
            }
            else
            {
                ps.setString(data.length+2,"");
            }
            
            //query=query+"'U'"+" ,";
            
            Calendar cal=Calendar.getInstance();
            ps.setDate(data.length+3, new java.sql.Date(cal.getTime().getTime()));
            
            //query=query+"'"+new java.sql.Date(cal.getTime().getTime())+"' ,";
            
            ps.setInt(data.length+4, ID_Parcela);
            
            
            //query=query+ID_Parcela +")";
            //System.out.println(query);
            
            ps.executeUpdate();
            //app.closeConnection(con, ps,null,null);
            
            return true;
            
        }
        catch (SQLException ex)
        {
            //ex.printStackTrace();
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            return false;         
        }
        
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }//catch     
        
    }
    
    
    public boolean IncluirTitular (String Linea)
    {
        int ID_Titular=0;
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        
        try
        {
            String[] columnNames2 = {"Tipo","Inicio","Fin"};
            Object[][] data2 =
            {
                    {"1", "9","23"},		//referenciacatastral
                    {"1", "59","68"},		//nif
                    {"1", "52","54"},		//codigoderecho
                    {"1", "68","128"},		//identificaciontitular
                    {"0", "239","244"},	//codigovia
                    {"0", "136","138"},	//codigo_delegacion_meh
                    {"1", "163","166"},	//codigo_municipio_dgc
                    {"1", "166","169"},	//codigo_municipio_ine
                    {"1", "244","249"},	//tipovia
                    {"1", "249","274"},	//nombrevia
                    {"0", "274","278"},	//primer_numero
                    {"1", "278","279"},	//primera_letra
                    {"0", "279","283"},	//segundo_numero
                    {"1", "283","284"},	//segunda_letra             
                    {"3", "284","289"},	//kilometro
                    {"1", "289","293"},	//bloque
                    {"1", "301","326"},	//direccion_no_estructurada
                    {"1", "293","295"},	//escalera
                    {"1", "295","298"},	//planta
                    {"1", "298","301"},	//puerta
                    {"1", "326","331"},	//codigo_postal
                    {"1", "331","336"},	//apartado_correos
                    {"0", "54","59"},		//porcentajesujetoderecho
                    
            };
            
            JTable titular = new JTable(data2, columnNames2);
            
            //Comprobar si existe el titular
            boolean existeTitular = false;
            PreparedStatement ps = con.prepareStatement("idTitular");
            ResultSet r = null;
            String nif = Linea.substring(59,68);
            
            if (nif.trim().equals(""))
            {
                int numSujetosSinDNI = new Integer(app.getBlackboard().get("SujetoSinDNI").toString()).intValue();
                app.getBlackboard().put("SujetoSinDNI", ++numSujetosSinDNI);
                return false;
            }
                
                
            ps.setString(1,nif); //nif
            ps.setString(2,Linea.substring(9,23));    //referencia catastral
            
            if(ps.execute())
            {
                r  = ps.getResultSet();
                while(r.next())
                {
                    ID_Titular = r.getInt(1);  
                    existeTitular = true;
                }
            }
            
            app.closeConnection(con, ps,null,r);
            
            if (ID_Titular==0){
                
                //Si el titular no existe, se busca el máximo identificador
                r=null;
                ps = con.prepareStatement("catfinurbmaxtitular");
                
                if(ps.execute())
                {
                    r  = ps.getResultSet();
                    while(r.next())
                    {
                        ID_Titular= Integer.parseInt(r.getString(1).toString());
                    }
                }
                app.closeConnection(con, ps,null,r);               
            }             
            
            if (existeTitular){
                ps = con.prepareStatement("catpadronactualizartitular2006");
                
            }
            else{
                ps = con.prepareStatement("catpadroninsertartitular2006");
            }
            
            int  n=0;
            String  valor="";
            //String valu = "";
            while(n < data2.length)
            {
                n=n+1;
                int a=Integer.parseInt(titular.getValueAt(n-1,1).toString());
                int b=Integer.parseInt(titular.getValueAt(n-1,2).toString());
                valor= Linea.substring(a,b);
                switch (Integer.parseInt(titular.getValueAt(n-1,0).toString()))
                {
                case 0:
                    ps.setInt(n,Integer.parseInt(valor.trim()));
                    //valu = valu +valor + ", ";
                    break;
                    
                case 1:
                    
                    ps.setString(n,valor);
                    //valu = valu +"'"+valor + "', ";
                    break;
                    
                case 2:
                    if (valor.equals("S"))
                    {
                        ps.setBoolean(n,true);		                   
                    }
                    else
                    {
                        ps.setBoolean(n,false);		                   
                    }
                    break;
                    
                case 3:
                    
                    if (valor.length()==5)
                    {
                        String parte_1 = valor.substring(0,3);
                        String parte_2= valor.substring(3);
                        String cadena_num = parte_1 + "." + parte_2;
                        //Float c = Float.valueOf(cadena_num);
                        //ps.setFloat(n, c.floatValue()); 
                        ps.setString(n, cadena_num);
                        //valu = valu +cadena_num + ", ";
                    }
                    else
                    {
                        ps.setFloat(n, 0);
                    }
                    
                    break;
                    
                default:
                    break;
                }//switch
                
            }  //while
            
            
            
            ps.setInt(data2.length+1, ID_Titular);
            //valu=valu + ID_Titular + ")";
            //System.out.println(valu);
            
            ps.executeUpdate();
            app.closeConnection(con, ps,null,null);
            return true;
            
            
        }
        catch (SQLException ex)
        {
            //ex.printStackTrace();
            ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            return false;         
        }
        
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }//catch  
        
    }
    
}