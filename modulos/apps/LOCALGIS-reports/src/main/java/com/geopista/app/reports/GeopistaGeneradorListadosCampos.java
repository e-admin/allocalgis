/**
 * GeopistaGeneradorListadosCampos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.geopista.app.AppContext;
import com.geopista.sql.GeopistaInformesPostgresCon;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


/**
 * Muestra la ventana de los campos que se introduciran en el informe.
 * Los campos podrán ser el resultado de operaciones espaciales o bien funciones de sql relacional.
 */

public class GeopistaGeneradorListadosCampos extends JPanel implements WizardPanel
{


  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


  private Blackboard blackboardInformes  = aplicacion.getBlackboard();
  
  private JPanel jPanel1 = new JPanel();
 
  private ArrayList camposDeDominios = new ArrayList();
  private ArrayList camposOrdenados = new ArrayList();
  private GeopistaGeneradorListadosConexionBD conexion = new GeopistaGeneradorListadosConexionBD();
  private ListSelectionModel listSelectionModel1 = new DefaultListSelectionModel();

  private JComboBox cmbTabla = new JComboBox();
  private JLabel lblCampo = new JLabel();
  private JComboBox cmbCampo = new JComboBox();
  private JPanel jPanel2 = new JPanel();
  private JLabel lblNombreTabla = new JLabel();
  private JLabel lblCamposInforme = new JLabel();
  private JLabel lblNoEspaciales = new JLabel();
  private JLabel jLabel1 = new JLabel();
  private JTextField txtEtiqueta = new JTextField();
  private JLabel lblAgregado = new JLabel();
  private JComboBox cmbAgrupamiento = new JComboBox();
  private JButton btnAnadir = new JButton();
  private JPanel jPanel3 = new JPanel();
  private JLabel jLabel2 = new JLabel();
  private JLabel lblAgregado2 = new JLabel();
  private JComboBox cmbAgregado = new JComboBox();
  private JLabel lblOperacion = new JLabel();
  private JComboBox cmbOperacion = new JComboBox();
  private JLabel lblEtiqueta2 = new JLabel();
  private JTextField txtEtiqueta2 = new JTextField();
  private JComboBox cmbTabla1 = new JComboBox();
  private JComboBox cmbEspacial = new JComboBox();
  private JComboBox cmbTabla2 = new JComboBox();
  private JPanel jPanel4 = new JPanel();

