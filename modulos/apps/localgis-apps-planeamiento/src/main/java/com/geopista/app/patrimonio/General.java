/**
 * General.java
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
import javax.swing.JComponent;
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

//public class General extends FeatureExtendedPanel {
public class General extends  JPanel implements FeatureExtendedPanel {

  private JTextField txtRefPla = new JTextField();
  private JLabel lblRefPlano = new JLabel();
  private JTextField txtRefPar = new JTextField();
  private JLabel lblRefParcela = new JLabel();
  private JSeparator jSeparator3 = new JSeparator();
  private JSeparator jSeparator2 = new JSeparator();
  private JSeparator jSeparator1 = new JSeparator();
  //private JTextField txtSubparcela = new JTextField();
  private JTextField txtParcelaRustica = new JTextField();
  //private JLabel jLabel114 = new JLabel();
  private JLabel lblParcela1 = new JLabel();
  private JTextField txtPoligono = new JTextField();
  private JLabel lblPoligono = new JLabel();
  private JTextField txtParaje = new JTextField();
  private JLabel lblParaje = new JLabel();
  private JLabel lblAprovechamiento = new JLabel();
  private JTextField txtOeste = new JTextField();
  private JLabel lblOeste = new JLabel();
  private JLabel lblEste = new JLabel();
  private JTextField txtEste = new JTextField();
  private JTextField txtSur = new JTextField();
  private JLabel lblSur = new JLabel();
  private JLabel lblNorte = new JLabel();
  private JTextField txtNorte = new JTextField();
  private JTextField txtParcelaUrbana = new JTextField();
  private JLabel lblParcela = new JLabel();
  private JLabel lblManzana = new JLabel();
  private JTextField txtManzana = new JTextField();
  private JLabel lblDireccion = new JLabel();
  private JTextField txtDireccion = new JTextField();
  private JLabel lblCesionario = new JLabel();
  private JTextField txtCesionario = new JTextField();
  private JTextField txtCesion = new JTextField();
  private JLabel lblCesion = new JLabel();
  private JLabel lblPropiedad = new JLabel();
  private JLabel lblFormaAdquisicion = new JLabel();
  private JTextField txtFormaAd = new JTextField();
  private JTextField txtDestino = new JTextField();
  private JLabel lblDestino = new JLabel();
  private JTextField txtFechaAd = new JTextField();

  private JLabel lblFechaAdquisicion = new JLabel();
  private JPanel jPanel1 = new JPanel();
  private JButton btnAplicar = new JButton();
  //private JButton btnNuevo = new JButton();
  private JButton btnEliminar = new JButton();
  ArrayList ControlesGenerales = new ArrayList();
  private JCheckBox chkUrbana = new JCheckBox();
  private JCheckBox chkRusticos = new JCheckBox();
  //private JComboBox cmbPropiedad = new JComboBox();
  
  private ComboBoxEstructuras cmbPropiedad;  
  private ComboBoxEstructuras cmbAprovechamiento;
  
  private Date date=null;  
  private DateField inst = new DateField(date, 15);
 
  private boolean alta=false;
  private int ID_Parcela;
  private int ID_Inmueble;
  
  public final int URBANO  = 1;
  public final int RUSTICO = 2;
  
  AppContext app =(AppContext) AppContext.getApplicationContext();
  
  public General()
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
    this.setName(app.getI18nString("patrimonio.general.titulo"));
    this.setSize(new Dimension (710, 477));
    this.setBounds(new Rectangle(5, 10, 630, 400));
    this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setLayout(null);
    
    
    
    
    cmbPropiedad = new ComboBoxEstructuras(Estructuras.getListaTiposPropiedad(), null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    cmbPropiedad.setSelectedIndex(0);
    
    cmbAprovechamiento = new ComboBoxEstructuras(Estructuras.getListaTiposAprovechamiento(), null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    cmbAprovechamiento.setSelectedIndex(0);
    
       
    ControlesGenerales.add(txtDestino);
    //ControlesGenerales.add(txtFechaAd);
    
    //ControlesGenerales.add(frameFecha);
    ControlesGenerales.add(inst);
    
    ControlesGenerales.add(txtFormaAd);
    ControlesGenerales.add(cmbPropiedad);
    ControlesGenerales.add(txtCesion);
    ControlesGenerales.add(txtCesionario);
    ControlesGenerales.add(txtDireccion);
    ControlesGenerales.add(txtNorte);
    ControlesGenerales.add(txtSur);
    ControlesGenerales.add(txtEste);
    ControlesGenerales.add(txtOeste);
    ControlesGenerales.add(txtRefPar);
    ControlesGenerales.add(txtRefPla);   
    ControlesGenerales.add(chkRusticos); 
    ControlesGenerales.add(chkUrbana); 
    
    txtRefPla.setHorizontalAlignment(JTextField.LEFT);
    lblRefPlano.setText(app.getI18nString("patrimonio.general.refplano"));
    lblRefParcela.setText(app.getI18nString("patrimonio.general.refparcela"));
    //jLabel114.setText("SubParcela");
    lblParcela1.setText(app.getI18nString("patrimonio.general.parcela"));
    lblPoligono.setText(app.getI18nString("patrimonio.general.poligono"));
    lblParaje.setText(app.getI18nString("patrimonio.general.paraje"));
    lblAprovechamiento.setText(app.getI18nString("patrimonio.general.aprovechamiento"));
    lblOeste.setText(app.getI18nString("patrimonio.general.oeste"));
    lblEste.setText(app.getI18nString("patrimonio.general.este"));
    lblSur.setText(app.getI18nString("patrimonio.general.sur"));
    lblNorte.setText(app.getI18nString("patrimonio.general.norte"));
    lblParcela.setText(app.getI18nString("patrimonio.general.parcela"));
    lblManzana.setText(app.getI18nString("patrimonio.general.manzana"));
    lblDireccion.setText(app.getI18nString("patrimonio.general.direccion"));
    lblCesionario.setText(app.getI18nString("patrimonio.general.cesionario"));
    txtCesionario.setText("");
    lblCesion.setText(app.getI18nString("patrimonio.general.cesion"));
    
    lblPropiedad.setText(app.getI18nString("patrimonio.general.propiedad"));
    lblFormaAdquisicion.setText(app.getI18nString("patrimonio.general.formaadquisicion"));
    txtFormaAd.setText("");
    txtDestino.setText("");
    lblDestino.setText(app.getI18nString("patrimonio.general.destino"));
    
    
    //txtFechaAd.setText("");
    lblFechaAdquisicion.setText(app.getI18nString("patrimonio.general.fechaadquisicion"));
    btnAplicar.setText(app.getI18nString("patrimonio.aplicar"));
    btnAplicar.addActionListener(new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
          btnAplicar_actionPerformed(e);
        }
      });
   
    jPanel1.add(btnAplicar, null);
   
    
    chkRusticos.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    chkRusticos_actionPerformed(e);
                }
              });
    
    chkUrbana.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    chkUrbana_actionPerformed(e);
                }
              });
    
    cmbAprovechamiento.setBounds(225, 205, 290, 20);
    cmbPropiedad.setBounds(115, 330, 400, 20);
    chkRusticos.setBounds(5, 175, 80, 30);
    chkRusticos.setText (app.getI18nString("patrimonio.general.rustico"));
    chkUrbana.setBounds(5, 120, 80, 30);
    chkUrbana.setText(app.getI18nString("patrimonio.general.urbano"));    
   
    jPanel1.setBounds(260, 410, 100, 35);
    txtRefPla.setBounds(435, 85, 80, 20);    
    lblRefPlano.setBounds(310, 85, 115, 20);
    txtRefPar.setBounds(115, 85, 155, 20);    
    lblRefParcela.setBounds(5, 85, 115, 20);
    jSeparator3.setBounds(0, 230, 520, 5);
    jSeparator2.setBounds(0, 145, 520, 5);
    jSeparator1.setBounds(0, 110, 520, 5);
    //txtSubparcela.setBounds(515, 155, 75, 20);    
    //jLabel114.setBounds(440, 160, 70, 15);
    txtPoligono.setBounds(225, 155, 70, 20);    
    lblPoligono.setBounds(115, 155, 55, 15);    
    txtParaje.setBounds(225, 180, 290, 20);    
    lblParaje.setBounds(115, 180, 45, 15);
    lblAprovechamiento.setBounds(115, 205, 110, 15);
    txtOeste.setBounds(115, 305, 400, 20);
    lblOeste.setBounds(5, 315, 115, 15);
    lblEste.setBounds(5, 290, 115, 15);
    txtEste.setBounds(115, 280, 400, 20);
    txtSur.setBounds(115, 255, 400, 20);
    lblSur.setBounds(5, 265, 115, 15);
    lblNorte.setBounds(5, 240, 115, 15);
    txtNorte.setBounds(115, 235, 400, 20);
    txtParcelaUrbana.setBounds(435, 120, 80, 20);   
    txtParcelaRustica.setBounds(435, 155, 80, 20); 
    lblParcela1.setBounds(355, 160, 55, 15); 
    lblParcela.setBounds(355, 125, 50, 15);
    lblManzana.setBounds(115, 125, 60, 15);
    txtManzana.setBounds(225, 120, 70, 20);    
    lblDireccion.setBounds(5, 65, 100, 15);
    txtDireccion.setBounds(115, 60, 400, 20);
    lblCesionario.setBounds(5, 385, 100, 15);
    txtCesionario.setBounds(115, 380, 400, 20);
    txtCesion.setBounds(115, 355, 400, 20);
    lblCesion.setBounds(5, 360, 75, 15);
    lblPropiedad.setBounds(5, 335, 100, 15);
    lblFormaAdquisicion.setBounds(5, 40, 115, 15);
    txtFormaAd.setBounds(115, 35, 400, 20);
    txtDestino.setBounds(265, 5, 250, 20);
    lblDestino.setBounds(195, 10, 50, 15);
    inst.setBounds(115, 7, 70, 20);
    lblFechaAdquisicion.setBounds(5, 10, 105, 15);
    
    deshabilitaFijos();
            
    
    this.add(cmbAprovechamiento, null);
    this.add(cmbPropiedad, null);
    this.add(chkRusticos, null);
    this.add(chkUrbana, null);
    this.add(jPanel1, null);
    this.add(txtRefPla, null);
    this.add(lblRefPlano, null);
    this.add(txtRefPar, null);
    this.add(lblRefParcela, null);
    this.add(jSeparator3, null);
    this.add(jSeparator2, null);
    this.add(jSeparator1, null);
    //this.add(txtSubparcela, null);
    this.add(txtParcelaRustica, null);
    //this.add(jLabel114, null);
    this.add(lblParcela1, null);
    this.add(txtPoligono, null);
    this.add(lblPoligono, null);
    this.add(txtParaje, null);
    this.add(lblParaje, null);
    this.add(lblAprovechamiento, null);
    this.add(txtOeste, null);
    this.add(lblOeste, null);
    this.add(lblEste, null);
    this.add(txtEste, null);
    this.add(txtSur, null);
    this.add(lblSur, null);
    this.add(lblNorte, null);
    this.add(txtNorte, null);
    this.add(txtParcelaUrbana, null);
    this.add(lblParcela, null);
    this.add(lblManzana, null);
    this.add(txtManzana, null);
    this.add(lblDireccion, null);
    this.add(txtDireccion, null);
    this.add(lblCesionario, null);
    this.add(txtCesionario, null);
    this.add(txtCesion, null);
    this.add(lblCesion, null);
    this.add(lblPropiedad, null);
    this.add(lblFormaAdquisicion, null);
    this.add(txtFormaAd, null);
    this.add(txtDestino, null);
    this.add(lblDestino, null);
    this.add(inst, null);
    
    this.add(lblFechaAdquisicion, null);
    this.add(chkRusticos, null);
    this.add(chkUrbana, null);

  }

 

  public void enter()
  {    
    AppContext app =(AppContext) AppContext.getApplicationContext();
 	Blackboard Identificadores = app.getBlackboard();
	String idst = Identificadores.get("IdInmueble").toString();
   	if (!idst.equals("")){
   	 ID_Inmueble = Integer.parseInt(idst);
   	}
   		
	Iterator itControles = ControlesGenerales.iterator(); 
		
	//Mostramos los datos para el inmueble
	PatrimonioPostgre General = new PatrimonioPostgre();
	ArrayList Datos= General.DatosGenerales(ID_Inmueble);
		
	if (Datos==null)return;
	Iterator alIt = Datos.iterator();
	   
    while (itControles.hasNext()) 
      {
         try
        {
            
           JComponent comp=(JComponent)itControles.next();
           Object obj=alIt.next();
           if (comp instanceof JTextField)
               {
               
               	((JTextField)comp).setText((obj!=null)?obj.toString():"");
               }
           if (comp instanceof DateField)
           {
               Date date = null;
               this.remove(inst);
               
               if (obj!=null){
                   Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                   inst = new DateField ((Date)formatter.parseObject(obj.toString()), 15);
                   
               }
               else{                   
                   inst = new DateField(date, 15);
               }
               inst.setBounds(115, 7, 70, 20);
               this.add(inst);
               
           }
           
       
           if (comp instanceof JCheckBox){
               
               String check = (obj!=null)?obj.toString():"";
              if (check == "TRUE"){
                ((JCheckBox)comp).setSelected(true);}
              else{
                ((JCheckBox)comp).setSelected(false);}}
         /*if (comp instanceof JComboBox)
             {            
             	((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
             }
         */
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

     if ((alIt.hasNext())){
         if (Integer.parseInt(alIt.next().toString())==URBANO)
         {
             Object obj= alIt.next();
             txtManzana.setText((obj!=null)?obj.toString():"");    
             obj= alIt.next();
             txtParcelaUrbana.setText((obj!=null)?obj.toString():""); 
             
             txtPoligono.setText("");
             txtParcelaRustica.setText("");
             //txtSubparcela.setText("");
             txtParaje.setText("");             
             cmbAprovechamiento.setSelectedIndex(0);
             
             //txtParaje.setEnabled(false);
             //cmbAprovechamiento.setEnabled(false);
             deshabilitaRusticos();
             habilitaUrbanos();
             
             /*
             deshabilitaRusticos();
             habilitaUrbanos();
             deshabilitaFijos();
             */
             
             
             
         }
	     //else if (alIt.next().toString().equals("Rústico"))
         else
	     {
          Object obj= alIt.next();  
	      txtPoligono.setText((obj!=null)?obj.toString():"");
	      obj= alIt.next();  
	      txtParcelaRustica.setText((obj!=null)?obj.toString():"");
	      obj= alIt.next();  
	      //txtSubparcela.setText((obj!=null)?obj.toString():"");
	      obj= alIt.next();  
	      txtParaje.setText((obj!=null)?obj.toString():"");
	      obj= alIt.next();  
	      cmbAprovechamiento.setSelectedPatron((obj!=null)?obj.toString():"");
	      
	      txtManzana.setText("");
	      txtParcelaUrbana.setText("");
	      
	      deshabilitaUrbanos();
	      habilitaRusticos();
	      
	      /*
	      deshabilitaUrbanos();
	      habilitaRusticos();
	      deshabilitaFijos();
	      */
	     }
        }
     else{
         
     }
  }


  public void exit()
  {
      // AppContext app =(AppContext) AppContext.getApplicationContext();
      //    Blackboard Identificadores = app.getBlackboard();
      //Identificadores.put ("ID_Bien", ID_Bien);
  }

  private void btnAplicar_actionPerformed(ActionEvent e)
  {
  //Aplicamos los cambios

    ArrayList Valor= new ArrayList();
    ArrayList Tipo= new ArrayList();
    boolean  Urbana=false;  
    Valor.add(String.valueOf(ID_Inmueble));
    Tipo.add("0");
    Valor.add(txtDestino.getText());
    Tipo.add("1");
    
    /*String s= null;
    try{
	    Format formatter;
	    formatter = new SimpleDateFormat("dd-MM-yy");
	    s = formatter.format(txtFechaAd.getText());
    }catch(Exception ex){
        System.out.println ("no se ha podido formatear la fecha " +txtFechaAd.getText());
        s="";
    }*/
    
    if (inst.getValue()!=null){
        Valor.add(inst.getValue());
    }else
    {
        Valor.add("");
    }
    //Valor.add(txtFechaAd.getText());
    Tipo.add("1");
    Valor.add(txtFormaAd.getText());
    Tipo.add("1");
    Valor.add(cmbPropiedad.getSelectedPatron()!=null?cmbPropiedad.getSelectedPatron():"0");
    Tipo.add("0");
    Valor.add(txtCesion.getText());
    Tipo.add("1");
    Valor.add(txtCesionario.getText());
    Tipo.add("1");
    Valor.add(txtDireccion.getText());
    Tipo.add("1");
    Valor.add(txtNorte.getText());
    Tipo.add("1");
    Valor.add(txtSur.getText());
    Tipo.add("1");
    Valor.add(txtEste.getText());
    Tipo.add("1");
    Valor.add(txtOeste.getText());
    Tipo.add("1");
    Valor.add(txtRefPar.getText());
    Tipo.add("1");
    Valor.add(txtRefPla.getText());
    Tipo.add("1");
    
    if (chkUrbana.isSelected()) 
    { 
         Valor.add(txtManzana.getText());
         Tipo.add("0");
         Valor.add(txtParcelaUrbana.getText());
         Tipo.add("0");
         Urbana=true;
         
         deshabilitaRusticos();
         //chkUrbana.setEnabled(true);
        
          
         
    }else
    {
         Valor.add(txtPoligono.getText());
         Tipo.add("1");
         Valor.add(txtParcelaRustica.getText());
         Tipo.add("1");
         
         //se elimina la subparcela del panel
         //Valor.add(txtSubparcela.getText());
         Valor.add("");
         Tipo.add("1");
         Valor.add(txtParaje.getText());
         Tipo.add("1");
         //Valor.add(cmbAprovechamiento.getSelectedItem().toString());
         Valor.add(cmbAprovechamiento.getSelectedPatron()!=null?cmbAprovechamiento.getSelectedPatron():"0");
         Tipo.add("0");
         Urbana=false;
         
         deshabilitaUrbanos();
         //chkRusticos.setEnabled(true);
       
    }

    //Actualizamos la información almacenada
    PatrimonioPostgre General = new PatrimonioPostgre();
	if (alta==false){
		String Result = General.ActualizarGeneral(ID_Inmueble, Valor, Tipo, Urbana);
		//System.out.println(Result);
	}
	else{
		ID_Inmueble = General.AltaGeneral(Valor, Tipo, Urbana);
		//System.out.println(ID_Inmueble);
		alta=false;
	}
  }

 
  
  private void chkRusticos_actionPerformed(ActionEvent e)
  {
      
      deshabilitaUrbanos();
      habilitaRusticos();
      deshabilitaFijos();
  
     
  }
  private void chkUrbana_actionPerformed(ActionEvent e)
  {
      
      deshabilitaRusticos();
      habilitaUrbanos();
      deshabilitaFijos();
      
     
  }
  
  private void deshabilitaRusticos()
  {
     
      txtParaje.setEnabled(false);
      cmbAprovechamiento.setEnabled(false);
      
      
      //Paraje
      lblParaje.setForeground(Color.GRAY);
      //Subparcela
      //jLabel114.setForeground(Color.GRAY);
      //Poligono
      lblPoligono.setForeground(Color.GRAY);
      //Parcela rustica
      lblParcela1.setForeground(Color.GRAY);
      //Aprovechamiento
      lblAprovechamiento.setForeground(Color.GRAY);
      //Rustica
      chkRusticos.setForeground(Color.GRAY);
      
      
      
  }
  
  private void deshabilitaUrbanos()
  {
     
      lblManzana.setForeground(Color.GRAY);
      lblParcela.setForeground(Color.GRAY);
      chkUrbana.setForeground(Color.GRAY);

  }
  private void habilitaRusticos()
  {
      chkUrbana.setSelected(false);
      
     
      txtParaje.setEnabled(true);
      cmbAprovechamiento.setEnabled(true);
      
      
      //Paraje
      lblParaje.setForeground(Color.BLACK);
      //Subparcela
      //jLabel114.setForeground(Color.BLACK);
      //Poligono
      lblPoligono.setForeground(Color.BLACK);
      //Parcela rustica
      lblParcela1.setForeground(Color.BLACK);
      //Aprovechamiento
      lblAprovechamiento.setForeground(Color.BLACK);
      //Rustica
      //chkRusticos.setForeground(Color.BLACK);
      
      
  }
  
  private void habilitaUrbanos()
  {
      //chkRusticos.setSelected(false);
      
      //txtManzana.setEnabled(true);
      lblManzana.setForeground(Color.BLACK);
      //txtParcelaUrbana.setEnabled(true);
      lblParcela.setForeground(Color.BLACK);
      //chkUrbana.setForeground(Color.BLACK);
      
     

  }
  
  
  private void deshabilitaFijos()
  {
      txtRefPar.setEnabled(false);
      txtRefPla.setEnabled(false);
      //txtSubparcela.setEnabled(false);
      txtParcelaRustica.setEnabled(false);
      txtPoligono.setEnabled(false);
      txtParcelaUrbana.setEnabled(false);
      txtManzana.setEnabled(false);
      //txtParaje.setEnabled(false);
      chkRusticos.setEnabled(false);
      chkUrbana.setEnabled(false);
      //cmbAprovechamiento.setEnabled(false);
  }

}