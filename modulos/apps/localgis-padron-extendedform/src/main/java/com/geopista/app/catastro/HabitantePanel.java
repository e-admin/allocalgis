/**
 * HabitantePanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 13-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.geopista.app.catastro;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.ui.components.DateField;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

/**
 * @author hgarcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HabitantePanel extends  JPanel implements FeatureExtendedPanel
{

  //Datos de identificación del titular
  private JLabel lblProvincia = new JLabel();
  private JLabel lblMunicipio = new JLabel();
  private JLabel lblNombre = new JLabel();
  private JLabel lblSexo = new JLabel();
  private JLabel lblParticulaPrimerApellido = new JLabel();
  private JLabel lblPrimerApellido = new JLabel();
  private JLabel lblParticulaSegundoApellido = new JLabel();
  private JLabel lblSegundoApellido = new JLabel();
  
  private JPanel jPanelNacimiento = new JPanel();
  private JPanel jPanelHabitante = new JPanel();
  private JPanel jPanelModificacion = new JPanel();
  private JLabel lblProvinciaNacimiento = new JLabel();
  private JLabel lblMunicipioNacimiento = new JLabel(); 
  private JLabel lblFechaNacimiento = new JLabel();  
  private Date date = null;  
  private DateField fechaNacimiento = new DateField(date, 15);
  private JTextField txtFechaNacimiento = new JTextField();
  
  private JLabel lblTipoIdentificador = new JLabel();
  private JLabel lblLetraDocumentoExtranjeros = new JLabel();
  private JLabel lblIdentificador = new JLabel();
  private JLabel lblCodigoControlIdentificador = new JLabel();
  private JLabel lblNumeroDocumento = new JLabel();
  private JLabel lblNumeroHojaPadronal = new JLabel();
  private JLabel lblNIE = new JLabel();
  private JLabel lblTipoInformacion = new JLabel();
  private JLabel lblCausaDevolucion = new JLabel();
  private JLabel lblFechaVariacion = new JLabel();
  private JLabel lblCodigoVariacion = new JLabel();
  private JLabel lblCausaVariacion = new JLabel();
  
  
  private JTextField txtProvincia = new JTextField();
  private JTextField txtMunicipio = new JTextField();
  private JTextField txtNombre = new JTextField();
  private JComboBox cmbSexo = new JComboBox();
  private JTextField txtParticulaPrimerApellido = new JTextField();
  private JTextField txtPrimerApellido = new JTextField();
  private JTextField txtParticulaSegundoApellido = new JTextField();
  private JTextField txtSegundoApellido = new JTextField();
  
  private JTextField txtProvinciaNacimiento = new JTextField();
  private JTextField txtMunicipioNacimiento = new JTextField();
  
  private JComboBox cmbTipoIdentificador = new JComboBox();
  private JTextField txtLetraDocumentoExtranjeros = new JTextField();
  private JTextField txtIdentificador = new JTextField();
  private JTextField txtCodigoControlIdentificador = new JTextField();
  private JTextField txtNumeroDocumento = new JTextField();
  private JTextField txtNumeroHojaPadronal = new JTextField();
  private JTextField txtNIE = new JTextField();
  private JTextField txtTipoInformacion = new JTextField();
  private JTextField txtCausaDevolucion = new JTextField();
  private DateField fechaVariacion = new DateField(date, 15);
  private JComboBox cmbCodigoVariacion = new JComboBox();
  private JComboBox cmbCausaVariacion = new JComboBox();
   
  
  private JLabel lblNivelEstudios = new JLabel();
  private JLabel lblCodigoNacionalidad = new JLabel();  
  private JTextField txtNivelEstudios = new JTextField();
  private JTextField txtCodigoNacionalidad = new JTextField();
  
  
  //Otras variables
  private ArrayList lstHabitantes= new ArrayList();
  ArrayList lstDatos = new ArrayList();
   
  
  public HabitantePanel(FeatureDialogHome fd)
  {
    try
    {
      jbInit();
      if (fd instanceof FeatureDialog)
      {
          ((FeatureDialog)fd).setSize(new Dimension(1200, 1200));
          
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }
  

  private void jbInit() throws Exception
  {
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    this.setName(aplicacion.getI18nString("habitantes.panel.personales"));
    this.setLayout(null);
    this.setSize(new Dimension(600, 550));
    this.setBounds(new java.awt.Rectangle(5,10,636,493)); 
    
    jPanelHabitante.setBounds(new Rectangle(8, 5, 487, 365));
    jPanelHabitante.setLayout(null);
    jPanelHabitante.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("habitantes.panel.habitante")));
    
    
    lblProvincia.setText(aplicacion.getI18nString("habitantes.panel.provincia"));
    lblProvincia.setBounds(new Rectangle(10, 19, 78, 15));
    txtProvincia.setText("");
    txtProvincia.setBounds(new Rectangle(123, 19, 60, 20));
    
    lblMunicipio.setText(aplicacion.getI18nString("habitantes.panel.municipio"));
    lblMunicipio.setBounds(new Rectangle(193, 19, 78, 15));
    txtMunicipio.setText("");
    txtMunicipio.setBounds(new Rectangle(276, 19, 90, 20));
    
    lblNombre.setText(aplicacion.getI18nString("habitantes.panel.nombre"));
    lblNombre.setBounds(new Rectangle(10, 44, 78, 15));
    txtNombre.setText("");
    txtNombre.setBounds(new Rectangle(123, 44, 153, 20));
    
    lblSexo.setText(aplicacion.getI18nString("habitantes.panel.sexo"));
    lblSexo.setBounds(new Rectangle(286, 44, 78, 15));
    cmbSexo.addItem(aplicacion.getI18nString("habitantes.panel.sexo.mujer"));
    cmbSexo.addItem(aplicacion.getI18nString("habitantes.panel.sexo.varon"));
    cmbSexo.setBounds(new Rectangle(348, 44, 90, 20));
    
    lblParticulaPrimerApellido.setText(aplicacion.getI18nString("habitantes.panel.primerapellido"));
    lblParticulaPrimerApellido.setBounds(new Rectangle(10, 69, 90, 15));
    txtParticulaPrimerApellido.setText("");
    txtParticulaPrimerApellido.setBounds(new Rectangle(123, 69, 60, 20));
    txtPrimerApellido.setText("");
    txtPrimerApellido.setBounds(new Rectangle(188, 69, 250, 20));
    
    lblParticulaSegundoApellido.setText(aplicacion.getI18nString("habitantes.panel.segundoapellido"));
    lblParticulaSegundoApellido.setBounds(new Rectangle(10, 94, 90, 15));
    txtParticulaSegundoApellido.setText("");
    txtParticulaSegundoApellido.setBounds(new Rectangle(123, 94, 60, 20));
    txtSegundoApellido.setText("");
    txtSegundoApellido.setBounds(new Rectangle(188, 94, 250, 20));
    
    jPanelNacimiento.setBounds(new Rectangle(8, 120, 470, 75));
    jPanelNacimiento.setLayout(null);
    jPanelNacimiento.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("habitantes.panel.nacimiento")));
    
    lblProvinciaNacimiento.setText(aplicacion.getI18nString("habitantes.panel.provincia"));
    lblProvinciaNacimiento.setBounds(new Rectangle(7, 20, 78, 15));
    txtProvinciaNacimiento.setText("");
    txtProvinciaNacimiento.setBounds(new Rectangle(120, 20, 60, 20));
    
    lblMunicipioNacimiento.setText(aplicacion.getI18nString("habitantes.panel.municipio"));
    lblMunicipioNacimiento.setBounds(new Rectangle(190, 20, 78, 15));
    txtMunicipioNacimiento.setText("");
    txtMunicipioNacimiento.setBounds(new Rectangle(273, 20, 90, 20));
    lblFechaNacimiento.setText(aplicacion.getI18nString("habitantes.panel.fechanacimiento"));
    lblFechaNacimiento.setBounds(new Rectangle(7, 48, 100, 20));
    //fechaNacimiento.setBounds(120, 48, 70, 20);
    txtFechaNacimiento.setText("");
    txtFechaNacimiento.setBounds(120, 48, 70, 20);
    
    lblTipoIdentificador.setText(aplicacion.getI18nString("habitantes.panel.tipoidentificador"));
    lblTipoIdentificador.setBounds(new Rectangle(10, 200, 118, 15));
    cmbTipoIdentificador.setBounds(new Rectangle(123, 200, 140, 20));
    
    //TODO Dominios
    cmbTipoIdentificador.addItem(aplicacion.getI18nString("habitantes.panel.identificador.sindocumento"));
    cmbTipoIdentificador.addItem(aplicacion.getI18nString("habitantes.panel.identificador.dni"));
    cmbTipoIdentificador.addItem(aplicacion.getI18nString("habitantes.panel.identificador.pasaporte"));
    cmbTipoIdentificador.addItem(aplicacion.getI18nString("habitantes.panel.identificador.tarjeta"));
    cmbTipoIdentificador.setSelectedIndex(-1);
    
    lblLetraDocumentoExtranjeros.setText(aplicacion.getI18nString("habitantes.panel.letra"));
    lblLetraDocumentoExtranjeros.setBounds(new Rectangle(273, 200, 160, 20));
    txtLetraDocumentoExtranjeros.setText("");
    txtLetraDocumentoExtranjeros.setBounds(new Rectangle(438, 200, 20, 20));
    
    lblIdentificador.setText(aplicacion.getI18nString("habitantes.panel.identificador"));
    lblIdentificador.setBounds(new Rectangle(10, 228, 70, 20));
    txtIdentificador.setText("");
    txtIdentificador.setBounds(new Rectangle(123, 228, 80, 20));
    
    lblCodigoControlIdentificador.setText(aplicacion.getI18nString("habitantes.panel.codigocontrol"));
    lblCodigoControlIdentificador.setBounds(new Rectangle(273, 228, 100, 20));
    txtCodigoControlIdentificador.setText("");
    txtCodigoControlIdentificador.setBounds(new Rectangle(378, 228, 20, 20));
    
    lblNumeroDocumento.setText(aplicacion.getI18nString("habitantes.panel.numerodocumento"));
    lblNumeroDocumento.setBounds(new Rectangle(10, 254, 110, 20));
    txtNumeroDocumento.setText("");
    txtNumeroDocumento.setBounds(new Rectangle(123, 254, 200, 20));
    
    
    lblNumeroHojaPadronal.setText(aplicacion.getI18nString("habitantes.panel.numerohoja"));
    lblNumeroHojaPadronal.setBounds(new Rectangle(10, 282, 215, 20));
    txtNumeroHojaPadronal.setText("");
    txtNumeroHojaPadronal.setBounds(new Rectangle(230, 282, 200, 20));
    
    lblNIE.setText(aplicacion.getI18nString("habitantes.panel.nie"));
    lblNIE.setBounds(new Rectangle(10, 310, 215, 20));
    txtNIE.setText("");
    txtNIE.setBounds(new Rectangle(230, 310, 200, 20));
    
    lblNivelEstudios.setText(aplicacion.getI18nString("habitantes.panel.codigoocupacion"));
    lblNivelEstudios.setBounds(new Rectangle(10, 338, 100, 15));
    txtNivelEstudios.setText("");
    txtNivelEstudios.setBounds(new Rectangle(115, 338, 30, 20));
    lblCodigoNacionalidad.setText(aplicacion.getI18nString("habitantes.panel.codigonacionalidad"));
    lblCodigoNacionalidad.setBounds(new Rectangle(165, 338, 120, 15));
    txtCodigoNacionalidad.setText("");
    txtCodigoNacionalidad.setBounds(new Rectangle(290, 338, 40, 20));
       
    /*   
    
    jPanelModificacion.setBounds(new Rectangle(8, 343, 487, 80));
    jPanelModificacion.setLayout(null);
    jPanelModificacion.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("habitantes.panel.modificacion")));
       
    lblTipoInformacion.setText("Tipo de información");
    lblTipoInformacion.setBounds(new Rectangle(10, 24, 110, 20));
    txtTipoInformacion.setText("");
    txtTipoInformacion.setBounds(new Rectangle(125, 24, 20, 20));
    
    lblCausaDevolucion.setText("Causa de devolución");
    lblCausaDevolucion.setBounds(new Rectangle(155, 24, 105, 20));
    txtCausaDevolucion.setText("");
    txtCausaDevolucion.setBounds(new Rectangle(265, 24, 30, 20));
    
    lblFechaVariacion.setText("Fecha de variación");
    lblFechaVariacion.setBounds(new Rectangle(305, 24, 100, 20));
    fechaVariacion.setBounds(new Rectangle(410, 24, 70, 20));
    
    
    lblCodigoVariacion.setText("Código de variación");
    lblCodigoVariacion.setBounds(new Rectangle(10, 52, 110, 15));
    cmbCodigoVariacion.setBounds(new Rectangle(125, 52, 80, 20));
    
    //TODO Dominios
    cmbCodigoVariacion.addItem("Alta");
    cmbCodigoVariacion.addItem("Baja");
    cmbCodigoVariacion.addItem("Modificacion");
    cmbCodigoVariacion.setSelectedIndex(-1);
    
    lblCausaVariacion.setText("Causa de variación");
    lblCausaVariacion.setBounds(new Rectangle(215, 52, 100, 15));
    cmbCausaVariacion.setBounds(new Rectangle(320, 52, 160, 20));
    
    //TODO Dominios
    cmbCausaVariacion.addItem("alta por omision");
    cmbCausaVariacion.addItem("alta por cambio de residencia");
    cmbCausaVariacion.addItem("alta por nacimiento");
    cmbCausaVariacion.addItem("baja por defuncion");
    cmbCausaVariacion.addItem("baja por inclusion indebida");
    cmbCausaVariacion.addItem("baja por duplicado");
    cmbCausaVariacion.addItem("baja por cambio de residencia");
    cmbCausaVariacion.addItem("modificacion de datos personales");
    cmbCausaVariacion.addItem("rectificadion por modificaciones territoriales");
    cmbCausaVariacion.addItem("cambio de domicilio");
    cmbCausaVariacion.setSelectedIndex(-1);
    
    
        
    jPanelModificacion.add(lblTipoInformacion, null);
    jPanelModificacion.add(txtTipoInformacion, null);
    jPanelModificacion.add(lblCausaDevolucion, null);    
    jPanelModificacion.add(txtCausaDevolucion, null);  
    jPanelModificacion.add(lblFechaVariacion, null);  
    jPanelModificacion.add(fechaVariacion, null);  
    jPanelModificacion.add(lblCodigoVariacion, null);  
    jPanelModificacion.add(cmbCodigoVariacion, null);  
    jPanelModificacion.add(lblCausaVariacion, null);  
    jPanelModificacion.add(cmbCausaVariacion, null);  
    
    */
       
       
       
    jPanelNacimiento.add(lblProvinciaNacimiento, null);
    jPanelNacimiento.add(lblMunicipioNacimiento, null);
    jPanelNacimiento.add(txtProvinciaNacimiento, null);
    jPanelNacimiento.add(txtMunicipioNacimiento, null);
    jPanelNacimiento.add(lblFechaNacimiento, null);
    jPanelNacimiento.add(fechaNacimiento, null);
    jPanelNacimiento.add(txtFechaNacimiento, null);
    
    
    jPanelHabitante.add(lblProvincia, null);
    jPanelHabitante.add(txtProvincia, null);
    jPanelHabitante.add(lblMunicipio, null);
    jPanelHabitante.add(txtMunicipio, null);
    jPanelHabitante.add(lblNombre, null);
    jPanelHabitante.add(txtNombre, null);
    jPanelHabitante.add(lblSexo, null);
    jPanelHabitante.add(cmbSexo, null);
    jPanelHabitante.add(lblParticulaPrimerApellido, null);
    jPanelHabitante.add(txtParticulaPrimerApellido, null);
    jPanelHabitante.add(txtPrimerApellido, null);
    jPanelHabitante.add(lblParticulaSegundoApellido, null);
    jPanelHabitante.add(txtParticulaSegundoApellido, null);
    jPanelHabitante.add(txtSegundoApellido, null);
    jPanelHabitante.add(jPanelNacimiento, null);
    jPanelHabitante.add(lblTipoIdentificador, null);
    jPanelHabitante.add(cmbTipoIdentificador, null);
    jPanelHabitante.add(lblLetraDocumentoExtranjeros, null);
    jPanelHabitante.add(txtLetraDocumentoExtranjeros, null);
    jPanelHabitante.add(lblIdentificador, null);
    jPanelHabitante.add(txtIdentificador, null);
    jPanelHabitante.add(lblCodigoControlIdentificador, null);
    jPanelHabitante.add(txtCodigoControlIdentificador, null);
    jPanelHabitante.add(lblNumeroDocumento, null);
    jPanelHabitante.add(txtNumeroDocumento, null);
    jPanelHabitante.add(lblNumeroHojaPadronal, null);
    jPanelHabitante.add(txtNumeroHojaPadronal, null);
    jPanelHabitante.add(lblNIE, null);
    jPanelHabitante.add(txtNIE, null);    
    jPanelHabitante.add(lblNivelEstudios, null);
    jPanelHabitante.add(txtNivelEstudios, null);
    jPanelHabitante.add(lblCodigoNacionalidad, null);
    jPanelHabitante.add(txtCodigoNacionalidad, null);    
    
    this.add(jPanelHabitante);
    
    //Se elimina de momento porque el panel unicamente va a servir para mostrar los datos
    //this.add(jPanelModificacion);
    
    /*
    this.add(btnBorrar, null);
    this.add(btnNuevo, null);
    this.add(btnModificar, null);
    */
    
    
    //lstDatos.add(txtProvincia);
    //lstDatos.add(txtMunicipio);
    lstDatos.add(txtNombre);
    lstDatos.add(cmbSexo);
    lstDatos.add(txtParticulaPrimerApellido);
    lstDatos.add(txtPrimerApellido);
    lstDatos.add(txtParticulaSegundoApellido);
    lstDatos.add(txtSegundoApellido);
    
    lstDatos.add(txtProvinciaNacimiento);
    lstDatos.add(txtMunicipioNacimiento);
    lstDatos.add(txtFechaNacimiento);
    
    lstDatos.add(cmbTipoIdentificador);
    
    lstDatos.add(txtIdentificador);
    lstDatos.add(txtCodigoControlIdentificador);
    
    lstDatos.add(txtNumeroDocumento);
    lstDatos.add(txtLetraDocumentoExtranjeros);
    
    lstDatos.add(txtNumeroHojaPadronal);
    lstDatos.add(txtNivelEstudios);
    
    
   
  }   

 public void enter()
  {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
       
    int idHabitante= Integer.parseInt(Identificadores.get("ID_Habitante").toString());
    
    //Se extraen los datos del habitante
	PadronHabitantesPostgre consultasBD = new PadronHabitantesPostgre();
    
	//  - Municipio y provincia
	Hashtable poblacion= consultasBD.DatosPoblacion(idHabitante);
	if (poblacion==null)return;
	
	String codMunicipio= poblacion.get("CodMunicipio").toString();
	String codProvincia= poblacion.get("CodProvincia").toString();
	
	txtMunicipio.setText(codMunicipio);
	txtProvincia.setText(codProvincia);
	txtMunicipio.setEnabled(false);
	txtProvincia.setEnabled(false);
	
	Identificadores.put("Id_Municipio", codMunicipio);
	Identificadores.put("Id_Provincia", codProvincia);
	
	
	txtNIE.setEnabled(false);
	txtCodigoNacionalidad.setEnabled(false);
	
	//  - Resto de datos     
 	Iterator itControles = lstDatos.iterator(); 	
	ArrayList Datos= consultasBD.DatosHabitante(idHabitante);
		
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
	      
       
	       else if (comp instanceof JComboBox)
	       {  
	           try{
	               ((JComboBox)comp).setSelectedIndex((obj!=null)?Integer.parseInt(obj.toString()):-1);
	           }catch(Exception e){
	               
	               ((JComboBox)comp).setSelectedIndex(-1);
	           }
	       }
	       
	       comp.setEnabled(false);
    
	    }

	    catch(Exception A)
	    {
	        A.printStackTrace();
	        ErrorDialog.show(app.getMainFrame(), app.getI18nString("SwingError.Titulo"), app.getI18nString("SwingError.Aviso"), StringUtil.stackTrace(A));
	    }
	}//fin while	
	
	int idDomicilio=0;
	
	try{
	    //Identificador de domicilio
	    idDomicilio=Integer.parseInt(alIt.next().toString());
	    
	}catch(NullPointerException ex){
	    //ex.printStackTrace();
	}
	
    Identificadores.put("Id_Domicilio", idDomicilio);
	
  }
 
  public void exit()
  {
  }

}

