/**
 * ImportarFINURB.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImportarFINURB extends JPanel
{
  private   boolean Correcto=true;
  private int ID_Parcela=0;
  private int ID_Subparcela=0;
  private int ID_Unidad=0;
  private String ID_Cons;
  private int ID_Cargo=0;
  private ValidarFINURB Validar = new ValidarFINURB();    
  
  public ImportarFINURB()
  {
    
     String linea=" ";
     ResourceBundle RB = ResourceBundle.getBundle("GeoPista");
     String Ruta = RB.getString("url.finurb");
     File file = new File(Ruta);
     boolean Validado=true;
    //Aquí vamos leyendo
   
      try {
          FileReader fr = null;
          fr = new FileReader(file);
          BufferedReader br = new BufferedReader (fr);
          while ( (linea = br.readLine()) != null ) {
  //          while (Correcto ==true){
//          if( ( (linea=br.readLine()) != null) & (Correcto=true)){
            if (Correcto=true){
              System.out.println(Correcto + "Inicio: " + linea.substring(0,2));
              if (linea.substring(0,2).equals("21"))
                    { Correcto= Validar.ValidarParcela (linea); }
                  else if (linea.substring(0,2).equals("22"))
                    { Correcto= Validar.ValidarSubparcela (linea);  }
                  else if (linea.substring(0,2).equals("23"))
                    { Correcto= Validar.ValidarUC (linea); }
                  else if (linea.substring(0,2).equals("24"))
                    { Correcto= Validar.ValidarConstruccion (linea);  }
                  else if (linea.substring(0,2).equals("25"))
                    { Correcto= Validar.ValidarCargo (linea);  }   
                  else {Correcto=true;}
                }
    //        }
          else{Validado=false;}}
         }catch (Exception ex)
         {
          System.out.println("Error");
          System.out.println(ex.getMessage());
          ex.printStackTrace();
        }

    //Una vez hemos validado el fichero procedemos a cargar los datos
    //Para ello deberemos ir almacenando las variables identificadoras y metiendo los datos


     try {
          FileReader fr = null;
          fr = new FileReader(file);
          BufferedReader br = new BufferedReader (fr);
          System.out.println(linea=br.readLine());
          while ( (linea = br.readLine()) != null ) {
              if (linea.substring(0,2).equals("21")){
                    ID_Parcela= Validar.IncluirParcela (linea); 
              }else if (linea.substring(0,2).equals("22")){
                    ID_Subparcela= Validar.IncluirSubparcela (linea, ID_Parcela); 
              }else if (linea.substring(0,2).equals("23")){
                    ID_Unidad= Validar.IncluirUC (linea, ID_Parcela); 
              }else if (linea.substring(0,2).equals("24")){
                    ID_Cons= Validar.IncluirConstruccion(linea, ID_Unidad); 
              }else if (linea.substring(0,2).equals("25")){
                    ID_Cargo= Validar.IncluirCargo(linea); 
              }
          }




  }catch (Exception ex)
         {
          System.out.println("Error");
          System.out.println(ex.getMessage());
          ex.printStackTrace();
        }
  }
  public static void main(String[] args)
  {
    JFrame frame1 = new JFrame("Edición Datos");
    ImportarFINURB Importar = new ImportarFINURB();
    frame1.getContentPane().add(Importar);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }

}