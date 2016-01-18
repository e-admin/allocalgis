/**
 * PatrimonioPostgre.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.patrimonio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;



public class PatrimonioPostgre
{
    
    public Connection conn = null;
    
    public final int URBANO  = 1;
    public final int RUSTICO = 2;
    
    
    public PatrimonioPostgre()
    {
        try
        {
            conn = getDBConnection();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static Connection getDBConnection () throws SQLException
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Connection con=  app.getConnection();
        con.setAutoCommit(false);
        return con;
    }  
    
    
    
    public ArrayList DatosInmuebles ()
    {
        ArrayList Datos = new ArrayList(); 
        
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        String RefParcela= String.valueOf(Identificadores.get("RefParcela"));
        
        
        try
        {
            
            //String query = "select id, descripcion from bienes where id in (select id from inmuebles where refpar=?)";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patdescinmuebles");
            s.setString(1, RefParcela);
            r = s.executeQuery();  
            while (r.next())
            {
                //Datos.add(r.getString("referencia_catastral"));
                
                Datos.add(r.getString("descripcion")); 
                Datos.add(r.getString("id")); 
                
            }     
            
            
            s.close();
            r.close(); 
            s = null;
            r = null;
            conn.close();
            
            return Datos;
            
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            return null;
        } 
        
        
    }
    
    
    
    
    public ArrayList Propiedades ()
    {
        ArrayList Datos = new ArrayList(); 
        
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        String ID_Inmueble= String.valueOf(Identificadores.get("IdInmueble"));
        
        
        try
        {
            
            //String query = "select nombre, descripcion, num_inventario, tipo_bien from bienes where id=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patpropiedadesbien");
            s.setString(1, ID_Inmueble);
            r = s.executeQuery();  
            while (r.next())
            {
                Datos.add(r.getString("nombre")); 
                Datos.add(r.getString("descripcion")); 
                Datos.add(r.getString("num_inventario")); 
                
                //TODO Dominios!!! De momento suponemos 1=urbano 2=rustico
                Datos.add(String.valueOf(r.getInt("tipo_bien")).toString()); 
                
            }     
            
            
            s.close();
            r.close(); 
            s = null;
            r = null;
            conn.close();
            return Datos;
            
        }catch (Exception ex)
        {
            
            ex.printStackTrace();
            
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            return null;
            
        }//catch 
        
        
        
    }
    
    
    
    public String AltaInmueble (String Nombre, String Descripcion, int Tipo)
    {
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        
        
        
        
        boolean esUrbano;  
        
        if (Tipo==URBANO) esUrbano=true;
        else esUrbano=false;
        
        try
        {
            
            if(Identificadores.get("RefParcela")==null || Identificadores.get("RefPlano")==null)
            {
                throw new StringIndexOutOfBoundsException();
            }
            
            
            // query = select max(id) from bienes
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patidbien");
            r = s.executeQuery();  
            int ID_Bien=0;
            while (r.next()) 
            {
                if (r.getString(1)!=null){
                    ID_Bien= Integer.parseInt(r.getString(1).toString());
                }
            }
            
            s = null;
            r = null;
            ID_Bien=ID_Bien+1;
            
            //Calculamos primeramente el número de inventario, para posteriormente actualizarlo
            //query= select max(numeroorden) from bienes where num_inventario like ?
            s = conn.prepareStatement("patnuminventario");
            if (esUrbano)
            {
                s.setString(1,"1.1.%");
            }
            else   
            {   
                s.setString(1,"1.2.%");
            }
            r = s.executeQuery();  
            
            
            int Num=0;
            while (r.next())
            {
                if(r.getString(1)!=null){
                    Num= Integer.parseInt(r.getString(1).toString());        
                }
            }
            Num = Num + 1;
            
            NumberFormat formatter = new DecimalFormat("##0000");
            String Inven = formatter.format(Num);
            
            if (esUrbano)
            {
                Inven="1.1." + Inven + ".01";
            }
            else 
            { 
                Inven="1.2." + Inven + ".01";
            }
            
            /* query: insert into bienes 
             *          (id, nombre, descripcion, tipo_bien, num_inventario, numeroorden, fechaalta, id_municipio) 
             *          values (?, ?, ?, ?, ?, ?, ?, ?)
             * */
            
            s = conn.prepareStatement("patnuevobieninmueble");
            s.setInt(1,ID_Bien);
            s.setString(2, Nombre);
            s.setString(3, Descripcion);
            s.setInt(4, Tipo);
            
            
            s.setString(5, Inven);
            s.setInt(6, Num);
            
            Calendar cal = new GregorianCalendar();
            
            // Get the components of the date
            int year = cal.get(Calendar.YEAR);            
            int month = cal.get(Calendar.MONTH);           // 0=Jan, 1=Feb, ...
            int day = cal.get(Calendar.DAY_OF_MONTH);    
            formatter = new DecimalFormat("##00");
            
            String Fecha = String.valueOf(year).toString()+ String.valueOf(formatter.format(month+1)).toString() +String.valueOf(formatter.format(day)).toString();
            
            s.setString(7, Fecha);
            
            //Identificador del municipio
            s.setInt(8,  Integer.parseInt(AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID)));
            s.executeUpdate();  
            conn.commit();
            //System.out.println("Insertado nuevo inmueble en la tabla bienes con numero de inventario "+Inven );
            
            /* query: insert into 
             * 			inmuebles (id, tipo, refpar, refpla) 
             * 			values (?,?,?)
             * */
            
            s = conn.prepareStatement("patnuevoinmuebleinmueble");
            s.setInt(1,ID_Bien);
            
            
            
            //System.out.println(Identificadores.get("IdParcela").toString());
            
            //s.setString(2, String.valueOf(Identificadores.get("IdParcela")).toString().substring(0,7));
            //s.setString(3, String.valueOf(Identificadores.get("IdParcela")).toString().substring(7,14));
            s.setInt(2, Tipo);
            
            s.setString(3,String.valueOf(Identificadores.get("RefParcela")).toString());
            s.setString(4,String.valueOf(Identificadores.get("RefPlano")).toString());
            
            s.executeUpdate();  
            conn.commit();
            //System.out.println("Insertado nuevo inmueble en la tabla inmuebles");
            
            
            
            if (esUrbano)
            {
                /* query = Insert into inmuebles_urbanos (id, manzana, parcela) 
                 * 						values (?, ?, ?) */
                s = conn.prepareStatement("patnuevoinmuebleurbano");
                
                s.setString(2,Identificadores.get("RefParcela").toString().substring(0,5));
                s.setString(3,Identificadores.get("RefParcela").toString().substring(5,7));
                
                
                
            }
            else
            {
                /* query = insert into inmuebles_rusticos 
                 * 		(id, poligono, parcela) 
                 * 			values (?,?,?);*/
                s = conn.prepareStatement("patnuevoinmueblerustico");
                
                String par= Identificadores.get("RefParcela").toString();
                String pla = Identificadores.get("RefPlano").toString();
                s.setString(2,par.substring(6,7)+pla.substring(0,2));
                s.setString(3,pla.substring(2,7));
                
                
            }
            
            s.setInt(1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            //System.out.println("Insertado nuevo inmueble en la tabla inmuebles_urbanos o inmuebles_rusticos");
            
            Identificadores.put("IdInmueble", ID_Bien);
            //System.out.println ("el IdInmueble vale " + ID_Bien);
            
            //Se introduce automáticamente por defecto como dirección del inmueble la de la parcela donde se encuentra
            
            //patdatosdireccionine: select vias.tipoviaine,vias.nombreviaine, parcelas.primer_numero, parcelas.segundo_numero, parcelas.primera_letra, parcelas.segunda_letra from vias left join parcelas
            //on parcelas.id_via = vias.codigocatastro
            //where parcelas.id=?
            
            s=null;
            r=null;
            
            s = conn.prepareStatement("patdatosdireccionine");
            s.setInt(1, Integer.parseInt(Identificadores.get("IdParcela").toString()));
            r = s.executeQuery();
            StringBuffer direccion = new StringBuffer();
            
            if (r.next())
            {
                String tipoVia = r.getString("tipoviaine");
                String nombreVia = r.getString("nombreviaine");
                String primerNum = r.getString("primer_numero");
                String primeraLetra = r.getString("primera_letra");
                String segundoNum = r.getString("segundo_numero");
                String segundaLetra = r.getString("segunda_letra");
                
                if (tipoVia!=null)
                    direccion.append(tipoVia).append(" ");
                if (nombreVia !=null)
                    direccion.append(nombreVia).append(" ");
                if (primerNum!=null || primeraLetra!=null || segundoNum!=null || segundaLetra!=null )
                {
                    direccion.append(" Nº ").append(primerNum!=null?primerNum:"").append(primeraLetra!=null?primeraLetra:"")
                    .append(" ").append(segundoNum!=null?segundoNum:"").append(segundaLetra!=null?segundaLetra:"");
                    
                }
                
            }
            
            //patdirecciondefecto: update inmuebles set direccion=? where id=?
            r=null;
            s=null;
            
            s = conn.prepareStatement("patdirecciondefecto");
            s.setString(1, direccion.toString());
            s.setInt(2,ID_Bien);
            
            s.executeUpdate();  
            conn.commit();
            
            s.close();
            conn.close();
            
            return Inven;
            
        }
        catch (SQLException ex)
        {
            //ex.printStackTrace();         
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));     
            return "Error";
        }//catch
        
        catch (StringIndexOutOfBoundsException ex)
        {
            //ex.printStackTrace();     
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("PatrimonioPostgre.RefCatastralIncorrecto"), app.getI18nString("PatrimonioPostgre.RefCatastralIncorrecto"), StringUtil.stackTrace(ex));
            return "Error";
            
        }//catch 
        
        
    } 
    
    
    
    public String ModificacionInmueble (String Nombre, String Descripcion)
    {
        
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        String ID_Inmueble= String.valueOf(Identificadores.get("IdInmueble"));
        
        try
        {
            
            //query: update bienes set nombre=?, descripcion=? where id=?
            PreparedStatement s = null;
            ResultSet r = null;   
            
            s = conn.prepareStatement("patactualizarpropiedadesbien");
            s.setString(1, Nombre);
            s.setString(2, Descripcion);    
            s.setInt(3, Integer.parseInt(ID_Inmueble));    
            s.executeUpdate();  
            conn.commit();
            //System.out.println("Actualizados nombre y descripcion del inmueble");
            
            
            s.close();
            conn.close();
            
            
            return "Correcto";
            
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }  
    
    
    
    
    public ArrayList DatosGenerales (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        int Tipo=0; 
        try
        {
            
//          String query = "select * from  bienes where ID_Bien=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patbienes");
            s.setInt(1, ID_Bien);
            r = s.executeQuery();  
            while (r.next())
            {
                Datos.add(r.getString("destino")); 
                Datos.add(r.getString("fecha_adquisicion")); 
                Datos.add(r.getString("forma_adquisicion"));   
                Datos.add(r.getString("propiedad"));      
                Datos.add(r.getString("datoscesion")); 
                Datos.add(r.getString("datoscesionario")); 
            }     
            
            // query = "select * from inmuebles where id_inmueble=?";
            s = null;
            r = null;
            
            s = conn.prepareStatement("patinmuebles");
            s.setInt(1,ID_Bien);
            r = s.executeQuery();
            while (r.next())
            {
                Datos.add(r.getString("direccion")); 
                Datos.add(r.getString("lindero_norte")); 
                Datos.add(r.getString("lindero_sur"));   
                Datos.add(r.getString("lindero_este"));      
                Datos.add(r.getString("lindero_oeste")); 
                Datos.add(r.getString("refpar"));           
                Datos.add(r.getString("refpla")); 
                //Datos.add(r.getString("tipo"));
                Tipo= Integer.parseInt(r.getString("tipo").toString());
            }
            
            if (Tipo == URBANO){
//              query = "select * from inmuebles_urbanos where id=" + ID_Bien;
                s = null;
                r = null;
                
                s = conn.prepareStatement("paturbanos");
                Datos.add("FALSE"); //No rustico
                Datos.add("TRUE");  //Sí urbano
                Datos.add(String.valueOf(Tipo));
                s.setInt(1,ID_Bien);
                r = s.executeQuery();
                while (r.next())
                {
                    Datos.add(r.getString("manzana"));
                    Datos.add(r.getString("parcela"));              
                }
                
                
            }else{
//              query = "select * from rusticos where id_inmueble=" + ID_Bien;
                s = null;
                r = null;
                s = conn.prepareStatement("patrusticos");
                s.setInt(1,ID_Bien);
                r = s.executeQuery();
                Datos.add("TRUE"); //Sí rustico
                Datos.add("FALSE");  //No urbano
                Datos.add(String.valueOf(Tipo));
                while (r.next())
                {
                    Datos.add(r.getString("poligono"));
                    Datos.add(r.getString("parcela"));
                    Datos.add(r.getString("subparcela"));
                    Datos.add(r.getString("paraje"));
                    Datos.add(r.getString("aprovechemiento"));
                }
                
            } 
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
        
        
    }
    
    
    public String ActualizarGeneral (int ID_Bien, ArrayList Valores, ArrayList Tipos, boolean Urbana)
    {
        try
        {
            /* 
             * query: update bienes 
             * 			set destino=?,fecha_adquisicion=?, forma_adquisicion=?, propiedad=?, datoscesion=?, datoscesionario=? 
             * 			where id=?
             */
            PreparedStatement s = null;
            s = conn.prepareStatement("patactualizarbienes");
            Iterator Datos = Valores.iterator();
            Iterator TipoDatos = Tipos.iterator();
            int n =0;
            Datos.next();
            TipoDatos.next();
            while (n<6)
            {
                n=n+1;
                
                String Dat=Datos.next().toString();
                
                
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }            
                    
                    break;
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1, ID_Bien);
            s.executeUpdate();  
            conn.commit();
            
            /*	 query = update inmuebles 
             * 				set direccion=?,lindero_norte=?, lindero_sur=?, lindero_este=?, lindero_oeste=?,refpar=?, refpla=? 
             * 				where ID=?
             
             *
             */
            s = null;
            s = conn.prepareStatement("patactualizarinmuebles");
            
            n =0;
            while (n<7)
            {
                n=n+1;
                String Dat=Datos.next().toString();
                
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    s.setInt(n,Integer.parseInt(Dat));
                    break;
                case 1:
                    s.setString(n,Dat);
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            s=null;
            if (Urbana==true)
            {
                s = conn.prepareStatement("patactualizarurbanos");
                /*    query = update inmuebles_urbanos 
                 * 				set manzana=?, parcela=? 
                 * 				where ID=?
                 */
            }else{
                
                s = conn.prepareStatement("patactualizarrusticos");
                /* query = update inmuebles_rusticos 
                 * 			set poligono=?, parcela=?,subparcela=?, paraje=?, aprovechamiento=? 
                 * 			where ID=?*/
            }
            
            
            
            n =0;
            while (Datos.hasNext())
            {
                n=n+1;
                
                String Dat=Datos.next().toString();
                
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }   
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public int AltaGeneral (ArrayList Valores, ArrayList Tipos, boolean Urbana)
    {
        try
        {
            
//          String query ="select max(id_bien) from bienes";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patidbien");
            r = s.executeQuery();  
            int ID_Bien=0;
            while (r.next())
            {
                ID_Bien= Integer.parseInt(r.getString(1).toString());             
            }
            
            s = null;
            r = null;
            ID_Bien=ID_Bien+1;
            //Calculamos primeramente el número de inventario, para posteriormente actualizarlo
            //String query= "select max(OrdenNumero) from bienes where num_inventario like '1.1.%'""
            /* 	 query = "insert into bienes (id_bien, destino," + 
             " fecha_adquisicion, forma_adquisicion, propiedad," +
             " datoscesion, datoscesionario)" +
             " values (?,?,?,?,?,?,?)";*/
            s = null;
            
            s = conn.prepareStatement("patnuevobien");
            Iterator Datos = Valores.iterator();
            Iterator TipoDatos = Tipos.iterator();
            s.setInt(1,ID_Bien);
            s.setString(2,"28");
            //s.setInt(3,13);
            Datos.next();
            TipoDatos.next();
            int n =2;
            while (n<8)
            {
                n=n+1;
                String Dat=Datos.next().toString();
                
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }   
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            s.setString(9,"1.1.0029.01"); 
            s.executeUpdate();  
            conn.commit();
            
            /*	 query = "Insert into inmuebles (id_inmueble, direccion," + 
             " lindero_norte, lindero_sur, lindero_este, lindero_oeste," +
             " refpar, refpla, tipo)" +
             " values (?,?,?,?,?,?,?,?,?)";*/
            s = null;
            s = conn.prepareStatement("patnuevoinmueble");
            s.setInt(1,ID_Bien);
            n =1;
            while (n<8)
            {
                n=n+1;
                String Dat=Datos.next().toString();
                
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }   
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            if (Urbana==true)
            {
                s.setString(9,"Urbano");
            }else{ 
                s.setString(9,"Rústico");
            }
            s.executeUpdate();  
            conn.commit();
            s = null;
            if (Urbana==true)
            {
                s = conn.prepareStatement("patnuevourbano");
                /*      query = "Insert into urbanos (id_inmueble, manzana, parcela)" +
                 " values (?,?,?)";*/
            }else{
                /*      query = "Insert into rusticos (id_inmueble, poligono, parcela," +
                 "subparcela, paraje, aprovechemiento)" +
                 " values (?,?,?,?,?,?)";*/
                s = conn.prepareStatement("patnuevorustico");
            }
            
            s.setInt(1,ID_Bien);
            n =1;
            
            while (Datos.hasNext())
            {
                n=n+1;
                String Dat= Datos.next().toString();
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }   
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            s.executeUpdate();  
            conn.commit();
            
            s.close();
            conn.close();
            return ID_Bien;
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return 0;
        }//catch  
    }
    
    public String BajaGeneral (int ID)
    {
        try
        {
            
//          String query = "Update bienes set fecha_baja=? where ID_Bien=" + ID;
            PreparedStatement s = null;
            s = conn.prepareStatement("patbajabien");
            Calendar cal=Calendar.getInstance();
            s.setDate(1,new java.sql.Date(cal.getTime().getTime()));
            s.setInt(2,ID);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    
    public String BajaInmueble (int ID_inmueble)
    {
        try
        {
            //Dar de baja en la tabla de bienes - bienes -
            //String query = "Update bienes set fechabaja=? where id=" + ID;
            PreparedStatement s = null;
            s = conn.prepareStatement("patbajabien");
            Calendar cal=Calendar.getInstance();
            s.setDate(1,new java.sql.Date(cal.getTime().getTime()));
            s.setInt(2,ID_inmueble);
            s.executeUpdate();  
            
            s.close();
            conn.close();
            s= null;
            
            //  Dar de baja en la tabla de inmuebles - inmuebles -
            //String query = "Update inmuebles set fechabaja=? where id=" + ID;
            s = conn.prepareStatement("patbajainmueble");
            s.setDate(1,new java.sql.Date(cal.getTime().getTime()));
            s.setInt(2,ID_inmueble);
            s.executeUpdate();  
            
            conn.commit();
            s.close();
            conn.close();
            s=null;
            
            return "Correcto";
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    
    
    
    
    public ArrayList DatosRegistro (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        String Tipo="";
        try
        {
            //String query = "select * from  inmuebles where id=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patinmuebles");
            s.setInt(1, ID_Bien);
            //System.out.println("ID_Inmueble: " + ID_Bien);
            r = s.executeQuery();  
            while (r.next())
            {
                Datos.add(r.getString("registro_tomo")); 
                Datos.add(r.getString("registro_folio")); 
                Datos.add(r.getString("registro_libro"));   
                Datos.add(r.getString("registro_finca"));      
                Datos.add(r.getString("registro_inscripcion")); 
                Datos.add(r.getString("registro_protocolo")); 
                Datos.add(r.getString("registro_notario")); 
                Datos.add(r.getString("registro")); 
            }     
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
        
        
    }
    
    public String ActualizarRegistro (int ID_Bien, ArrayList Valores, ArrayList Tipos)
    {
        try
        {
            /*    query: update inmuebles 
             * 				set registro_tomo=?, registro_folio=?, registro_libro=?, registro_finca=?, registro_inscripcion=?, registro_protocolo=?, registro_notario=?, registro=? 
             * 				where ID=?*/
            
            PreparedStatement s = null;
            s = conn.prepareStatement("patactualizarregistro");
            Iterator Datos = Valores.iterator();
            Iterator TipoDatos = Tipos.iterator();
            int n =0;
            
            while (Datos.hasNext())
            {
                n=n+1;
                String Dat= Datos.next().toString();
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }   
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    
    public ArrayList DatosConstruccion (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        String Tipo="";
        try
        {
            
//          String query = "select * from  inmuebles where ID_inmueble=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patinmuebles");
            s.setInt(1, ID_Bien);
            r = s.executeQuery();  
            while (r.next())
            {
                Datos.add(r.getString("calificacion")); 
                Datos.add(r.getString("edificabilidad")); 
                Datos.add(r.getString("tipoconstruccion"));   
                Datos.add(r.getString("fechaobra"));      
                Datos.add(r.getString("estadoconservacion")); 
                Datos.add(r.getString("material")); 
                Datos.add(r.getString("cubierta")); 
                Datos.add(r.getString("carpinteria")); 
            }     
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
        
        
    }
    
    public String ActualizarConstruccion (int ID_Bien, ArrayList Valores, ArrayList Tipos)
    {
        try
        {
            /*     query: update inmuebles 
             * 				set calificacion=?, edificabilidad=?, tipoconstruccion=?, fechaobra=?, estadoconservacion=?, material=?, cubierta=?, carpinteria=? 
             * 				where id=?*/
            PreparedStatement s = null;
            s = conn.prepareStatement("patactualizarconstruccion");
            Iterator Datos = Valores.iterator();
            Iterator TipoDatos = Tipos.iterator();
            int n =0;
            while (Datos.hasNext())
            {
                n=n+1;
                String Dat= Datos.next().toString();
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }   
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public ArrayList DatosValoracion (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        String Tipo="";
        try
        {
            
            // String query = "select * from  inmuebles where ID_inmueble=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patinmuebles");
            s.setInt(1, ID_Bien);
            r = s.executeQuery();  
            while (r.next())
            {
                Datos.add(r.getString("superficie_registral_suelo")); 
                Datos.add(r.getString("superficie_catastral_suelo")); 
                Datos.add(r.getString("superficie_real_suelo"));   
                Datos.add(r.getString("valor_adquisicion_suelo"));      
                Datos.add(r.getString("valor_catastral_suelo")); 
                Datos.add(r.getString("valor_actual_suelo")); 
                Datos.add(r.getString("superficie_registral_construccion")); 
                Datos.add(r.getString("superficie_catastral_construccion")); 
                Datos.add(r.getString("superficie_real_construccion"));   
                Datos.add(r.getString("valor_adquisicion_construccion"));      
                Datos.add(r.getString("valor_catastral_construccion")); 
                Datos.add(r.getString("valor_actual_construccion")); 
                Datos.add(r.getString("valor_adquisicion_inmueble")); 
                Datos.add(r.getString("valor_actual_inmueble")); 
                Datos.add(r.getString("cubierta")); 
                Datos.add(r.getString("carpinteria")); 
            }     
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
        
        
    }
    
    public String ActualizarValoracion (int ID_Bien, ArrayList Valores, ArrayList Tipos)
    {
        try
        {
            
            /*
             String query = "Update inmuebles set superficie_registral_suelo=?, superficie_catastral_suelo=?, "+
             "superficie_real_suelo=?, valor_adquisicion_suelo=?, valor_catastral_suelo=?, "+
             " valor_actual_suelo=?, superficie_registral_construccion=?, superficie_catastral_construccion=?," +
             "superficie_real_construccion=?, valor_adquisicion_construccion=?,valor_catastral_construccion=?," +
             "valor_actual_construccion=?,valor_adquisicion_inmueble=?,valor_actual_inmueble=? " +
             " where ID_inmueble=" + ID_Bien;*/
            PreparedStatement s = null;
            s = conn.prepareStatement("patactualizarvaloracion");
            Iterator Datos = Valores.iterator();
            Iterator TipoDatos = Tipos.iterator();
            int n =0;
            
            while (Datos.hasNext())
            {
                n=n+1;
                String Dat= Datos.next().toString();
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }   
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return "Correcto";
            
        }
        catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public ArrayList DatosDerecho (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        
        try
        {
            
            //   String query = "select * from  inmuebles where ID_inmueble=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patinmuebles");
            s.setInt(1, ID_Bien);
            r = s.executeQuery();  
            while (r.next())
            {
                Datos.add(r.getString("derechosrealesfavor")); 
                Datos.add(r.getString("valor_derechos_favor")); 
                Datos.add(r.getString("derechosrealescontra"));   
                Datos.add(r.getString("valor_derechos_contra"));      
                Datos.add(r.getString("derechospersonales")); 
            }     
            
//          query = "select * from bienes where ID_Bien=?";
            s = null;
            s = conn.prepareStatement("patbienes");
            s.setInt(1, ID_Bien);
            r = s.executeQuery();  
            
            while (r.next())
            {
                Datos.add(r.getString("frutos")); 
                Datos.add(r.getString("importe_frutos")); 
            }
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
        
    }
    
    public String ActualizarDerechos (int ID_Bien, ArrayList Valores, ArrayList Tipos)
    {
        try
        {
            
            
            /*   String query = "Update inmuebles set derechosrealesfavor=?, valor_derechos_favor=?, "+
             "derechosrealescontra=?, valor_derechos_contra=?, derechospersonales=? "+
             " where ID_inmueble=" + ID_Bien;*/
            PreparedStatement s = null;
            s = conn.prepareStatement("patactualizarderechos");
            Iterator Datos = Valores.iterator();
            Iterator TipoDatos = Tipos.iterator();
            int n =0;
            
            while (n<5)
            {
                n=n+1;
                
                String Dat= Datos.next().toString();
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }   
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            
            /* query = "Update bienes set frutos=?, importe_frutos=? "+
             " where ID_bien=" + ID_Bien;*/
            s = null;
            s = conn.prepareStatement("patactualizarfrutos");
            n =0;
            
            while (Datos.hasNext())
            {
                n=n+1;
                String Dat= Datos.next().toString();
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    if (!Dat.equals("")){
                        s.setInt(n,Integer.parseInt(Dat));
                    }else
                    {
                        s.setNull(n, 0);
                    }   
                case 1:
                    if (!Dat.equals("")){
                        s.setString(n,Dat);
                    }else
                    {
                        s.setNull(n, 1);
                    }   
                    
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return "Correcto";
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
        
    }
    
    public ArrayList DatosSeguro (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        ArrayList Relacion = new ArrayList(); 
//      
        try
        {
            //  String query = "select * from  bienesseguros where ID_Bien=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patbienseguro");
            s.setInt(1, ID_Bien);
            r = s.executeQuery(); 
            while (r.next())
            {
                Relacion.add(r.getString("id_seguro"));
            }     
            
//          query = "select * from seguros where id_seguro=?";
            s = null;
            s = conn.prepareStatement("patseguros");
            Iterator iDatos = Relacion.iterator();
            while (iDatos.hasNext()){
                s.setInt(1,Integer.parseInt(iDatos.next().toString()));
                r = s.executeQuery();  
                while (r.next())
                {
                    Datos.add(r.getString("id")); 
                    Datos.add(r.getString("compannia")); 
                    Datos.add(r.getString("descripcion")); 
                    Datos.add(r.getString("poliza")); 
                }
            }
            
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
    }
    
    public String ActualizarSeguro (int ID_Seguro, ArrayList Valores, ArrayList Tipos)
    {
        try
        {
            /*  	 String query = "Update seguros set compannia=?, descripcion=?," + 
             " prima=?, poliza=?, fecha_inicio=?," +
             " fecha_vencimiento=?" +
             " where ID_Seguro=" + ID_Seguro;*/
            PreparedStatement s = null;
            s = conn.prepareStatement("patactualizarseguro");
            Iterator Datos = Valores.iterator();
            Iterator TipoDatos = Tipos.iterator();
            int n =0;
            while (Datos.hasNext())
            {
                n=n+1;
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    s.setInt(n,Integer.parseInt(Datos.next().toString()));
                    break;
                case 1:
                    s.setString(n,Datos.next().toString());
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1, ID_Seguro);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public int AltaSeguro (int ID_Bien,ArrayList Valores, ArrayList Tipos)
    {
        try
        {
            
            //   String query ="select max(id_seguro) from seguros";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patidseguro");
            r = s.executeQuery();  
            int ID_Seguro=0;
            while (r.next())
            {
                ID_Seguro= Integer.parseInt(r.getString(1).toString());             
            }
            
            s = null;
            r = null;
            ID_Seguro=ID_Seguro+1;
            /* 	 query = "insert into seguros (id_seguro, compannia," + 
             " descripcion, prima, poliza," +
             " fecha_inicio, fecha_vencimiento)" +
             " values (?,?,?,?,?,?,?)";*/
            s = null;
            s = conn.prepareStatement("patnuevoseguro");
            Iterator Datos = Valores.iterator();
            Iterator TipoDatos = Tipos.iterator();
            s.setInt(1,ID_Seguro);
            int n =1;
            while (Datos.hasNext())
            {
                n=n+1;
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    s.setInt(n,Integer.parseInt(Datos.next().toString()));
                    break;
                case 1:
                    s.setString(n,Datos.next().toString());
                    break;
                default:
                    break;
                }    
            }
            s.executeUpdate();  
            conn.commit();
            
            /*	 query = "Insert into bienesseguros (id_bien, id_seguro)" +
             " values (?,?)";*/
            s = null;
            s = conn.prepareStatement("patnuevobienseguro");
            s.setInt(1,ID_Bien);
            s.setInt(2,ID_Seguro);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return ID_Seguro;
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return 0;
        }//catch  
    }
    
    
    public ArrayList DatosObservacion (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        ArrayList Relacion = new ArrayList(); 
//      
        try
        {
            
//          String query = "select * from  bienesobservaciones where ID_Bien=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patbienobservacion");
            s.setInt(1, ID_Bien);
            r = s.executeQuery(); 
            while (r.next())
            {
                Relacion.add(r.getString("id_observacion"));
            }     
            //System.out.println("Cuenta " + Relacion.size());
//          query = "select * from observaciones where id_observacion=?";
            s = null;
            s = conn.prepareStatement("patobservaciones");
            Iterator iDatos = Relacion.iterator();
            while (iDatos.hasNext()){
                s.setInt(1,Integer.parseInt(iDatos.next().toString()));
                r = s.executeQuery();  
                while (r.next())
                {
                    Datos.add(r.getString("id")); 
                    Datos.add(r.getString("observacion"));
                    Datos.add(r.getString("fecha")); 
                }
            }
            
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
        
        
    }
    
    public String ActualizarObservacion (int ID_Observacion, String Observacion, String Fecha)
    {
        try
        {
            /*  	 String query = "Update observaciones set observacion=?, fecha=?" + 
             " where ID_Observacion=" + ID_Observacion;*/
            if (ID_Observacion!=0){
                PreparedStatement s = null;
                s = conn.prepareStatement("patactualizarobservacion");
                s.setString(1,Observacion);
                s.setString(2,Fecha);
                s.setInt(3,ID_Observacion);
                s.executeUpdate();  
                conn.commit();
                s.close();
                conn.close();
                return "Correcto";
            }
            else{
                return "";
            }
            
            
        }catch (Exception ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public int altaObservacion (int ID_Bien, String Observacion, String Fecha)
    {
        try
        {
            
//          String query ="select max(id_observacion) from observaciones";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patidobservacion");
            r = s.executeQuery();  
            int ID_Observacion=0;
            while (r.next())
            {
                ID_Observacion= Integer.parseInt(r.getString(1).toString());             
            }
            
            s = null;
            r = null;
            ID_Observacion=ID_Observacion+1;
            /* 	 query = "insert into observaciones (id_observacion,observacion,fecha)" +
             " values (?,?,?)";*/
            s = null;
            s = conn.prepareStatement("patnuevaobservacion");
            s.setInt(1,ID_Observacion);
            s.setString(2,Observacion);
            s.setString(3,Fecha);
            s.executeUpdate();  
            conn.commit();
            
            /* query = "Insert into bienesobservaciones (id_bien, id_observacion)
             values (?,?)*/
            s = null;
            s = conn.prepareStatement("patnuevobienobservacion");
            s.setInt(1,ID_Bien);
            s.setInt(2,ID_Observacion);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return ID_Observacion;
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return 0;
        }//catch  
    }
    
    public ArrayList DatosMejoras (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        ArrayList Relacion = new ArrayList(); 
//      
        try
        {
            
            //  String query = "select * from  bienesmejoras where ID_Bien=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patbienesmejoras");
            s.setInt(1, ID_Bien);
            r = s.executeQuery(); 
            while (r.next())
            {
                Relacion.add(r.getString("id_reforma"));
            }     
            
            // query = "select * from reformas where id_reforma=?";
            s = null;
            s = conn.prepareStatement("patmejoras");
            Iterator iDatos = Relacion.iterator();
            while (iDatos.hasNext()){
                s.setInt(1,Integer.parseInt(iDatos.next().toString()));
                r = s.executeQuery();  
                while (r.next())
                {
                    Datos.add(r.getString("id")); 
                    Datos.add(r.getString("reforma"));
                    Datos.add(r.getString("fecha")); 
                    Datos.add(r.getString("importe")); 
                }
            }
            
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
        
        
    }
    
    public String ActualizarMejora (int ID_Mejora, String Mejora, String Fecha, int Importe)
    {
        try
        {
            /*	 String query = "Update reformas set reforma=?, fecha=?, importe=?" + 
             " where id_reforma=" + ID_Mejora;*/
            PreparedStatement s = null;
            s = conn.prepareStatement("patactualizarmejora");
            s.setString(1,Mejora);
            s.setString(2,Fecha);
            s.setInt(3, Importe);
            s.setInt(4,ID_Mejora);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public int altaMejora (int ID_Bien, String Mejora, String Fecha, int Importe)
    {
        try
        {
            
//          String query ="select max(id_reforma) from reformas";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patidmejora");
            r = s.executeQuery();  
            int ID_Mejora=0;
            while (r.next())
            {
                ID_Mejora= Integer.parseInt(r.getString(1).toString());             
            }
            
            s = null;
            r = null;
            ID_Mejora=ID_Mejora+1;
            /* 	 query = "insert into reformas (id_reforma,reforma,fecha, importe)" +
             " values (?,?,?,?)";*/
            s = null;
            s = conn.prepareStatement("patnuevamejora");
            s.setInt(1,ID_Mejora);
            s.setString(2,Mejora);
            s.setString(3,Fecha);
            s.setInt(4, Importe);
            s.executeUpdate();  
            conn.commit();
            
            /*	 query = "Insert into bienesmejoras (id_bien, id_reforma)" +
             " values (?,?)";*/
            s = null;
            s = conn.prepareStatement("patnuevobienmejora");
            s.setInt(1,ID_Bien);
            s.setInt(2,ID_Mejora);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return ID_Mejora;
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return 0;
        }//catch   
    }
    public String ActualizarUso (int ID_Uso, String Uso, int Superficie)
    {
        try
        {
            /* String query = "Update usos set uso=?, sueprficie=?" + 
             " where ID_Uso=" + ID_Uso;*/
            PreparedStatement s = null;
            s = conn.prepareStatement("patactualizaruso");
            s.setString(1,Uso);
            s.setInt(2, Superficie);
            s.setInt(3,ID_Uso);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public String AltaUso (int ID_Bien, String Uso,  int Superficie)
    {
        try
        {
            
            //   String query ="select max(id_uso) from usos";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patiduso");
            r = s.executeQuery();  
            int ID_Uso=0;
            while (r.next())
            {
                ID_Uso= Integer.parseInt(r.getString(1).toString());             
            }
            
            s = null;
            r = null;
            ID_Uso=ID_Uso+1;
            /* 	 query = "insert into usos (id_uso,uso, superficie)" +
             " values (?,?,?)";*/
            s = null;
            s = conn.prepareStatement("patnuevouso");
            s.setInt(1,ID_Uso);
            s.setString(2,Uso);
            s.setInt(3,Superficie);
            s.executeUpdate();  
            conn.commit();
            
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    public ArrayList DatosGeneralesViales (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        try
        {
            
//          String query = "select * from  bienes where ID_Bien=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patbienes");
            s.setInt(1, ID_Bien);
            r = s.executeQuery();  
            while (r.next())
            {
                Datos.add(r.getString("destino")); 
                Datos.add(r.getString("fecha_adquisicion")); 
                Datos.add(r.getString("forma_adquisicion"));   
                Datos.add(r.getString("propiedad"));      
                Datos.add(r.getString("datoscesion")); 
                Datos.add(r.getString("datoscesionario")); 
            }     
            
            //  query = "select * from viales where id_bien=?";
            s = null;
            r = null;
            s = conn.prepareStatement("patviales");
            s.setInt(1,ID_Bien);
            r = s.executeQuery();
            while (r.next())
            {
                Datos.add(r.getString("categoria")); 
                Datos.add(r.getString("numerofarolas")); 
                Datos.add(r.getString("numeroapliques"));   
                Datos.add(r.getString("numerobancos"));      
                Datos.add(r.getString("numeropapeleras")); 
                Datos.add(r.getString("metrospavimentados"));           
                Datos.add(r.getString("metrosnopavimentados")); 
                Datos.add(r.getString("estadoconservacion"));
                Datos.add(r.getString("valor")); 
                Datos.add(r.getString("zonaverdevial"));           
                Datos.add(r.getString("iniciovial")); 
                Datos.add(r.getString("finvial"));
                Datos.add(r.getString("longitud")); 
                Datos.add(r.getString("ancho"));
            }
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
        
        
    }
    
    
    public String ActualizarGeneralViales (int ID_Bien, ArrayList Valores, ArrayList Tipos)
    {
        try
        {
            /*  	 String query = "Update bienes set id_bien=?, destino=?," + 
             " fecha_adquisicion=?, forma_adquisicion=?, propiedad=?," +
             " datoscesion=?, datoscesionario=?" +
             " where ID_bien=" + ID_Bien;*/
            PreparedStatement s = null;
            s = conn.prepareStatement("patactualizargeneralviales");
            Iterator Datos = Valores.iterator();
            Iterator TipoDatos = Tipos.iterator();
            int n =0;
            while (n<7)
            {
                n=n+1;
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    s.setInt(n,Integer.parseInt(Datos.next().toString()));
                    break;
                case 1:
                    s.setString(n,Datos.next().toString());
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            
            /*	 query = "Update viales set categoria=?, numerofarolas=?, numeroapliques=?, numerobancos=?," + 
             " numeropapeleras=?, metrospavimentados=?, metrosnopavimentados=?, estadoconservacion=?," +
             " valor=?, zonaverdevial=?, iniciovial=?, finvial=?, longitud=?, ancho=?" +
             " where ID_Bien=" + ID_Bien;*/
            s = null;
            s = conn.prepareStatement("patactualizarviales");
            n=0;
            while (Datos.hasNext())
            {
                n=n+1;
                switch (Integer.parseInt(TipoDatos.next().toString())){
                case 0:
                    s.setInt(n,Integer.parseInt(Datos.next().toString()));
                    break;
                case 1:
                    s.setString(n,Datos.next().toString());
                    break;
                default:
                    break;
                }    
            }
            s.setInt(n+1,ID_Bien);
            s.executeUpdate();  
            conn.commit();
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public ArrayList EjecutarConsulta (String query)
    {
        ArrayList Datos = new ArrayList(); 
        
        try
        {
            
            PreparedStatement s = null;
            ResultSet r = null;
            // Connection conn = getDBConnection ();
            s = conn.prepareStatement(query);
            r = s.executeQuery();  
            while (r.next())
            {
                Datos.add(r.getString("num_inventario")); 
                Datos.add(r.getString("nombre")); 
                Datos.add(r.getString("descripcion"));   
                Datos.add(r.getString("tipo_bien"));      
            }     
            
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
        
        
    }
    
    public String BorrarObservacion (int ID_Bien, int ID_Observacion)
    {
        try
        {
            
//          String query = "Delete from bienesobservaciones  where ID_Bien=? AND ID_Observacion=?";
            PreparedStatement s = null;
            s = conn.prepareStatement("patbajabienobservacion");
            s.setInt(1,ID_Bien);
            s.setInt(2,ID_Observacion);
            s.executeUpdate();  
            conn.commit();
            //  query = "Delete from observaciones  where ID_Observacion=?";
            s = null;
            s = conn.prepareStatement("patbajaobservacion");
            s.setInt(1,ID_Observacion);
            s.executeUpdate();  
            conn.commit();
            
            
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch   
    }
    
    public String BorrarMejora (int ID_Bien, int ID_Mejora)
    {
        try
        {
            
            //  String query = "Delete from bienesmejoras  where ID_Bien=? AND ID_Reforma=?";
            PreparedStatement s = null;
            s = conn.prepareStatement("patbajabienmejora");
            s.setInt(1,ID_Bien);
            s.setInt(2,ID_Mejora);
            s.executeUpdate();  
            conn.commit();
            //   query = "Delete from reformas  where ID_Reforma=?";
            s = null;
            s = conn.prepareStatement("patbajamejora");
            s.setInt(1,ID_Mejora);
            s.executeUpdate();  
            conn.commit();
            
            
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public ArrayList DatosUsos (int ID_Bien)
    {
        ArrayList Datos = new ArrayList(); 
        //
        try
        {
            
//          String query = "select * from  usos  where ID_Bien=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patusos");
            s.setInt(1, ID_Bien);
            r = s.executeQuery(); 
            while (r.next())
            {
                Datos.add(r.getString("id_bien")); 
                Datos.add(r.getString("uso")); 
                Datos.add(r.getString("superficie")); 
            }
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
    }
    
    public ArrayList RecuperarDatosSeguro (int ID_Seguro)
    {
        ArrayList Datos = new ArrayList(); 
        
//      
        try
        {
            PreparedStatement s = null;
            ResultSet r = null;
            //  	String query = "select * from seguros where id_seguro=?";
            s = conn.prepareStatement("patseguros");
            s.setInt(1,ID_Seguro);
            r = s.executeQuery(); 
            while (r.next())
            {
                Datos.add(r.getString("compannia")); 
                Datos.add(r.getString("descripcion")); 
                Datos.add(r.getString("prima"));
                Datos.add(r.getString("poliza")); 
                Datos.add(r.getString("fecha_inicio"));
                Datos.add(r.getString("fecha_vencimiento")); 
            }
            s.close();
            r.close(); 
            conn.close();
            return Datos;
            
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return null;
        }//catch  
    }
    public String BorrarSeguro (int ID_Bien, int ID_Seguro)
    {
        try
        {
            
//          String query = "Delete from bienesseguros  where ID_Bien=? AND ID_seguro=?";
            PreparedStatement s = null;
            s = conn.prepareStatement("patbajabienseguro");
            s.setInt(1,ID_Bien);
            s.setInt(2,ID_Seguro);
            s.executeUpdate();  
            conn.commit();
            //  query = "Delete from seguros  where ID_Seguro=?";
            s = null;
            s = conn.prepareStatement("patbajaseguro");
            s.setInt(1,ID_Seguro);
            s.executeUpdate();  
            conn.commit();
            
            
            s.close();
            conn.close();
            return "Correcto";
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return "Error";
        }//catch  
    }
    
    public int Bien (int ID_Parcela)
    {
        ArrayList Datos = new ArrayList(); 
        try
        {
            
            //String query = "select referencia_catastral from parcelas where id=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patref");
            s.setInt(1,ID_Parcela);
            r = s.executeQuery(); 
            String Ref=" ";
            while (r.next())
            {
                Ref= r.getString("referencia_catastral");
            }
            String Refpar= Ref.substring(0,6);
            //System.out.println("Refpar " + Refpar);
            String Refpla= Ref.substring(7,13);
            //System.out.println("Refpla " + Refpla);
            r=null;
            s=null;
            
            //Query: select id from inmuebles where refpar=? and refpla=?
            s = conn.prepareStatement("patgetidbien");
            s.setString(1,Refpar);
            s.setString(2,Refpla);
            r = s.executeQuery(); 
            int ID=0;
            while (r.next())
            {
                ID= r.getInt(1);
                //System.out.println("ID_Bien " + ID);
            }
            s.close();
            r.close(); 
            conn.close();
            return ID;
        }catch (SQLException ex)
        {
            
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            return 0;
        }//catch  
        catch (StringIndexOutOfBoundsException ex)
        {
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("PatrimonioPostgre.RefCatastralIncorrecto"), app.getI18nString("PatrimonioPostgre.RefCatastralIncorrecto"), StringUtil.stackTrace(ex));
            return 0;
            
        }//catch 
    }
    
    
    public void Referencias (int ID_Parcela)
    {
        ArrayList Datos = new ArrayList(); 
        try
        {
            
            //String query = "select referencia_catastral from parcelas where id=?";
            PreparedStatement s = null;
            ResultSet r = null;
            s = conn.prepareStatement("patref");
            s.setInt(1,ID_Parcela);
            r = s.executeQuery(); 
            String Ref=" ";
            while (r.next())
            {
                Ref= r.getString("referencia_catastral");
            }
            if (Ref.length()<14) Ref = GeopistaUtil.completarConCeros(Ref,14);
            String RefPar= Ref.substring(0,7);
            String RefPla= Ref.substring(7,14);
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            Identificadores.put("RefCatastral", Ref);
            Identificadores.put("RefParcela", RefPar);
            Identificadores.put("RefPlano", RefPla);
            
            
            
            s.close();
            r.close(); 
            conn.close();
            r=null;
            s=null;
            
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("SQLError.Titulo"), app.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(ex));
            
            
        }
        catch (StringIndexOutOfBoundsException ex)
        {
            //ex.printStackTrace();
            AppContext app =(AppContext) AppContext.getApplicationContext();
            ErrorDialog.show(app.getMainFrame(), app.getI18nString("PatrimonioPostgre.RefCatastralIncorrecto"), app.getI18nString("PatrimonioPostgre.RefCatastralIncorrecto"), StringUtil.stackTrace(ex));
            
            
        }//catch  
    }
    
    
    
}

