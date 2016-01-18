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
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
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
public class DomicilioPanel extends JPanel implements  FeatureExtendedPanel
{

  //Datos de ubicacion
  private JLabel lblDistrito = new JLabel();
  private JLabel lblSeccion = new JLabel();
  private JLabel lblCodProvincia = new JLabel();
  private JLabel lblNombreProvincia = new JLabel();
  private JLabel lblCodMunicipio = new JLabel();
  private JLabel lblNombreMunicipio = new JLabel();
  private JLabel lblCodEntidadColectiva = new JLabel();
  private JLabel lblNombreCortoEntidadColectiva = new JLabel();
  private JLabel lblCodEntidadSingular = new JLabel();
  private JLabel lblNombreCortoEntidadSingular = new JLabel();
  private JLabel lblCodNucleo= new JLabel();
  private JLabel lblNombreCortoNucleo = new JLabel();
  private JLabel lblCodVia = new JLabel();
  private JLabel lblTipoVia = new JLabel();
  private JLabel lblNombreCortoVia = new JLabel();
  private JLabel lblCodPseudovia = new JLabel();
  private JLabel lblNombreCortoPseudovia = new JLabel();
  
  private JLabel lblTipoNumeracion = new JLabel();
  private JLabel lblNumero = new JLabel();
  private JLabel lblCalificacion = new JLabel();
  private JLabel lblNumeroSuperior = new JLabel();
  private JLabel lblCalificacionNumeroSuperior = new JLabel();
  private JLabel lblKilometro = new JLabel();
  private JLabel lblHectometro = new JLabel();
  private JLabel lblBloque = new JLabel();
  private JLabel lblPortal = new JLabel();
  private JLabel lblEscalera = new JLabel();
  private JLabel lblPlanta = new JLabel();
  private JLabel lblPuerta = new JLabel();
  private JLabel lblTipoLocal = new JLabel();
  private JLabel lblNumeroIdentificacionAyuntamiento = new JLabel();
  
  private JTextField txtDistrito = new JTextField();
  private JTextField txtSeccion = new JTextField();
  private JTextField txtCodProvincia = new JTextField();
  private JTextField txtNombreProvincia = new JTextField();
  private JTextField txtCodMunicipio = new JTextField();
  private JTextField txtNombreMunicipio = new JTextField();
  private JTextField txtCodEntidadColectiva = new JTextField();
  private JTextField txtNombreCortoEntidadColectiva = new JTextField();
  private JTextField txtCodEntidadSingular = new JTextField();
  private JTextField txtNombreCortoEntidadSingular = new JTextField();
  private JTextField txtCodNucleo= new JTextField();
  private JTextField txtNombreCortoNucleo = new JTextField();
  private JTextField txtCodVia = new JTextField();
  private JTextField txtTipoVia = new JTextField();
  private JTextField txtNombreCortoVia = new JTextField();
  private JTextField txtCodPseudovia = new JTextField();
  private JTextField txtNombreCortoPseudovia = new JTextField();
  
  private JTextField txtTipoNumeracion = new JTextField();
  private JTextField txtNumero = new JTextField();
  private JTextField txtCalificacion = new JTextField();
  private JTextField txtNumeroSuperior = new JTextField();
  private JTextField txtCalificacionNumeroSuperior = new JTextField();
  private JTextField txtKilometro = new JTextField();
  private JTextField txtHectometro = new JTextField();
  private JTextField txtBloque = new JTextField();
  private JTextField txtPortal = new JTextField();
  private JTextField txtEscalera = new JTextField();
  private JTextField txtPlanta = new JTextField();
  private JTextField txtPuerta = new JTextField();
  private JTextField txtTipoLocal = new JTextField();
  private JTextField txtNumeroIdentificacionAyuntamiento = new JTextField();
  
  
  //Otros datos
  private JLabel lblNivelEstudios = new JLabel();
  private JLabel lblCodigoNacionalidad = new JLabel();
  
