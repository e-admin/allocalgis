/**
 * ListadoInmueblesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @author hgarcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.patrimonio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;

//public class General extends FeatureExtendedPanel {
public class ListadoInmueblesPanel extends  JPanel implements FeatureExtendedPanel {

  private JLabel jLabel1 = new JLabel();
  private JPanel jPanel1 = new JPanel();
  
  int align = FlowLayout.LEFT;   
  private JPanel jPanel2 = new JPanel(new FlowLayout(align));
  private JPanel jPanelLateral = new JPanel();
  
 
  
  private JButton btnNuevo = new JButton();
  private JButton btnEliminar = new JButton();
  private JButton btnInsertar = new JButton();
  private JLabel jLabelNombre = new JLabel();
  private JTextField txtNombre = new JTextField();
  private JLabel jLabelNumInventario = new JLabel();
  private JTextField txtNumInventario = new JTextField();
  private JLabel jLabelDescripcion = new JLabel();
  private JTextField txtDescripcion = new JTextField();
  private JLabel jLabelTipo = new JLabel();
  private JComboBox cmbTipo = new JComboBox();
  //private ArrayList ControlesGenerales = new ArrayList();

 
  private ButtonGroup bg = new ButtonGroup();
  
  
  
  private boolean alta=false;
  private int ID_Bien;
  private String ID_Parcela;
  private int InmuebleSeleccion;
  private boolean primvez=true;
  
  
  public final int URBANO  = 1;
  public final int RUSTICO = 2;
  public FeatureDialogHome fdh;
  
  AppContext app =(AppContext) AppContext.getApplicationContext();
  
  
  public ListadoInmueblesPanel(FeatureDialogHome fd)
  {
    try
    {
      fdh = fd; 
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }
  
  private void jbInit() throws Exception
  {
    jLabel1.setText(app.getI18nString("patrimonio.inmuebles.lista"));
    this.setLayout(null);
    this.setName(app.getI18nString("patrimonio.inmuebles.titulo"));
    this.setSize(new Dimension (710,477));
    this.setBounds(new Rectangle(5, 10, 630, 400));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);

    jLabel1.setBounds(15, 15, 290, 35);       
       
    btnNuevo.setText(app.getI18nString("patrimonio.nuevo"));
    btnNuevo.addActionListener(new ActionListener()
     {
        public void actionPerformed(ActionEvent e)
        {
          btnNuevo_actionPerformed(e);
        }
     });
    btnEliminar.setText(app.getI18nString("patrimonio.eliminar"));
    btnEliminar.addActionListener(new ActionListener()
     {      
        public void actionPerformed(ActionEvent e)
        {
          btnEliminar_actionPerformed(e);
        }
      });
    btnInsertar.setText(app.getI18nString("patrimonio.insertar"));
    btnInsertar.addActionListener(new ActionListener()
     {
        public void actionPerformed(ActionEvent e)
        {
            btnInsertar_actionPerformed(e);
        }
     });
    
    jPanel1.add(btnNuevo, null);
    jPanel1.add(btnEliminar, null);
           
    jPanel1.setBounds(300, 410, 250, 30);
   
    this.add(jPanel1, null);
    this.add(jLabel1, null);
  
  }

 

  public void enter()
  {   
    
		
		Blackboard Identificadores = app.getBlackboard();
		//ID_Bien= Integer.parseInt(Identificadores.get("IdBien").toString());
		ID_Parcela= String.valueOf(Identificadores.get("IdParcela"));
		Identificadores.put("IdInmueble", "");
		
		
		 jPanelLateral.setLayout(null);
		 jPanelLateral.removeAll();
	     
	     jPanelLateral.setBounds (300, 50, 225, 175);
	     jPanelLateral.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
	     //jPanelLateral.setBackground(Color.GRAY);
	     
	     jLabelNombre.setText(app.getI18nString("patrimonio.inmuebles.nombre"));
	     jLabelNombre.setBounds(10, 10, 100, 15);
	     jPanelLateral.add(jLabelNombre, null);
	     txtNombre.setText("");
	     txtNombre.setBounds(125, 10, 90, 20);
	     jPanelLateral.add(txtNombre, null);
	     
	     
	     jLabelDescripcion.setText(app.getI18nString("patrimonio.inmuebles.descripcion"));
	     jLabelDescripcion.setBounds(10, 40, 100, 15);
	     jPanelLateral.add(jLabelDescripcion, null);
	     txtDescripcion.setText("");
	     txtDescripcion.setBounds(125, 40, 90, 20);
	     jPanelLateral.add(txtDescripcion, null);
	     
	     jLabelNumInventario.setText(app.getI18nString("patrimonio.inmuebles.numeroinventario"));
	     jLabelNumInventario.setBounds(10, 70, 120, 15);
	     jPanelLateral.add(jLabelNumInventario, null);
	     txtNumInventario.setText("");
	     txtNumInventario.setEditable(false);
	     txtNumInventario.setBounds(125, 70, 90, 20);
	     jPanelLateral.add(txtNumInventario, null);
	     
	     jLabelTipo.setText(app.getI18nString("patrimonio.inmuebles.tipoinmueble"));
	     jLabelTipo.setBounds(10, 100, 120, 15);
	     jPanelLateral.add(jLabelTipo, null);
	     cmbTipo.removeAllItems();
	     cmbTipo.addItem("");
	     cmbTipo.addItem(app.getI18nString("patrimonio.general.urbano"));
	     cmbTipo.addItem(app.getI18nString("patrimonio.general.rustico"));
	     cmbTipo.setSelectedIndex(-1);
	     cmbTipo.setBounds(125, 100, 90, 20);
	     jPanelLateral.add(cmbTipo, null);
	     
	     btnInsertar.setBounds(80,140, 90, 25);
	     btnInsertar.setEnabled(false);
	     
	     jPanelLateral.add(btnInsertar, null);
	     this.add(jPanelLateral, null);
	         
	     cargarListado();
	     
     
  }


  public void exit()
  {
   // AppContext app =(AppContext) AppContext.getApplicationContext();
      //    Blackboard Identificadores = app.getBlackboard();
      //Identificadores.put ("ID_Bien", ID_Bien);
  }

  

  private void btnNuevo_actionPerformed(ActionEvent e)
  {
     
      AppContext app =(AppContext) AppContext.getApplicationContext();
      Blackboard Identificadores = app.getBlackboard();
      
      //System.out.println("Se ha pulsado el botón de Nuevo en la parcela " + Identificadores.get("IdParcela"));
      alta = true;
      
      txtNombre.setText("");
	  txtDescripcion.setText("");
	  txtNumInventario.setText("");
	  cmbTipo.setSelectedIndex(-1);
	  btnInsertar.setEnabled(true);
	  btnInsertar.setText(app.getI18nString("patrimonio.insertar"));
	  
	  txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
	  txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
	  cmbTipo.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
	  
	  
  }

  
  private void btnInsertar_actionPerformed(ActionEvent e)
  {
     
      AppContext app =(AppContext) AppContext.getApplicationContext();
      Blackboard Identificadores = app.getBlackboard();
      
      
      //System.out.println("Se desea Insertar o modificar un nuevo bien inmueble ");
      
      String nombre = txtNombre.getText();
      String descripcion = txtDescripcion.getText();
      int tipo = cmbTipo.getSelectedIndex();
            
      PatrimonioPostgre Inmueble = new PatrimonioPostgre();
      
      //Alta
      if (alta)
      {
          String numInven = Inmueble.AltaInmueble (nombre, descripcion, tipo);
          if (numInven.equals("Error"))
          {
              cargarListado();
              return;
          }
          
	      txtNumInventario.setText(numInven);
		  
	      txtNombre.setBorder(BorderFactory.createLoweredBevelBorder());
	      txtNombre.setText("");
		  txtDescripcion.setBorder(BorderFactory.createLoweredBevelBorder());
		  txtDescripcion.setText("");
		  txtNumInventario.setText("");
		  cmbTipo.setSelectedIndex(-1);
		  cmbTipo.setBorder(null);
		  btnInsertar.setEnabled(false);
		  cargarListado();

      }
      //Modificación
      else
      {
          String Resultado=Inmueble.ModificacionInmueble(nombre, descripcion);
          //System.out.println("Resultado de la modificación: " + Resultado);
          cargarListado();
          
      }
  }

  
  private void btnEliminar_actionPerformed(ActionEvent e)
  { 
     
    
    PatrimonioPostgre Inmueble = new PatrimonioPostgre();
	          
	String Result = Inmueble.BajaInmueble (InmuebleSeleccion);
	//System.out.println("Resultado del proceso de borrado de "+InmuebleSeleccion +": " + Result);
	((FeatureDialog)fdh).setSideBarDescription(" ");	
	txtNombre.setText("");
	txtDescripcion.setText("");
	txtNumInventario.setText("");
	cmbTipo.setSelectedIndex(-1);
	jPanel2.setVisible(false);
	cargarListado();
	
   }

  private void radiobutton_actionPerformed(ActionEvent e)
  {
      alta = false;
      
      if (fdh instanceof FeatureDialogHome)
      {
          
          ((FeatureDialog)fdh).setPanelEnabled(General.class, true);
          ((FeatureDialog)fdh).setPanelEnabled(RegistroPatrimonioPanel.class, true);
          ((FeatureDialog)fdh).setPanelEnabled(ConstruccionPatrimonioPanel.class, true);
          ((FeatureDialog)fdh).setPanelEnabled(ValoracionesPatrimonioPanel.class, true);
          ((FeatureDialog)fdh).setPanelEnabled(DerechosPatrimonioPanel.class, true);
          ((FeatureDialog)fdh).setPanelEnabled(ObservacionesPatrimonio.class, true);
                    
          ((FeatureDialog)fdh).setSideBarDescription("");
          ((FeatureDialog)fdh).setTitle(app.getI18nString("patrimonio.inmueblesdisponibles")+" "+ID_Parcela);
          
      }
      
      
      try{
          
	      InmuebleSeleccion = Integer.valueOf(e.getActionCommand()).intValue();
		  txtNombre.setText("");
		  txtDescripcion.setText("");
		  txtNumInventario.setText("");
		  txtNombre.setBorder(BorderFactory.createLoweredBevelBorder());
		  txtDescripcion.setBorder(BorderFactory.createLoweredBevelBorder());
		  txtNumInventario.setBorder(BorderFactory.createLoweredBevelBorder());
		  cmbTipo.setBorder(null);
		  cmbTipo.setSelectedIndex(-1);
		  btnInsertar.setEnabled(true);
		  btnInsertar.setText(app.getI18nString("patrimonio.modificar"));
		  
		  
		  //System.out.println("Entro al radiobutton_actionPerformed con " + InmuebleSeleccion); 
		      
		  AppContext app =(AppContext) AppContext.getApplicationContext();
		  Blackboard Identificadores = app.getBlackboard();
		  Identificadores.put ("IdInmueble", InmuebleSeleccion);
		  
		  
		  //Actualizar los valores del panel lateral
		  PatrimonioPostgre Bien = new PatrimonioPostgre();
		  ArrayList Datos= Bien.Propiedades();
			     
		  if (Datos==null)return;
		     
		  Iterator alIt = Datos.iterator();
		  String Nombre="";
		  String Descripcion="";
		
		  while (alIt.hasNext()) {
		         
		      //Nombre
		      Nombre = alIt.next().toString();
		      txtNombre.setText(Nombre);
		     
		      //Descripción
		      Descripcion=alIt.next().toString();
		      txtDescripcion.setText(Descripcion);
		      
		      //Numero de inventario
		      txtNumInventario.setText(alIt.next().toString());
		      
		      //Tipo de bien: urbano o rústico
		      cmbTipo.setSelectedIndex(Integer.parseInt(alIt.next().toString()));
		      
		      
		     }
		  
		  
	
	  if(!Nombre.equals("")) 
			      ((FeatureDialog)fdh).setSideBarDescription(Nombre);	
	  
			  
	  }catch (Exception ex)
	  {
	      ((FeatureDialog)fdh).setSideBarDescription(app.getI18nString("patrimonio.inmuebles.identificacion")+ " <b>"+InmuebleSeleccion + "</b>");	
	  }
	  
	  
  }
  
  private void cargarListado()
  {
      
//	   Mostramos los inmuebles para la parcela
  	PatrimonioPostgre ListadoInmuebles = new PatrimonioPostgre();
  	ArrayList Datos= ListadoInmuebles.DatosInmuebles();
  
  	if (Datos==null)return;
  	     
  	Iterator alIt = Datos.iterator();
  	     
  	JRadioButton b1;
  	jPanel2.setBounds(20, 50, 230, 350);
  	jPanel2.removeAll();
  	jPanel2.setVisible(false);
  		 
       while (alIt.hasNext()) {
           
           //Descripcion
           b1=new JRadioButton (alIt.next().toString());
           
           //Identificador del bien (ID de las tablas inmuebles y bienes)
           b1.setActionCommand(alIt.next().toString());
                   
           b1.addActionListener(new ActionListener()
                   {
                      public void actionPerformed(ActionEvent e)
                      {
                        radiobutton_actionPerformed(e);
                      }
                   });
           	         
           
           
           bg.add(b1);
           
           if (Integer.parseInt(b1.getActionCommand())==InmuebleSeleccion){
               
               ButtonModel model = b1.getModel();
               bg.setSelected(model, true);
               b1.doClick();
            }
           
          
           jPanel2.add(b1, null);
           //jPanel2.setBackground(Color.RED);
       }
       
       jPanel2.setVisible(true);     
       this.add(jPanel2, null);
      
  }
  

}