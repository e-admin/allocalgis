package com.geopista.app.catastro;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JTable;

import com.geopista.app.AppContext;



public class ValidarFINURB
{
public static Connection getDBConnection () throws SQLException{

       AppContext app =(AppContext) AppContext.getApplicationContext();
      Connection con=  app.getConnection();
      con.setAutoCommit(false);
      return con;
 }
	
    public boolean ValidarParcela (String Parcela)
    {
      return true;
    }
    public boolean ValidarSubparcela (String Parcela)
    {
      return true;
    }
    public boolean ValidarUC (String Parcela)
    {
      return true;
    }
    public boolean ValidarConstruccion (String Parcela)
    {
      return true;
    }
    public boolean ValidarCargo (String Parcela)
    {
      return true;
    }


    public int IncluirParcela (String Linea)
    {
      try
      {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data = {      
          {"1", "2","16"},
          {"0", "56","58"},//distrito
          {"1", "58","60"},
          {"0", "60","65"},
          {"0", "95","99"},
          {"1", "99","100"},
          {"0", "100","104"},
          {"1", "104","105"},
          {"0", "105","110"},
          {"1", "110","114"},
          {"1", "114","139"},
          {"0", "139","144"},
          {"0", "144","151"},
          {"0", "151","158"},
          {"0", "158","165"},
          {"0", "165","172"},
          {"0", "172","179"},
          {"0", "179","183"},
          {"0", "183","185"},          
          {"1", "185","186"}};
        JTable parcela = new JTable(data, columnNames);


        String query ="select max(id_parcela) from parcelas";
        PreparedStatement s = null;
        ResultSet r = null;
        Connection conn = getDBConnection ();
        s = conn.prepareStatement(query);
        r = s.executeQuery();  
        int ID_Parcela=0;
        while (r.next())
        {
            ID_Parcela= Integer.parseInt(r.getString(1).toString());             
        };

        s = null;
        r = null;
    //Falta el ID_Municipio
        query = "Insert into parcelas (referencia_catastral," + 
                      " id_distrito, codigo_entidad_menor, ID_Via," +
                      "primer_numero, primera_letra, segundo_numero, " + 
                      " segunda_letra, kilometro, bloque, direccion_no_estructurada," +
                      " codigo_postal, superficie_solar, superficie_construida_total,"+
                      "superficie_const_sobrerasante, superficie_const_bajorasante," +
                      " superficie_cubierta, anio_aprobacion, " +
                      "codigo_calculo_valor, modalidad_reparto,id_parcela, tipo,fecha_alta, id_municipio) " +
                      "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


//        conn = getDBConnection (user,pass,url, driver);
        s = null;
        r = null;
        conn = getDBConnection ();
        s = conn.prepareStatement(query);

        ID_Parcela=ID_Parcela+1;
        int n=0;
       String valor="";
       while(n<20)
        
        {
            n=n+1;
            int a=Integer.parseInt(parcela.getValueAt(n-1,1).toString());            
            int b=Integer.parseInt(parcela.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            System.out.println("Inicio: " +a  +"; Fin: " + b +"; Valor: "+ valor);
            switch (Integer.parseInt(parcela.getValueAt(n-1,0).toString())){
            case 0:
               s.setInt(n,Integer.parseInt(valor));
               break;
            case 1:
                s.setString(n,valor);
                break;
           case 2:
             if (valor.equals("S"))
                  {s.setBoolean(n,true);}
                else
                   {s.setBoolean(n,false);}
                break;
            default:
              break;
          
        }}

        s.setInt(21, ID_Parcela);
        s.setString(22,"U");
        Calendar cal=Calendar.getInstance();
        s.setDate(23,new java.sql.Date(cal.getTime().getTime()));
        s.setInt(24,5);
        s.executeUpdate();  
        conn.commit();
        s.close();
        conn.close();
        return ID_Parcela;
      }catch (Exception ex)
      {
        System.out.println("Error");
        System.out.println(ex.getMessage());
        ex.printStackTrace();
          return 0;
      }//catch  
     }
    public int IncluirSubparcela (String Linea, int ID_Parcela)
    {
      try
      {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data = {      
          {"0", "16","20"},
          {"0", "58","63"},
          {"1", "63","65"},//distrito
          {"0", "65","72"},
          {"0", "72","75"},
          {"0", "80","83"},
          {"1", "83","84"},
          {"1", "84","85"},
          {"2", "85","86"},
          {"2", "86","87"},          
          {"2", "87","88"},          
          {"2", "88","89"},
          {"2", "89","90"},          
          {"2", "90","91"},                    
          {"0", "93","96"},
          {"2", "96","97"},
          {"0", "98","101"},
          {"2", "101","102"},
          {"2", "102","103"},
          {"1", "103","104"},
          {"1", "104","105"},
          {"1", "105","106"},
          {"1", "106","107"},
          {"1", "107","108"},
          {"1", "108","109"}};
          
        JTable subparcela = new JTable(data, columnNames);


        String query ="select max(id_subparcela) from subparcelas";
        PreparedStatement s = null;
        ResultSet r = null;
        Connection conn = getDBConnection ();
        s = conn.prepareStatement(query);
        r = s.executeQuery();  
        int ID_Subparcela=0;
        while (r.next())
        {
            ID_Subparcela= Integer.parseInt(r.getString(1).toString());             
        };

        s = null;
        r = null;
    //Falta el ID_Municipio
        query = "Insert into subparcelas (" + 
                  " numero_orden, longitud_fachada, tipo_fachada, superficie_elemento_suelo," +
                  "fondo_elemento_suelo, id_tramo_via, codigo_tipo_valor, numero_fachadas, " + 
                  " corrector_longitud_fachada, corrector_forma_irregular," +
                  " corrector_desmonte_excesivo, corrector_profundidad_firme,"+
                  "corrector_fondo_excesivo, corrector_superficie_distinta," +
                  " corrector_depreciacion_funcional, corrector_situaciones_especiales, " +
                  "corrector_uso_no_lucrativo, corrector_apreciacion," +
                  "corrector_cargas_singulares,agua, electricidad, alumbrado, "+
                  "desmonte, pavimentacion, alcantarillado, id_subparcela, id_parcela, fecha_alta) " +
                  "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


//        conn = getDBConnection (user,pass,url, driver);
        s = null;
        r = null;
        conn = getDBConnection ();
        s = conn.prepareStatement(query);
        ID_Subparcela=ID_Subparcela+1;
        int n=0;
       String valor="";
       while(n<27)
        
        {
            n=n+1;
            int a=Integer.parseInt(subparcela.getValueAt(n-1,1).toString());            
            int b=Integer.parseInt(subparcela.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            System.out.println("Inicio: " +a  +"; Fin: " + b +"; Valor: "+ valor);
            switch (Integer.parseInt(subparcela.getValueAt(n-1,0).toString())){
            case 0:
               s.setInt(n,Integer.parseInt(valor));
               break;
            case 1:
                s.setString(n,valor);
                break;
           case 2:
                if (valor.equals("S"))
                  {s.setBoolean(n,true);}
                else
                   {s.setBoolean(n,false);}
                break;
            default:
              break;
          
        }}
        s.setInt(28, ID_Subparcela);
        s.setInt(29, ID_Parcela);
        Calendar cal=Calendar.getInstance();
        s.setDate(30,new java.sql.Date(cal.getTime().getTime()));
        s.executeUpdate();  
        conn.commit();
        s.close();
        conn.close();
        return ID_Subparcela;
      }catch (Exception ex)
      {
        System.out.println("Error");
        System.out.println(ex.getMessage());
        ex.printStackTrace();
          return 0;
      }//catch  
     }
    public int IncluirUC (String Linea, int ID_Parcela)
    {
      try
      {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data = {      
          {"1", "16","20"},
          {"1", "58","60"},
          {"0", "95","99"},
          {"1", "99","100"},
          {"0", "100","104"},
          {"1", "104","105"},
          {"0", "105","110"},
          {"1", "110","114"},
          {"1", "114","138"},
          {"0", "139","144"},          
          {"0", "144","148"},          
          {"0", "148","153"},
          {"2", "153","154"},          
          {"0", "159","162"},                    
          {"1", "162","163"},
          {"2", "163","164"},
          {"1", "164","165"},
          {"2", "165","166"},
          {"0", "166","169"},
          {"2", "169","170"},
          {"2", "170","171"},
          {"1", "171","172"},
          {"1", "172","173"},
          {"1", "173","174"},
          {"1", "174","175"},
          {"1", "175","176"},
          {"1", "176","177"},          
          {"1", "177","178"}};
          
          
        JTable UC = new JTable(data, columnNames);


        String query ="select max(id_unidadconstructiva) from unidades_constructivas";
        PreparedStatement s = null;
        ResultSet r = null;
        Connection conn = getDBConnection ();
        s = conn.prepareStatement(query);
        r = s.executeQuery();  
        int ID_UC=0;
        while (r.next())
        {
            ID_UC= Integer.parseInt(r.getString(1).toString());             
        };

        s = null;
        r = null;
    //Falta el ID_Municipio
        query = "Insert into unidades_constructivas " +
                  "(codigo_unidad, codigo_entidad_menor," +
                  "primer_numero, primera_letra, segundo_numero, segunda_letra," +
                  " kilometro, bloque, direccion_no_estructurada, codigo_postal," +
                  "anio_construccion,longitud_fachada, indicador_uso_agrario,"+
                  "id_tramo_via, numero_fachadas, corrector_longitud_fachada," +
                  "corrector_estado_conservacion,corrector_depreciacion_funcional," +
                  "corrector_cargas_singulares, corrector_situaciones_especiales," +
                  "corrector_uso_no_lucrativo,agua, electricidad, alumbrado, desmonte, " +
                  "pavimentacion, alcantarillado,exactitud_anio_construccion," +
                  " id_unidadconstructiva, id_parcela, fecha_alta)" +
                  "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


//        conn = getDBConnection (user,pass,url, driver);
        s = null;
        r = null;
        conn = getDBConnection ();
        s = conn.prepareStatement(query);
        int n=0;
       String valor="";
       while(n<27)
        {
            n=n+1;
            int a=Integer.parseInt(UC.getValueAt(n-1,1).toString());            
            int b=Integer.parseInt(UC.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            System.out.println("Inicio: " +a  +"; Fin: " + b +"; Valor: "+ valor);
            switch (Integer.parseInt(UC.getValueAt(n-1,0).toString())){
            case 0:
               s.setInt(n,Integer.parseInt(valor));
               break;
            case 1:
                s.setString(n,valor);
                break;
           case 2:
                if (valor.equals("S"))
                  {s.setBoolean(n,true);}
                else
                   {s.setBoolean(n,false);}
                break;
            default:
              break;
          
        }}
        ID_UC = ID_UC + 1;
        s.setInt(29, ID_UC);
        s.setInt(30, ID_Parcela);
        Calendar cal=Calendar.getInstance();
        s.setDate(31,new java.sql.Date(cal.getTime().getTime()));
        s.executeUpdate();  
        conn.commit();
        s.close();
        conn.close();
        return ID_UC;
      }catch (Exception ex)
      {
        System.out.println("Error");
        System.out.println(ex.getMessage());
        ex.printStackTrace();
          return 0;
      }//catch  
     }

    public String IncluirConstruccion (String Linea, int ID_Unidad)
    {
      try
      {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data = {      
          {"0", "16","20"},
          {"0", "58","60"},
          {"1", "95","99"},
          {"1", "99","100"},
          {"1", "100","104"},
          {"1", "104","105"},
          {"1", "105","110"},
          {"1", "110","114"},
          {"0", "114","138"},
          {"2", "139","144"},          
          {"0", "144","148"},          
          {"0", "148","153"},
          {"0", "153","154"},          
          {"1", "159","162"},                    
          {"1", "162","163"},
          {"1", "163","164"},
          {"1", "164","165"},
          {"1", "165","166"},
          {"0", "166","169"},
          {"2", "169","170"}};
          
        JTable Cons = new JTable(data, columnNames);

   
    //Falta el ID_Municipio
       String query = "Insert into construcciones " +
                  "(numero_cargo," +
                  " bloque, escalera, planta, puerta," + 
                  " codigo_destino_dgc, indicador_tipo_reforma, anio_reforma," + 
                  "indicador_local_interior, superficie_total_local, superficie_terrazas_local," + 
                  " superficie_imputable_local,  tipologia_constructiva," + 
                  "codigo_uso_predominante, codigo_categoria_predominante, codigo_modalidad_reparto," + 
                  "  codigo_tipo_valor, corrector_apreciacion_economica," + 
                  " corrector_vivienda, id_construccion, id_unidadconstructiva,fecha_alta)" + 
                  "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


      String ID_Construccion= Linea.substring(2,16) + Linea.substring(20,24);
      PreparedStatement s = null;
        ResultSet r = null;
        Connection conn = getDBConnection ();
        s = conn.prepareStatement(query);
        int n=0;
       String valor="";
       while(n<18)
        {
            n=n+1;
            int a=Integer.parseInt(Cons.getValueAt(n-1,1).toString());            
            int b=Integer.parseInt(Cons.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            System.out.println("Inicio: " +a  +"; Fin: " + b +"; Valor: "+ valor);
            switch (Integer.parseInt(Cons.getValueAt(n-1,0).toString())){
            case 0:
               s.setInt(n,Integer.parseInt(valor));
               break;
            case 1:
                s.setString(n,valor);
                break;
           case 2:
                if (valor.equals("S"))
                  {s.setBoolean(n,true);}
                else
                   {s.setBoolean(n,false);}
                break;
            default:
              break;
          
        }}
        s.setString(19, ID_Construccion);
        s.setInt(20, ID_Unidad);
        Calendar cal=Calendar.getInstance();
        s.setDate(21,new java.sql.Date(cal.getTime().getTime()));
        s.executeUpdate();  
        conn.commit();
        s.close();
        conn.close();
        return ID_Construccion;
      }catch (Exception ex)
      {
        System.out.println("Error");
        System.out.println(ex.getMessage());
        ex.printStackTrace();
          return "Error";
      }//catch  
     }

  public int IncluirCargo (String Linea)
    {
      try
      {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data = {      
          {"1", "16","20"},
          {"1", "20","21"},
          {"1", "21","22"},
          {"0", "22","30"},
          {"0", "31","36"},
          {"0", "56","58"},
          {"1", "58","60"},
          {"0", "60","65"},
          {"0", "95","99"},
          {"1", "99","100"},          
          {"0", "100","104"},          
          {"1", "104","105"},
          {"0", "105","110"},          
          {"1", "110","114"},                    
          {"1", "114","139"},
          {"0", "139","144"},
          {"1", "144","146"},
          {"1", "146","149"},
          {"1", "149","152"},
          {"0", "428","432"},
          {"0", "432","444"},
          {"0", "444","456"},
          {"0", "456","468"},
          {"0", "468","480"},
          {"1", "480","481"},
          {"0", "481","485"},          
          {"0", "485","489"},
          {"0", "489","495"},
          {"0", "495","502"},
          {"0", "502","509"},
          {"0", "509","516"},
          {"2", "516","517"},
          {"0", "517","521"},
          {"1", "521","522"}};
          
        JTable Cargo = new JTable(data, columnNames);


      Object[][] data1 = {      
          {"1", "152","160"},
          {"1", "160","161"},
          {"1", "161","162"},
          {"1", "162","222"},
          {"0", "222","226"},
          {"1", "226","246"},
          {"0", "246","249"},
          {"0", "249","251"},
          {"0", "251","253"},
          {"0", "253","256"},          
          {"0", "256","261"},          
          {"0", "291","295"},
          {"1", "295","296"},          
          {"0", "296","300"},                    
          {"1", "300","301"},
          {"0", "301","306"},
          {"1", "306","310"},
          {"1", "310","335"},
          {"1", "335","337"},
          {"1", "337","340"},
          {"1", "340","343"},
          {"0", "344","348"},
          {"0", "348","353"},
          {"1", "353","378"},
          {"1", "378","403"},
          {"1", "403","428"}};
        JTable Titular = new JTable(data1, columnNames);
        int ID_Cargo=0;
        int ID_Titular=0;
        boolean NuevoTitular=false;
        String ID_Construccion= Linea.substring(2,20);
        String query ="select max(id_cargo) from cargos";
        PreparedStatement s = null;
        ResultSet r = null;
        Connection conn = getDBConnection ();
        s = conn.prepareStatement(query);
        r = s.executeQuery();  
        while (r.next())
        {
            ID_Cargo= Integer.parseInt(r.getString(1).toString());             
        };
        ID_Cargo=ID_Cargo+1;
        String nif= Linea.substring(152,160);
        s = null;
        r = null;

        
        query = "select id_titular from titulares where nif=?";
        conn = getDBConnection ();
        s = conn.prepareStatement(query);
        s.setString(1,nif);
        r = s.executeQuery();  
        while (r.next())
        {
            ID_Titular= Integer.parseInt(r.getString(1).toString());             
        };
        if (ID_Titular==0){ NuevoTitular=true;
            query = "select max(id_titular) from titulares";
            conn = getDBConnection ();
            s = conn.prepareStatement(query);
            s.setString(1,nif);
            r = s.executeQuery();  
            while (r.next())
            {
                ID_Titular= Integer.parseInt(r.getString(1).toString());             
            };
        }
        else { NuevoTitular=false;}

        s = null;
        r = null;
    //Falta el ID_Municipio
     query = "Insert into cargos (numero_cargo," + 
                  " primer_caracter_control, segundo_caracter_control, numero_fijo_inmueble, "+ 
                  "coeficiente_participacion, id_distrito,codigo_entidad_menor," + 
                  " id_via, primer_numero, primera_letra, segundo_numero, segunda_letra, kilometro," + 
                  " bloque, direccion_no_estructurada, codigo_postal," + 
                  "escalera, planta, puerta, " + 
                  " anio_valor_catastral, valor_catastral," + 
                  " valor_catastral_suelo, valor_catastral_construccion, base_liquidable," + 
                  "clave_uso_dgc, anio_ultima_revision, anio_ultima_notificacion, " + 
                  "numero_ultima_notificacion, superficie_elementos_constructivos," + 
                  "superficie_suelo, coeficiente_propiedad, indicador_aplicacion_valoracion," +
                  "anio_finalizacion_valoracion, indicador_tipo_propiedad, "+
                  "id_cargo, id_construccion,id_titular, fecha_alta)" + 
                  "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        s = null;
        r = null;
        conn = getDBConnection ();
        s = conn.prepareStatement(query);
        int n=0;
       String valor="";
       while(n<34)
        {
            n=n+1;
            int a=Integer.parseInt(Cargo.getValueAt(n-1,1).toString());            
            int b=Integer.parseInt(Cargo.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            System.out.println("Inicio: " +a  +"; Fin: " + b +"; Valor: "+ valor);
            switch (Integer.parseInt(Cargo.getValueAt(n-1,0).toString())){
            case 0:
               s.setInt(n,Integer.parseInt(valor));
               break;
            case 1:
                s.setString(n,valor);
                break;
           case 2:
                if (valor.equals("S"))
                  {s.setBoolean(n,true);}
                else
                   {s.setBoolean(n,false);}
                break;
            default:
              break;
          
        }}
        s.setInt(35, ID_Cargo);
        s.setString(36, ID_Construccion);
        s.setInt(37, ID_Titular);
        Calendar cal=Calendar.getInstance();
        s.setDate(38,new java.sql.Date(cal.getTime().getTime()));
        s.executeUpdate();  
        conn.commit();
        if (NuevoTitular=true)
        {
          query = "Insert into titulares "+
                  "(nif,caracter_control_nif," + //3
                  " personalidad, identidad, numero_componentes,complemento_identidad," + //4
                  " codigo_delegacion_meh, codigo_municipio_dgc, codigo_provincia_ine,codigo_municipio_ine," + //4
                  "id_via,primer_numero, primera_letra, segundo_numero, segunda_letra, kilometro," +  //5
                  " bloque, direccion_no_estructurada, escalera, planta, puerta,codigo_postal, " + //6
                  " apartado_correos, pais, provincia, nombre_municipio, id_titular)" + //4
                  "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

          
        }else
        {
         query = "Update titulares set  nif=?," + 
                  " caracter_control_nif=?, personalidad=?, identidad=?, numero_componentes=?, complemento_identidad=?," +
                  " codigo_delegacion_meh=?, codigo_municipio_dgc=?, codigo_provincia_ine=?,codigo_municipio_ine=?," + //4
                  " id_via=?, primer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?, kilometro=?," + 
                  " bloque=?, direccion_no_estructurada=?, escalera=?, planta=?, puerta=?,codigo_postal=?, " + 
                  " apartado_correos=?, pais=?, provincia=?, nombre_municipio=?" +
                  " where id_titular=?";
          
        }
       s = null;
        r = null;
        conn = getDBConnection ();
        s = conn.prepareStatement(query);
        n=0;
       valor="";
       while(n<26)
        {
            n=n+1;
            int a=Integer.parseInt(Titular.getValueAt(n-1,1).toString());            
            int b=Integer.parseInt(Titular.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            System.out.println("Inicio: " +a  +"; Fin: " + b +"; Valor: "+ valor);
            switch (Integer.parseInt(Titular.getValueAt(n-1,0).toString())){
            case 0:
               s.setInt(n,Integer.parseInt(valor));
               break;
            case 1:
                s.setString(n,valor);
                break;
           case 2:
                if (valor.equals("S"))
                  {s.setBoolean(n,true);}
                else
                   {s.setBoolean(n,false);}
                break;
            default:
              break;
          
        }}
        s.setInt(27, ID_Titular);
        s.executeUpdate();  
        conn.commit();
        s.close();
        conn.close();
        return ID_Cargo;
      }catch (Exception ex)
      {
        System.out.println("Error");
        System.out.println(ex.getMessage());
        ex.printStackTrace();
          return 0;
      }//catch  
     }

}