  private JTextField txtNivelEstudios = new JTextField();
  private JTextField txtCodigoNacionalidad = new JTextField();
  
  
  //Paneles
  private JPanel jPanelUbicacion  = new JPanel();
  private JPanel jPanelOtrosDatos = new JPanel();
  
  
  //Otras variables
  ArrayList lstDatos = new ArrayList();
  
  
  public DomicilioPanel()
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
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    this.setName(aplicacion.getI18nString("habitantes.panel.datoscensales"));
    this.setLayout(null);
    this.setSize(new Dimension(600, 550));
    this.setBounds(new java.awt.Rectangle(5,10,636,493)); 
    
    jPanelUbicacion.setBounds(new Rectangle(8, 5, 487, 388));
    jPanelUbicacion.setLayout(null);
    jPanelUbicacion.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("habitantes.panel.ubicacion")));
    
    
    lblDistrito.setText(aplicacion.getI18nString("habitantes.datoscensales.distrito"));
    lblDistrito.setBounds(new Rectangle(10, 24, 80, 15));
    txtDistrito.setText("");
    txtDistrito.setBounds(new Rectangle(95, 24, 30, 20));
    
    lblSeccion.setText(aplicacion.getI18nString("habitantes.datoscensales.seccion"));
    lblSeccion.setBounds(new Rectangle(140, 24, 80, 15));
    txtSeccion.setText("");
    txtSeccion.setBounds(new Rectangle(225, 24, 90, 20));
    
     
    
    lblCodProvincia.setText(aplicacion.getI18nString("habitantes.datoscensales.codprovincia"));
    lblCodProvincia.setBounds(new Rectangle(10, 53, 90, 15));
    txtCodProvincia.setText("");
    txtCodProvincia.setBounds(new Rectangle(105, 53, 80, 20));
    lblNombreProvincia.setText(aplicacion.getI18nString("habitantes.datoscensales.nombreoficial"));
    lblNombreProvincia.setBounds(new Rectangle(195, 53, 90, 15));
    txtNombreProvincia.setText("");
    txtNombreProvincia.setBounds(new Rectangle(290, 53, 170, 20));
    
    lblCodMunicipio.setText(aplicacion.getI18nString("habitantes.datoscensales.codmunicipio"));
    lblCodMunicipio.setBounds(new Rectangle(10, 78, 90, 15));
    txtCodMunicipio.setText("");
    txtCodMunicipio.setBounds(new Rectangle(105, 78, 80, 20));
    lblNombreMunicipio.setText(aplicacion.getI18nString("habitantes.datoscensales.nombreoficial"));
    lblNombreMunicipio.setBounds(new Rectangle(195, 78, 90, 15));
    txtNombreMunicipio.setText("");
    txtNombreMunicipio.setBounds(new Rectangle(290, 78, 170, 20));
    
    
    lblCodEntidadColectiva.setText(aplicacion.getI18nString("habitantes.datoscensales.codentidadcolectiva"));
    lblCodEntidadColectiva.setBounds(new Rectangle(10, 103, 90, 15));
    txtCodEntidadColectiva.setText("");
    txtCodEntidadColectiva.setBounds(new Rectangle(105, 103, 80, 20));
    lblNombreCortoEntidadColectiva.setText(aplicacion.getI18nString("habitantes.datoscensales.nombrecorto"));
    lblNombreCortoEntidadColectiva.setBounds(new Rectangle(195, 103, 90, 15));
    txtNombreCortoEntidadColectiva.setText("");
    txtNombreCortoEntidadColectiva.setBounds(new Rectangle(290, 103, 170, 20));
    
    lblCodEntidadSingular.setText(aplicacion.getI18nString("habitantes.datoscensales.codentsingular"));
    lblCodEntidadSingular.setBounds(new Rectangle(10, 128, 90, 15));
    txtCodEntidadSingular.setText("");
    txtCodEntidadSingular.setBounds(new Rectangle(105, 128, 80, 20));
    lblNombreCortoEntidadSingular.setText(aplicacion.getI18nString("habitantes.datoscensales.nombrecorto"));
    lblNombreCortoEntidadSingular.setBounds(new Rectangle(195, 128, 90, 15));
    txtNombreCortoEntidadSingular.setText("");
    txtNombreCortoEntidadSingular.setBounds(new Rectangle(290, 128, 170, 20));
    
    lblCodNucleo.setText(aplicacion.getI18nString("habitantes.datoscensales.codnucleo"));
    lblCodNucleo.setBounds(new Rectangle(10, 153, 90, 15));
    txtCodNucleo.setText("");
    txtCodNucleo.setBounds(new Rectangle(105, 153, 80, 20));
    lblNombreCortoNucleo.setText(aplicacion.getI18nString("habitantes.datoscensales.nombrecorto"));
    lblNombreCortoNucleo.setBounds(new Rectangle(195, 153, 90, 15));
    txtNombreCortoNucleo.setText("");
    txtNombreCortoNucleo.setBounds(new Rectangle(290, 153, 170, 20));
    
    
    lblCodVia.setText(aplicacion.getI18nString("habitantes.datoscensales.codvia"));
    lblCodVia.setBounds(new Rectangle(10, 178, 50, 15));
    txtCodVia.setText("");
    txtCodVia.setBounds(new Rectangle(65, 178, 40, 20));
    lblTipoVia.setText(aplicacion.getI18nString("habitantes.datoscensales.tipovia"));
    lblTipoVia.setBounds(new Rectangle(115, 178, 50, 15));
    txtTipoVia.setText("");
    txtTipoVia.setBounds(new Rectangle(170, 178, 40, 20));    
    lblNombreCortoVia.setText(aplicacion.getI18nString("habitantes.datoscensales.nombrecorto"));
    lblNombreCortoVia.setBounds(new Rectangle(220, 178, 70, 15));
    txtNombreCortoVia.setText("");
    txtNombreCortoVia.setBounds(new Rectangle(295, 178, 165, 20));
    
    lblCodPseudovia.setText(aplicacion.getI18nString("habitantes.datoscensales.codpseudovia"));
    lblCodPseudovia.setBounds(new Rectangle(10, 203, 90, 15));
    txtCodPseudovia.setText("");
    txtCodPseudovia.setBounds(new Rectangle(105, 203, 60, 20));
    lblNombreCortoPseudovia.setText(aplicacion.getI18nString("habitantes.datoscensales.descripcion"));
    lblNombreCortoPseudovia.setBounds(new Rectangle(10, 228, 90, 15));
    txtNombreCortoPseudovia.setText("");
    txtNombreCortoPseudovia.setBounds(new Rectangle(105, 228, 355, 20));
    
    
    lblTipoNumeracion.setText(aplicacion.getI18nString("habitantes.datoscensales.tiponumeracion"));
    lblTipoNumeracion.setBounds(new Rectangle(10, 265, 90, 15));
    txtTipoNumeracion.setText("");
    txtTipoNumeracion.setBounds(new Rectangle(105, 265, 20, 20));
    lblNumero.setText(aplicacion.getI18nString("habitantes.datoscensales.numero"));
    lblNumero.setBounds(new Rectangle(135, 253, 90, 15));
    txtNumero.setText("");
    txtNumero.setBounds(new Rectangle(230, 253, 50, 20));    
    lblCalificacion.setText(aplicacion.getI18nString("habitantes.datoscensales.calificador"));
    lblCalificacion.setBounds(new Rectangle(325, 253, 100, 15));
    txtCalificacion.setText("");
    txtCalificacion.setBounds(new Rectangle(440, 253, 20, 20));
    
    lblNumeroSuperior.setText(aplicacion.getI18nString("habitantes.datoscensales.numerosuperior"));
    lblNumeroSuperior.setBounds(new Rectangle(135, 278, 90, 15));
    txtNumeroSuperior.setText("");
    txtNumeroSuperior.setBounds(new Rectangle(230, 278, 50, 20));    
    lblCalificacionNumeroSuperior.setText(aplicacion.getI18nString("habitantes.datoscensales.calificadorsuperior"));
    lblCalificacionNumeroSuperior.setBounds(new Rectangle(325, 278, 100, 15));
    txtCalificacionNumeroSuperior.setText("");
    txtCalificacionNumeroSuperior.setBounds(new Rectangle(440, 278, 20, 20));
    
    
    lblKilometro.setText(aplicacion.getI18nString("habitantes.datoscensales.kilometro"));
    lblKilometro.setBounds(new Rectangle(10, 303, 20, 15));
    txtKilometro.setText("");
    txtKilometro.setBounds(new Rectangle(35, 303, 30, 20));
    lblHectometro.setText(aplicacion.getI18nString("habitantes.datoscensales.hectometro"));
    lblHectometro.setBounds(new Rectangle(75, 303, 20, 15));
    txtHectometro.setText("");
    txtHectometro.setBounds(new Rectangle(100, 303, 20, 20));    
    lblBloque.setText(aplicacion.getI18nString("habitantes.datoscensales.bloque"));
    lblBloque.setBounds(new Rectangle(130, 303, 20, 15));
    txtBloque.setText("");
    txtBloque.setBounds(new Rectangle(155, 303, 25, 20));
    lblPortal.setText(aplicacion.getI18nString("habitantes.datoscensales.portal"));
    lblPortal.setBounds(new Rectangle(190, 303, 30, 15));
    txtPortal.setText("");
    txtPortal.setBounds(new Rectangle(225, 303, 25, 20));
    lblEscalera.setText(aplicacion.getI18nString("habitantes.datoscensales.escalera"));
    lblEscalera.setBounds(new Rectangle(260, 303, 25, 15));
    txtEscalera.setText("");
    txtEscalera.setBounds(new Rectangle(290, 303, 25, 20));
    lblPlanta.setText(aplicacion.getI18nString("habitantes.datoscensales.planta"));
    lblPlanta.setBounds(new Rectangle(323, 303, 30, 15));
    txtPlanta.setText("");
    txtPlanta.setBounds(new Rectangle(355, 303, 30, 20));
    lblPuerta.setText(aplicacion.getI18nString("habitantes.datoscensales.puerta"));
    lblPuerta.setBounds(new Rectangle(392, 303, 35, 15));
    txtPuerta.setText("");
    txtPuerta.setBounds(new Rectangle(425, 303, 35, 20));
    
    lblTipoLocal.setText(aplicacion.getI18nString("habitantes.datoscensales.tipolocal"));
    lblTipoLocal.setBounds(new Rectangle(10, 328, 80, 15));
    txtTipoLocal.setText("");
    txtTipoLocal.setBounds(new Rectangle(95, 328, 20, 20));
    
    lblNumeroIdentificacionAyuntamiento.setText(aplicacion.getI18nString("habitantes.datoscensales.nia"));
    lblNumeroIdentificacionAyuntamiento.setBounds(new Rectangle(10, 353, 220, 15));
    txtNumeroIdentificacionAyuntamiento.setText("");
    txtNumeroIdentificacionAyuntamiento.setBounds(new Rectangle(235, 353, 120, 20));
    
    
    jPanelOtrosDatos.setBounds(new Rectangle(8, 370, 487, 60));
    jPanelOtrosDatos.setLayout(null);
    jPanelOtrosDatos.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("habitantes.panel.otros")));
    
    /*
    lblNivelEstudios.setText("Nivel de estudios");
    lblNivelEstudios.setBounds(new Rectangle(10, 23, 100, 15));
    txtNivelEstudios.setText("");
    txtNivelEstudios.setBounds(new Rectangle(115, 23, 30, 20));
    lblCodigoNacionalidad.setText("Código de Nacionalidad");
    lblCodigoNacionalidad.setBounds(new Rectangle(165, 23, 120, 15));
    txtCodigoNacionalidad.setText("");
    txtCodigoNacionalidad.setBounds(new Rectangle(290, 23, 40, 20));
    */
    
    jPanelUbicacion.add(lblDistrito, null);
    jPanelUbicacion.add(txtDistrito, null);
    jPanelUbicacion.add(lblSeccion, null);
    jPanelUbicacion.add(txtSeccion, null);
    jPanelUbicacion.add(lblCodProvincia, null);
    jPanelUbicacion.add(txtCodProvincia, null);
    jPanelUbicacion.add(lblNombreProvincia, null);
    jPanelUbicacion.add(txtNombreProvincia, null);
    jPanelUbicacion.add(lblCodMunicipio, null);
    jPanelUbicacion.add(txtCodMunicipio, null);
    jPanelUbicacion.add(lblNombreMunicipio, null);
    jPanelUbicacion.add(txtNombreMunicipio, null);
    jPanelUbicacion.add(lblCodEntidadColectiva, null);
    jPanelUbicacion.add(txtCodEntidadColectiva, null);
    jPanelUbicacion.add(lblNombreCortoEntidadColectiva, null);
    jPanelUbicacion.add(txtNombreCortoEntidadColectiva, null);
    jPanelUbicacion.add(lblCodEntidadSingular, null);
    jPanelUbicacion.add(txtCodEntidadSingular, null);
    jPanelUbicacion.add(lblNombreCortoEntidadSingular, null);
    jPanelUbicacion.add(txtNombreCortoEntidadSingular, null);
    jPanelUbicacion.add(lblCodNucleo, null);
    jPanelUbicacion.add(txtCodNucleo, null);
    jPanelUbicacion.add(lblNombreCortoNucleo, null);
    jPanelUbicacion.add(txtNombreCortoNucleo, null);
    jPanelUbicacion.add(lblCodVia, null);
    jPanelUbicacion.add(txtCodVia, null);
    jPanelUbicacion.add(lblTipoVia, null);
    jPanelUbicacion.add(txtTipoVia, null);
    jPanelUbicacion.add(lblNombreCortoVia, null);
    jPanelUbicacion.add(txtNombreCortoVia, null);
    jPanelUbicacion.add(lblCodPseudovia, null);
    jPanelUbicacion.add(txtCodPseudovia, null);
    jPanelUbicacion.add(lblNombreCortoPseudovia, null);
    jPanelUbicacion.add(txtNombreCortoPseudovia, null);
    jPanelUbicacion.add(lblTipoNumeracion, null);
    jPanelUbicacion.add(txtTipoNumeracion, null);
    jPanelUbicacion.add(lblNumero, null);
    jPanelUbicacion.add(txtNumero, null);
    jPanelUbicacion.add(lblCalificacion, null);
    jPanelUbicacion.add(txtCalificacion, null);
    jPanelUbicacion.add(lblNumeroSuperior, null);
    jPanelUbicacion.add(txtNumeroSuperior, null);
    jPanelUbicacion.add(lblCalificacionNumeroSuperior, null);
    jPanelUbicacion.add(txtCalificacionNumeroSuperior, null);
    jPanelUbicacion.add(lblKilometro, null);
    jPanelUbicacion.add(txtKilometro, null);
    jPanelUbicacion.add(lblHectometro, null);
    jPanelUbicacion.add(txtHectometro, null);
    jPanelUbicacion.add(lblBloque, null);
    jPanelUbicacion.add(txtBloque, null);
    jPanelUbicacion.add(lblPortal, null);
    jPanelUbicacion.add(txtPortal, null);
    jPanelUbicacion.add(lblEscalera, null);
    jPanelUbicacion.add(txtEscalera, null);
    jPanelUbicacion.add(lblPlanta, null);
    jPanelUbicacion.add(txtPlanta, null);
    jPanelUbicacion.add(lblPuerta, null);
    jPanelUbicacion.add(txtPuerta, null);
    jPanelUbicacion.add(lblTipoLocal, null);
    jPanelUbicacion.add(txtTipoLocal, null);
    jPanelUbicacion.add(lblNumeroIdentificacionAyuntamiento, null);
    jPanelUbicacion.add(txtNumeroIdentificacionAyuntamiento, null);
   
    
    /*
    jPanelOtrosDatos.add(lblNivelEstudios, null);
    jPanelOtrosDatos.add(txtNivelEstudios, null);
    jPanelOtrosDatos.add(lblCodigoNacionalidad, null);
    jPanelOtrosDatos.add(txtCodigoNacionalidad, null);
    */
    
    this.add(jPanelUbicacion);
    //this.add(jPanelOtrosDatos);
    
    
    
    lstDatos.add(txtDistrito);
    lstDatos.add(txtSeccion);
    lstDatos.add(txtCodEntidadColectiva);
    lstDatos.add(txtNombreCortoEntidadColectiva);
    lstDatos.add(txtCodEntidadSingular);
    lstDatos.add(txtNombreCortoEntidadSingular);
    lstDatos.add(txtCodNucleo);
    lstDatos.add(txtNombreCortoNucleo);
    lstDatos.add(txtCodVia);
    lstDatos.add(txtTipoVia);
    lstDatos.add(txtNombreCortoVia);
    lstDatos.add(txtCodPseudovia);
    lstDatos.add(txtNombreCortoPseudovia);
    lstDatos.add(txtTipoNumeracion);
    lstDatos.add(txtNumero);
    lstDatos.add(txtCalificacion);
    lstDatos.add(txtNumeroSuperior);
    lstDatos.add(txtCalificacionNumeroSuperior);
    lstDatos.add(txtKilometro);
    lstDatos.add(txtHectometro);
    lstDatos.add(txtBloque);
    lstDatos.add(txtPortal);
    lstDatos.add(txtEscalera);
    lstDatos.add(txtPlanta);
    lstDatos.add(txtPuerta);
    lstDatos.add(txtTipoLocal);
    lstDatos.add(txtNumeroIdentificacionAyuntamiento);
    
    
  }
  

 public void enter()
 {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
         
    Iterator itControles = lstDatos.iterator(); 
 	
 	//Se muestran los datos del domicilio
    PadronHabitantesPostgre consultasBD = new PadronHabitantesPostgre();
    
    //	- Municipio y provincia se extraen del habitante
    String codMunicipio = Identificadores.get("Id_Municipio").toString();
    String codProvincia = Identificadores.get("Id_Provincia").toString();
    
    txtCodMunicipio.setText(codMunicipio);
    txtCodMunicipio.setEnabled(false);
    txtNombreMunicipio.setText(consultasBD.findMunicipioById(codProvincia, codMunicipio));
    txtNombreMunicipio.setEnabled(false);
    txtCodProvincia.setText(codProvincia);
    txtCodProvincia.setEnabled(false);
    txtNombreProvincia.setText(consultasBD.findProvinciaById(codProvincia));
    txtNombreProvincia.setEnabled(false);
    
    
    //	- Resto de datos del domicilio
 	
 	ArrayList Datos= consultasBD.DatosDomicilio(codProvincia, codMunicipio, Integer.parseInt(Identificadores.get("Id_Domicilio").toString()));
 		
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
 	       
 	       /*else if (comp instanceof DateField)
 	       {
 	           Date date = null;
 	           jPanelNacimiento.remove(fechaNacimiento);
 	           
 	           if (obj!=null){
 	               Format formatter = new SimpleDateFormat("yyyy-MM-dd");
 	               fechaNacimiento = new DateField ((Date)formatter.parseObject(obj.toString()), 15);	               
 	           }
 	           else{                   
 	               fechaNacimiento = new DateField(date, 15);
 	           }
 	           fechaNacimiento.setBounds(120, 48, 70, 20);
 	           jPanelNacimiento.add(fechaNacimiento, null);	         	           
 	       }*/
        
 	       else if (comp instanceof JComboBox)
 	       {            
 	           ((JComboBox)comp).setSelectedIndex((obj!=null)?Integer.parseInt(obj.toString()):-1);
 	       }
 	       
 	       comp.setEnabled(false);
     
 	    }

 	    catch(Exception A)
 	    {
 	        A.printStackTrace();
 	        ErrorDialog.show(app.getMainFrame(), app.getI18nString("SwingError.Titulo"), app.getI18nString("SwingError.Aviso"), StringUtil.stackTrace(A));
 	    }
 	}//fin while	
 	 
  }
 
  public void exit()
  {
  }


}