  private DefaultTableModel model = new DefaultTableModel(){
        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
        }
    };
    
 

  private JTable tablaCampos = new JTable(model);


  private JButton jButton2 = new JButton();
  private JButton jButton3 = new JButton();
  private JButton jButton4 = new JButton();
  private JButton btnAnadir2 = new JButton();
  private int filas = 0;

  public Connection con =null;
  public GeopistaGeneradorListadosConexionBD geopistaListados = new GeopistaGeneradorListadosConexionBD();

  public GeopistaGeneradorListadosCampos()
  {
    try
    {
       jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  
  private void jbInit() throws Exception
  {
    this.setLayout(null);
//    con = getDBConnection();
    this.setSize(new Dimension(765, 532));
    jPanel1.setBounds(new Rectangle(5, 5, 750, 500));
    jPanel1.setLayout(null);

    jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
     DefaultListModel modeloList = new DefaultListModel();
    //Cargamos la lista de capas
     ArrayList capas = geopistaListados.capasGeopista();

     Iterator i = capas.iterator();
     cmbTabla.setBounds(new Rectangle(75, 35, 220, 20));
     try {
    //Iniciamos la ayuda
      String helpHS="ayuda.hs";
      HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
      HelpBroker hb = hs.createHelpBroker();
    //fin de la ayuda

      hb.enableHelpKey(this,"generadorInformeCamposInforme",hs); 
     }catch (Exception e ){e.printStackTrace();}

    
    lblCampo.setText(aplicacion.getI18nString("generador.app.reports.campos.campo"));
    lblCampo.setBounds(new Rectangle(15, 65, 50, 15));
    cmbCampo.setBounds(new Rectangle(75, 60, 220, 20));
    jPanel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel2.setLayout(null);
    lblNombreTabla.setText(aplicacion.getI18nString("generador.app.reports.campos.tabla"));
    lblNombreTabla.setBounds(new Rectangle(15, 35, 40, 20));
    lblCamposInforme.setText(aplicacion.getI18nString("generador.app.reports.campos.campos.informe"));
    lblNoEspaciales.setText(aplicacion.getI18nString("generador.app.reports.campos.no.espacial"));
    lblNoEspaciales.setBounds(new Rectangle(15, 10, 95, 25));
    
    jLabel1.setText(aplicacion.getI18nString("generador.app.reports.campos.etiqueta"));
    jLabel1.setBounds(new Rectangle(300, 35, 190, 20));
    txtEtiqueta.setBounds(new Rectangle(300, 60, 290, 20));
    lblAgregado.setText(aplicacion.getI18nString("generador.app.reports.campos.agregado"));
    lblAgregado.setBounds(new Rectangle(15, 90, 55, 15));
    cmbAgrupamiento.setBounds(new Rectangle(75, 85, 220, 20));
    btnAnadir.setIcon(IconLoader.icon("anadir_campo.gif"));
    btnAnadir2.setIcon(IconLoader.icon("anadir_campo.gif"));
 //   btnAnadir.setText(aplicacion.getI18nString("generador.app.reports.campos.anadir"));
    btnAnadir.setBounds(new Rectangle(545, 85, 45, 25));
    jPanel3.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel3.setLayout(null);
    jLabel2.setText(aplicacion.getI18nString("generador.app.reports.campos.espaciales"));
    jLabel2.setBounds(new Rectangle(10, 10, 95, 15));
    lblAgregado2.setText(aplicacion.getI18nString("generador.app.reports.campos.agregado"));
    lblAgregado2.setBounds(new Rectangle(10, 30, 70, 20));
    cmbAgregado.setBounds(new Rectangle(95, 25, 285, 20));
    lblOperacion.setText(aplicacion.getI18nString("generador.app.reports.campos.operacion"));
    lblOperacion.setBounds(new Rectangle(10, 55, 60, 20));
    cmbOperacion.setBounds(new Rectangle(95, 50, 285, 20));
    lblEtiqueta2.setText(aplicacion.getI18nString("generador.app.reports.campos.etiqueta"));
    lblEtiqueta2.setBounds(new Rectangle(5, 105, 95, 20));
    txtEtiqueta2.setBounds(new Rectangle(95, 105, 430, 20));
    cmbTabla1.setBounds(new Rectangle(395, 25, 200, 20));
    cmbEspacial.setBounds(new Rectangle(395, 50, 200, 20));
    cmbTabla2.setBounds(new Rectangle(395, 75, 200, 20));
    jPanel4.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    jPanel4.setLayout(null);
    tablaCampos.setBounds(new Rectangle(10, 5, 535, 160));
    jButton2.setIcon(IconLoader.icon("borrar_campo.gif"));
    jButton3.setIcon(IconLoader.icon("haciaArriba.gif"));
    jButton4.setIcon(IconLoader.icon("haciaAbajo.gif"));

   // btnAnadir2.setText(aplicacion.getI18nString("generador.app.reports.campos.anadir"));
    btnAnadir2.setBounds(new Rectangle(555, 100, 40, 25));
    
     while (i.hasNext())
     {
        modeloList.addElement(i.next());
     }
        


     //Creamos la lista con el modelo anteriormente definido      
    jPanel1.setSize(new Dimension(750, 500));

    
    jPanel2.add(chkSubtotales, null);
    jPanel2.add(btnAnadir, null);
    jPanel2.add(cmbAgrupamiento, null);
    jPanel2.add(lblAgregado, null);
    jPanel2.add(txtEtiqueta, null);
    jPanel2.add(jLabel1, null);
    jPanel2.add(lblNoEspaciales, null);
    jPanel2.add(lblNombreTabla, null);
    jPanel2.add(cmbTabla, null);
    jPanel2.add(cmbCampo, null);
    jPanel2.add(lblCampo, null);
    jPanel3.add(chkSubtotales2, null);
    jPanel3.add(btnAnadir2, null);
    jPanel3.add(cmbTabla2, null);
    jPanel3.add(cmbEspacial, null);
    jPanel3.add(cmbTabla1, null);
    jPanel3.add(txtEtiqueta2, null);
    jPanel3.add(lblEtiqueta2, null);
    jPanel3.add(cmbOperacion, null);
    jPanel3.add(lblOperacion, null);
    jPanel3.add(cmbAgregado, null);
    jPanel3.add(lblAgregado2, null);
    jPanel3.add(jLabel2, null);
    jPanel1.add(lblImagen, null);
    jPanel1.add(jPanel4, null);
    jPanel1.add(jPanel3, null);
    jPanel1.add(lblCamposInforme, null);
    jPanel1.add(jPanel2, null);
    jPanel1.add(jButton4, null);
    jPanel1.add(jButton3, null);
    jPanel1.add(jButton2, null);

    //Cargamos las capas en la lista
     ArrayList tablasGeometria = new ArrayList(); 

      tablasGeometria = geopistaListados.tablasConGeometria();

      Iterator iGeometria = tablasGeometria.iterator();
      while (iGeometria.hasNext()){
          String geometria = (String) iGeometria.next();
          cmbTabla1.addItem(geometria);
          cmbTabla2.addItem(geometria);
      }

 

      tablaCampos.addMouseListener(new java.awt.event.MouseAdapter() { 
      	public void mouseClicked(java.awt.event.MouseEvent e) {    
      		//obtener el indice de la tabla 
      	    int fila;
      	    String tipoCampo ;
      	    String tabla1;
      	    String tabla2;
      	    String subtotales;
      	    String campo;
      	    String etiqueta;
      	    String agrupados;
      	    
      	    //Primero Inicializamos a vacío todo.
      	    cmbAgregado.setSelectedIndex(-1);
      	    cmbOperacion.setSelectedIndex(-1);
      	    chkSubtotales.setSelected(false);
      	    chkSubtotales2.setSelected(false);
      	    chkOrdenarCampo.setSelected(false);
      	    cmbTabla.setSelectedIndex(-1);
      	    cmbTabla1.setSelectedIndex(-1);
      	    cmbTabla2.setSelectedIndex(-1);
      	    cmbEspacial.setSelectedIndex(0);
      	    txtEtiqueta.setText("");
      	    txtEtiqueta2.setText("");
      	    cmbAgrupamiento.setSelectedIndex(-1);
      	    
      	    fila = tablaCampos.getSelectedRow();
      	    tipoCampo = (String)model.getValueAt(fila,1);
      	    
      	    
      	    if ((tipoCampo.equals("X") || tipoCampo.equals("E"))) {
      	        //El Campo es Espacial
      	        
      	        //Etiqueta
      	        etiqueta = (String) model.getValueAt(fila,2);
      	        txtEtiqueta2.setText(etiqueta);
      	        
      	      //Subtotales  
      	      subtotales = (String) model.getValueAt(fila,5);
      	      if (subtotales.equals("S")){
      	          chkSubtotales2.setSelected(true);
      	      }else{
      	          chkSubtotales2.setSelected(false);
      	      }
      	      
      	      tabla1 = (String) model.getValueAt(fila,4);
      	      //Buscamos el caracter / 
      	      int posicion = tabla1.indexOf("/");
      	      int tamanho = tabla1.length();
      	      tabla2 = tabla1.substring(posicion+1,tamanho);
      	      tabla1= tabla1.substring(0,posicion);
      	      
      	    for (int i=0; i<cmbTabla1.getItemCount();i++){
  	            if (cmbTabla1.getItemAt(i).equals(tabla1)){
  	                cmbTabla1.setSelectedIndex(i);
  	                break;
  	            }
  	        }// de la búsqueda de la tabla 
      	      
      	  for (int i=0; i<cmbTabla2.getItemCount();i++){
	            if (cmbTabla2.getItemAt(i).equals(tabla2)){
	                cmbTabla2.setSelectedIndex(i);
	                break;
	            }
	        }// de la búsqueda de la tabla   
      	   
      	  
      	  	//Preguntar si el campo es X entonces existe Avg(Sum(intersection)
      	  	if (tipoCampo.equals("E")){
      	  	    //Sin Agrupar.
      	  	campo = (String)model.getValueAt(fila,3);
      	  	int nPosicion= campo.indexOf("(");
      	  	String operacion = campo.substring(0,nPosicion);
      	  	if (operacion.equals("Area")){
      	  	    //Es el área
      	  	    for (int k=0; k<cmbOperacion.getItemCount();k++){
      	  	        if (cmbOperacion.getItemAt(k).equals(aplicacion.getI18nString("generador.app.reports.campos.area"))){
      	  	            cmbOperacion.setSelectedIndex(k);
      	  	            break;
      	  	        }
      	  	    }
      	      
      	      //cmbOperacion.addItem(aplicacion.getI18nString("generador.app.reports.campos.longitud"));
      	  	}else{
      	  	for (int k=0; k<cmbOperacion.getItemCount();k++){
  	  	        if (cmbOperacion.getItemAt(k).equals(aplicacion.getI18nString("generador.app.reports.campos.longitud"))){
  	  	            cmbOperacion.setSelectedIndex(k);
  	  	            break;
  	  	        }
  	  	    }
      	  	    
      	  	}
      
      	  	    
      	  	}else{
      	  	    //Agrupando.
      	  	    //System.out.println("Voy por agrupado");
      	  	    //En campo me quedo con el primer paréntesis
      	  	    String campoEspacial = (String)model.getValueAt(fila,3);
      	  	    campoEspacial=campoEspacial.substring(7,campoEspacial.length());
      	  	    int parentesis = campoEspacial.indexOf("(");
      	  	    String grupoEspacial = campoEspacial.substring(0,parentesis).trim();
      	  	    if (grupoEspacial.equals("Sum")){
      	  	        for (int k=0; k<cmbAgregado.getItemCount();k++){
      	  	            if (cmbAgregado.getItemAt(k).equals(aplicacion.getI18nString("generador.app.reports.campos.suma"))){
      	  	                cmbAgregado.setSelectedIndex(k);
      	  	                break;
      	  	            }
      	  	        }
      	  	    }else{
      	  	        if (grupoEspacial.equals("Avg")){
      	  	        for (int k=0; k<cmbAgregado.getItemCount();k++){
      	  	            if (cmbAgregado.getItemAt(k).equals(aplicacion.getI18nString("generador.app.reports.campos.media"))){
      	  	                cmbAgregado.setSelectedIndex(k);
      	  	                break;
      	  	            }
      	  	        }   
      	  	        }else{
      	  	            if (grupoEspacial.equals("Min")){
      	  	            for (int k=0; k<cmbAgregado.getItemCount();k++){
          	  	            if (cmbAgregado.getItemAt(k).equals(aplicacion.getI18nString("generador.app.reports.campos.minimo"))){
          	  	                cmbAgregado.setSelectedIndex(k);
          	  	                break;
          	  	            }
          	  	        }
      	  	            }else{
      	  	                if (grupoEspacial.equals("Max")){
      	  	                for (int k=0; k<cmbAgregado.getItemCount();k++){
              	  	            if (cmbAgregado.getItemAt(k).equals(aplicacion.getI18nString("generador.app.reports.campos.maximo"))){
              	  	                cmbAgregado.setSelectedIndex(k);
              	  	                break;
              	  	            }
              	  	        }       
      	  	                }else{
      	  	                    if (grupoEspacial.equals("Count")){
      	  	                    for (int k=0; k<cmbAgregado.getItemCount();k++){
                  	  	            if (cmbAgregado.getItemAt(k).equals(aplicacion.getI18nString("generador.app.reports.campos.contar"))){
                  	  	                cmbAgregado.setSelectedIndex(k);
                  	  	                break;
                  	  	            }
                  	  	        }       
      	  	                    }else{
      	  	                        
      	  	                    }
      	  	                }
      	  	            }
      	  	        }
      	  	    }//Del Agrupado
      	  	    
      	 
      		  	int nPosicion= campoEspacial.indexOf("(");
      		  	campoEspacial = campoEspacial.substring(nPosicion+1,campoEspacial.length());
      		  	//Busco de nuevo el (
      		  	nPosicion = campoEspacial.indexOf("(");
      		 
          	  	String operacion = campoEspacial.substring(0,nPosicion);
          	  	if (operacion.equals("Area")){
          	  	    //Es el área
          	  	    for (int k=0; k<cmbOperacion.getItemCount();k++){
          	  	        if (cmbOperacion.getItemAt(k).equals(aplicacion.getI18nString("generador.app.reports.campos.area"))){
          	  	            cmbOperacion.setSelectedIndex(k);
          	  	            break;
          	  	        }
          	  	    }
          	      
          	      //cmbOperacion.addItem(aplicacion.getI18nString("generador.app.reports.campos.longitud"));
          	  	}else{
          	  	for (int k=0; k<cmbOperacion.getItemCount();k++){
      	  	        if (cmbOperacion.getItemAt(k).equals(aplicacion.getI18nString("generador.app.reports.campos.longitud"))){
      	  	            cmbOperacion.setSelectedIndex(k);
      	  	            break;
      	  	        }
      	  	    }
          	  	    
          	  	}
          
      	  	    
      	  	}
      	  
      	        
      	    }else{
      	        //El campo no es espacial
      	        etiqueta = (String) model.getValueAt(fila,2);
      	        campo = (String)model.getValueAt(fila,3);
      	        tabla1 = (String) model.getValueAt(fila,4);
      	        subtotales = (String) model.getValueAt(fila,5);
      	        agrupados = (String) model.getValueAt(fila,1);
      	        

      	        //Localizamos la tabla en su combo
      	        for (int i=0; i<cmbTabla.getItemCount();i++){
      	            if (cmbTabla.getItemAt(i).equals(tabla1)){
      	                cmbTabla.setSelectedIndex(i);
      	                break;
      	            }
      	        }// de la búsqueda de la tabla 
      	        
      	        //Comprobamos que se creen subtotales
      	        if (subtotales.equals("S")){
      	            chkSubtotales.setSelected(true);
      	        }else{
      	            chkSubtotales.setSelected(false);
      	        }
      	        //Localizamos la búsqueda del campo.
      	        if (agrupados.equals("G")){
      	            //Hay un agrupamiento por tanto hay que
      	            //buscar el campo y el agrupamiento.
      	            campo=campo.substring(1).trim();
      	            campo =campo.substring(7,campo.length());
      	            int parInicial;
      	            int parFinal;
      	            String sTipo;
      	            
      	            parInicial = campo.indexOf("(");
      	            parFinal = campo.indexOf(")");
      	            sTipo = campo.substring(0,parInicial);
      	            campo = campo.substring(parInicial+1,parFinal);

      	            for (int i=0; i<cmbCampo.getItemCount();i++){
        	            if (cmbCampo.getItemAt(i).equals(campo)){
        	                cmbCampo.setSelectedIndex(i);
        	                break;
        	            }
          	          }// de la búsqueda de la tabla
      	            
      	            if (sTipo.equals("Min")){
      	                for (int j=0; j<cmbAgrupamiento.getItemCount();j++){
      	                    	if (cmbAgrupamiento.getItemAt(j).equals(aplicacion.getI18nString("generador.app.reports.campos.minimo"))){
      	                    	    cmbAgrupamiento.setSelectedIndex(j);
      	                    	    break;
      	                    	}
      	                }
      	            }else{
      	                if (sTipo.equals("Max")){
      	                  for (int j=0; j<cmbAgrupamiento.getItemCount();j++){
    	                    	if (cmbAgrupamiento.getItemAt(j).equals(aplicacion.getI18nString("generador.app.reports.campos.maximo"))){
    	                    	    cmbAgrupamiento.setSelectedIndex(j);
    	                    	    break;
    	                    	}
    	                }
     	                    
      	                }else{
      	                 if (sTipo.equals("Avg")){
      	                   for (int j=0; j<cmbAgrupamiento.getItemCount();j++){
   	                    	if (cmbAgrupamiento.getItemAt(j).equals(aplicacion.getI18nString("generador.app.reports.campos.media"))){
   	                    	    cmbAgrupamiento.setSelectedIndex(j);
   	                    	    break;
   	                    	}
   	                }   
      	                 }else{
      	                     if (sTipo.equals("Count")){
      	                       for (int j=0; j<cmbAgrupamiento.getItemCount();j++){
      	   	                    	if (cmbAgrupamiento.getItemAt(j).equals(aplicacion.getI18nString("generador.app.reports.campos.contar"))){
      	   	                    	    cmbAgrupamiento.setSelectedIndex(j);
      	   	                    	    break;
      	   	                    	}
      	   	                }   
      	                     }else{
      	                       for (int j=0; j<cmbAgrupamiento.getItemCount();j++){
     	   	                    	if (cmbAgrupamiento.getItemAt(j).equals(aplicacion.getI18nString("generador.app.reports.campos.suma"))){
     	   	                    	    cmbAgrupamiento.setSelectedIndex(j);
     	   	                    	    break;
     	   	                    	}
     	   	                }    
      	                     }
      	                 }   
      	                }
      	            }//del If MIn
      	            
      	            

      	        }else{
      	        for (int i=0; i<cmbCampo.getItemCount();i++){
    	            if (cmbCampo.getItemAt(i).equals(campo)){
    	                cmbCampo.setSelectedIndex(i);
    	                break;
    	            }
      	          }// de la búsqueda de la tabla
      	        }
      	        
      	        //Ponemos la Etiqueta 
      	        txtEtiqueta.setText(etiqueta);
      	        
      	        
      	    }
      	    
      	    
      	}
      });
      cmbTabla.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
        {
          //Recogemos el tipo de inmuebles
          //JComboBox combo =(JComboBox) evt.getSource();

               cmbCampo.removeAllItems();
              String  nombre = (String) cmbTabla.getSelectedItem();
              ArrayList capas = geopistaListados.camposDeUnaTabla(nombre);
              Iterator i = capas.iterator();
              while (i.hasNext()){
                  String Campo = (String)i.next();
                  cmbCampo.addItem(Campo);
              }
     
 

            }
         });  
 
        cmbAgregado.addItem("--");

      cmbAgregado.addItem(aplicacion.getI18nString("generador.app.reports.campos.suma"));
        cmbAgregado.addItem(aplicacion.getI18nString("generador.app.reports.campos.media"));
        cmbAgregado.addItem(aplicacion.getI18nString("generador.app.reports.campos.contar"));
        cmbAgregado.addItem(aplicacion.getI18nString("generador.app.reports.campos.minimo"));
        cmbAgregado.addItem(aplicacion.getI18nString("generador.app.reports.campos.maximo"));
   // añadimos en el de operacion la suma y longitud
    cmbOperacion.addItem("--");
    cmbOperacion.addItem(aplicacion.getI18nString("generador.app.reports.campos.area"));
   cmbOperacion.addItem(aplicacion.getI18nString("generador.app.reports.campos.longitud"));

   // en el cruce de las tablas Contener e Interseccion
    cmbEspacial.addItem(aplicacion.getI18nString("generador.app.reports.campos.intersecciona.con"));
     
    // Creamos las columnas

   JTableHeader header = tablaCampos.getTableHeader();
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setIcon(IconLoader.icon((String)blackboardInformes.get("tipoBanner")));
    lblImagen.setBounds(new Rectangle(15, 5, 110, 505));
    lblCamposInforme.setBounds(new Rectangle(135, 10, 180, 15));
    jPanel2.setBounds(new Rectangle(135, 35, 600, 120));
    jPanel3.setBounds(new Rectangle(135, 160, 605, 135));
    jButton2.setBounds(new Rectangle(705, 375, 35, 30));
    jPanel4.setBounds(new Rectangle(140, 310, 555, 180));
    jButton3.setBounds(new Rectangle(705, 415, 35, 30));
    jButton4.setBounds(new Rectangle(705, 455, 35, 30));
    cmbTabla2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbTabla2_actionPerformed(e);
        }
      });
    chkSubtotales2.setBounds(new Rectangle(95, 75, 125, 15));
    chkSubtotales2.setText(aplicacion.getI18nString("generador.app.reports.campos.crear.subtotales"));
    chkSubtotales.setBounds(new Rectangle(305, 90, 135, 15));
    chkSubtotales.setText(aplicacion.getI18nString("generador.app.reports.campos.crear.subtotales"));
   
   
    jButton4.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton4_actionPerformed(e);
        }
      });
    jButton3.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton3_actionPerformed(e);
        }
      });
    cmbCampo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbCampo_actionPerformed(e);
        }
      });
    jButton2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton2_actionPerformed(e);
        }
      });
  
   jPanel4.setLayout(new BorderLayout());
    // Add header in NORTH slot
    jPanel4.add(tablaCampos, null);
    jPanel4.add(header, BorderLayout.NORTH);
    // Add table itself to CENTER slot
    jPanel4.add(tablaCampos, BorderLayout.CENTER);
    this.add(jPanel1, null);
    jPanel2.add(getChkOrdenarCampo(), null);
    model.addColumn(aplicacion.getI18nString("generador.app.reports.campos.tabla.orden"));
    model.addColumn(aplicacion.getI18nString("generador.app.reports.campos.tabla.tipo"));
    model.addColumn(aplicacion.getI18nString("generador.app.reports.campos.tabla.etiqueta"));
    model.addColumn(aplicacion.getI18nString("generador.app.reports.campos.tabla.campo"));
    model.addColumn(aplicacion.getI18nString("generador.app.reports.campos.tabla.origen"));
    model.addColumn(aplicacion.getI18nString("generador.app.reports.campos.tabla.subtotales"));

    //Ocultar una columna?
        tablaCampos.removeColumn(tablaCampos.getColumnModel().getColumn(5));
        tablaCampos.removeColumn(tablaCampos.getColumnModel().getColumn(4));
        tablaCampos.removeColumn(tablaCampos.getColumnModel().getColumn(3));
        tablaCampos.removeColumn(tablaCampos.getColumnModel().getColumn(1));
        tablaCampos.removeColumn(tablaCampos.getColumnModel().getColumn(0));

    //Añado el evento de pasar un campo normal a la tabla

      btnAnadir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
        {
            //Recoger los datos : -Tabla, Nombre del Campo, Agrupamiento      
            GeopistaEstructuraCampos campos = new GeopistaEstructuraCampos();
            GeopistaObjetoOrdenCampos ordenados = new GeopistaObjetoOrdenCampos();
            // orden 

            
            filas = tablaCampos.getRowCount();
            campos.setOrden(filas);
            campos.setTabla((String)cmbTabla.getSelectedItem());
            campos.setEtiqueta(txtEtiqueta.getText());
            
            //Ponemos los campos que irán en el sort
            ordenados.setCampoOrdenado("public."+cmbTabla.getSelectedItem() +"."+ cmbCampo.getSelectedItem());
            camposOrdenados.add(ordenados);
            
            String agrupamiento = "";
            agrupamiento = (String) cmbAgrupamiento.getSelectedItem();

            //Codificación N-Normal
            //             G-Grupo

            
            if (agrupamiento.equals("--")){
                  campos.setCampo((String)cmbCampo.getSelectedItem());
                  campos.setTipo("N");
            }else{
              if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.campos.suma"))){
                  campos.setCampo("Sum(" + cmbCampo.getSelectedItem() +")");
                  campos.setTipo("G");
              }else{
                if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.campos.media"))){
                    campos.setCampo("Avg(" + cmbCampo.getSelectedItem() +")");
                    campos.setTipo("G");
                }else{
                  if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.campos.minimo"))){
                    campos.setCampo("Min(" + cmbCampo.getSelectedItem() +")");
                    campos.setTipo("G");
                  }else{
                    if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.campos.maximo"))){
                      campos.setCampo("Max(" + cmbCampo.getSelectedItem() +")");
                      campos.setTipo("G");
                    }else{
                      campos.setCampo("Count(" + cmbCampo.getSelectedItem() +")");
                      campos.setTipo("G");
                     
                    }
                  }
                }
              }
            }

            //Comenzmos con los campos espaciales.
            //Pasar los datos del campo a la tabla

            if  ((cmbTabla.getSelectedIndex()==-1) ||
                (cmbTabla.getSelectedItem().equals("--")) ||
                (cmbCampo.getSelectedItem().equals("--")) ||
                (cmbCampo.getSelectedIndex()==-1 ))
              {
              
              }
              else{
            Object[] cadena = new Object[6];
            cadena[0]= String.valueOf(filas);
            cadena[1]=campos.getTipo();
              if (!campos.getTipo().equals("N")){
                campos.setCampo("( Select " + campos.getCampo() + " From " + campos.getTabla() + ")");
              }else{
                //Como es un campo Normal buscaremos si hay dominio o no lo hay
                
                  String campoDominio= campos.getCampo();
                  String tablaDominio =campos.getTabla();
                //Buscamos el Dominio
                int dominio = geopistaListados.campoConDominio(tablaDominio,campoDominio);
                if (dominio>0){
                    //Existe el dominio y se debe grabar en el ArrayList de Dominios.
                    GeopistaDatosDominios geoDominios = new GeopistaDatosDominios();
                    geoDominios.setOrden(filas);
                    geoDominios.setDominio(String.valueOf(dominio));
                    geoDominios.setTabla(tablaDominio);
                    geoDominios.setCampo(campoDominio);
                    geoDominios.setEtiquetas(campos.getEtiqueta());
                    camposDeDominios.add(geoDominios);
                }
              }
            cadena[2]= campos.getEtiqueta();
            cadena[3]=campos.getCampo();
            cadena[4]=campos.getTabla();
            if (chkSubtotales.isSelected()){
              cadena[5]="S";
              campos.setSubtotales("S");
            }else{
               cadena[5]="N";
              campos.setSubtotales("N");
            }

            model.insertRow(filas, cadena);
            cmbAgrupamiento.setSelectedIndex(0);
            chkOrdenarCampo.setSelected(false);
            txtEtiqueta.setText(null);
            chkSubtotales.setSelected(false);
            cmbTabla.setSelectedIndex(-1);
            cmbCampo.setSelectedIndex(-1) ;
            wizardContext.inputChanged();
          }
                



            

            
            


            

          }
         });  


    //Añado el evento de pasar un campo normal a la tabla

      btnAnadir2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt)
        {
            //Recoger los datos : -Tabla, Nombre del Campo, Agrupamiento      
            GeopistaEstructuraCampos campos = new GeopistaEstructuraCampos();
            // orden 

            if ((cmbOperacion.getSelectedIndex()==-1) ||
                (cmbOperacion.getSelectedItem().equals("--")) ||
                (cmbTabla1.getSelectedIndex()==-1) ||
                (cmbTabla1.getSelectedItem().equals("--")) ||
                (cmbTabla2.getSelectedIndex()==-1) ||
                (cmbTabla2.getSelectedItem().equals("--")))
                {
                }else{
            filas = tablaCampos.getRowCount();
            campos.setOrden(filas);
            campos.setTabla(cmbTabla1.getSelectedItem() +"/" + cmbTabla2.getSelectedItem());
            campos.setEtiqueta(txtEtiqueta.getText());
            String agrupamiento = "";
            agrupamiento = (String) cmbAgregado.getSelectedItem();
            campos.setEtiqueta(txtEtiqueta2.getText());
            String funcion = (String) cmbOperacion.getSelectedItem();

            if (funcion.equals(aplicacion.getI18nString("generador.app.reports.campos.area"))){
              campos.setCampo("Area(intersection(" + cmbTabla1.getSelectedItem() + ".\"GEOMETRY\" ,"  +cmbTabla2.getSelectedItem() +".\"GEOMETRY\"))");
            }else{
              campos.setCampo("Length(intersection(" + cmbTabla1.getSelectedItem() + ".\"GEOMETRY\" ,"  +cmbTabla2.getSelectedItem() +".\"GEOMETRY\"))");
            }

            //Codificación N-Normal
            //             G-Grupo
            //             E-Espacial sin Grupo
            //             X- Espacial con Grupo
            
            if (agrupamiento.equals("--")){
                  campos.setCampo(campos.getCampo());
                  campos.setTipo("E");
            }else{
              if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.campos.suma"))){
                  campos.setCampo("Sum(" + campos.getCampo() +")");
                  campos.setTipo("X");
              }else{
                if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.campos.media"))){
                    campos.setCampo("Avg(" + campos.getCampo() +")");
                    campos.setTipo("X");
                }else{
                  if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.campos.minimo"))){
                    campos.setCampo("Min(" + campos.getCampo() +")");
                    campos.setTipo("X");
                  }else{
                    if (agrupamiento.equals(aplicacion.getI18nString("generador.app.reports.campos.maximo"))){
                      campos.setCampo("Max(" + campos.getCampo() +")");
                      campos.setTipo("X");
                    }else{
                         
                      campos.setCampo("Count(" + campos.getCampo() +")");
                      campos.setTipo("X");
                     
                    }
                  }
                }
              }
            }

            //Comenzmos con los campos espaciales.
           


            //Pasar los datos del campo a la tabla

            Object[] cadena = new Object[6];
            cadena[0]= String.valueOf(filas);
            cadena[1]=campos.getTipo();
            cadena[2]= campos.getEtiqueta();
            if (campos.getTipo().equals("E")){
               // campos.setCampo("(Select " + campos.getCampo() +")");
            }else{
             campos.setCampo("(Select " + campos.getCampo() +" )");
             }

            cadena[3]=campos.getCampo();
            cadena[4]=campos.getTabla();
            if (chkSubtotales2.isSelected()){
              cadena[5]="S";
              campos.setSubtotales("S");
              
            }else{
              cadena[5]="N";
              campos.setSubtotales("N");
            }
            model.insertRow(filas, cadena);

            cmbAgregado.setSelectedIndex(0);
            txtEtiqueta2.setText(null);
            cmbTabla1.setSelectedIndex(-1);
            cmbTabla2.setSelectedIndex(-1);
            cmbOperacion.setSelectedIndex(-1);
            chkSubtotales2.setSelected(false);

            wizardContext.inputChanged();
            }
          }
         });  


  }
 
  public void enteredFromLeft(Map dataMap)
  {
      try
      {
	      // Pasamos el list del blackBoard al combo
	      cmbTabla.removeAllItems();
	      GeopistaObjetoInformeDatosGenerales datosGenerales = new GeopistaObjetoInformeDatosGenerales();
	      datosGenerales = (GeopistaObjetoInformeDatosGenerales) blackboardInformes.get("datosGenerales");
	      java.util.List listas = (java.util.List) datosGenerales.getCapas();
	      Iterator i = listas.iterator();
	      while (i.hasNext())
	        {
	          cmbTabla.addItem((String) i.next());
	        }
      }catch(Exception e)
      {
          e.printStackTrace();
      }
  }


    public void exitingToRight() throws Exception
    {
      ArrayList alistaCampos = new ArrayList();

        for (int i=0;i<model.getRowCount();i++)
          {
          GeopistaObjetoInformeCampos objetoCampos = new GeopistaObjetoInformeCampos();
          objetoCampos.setOrden((String) tablaCampos.getModel().getValueAt(i,0));
          objetoCampos.setTipo((String) tablaCampos.getModel().getValueAt(i,1));
          objetoCampos.setEtiquetas((String) tablaCampos.getModel().getValueAt(i,2));
          objetoCampos.setCampo((String) tablaCampos.getModel().getValueAt(i,3));
          objetoCampos.setTabla((String) tablaCampos.getModel().getValueAt(i,4));
          objetoCampos.setSubtotales((String) tablaCampos.getModel().getValueAt(i,5));
          alistaCampos.add(objetoCampos);
          } 

        //Almacenar en el blackboard el ArrayList
        blackboardInformes.put("listaCampos",alistaCampos);
        blackboardInformes.put("camposConDominio",camposDeDominios);
        blackboardInformes.put("camposOrdenadosInformes",camposOrdenados);
       }

      

    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {     
    }

    public void remove(InputChangedListener listener)
    {     
    }

    public String getTitle()
    {
      return " ";
    }

    public String getID()
    {
      return "4";
    }
  private JCheckBox chkSubtotales = new JCheckBox();
  private JCheckBox chkSubtotales2 = new JCheckBox();

   private WizardContext wizardContext;
    public void setWizardContext(WizardContext wd)
    {
      wizardContext =wd;
    }
   
    public String getInstructions()
    {
     return " ";
    }

    public boolean isInputValid()
    {
      if (model.getRowCount()==0){
        return false;
      }else{
        return true;
      }
    }

  private String nextID="5";
  private JLabel lblImagen = new JLabel();
	private JCheckBox chkOrdenarCampo = null;
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }


    public String getNextID()
    {
      return nextID;
    }

  private void cmbCapas_actionPerformed(ActionEvent e)
  {

  }

  private void cmbModulos_actionPerformed(ActionEvent e)
  {
  }

  private void cmbHorientacion_actionPerformed(ActionEvent e)
  {
  }

  private void jButton2_actionPerformed(ActionEvent e)
  {
    if (tablaCampos.getSelectedRow() ==-1)
      {
 
      }else{
        
        //Buscar en el ArrayList camposDeDominio y borrar el que coincida con el orden
        Iterator i = camposDeDominios.iterator();
        while (i.hasNext()){
          GeopistaDatosDominios geoDominios = new GeopistaDatosDominios();
          geoDominios = (GeopistaDatosDominios) i.next();
//          String valor = (String)(tablaCampos.getValueAt(tablaCampos.getSelectedRow(),0));
          String valor = (String) tablaCampos.getModel().getValueAt(tablaCampos.getSelectedRow(),0);
          int valor2 = Integer.parseInt(valor);
          int orden = geoDominios.getOrden();
          if (valor2==orden){
            i.remove();
            break;
          }
        }
        model.removeRow(tablaCampos.getSelectedRow());
        
      }
      wizardContext.inputChanged();
    
  }




  private void cmbCampo_actionPerformed(ActionEvent e)
  {
      //recogemos el campo y la tabla
      int resultado = -1;

      if ((cmbTabla.getSelectedIndex()!=-1) && (cmbCampo.getSelectedIndex()!=-1)){
           resultado = geopistaListados.devolverTipoDatos((String) cmbTabla.getSelectedItem(),(String) cmbCampo.getSelectedItem());
          if (resultado >10) 
         {
            cmbAgrupamiento.removeAllItems();
            cmbAgrupamiento.addItem("--");
            cmbAgrupamiento.addItem(aplicacion.getI18nString("generador.app.reports.campos.contar"));
            cmbAgrupamiento.addItem(aplicacion.getI18nString("generador.app.reports.campos.minimo"));
            cmbAgrupamiento.addItem(aplicacion.getI18nString("generador.app.reports.campos.maximo"));  
            chkSubtotales.setEnabled(false);
          }else{
            chkSubtotales.setEnabled(true);
            cmbAgrupamiento.removeAllItems();
            cmbAgrupamiento.addItem("--");
            cmbAgrupamiento.addItem(aplicacion.getI18nString("generador.app.reports.campos.suma"));
            cmbAgrupamiento.addItem(aplicacion.getI18nString("generador.app.reports.campos.media"));
            cmbAgrupamiento.addItem(aplicacion.getI18nString("generador.app.reports.campos.contar"));
            cmbAgrupamiento.addItem(aplicacion.getI18nString("generador.app.reports.campos.minimo"));
            cmbAgrupamiento.addItem(aplicacion.getI18nString("generador.app.reports.campos.maximo"));
          }
    }
 }
  private void jButton3_actionPerformed(ActionEvent e)
  {
    //Subir la fila una más
  
    if (tablaCampos.getSelectedRow()!=0){
    //model.moveRow(tablaCampos.getSelectedRow(),tablaCampos.getSelectedRow()+1,tablaCampos.getSelectedRow()-1);
    betterMoveRow(model,tablaCampos.getSelectedRow(),tablaCampos.getSelectedRow()+1,tablaCampos.getSelectedRow()-1);
    }
  
  }


