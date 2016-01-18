/**
 * ConstruccionPatrimonioPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.patrimonio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.patrimonio.estructuras.Estructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.ui.components.DateField;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;

//public class ConstruccionPatrimonioPanel extends JPanel {
public class ConstruccionPatrimonioPanel extends  JPanel implements FeatureExtendedPanel 
{

  private JLabel carpinteriaExteriorlbl = new JLabel();
  private JLabel cubiertalbl = new JLabel();
  private JLabel materiallbl = new JLabel();
  private JLabel estadoConservacionlbl = new JLabel();
  private JLabel tipoConstruccionTxt = new JLabel();
  private JSeparator jSeparator4 = new JSeparator();
  private JTextField txtFechaObra = new JTextField();
  private JLabel fechaObraTxt = new JLabel();
  private JTextField txtEdificabilidad = new JTextField();
  private JLabel edificabilidadTxt = new JLabel();
  private JTextField txtCalificacion = new JTextField();
  private JLabel calificacionTxt = new JLabel();
  private ComboBoxEstructuras cmbTipo;
  private ComboBoxEstructuras cmbEstado;
  private ComboBoxEstructuras cmbMaterial;
  private ComboBoxEstructuras cmbCubierta;
  private ComboBoxEstructuras cmbCarpinteria;
  private JPanel jPanel1 = new JPanel();
  private JButton btnAplicar = new JButton();
  private ArrayList Cons = new ArrayList();
  private int ID_Bien;
  private int ID_Inmueble;
  
  private Date date=null;  
  private DateField fechaObra = new DateField(date, 15);
 
  AppContext app =(AppContext) AppContext.getApplicationContext();
  
  public ConstruccionPatrimonioPanel()
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
  public static void main(String[] args)
  {
    JFrame frame1 = new JFrame("Edición Datos");
    ConstruccionPatrimonioPanel ConstruccionPatrimonioPanel = new ConstruccionPatrimonioPanel();
    frame1.getContentPane().add(ConstruccionPatrimonioPanel);
    frame1.setSize(675, 725);
    frame1.setVisible(true); 
    frame1.setLocation(150, 90);
    
  }

  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setName(app.getI18nString("patrimonio.construcciones.titulo"));
    this.setSize(new Dimension(710, 477));
    this.setBounds(new Rectangle(5, 10, 630, 400));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);
    
    
    
    /*
    while (!Estructuras.isCargada())
    {
	    if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
	    try {Thread.sleep(500);}catch(Exception e){}
    }*/
        
    cmbTipo = new ComboBoxEstructuras(Estructuras.getListaTiposConstruccion(), null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    cmbEstado = new ComboBoxEstructuras(Estructuras.getListaEstadosConservacion(), null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    cmbMaterial = new ComboBoxEstructuras(Estructuras.getListaTiposMaterial(), null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    cmbCubierta = new ComboBoxEstructuras(Estructuras.getListaTiposCubierta(), null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    cmbCarpinteria = new ComboBoxEstructuras(Estructuras.getListaTiposCarpinteria(), null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    
    cmbTipo.setSelectedIndex(0);
    cmbEstado.setSelectedIndex(0);
    cmbMaterial.setSelectedIndex(0);
    cmbCubierta.setSelectedIndex(0);
    cmbCarpinteria.setSelectedIndex(0);
    
   
    
    Cons.add(txtCalificacion) ;
    Cons.add(txtEdificabilidad);
    Cons.add(cmbTipo);
    
    //Cons.add(txtFechaObra);
    Cons.add(fechaObra);
    
    Cons.add(cmbEstado);
    Cons.add(cmbMaterial);
    Cons.add(cmbCubierta);
    Cons.add(cmbCarpinteria);
        
    carpinteriaExteriorlbl.setText(app.getI18nString("patrimonio.construcciones.carpinteria"));
    cubiertalbl.setText(app.getI18nString("patrimonio.construcciones.cubierta"));
    materiallbl.setText(app.getI18nString("patrimonio.construcciones.material"));
    estadoConservacionlbl.setText(app.getI18nString("patrimonio.construcciones.estadoconservacion"));
    tipoConstruccionTxt.setText(app.getI18nString("patrimonio.construcciones.tipoconstruccion"));
    fechaObraTxt.setText(app.getI18nString("patrimonio.construcciones.fechaobra"));
    edificabilidadTxt.setText(app.getI18nString("patrimonio.construcciones.edificabilidad"));
    calificacionTxt.setText(app.getI18nString("patrimonio.construcciones.calificacion"));
    btnAplicar.setText(app.getI18nString("patrimonio.aplicar"));
    
    
    
    btnAplicar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnAplicar_actionPerformed(e);
        }
      });
    jPanel1.add(btnAplicar, null);
    jPanel1.setBounds(350, 305, 100, 40);
    cmbCarpinteria.setBounds(125, 245, 310, 20);
    cmbCubierta.setBounds(125, 210, 310, 20);
    cmbMaterial.setBounds(125, 175, 310, 20);
    cmbEstado.setBounds(125, 140, 310, 20);
    cmbTipo.setBounds(125, 105, 310, 20);
    carpinteriaExteriorlbl.setBounds(5, 250, 135, 15);
    cubiertalbl.setBounds(5, 210, 110, 15);
    materiallbl.setBounds(5, 180, 110, 15);
    estadoConservacionlbl.setBounds(5, 143, 120, 15);
    tipoConstruccionTxt.setBounds(5, 108, 135, 15);
    jSeparator4.setBounds(0, 95, 520, 15);
    
    //txtFechaObra.setBounds(120, 70, 125, 20);
    fechaObra.setBounds(122, 70, 70, 20);
    
    fechaObraTxt.setBounds(5, 70, 100, 15);
    txtEdificabilidad.setBounds(120, 40, 315, 20);
    edificabilidadTxt.setBounds(5, 45, 75, 15);
    txtCalificacion.setBounds(120, 10, 315, 20);
    calificacionTxt.setBounds(5, 15, 75, 15);
    this.add(jPanel1, null);
    this.add(cmbCarpinteria, null);
    this.add(cmbCubierta,null);
    this.add(cmbMaterial, null);
    this.add(cmbEstado, null);
    this.add(cmbTipo, null);
    this.add(carpinteriaExteriorlbl, null);
    this.add(cubiertalbl, null);
    this.add(materiallbl, null);
    this.add(estadoConservacionlbl,null);
    this.add(tipoConstruccionTxt, null);
    this.add(jSeparator4, null);
    
    //this.add(txtFechaObra, null);
    this.add(fechaObra);
    
    this.add(fechaObraTxt, null);
    this.add(txtEdificabilidad,null);
    this.add(edificabilidadTxt, null);
    this.add(txtCalificacion, null);
    this.add(calificacionTxt, null);

  }

  private void btnAplicar_actionPerformed(ActionEvent e){
    ArrayList Valor= new ArrayList();
    ArrayList Tipo= new ArrayList();
    Valor.add(txtCalificacion.getText());
    Tipo.add("1");
    Valor.add(txtEdificabilidad.getText());
    Tipo.add("1");
    Valor.add(cmbTipo.getSelectedPatron()!=null?cmbTipo.getSelectedPatron():"0");
    Tipo.add("0");
    
    //Valor.add(txtFechaObra.getText());       
    if (fechaObra.getValue()!=null){
        Valor.add(fechaObra.getValue());
    }else
    {
        Valor.add("");
    }
    Tipo.add("1");
    
    
    Valor.add(cmbEstado.getSelectedPatron()!=null?cmbEstado.getSelectedPatron():"0");
    Tipo.add("0");
    Valor.add(cmbMaterial.getSelectedPatron()!=null?cmbMaterial.getSelectedPatron():"0");
    Tipo.add("0");
    Valor.add(cmbCubierta.getSelectedPatron()!=null?cmbCubierta.getSelectedPatron():"0");
    Tipo.add("0");
    Valor.add(cmbCarpinteria.getSelectedPatron()!=null?cmbCarpinteria.getSelectedPatron():"0");
    Tipo.add("0");
    //Actualizamos la información almacenada
     PatrimonioPostgre Construcciones = new PatrimonioPostgre();
     String Result = Construcciones.ActualizarConstruccion(ID_Inmueble, Valor, Tipo);
     //System.out.println(Result);
  }


  public void enter()
  {
      
	AppContext app =(AppContext) AppContext.getApplicationContext();
	Blackboard Identificadores = app.getBlackboard();
	ID_Inmueble = Integer.parseInt(Identificadores.get("IdInmueble").toString());
	      
	//Mostramos los datos para el inmueble
	PatrimonioPostgre Construccion = new PatrimonioPostgre();
    ArrayList Datos= Construccion.DatosConstruccion (ID_Inmueble);
    if (Datos==null)return;
     Iterator alIt = Datos.iterator();
     Iterator itControles = Cons.iterator();     
     while (itControles.hasNext()) 
      {
         try
        {
           JComponent comp=(JComponent)itControles.next();
           Object obj=alIt.next();
           if (comp instanceof JTextField)((JTextField)comp).setText((obj!=null)?obj.toString():"");
           if (comp instanceof JCheckBox){
              String check = (obj!=null)?obj.toString():"";
              if (check == "TRUE"){
                ((JCheckBox)comp).setSelected(true);}
              else{
                ((JCheckBox)comp).setSelected(false);}}
           
           
           if (comp instanceof DateField)
           {
               Date date = null;
               this.remove(fechaObra);
               
               if (obj!=null){
                   
                   Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                   fechaObra = new DateField ((Date)formatter.parseObject(obj.toString()), 15);
               }
               else{
                   
                   fechaObra = new DateField(date, 15);                   
               }
               fechaObra.setBounds(122, 70, 70, 20);
               this.add(fechaObra);
           
           }
           
           
           if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
           if (comp instanceof ComboBoxEstructuras )
           {        
           	((ComboBoxEstructuras)comp).setSelectedPatron((obj!=null)?obj.toString(): "");
           }
         }

		catch(Exception A)
		{
		    A.printStackTrace();
		}
     }
  }


  public void exit()
  {
  }
}