/**
 * Método que mueve una fila dentro de una tabla
 * @param DefaultTableModel model
 * @param int start Inicio de la fila/s a mover
 * @param int end Final de la fila a mover
 * @param int destino Numero de fila de destino donde se dejará la seleccion
 */
    public static void betterMoveRow(DefaultTableModel model, int start, int end, int dest) {
        int count = end - start;
        if (count <= 0) {
            return;
        }
        if (dest > start) {
            dest = Math.max(start, dest-count);
        }
        end--;
        model.moveRow(start, end, dest);
    }

  private void jButton4_actionPerformed(ActionEvent e)
  {
    if (tablaCampos.getSelectedRow()!=model.getRowCount()-1){
    //model.moveRow(tablaCampos.getSelectedRow(),tablaCampos.getSelectedRow()+1,tablaCampos.getSelectedRow()-1);
    betterMoveRow(model,tablaCampos.getSelectedRow(),tablaCampos.getSelectedRow()+1,tablaCampos.getSelectedRow()+2);
    }
  }

  private void cmbTabla_actionPerformed(ActionEvent e)
  {
  }


   
/**
 * Devuevle la conexion para acceso a la base de datos
 * @return conexion para acceder a la base de datos
 */

public static Connection getDBConnection() throws SQLException
{
 /* AppContext app = (AppContext) AppContext.getApplicationContext();
  Connection conn= app.getConnection();*/
  GeopistaInformesPostgresCon  geoConex = new GeopistaInformesPostgresCon();
  Connection  conn=null;
  try {
  conn = geoConex.abrirConexion();
  conn.setAutoCommit(false);
  } catch (Exception e ) {return null;}
  
  return conn;
 
}



  private void cmbTabla2_actionPerformed(ActionEvent e)
  {
  }


/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getChkOrdenarCampo() {
		if (chkOrdenarCampo == null) {
			chkOrdenarCampo = new JCheckBox();
			chkOrdenarCampo.setBounds(449, 89, 89, 16);
			chkOrdenarCampo.setText("Ordenar");
		}
		return chkOrdenarCampo;
	}
 } 
