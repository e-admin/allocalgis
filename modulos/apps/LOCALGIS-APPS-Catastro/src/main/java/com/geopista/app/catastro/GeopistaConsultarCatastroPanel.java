/**
 * GeopistaConsultarCatastroPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.ui.cursortool.editing.GeopistaEditingPlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.IAbstractSelection;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.renderer.style.BasicStyle;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToFullExtentPlugIn;



public class GeopistaConsultarCatastroPanel extends JPanel implements WizardPanel
{
  private GeopistaEditor geopistaEditor1 = new GeopistaEditor();
  private String referenciacatastral = null;

  private JTabbedPane jTabbedPane1 = new JTabbedPane();
  private JPanel jPanel6 = new JPanel();
  private JTextField txtDireccion = new JTextField();
  private JLabel kmparlbl2 = new JLabel();
  private JLabel bloqueparlbl3 = new JLabel();
  private JTextField txtBloque = new JTextField();
  private JTextField txtKilometro = new JTextField();
  private JLabel dirnoestrucparlbl2 = new JLabel();
  private JLabel letra2parlbl2 = new JLabel();
  private JLabel numero2parlbl3 = new JLabel();
  private JTextField txtNumero2 = new JTextField();
  private JTextField letra2cargotxt = new JTextField();
  private JLabel letra1parlbl2 = new JLabel();
  private JTextField txtLetra1 = new JTextField();
  private JLabel numero1cargolbl = new JLabel();
  private JTextField txtNumero1 = new JTextField();
  private JLabel codigopostalcargolbl = new JLabel();
  private JTextField txtCP = new JTextField();
  private JLabel nombreviacargolbl = new JLabel();
  private JTextField txtNombre = new JTextField();
  private JLabel tipoviacargolbl = new JLabel();
  private JTextField txtTipo = new JTextField();
  private JLabel viapublicacargolbl = new JLabel();
  private JTextField txtCodigo = new JTextField();
  private JTextField txtEntidad = new JTextField();
  private JLabel entmenorcargolbl = new JLabel();
  private JPanel jPanel112 = new JPanel();
  private JLabel participacioncargolbl = new JLabel();
  private JTextField txtparticipacioncargo = new JTextField();
  private JLabel fijocargolbl = new JLabel();
  private JTextField txtnumerofijo = new JTextField();
  private JLabel cargocargolbl = new JLabel();
  private JTextField txtcargo = new JTextField();
  private JTextField txtcontrol1cargo = new JTextField();
  private JLabel controlcargolbl = new JLabel();
  private JTextField txtcontrol2cargo = new JTextField();
  private JTextField txtPuerta = new JTextField();
  private JLabel puertaconslbl1 = new JLabel();
  private JTextField txtPlanta = new JTextField();
  private JLabel plantaconslbl1 = new JLabel();
  private JTextField txtEscalera = new JTextField();
  private JLabel escaleralbl1 = new JLabel();
  private JPanel jPanel20 = new JPanel();
  private JLabel numero2parlbl4 = new JLabel();
  private JLabel formacalculoparlbl1 = new JLabel();
  private JTextField txtAnnoNotificacion = new JTextField();
  private JLabel aprobacionparlbl1 = new JLabel();
  private JTextField txtAnnoRevision = new JTextField();
  private JLabel supconsparlbl2 = new JLabel();
  private JTextField txtValorCatastral = new JTextField();
  private JTextField txtannovalor = new JTextField();
  private JLabel supsolarlbl2 = new JLabel();
  private JTextField txtValorSuelo = new JTextField();
  private JLabel supconsparlbl3 = new JLabel();
  private JTextField txtBaseLiquidable = new JTextField();
  private JLabel supconsparlbl4 = new JLabel();
  private JTextField txtValorCons = new JTextField();
  private JLabel valorconstruccioncargolbl = new JLabel();
  private JTextField claveusocargotxt = new JTextField();
  private JLabel tipoviacargolbl1 = new JLabel();
  private JTextField txtNotificacion = new JTextField();
  private JTextField coeficientepropiedadtxt = new JTextField();
  private JLabel supcubiertaparlbl7 = new JLabel();
  private JTextField txtSuperficieSuelo = new JTextField();
  private JLabel supconsparlbl5 = new JLabel();
  private JTextField txtSuperficieCons = new JTextField();
  private JLabel supsolarlbl3 = new JLabel();
  private JPanel jPanel22 = new JPanel();
  private JLabel escaleralbl2 = new JLabel();
  private JTextField txtEscaleraTitular = new JTextField();
  private JLabel plantaconslbl2 = new JLabel();
  private JTextField txtPlantaTitular = new JTextField();
  private JLabel puertaconslbl2 = new JLabel();
  private JTextField txtPuertaTitular = new JTextField();
  private JTextField txtDirecciontitular = new JTextField();
  private JLabel kmparlbl3 = new JLabel();
  private JLabel bloqueparlbl4 = new JLabel();
  private JTextField txtBloquetitular = new JTextField();
  private JTextField txtKilometrotitular = new JTextField();
  private JLabel dirnoestrucparlbl3 = new JLabel();
  private JLabel letra2parlbl3 = new JLabel();
  private JLabel numero2parlbl6 = new JLabel();
  private JTextField txtNumero2titular = new JTextField();
  private JTextField txtLetra2titular = new JTextField();
  private JLabel letra1parlbl3 = new JLabel();
  private JTextField txtLetra1titular =  new JTextField();
  private JLabel numero1cargolbl1 = new JLabel();
  private JTextField txtNumero1titular = new JTextField();
  private JLabel codigopostalcargolbl1 = new JLabel();
  private JTextField txtCPTitular = new JTextField();
  private JLabel nombreviacargolbl1 = new JLabel();
  private JTextField txtNombreviatitular = new JTextField();
  private JLabel tipoviacargolbl3 = new JLabel();
  private JTextField txtTipoViaTitular = new JTextField();
  private JLabel viapublicacargolbl1 = new JLabel();
  private JTextField txtCodviatitular = new JTextField();
  private JTextField txtMunicipioINE = new JTextField();
  private JLabel entmenorcargolbl1 = new JLabel();
  private JPanel jPanel111 = new JPanel();
  private JLabel superficiesubparlbl1 = new JLabel();
  private JTextField txtTitular = new JTextField();
  private JLabel personalidadtitularlbl = new JLabel();
  private JTextField txtNif = new JTextField();
  private JLabel niftitularlbl = new JLabel();
  private JTextField txtLetraNif = new JTextField();
  private JTextField txtProvincia = new JTextField();
  private JLabel tipoviacargolbl5 = new JLabel();
  private JTextField txtMunicipioDGC = new JTextField();
  private JLabel viapublicacargolbl2 = new JLabel();
  private JTextField txtDelegacion = new JTextField();
  private JLabel entmenorcargolbl2 = new JLabel();
  private JTextField txtApartado = new JTextField();
  private JLabel apartadocorreostitularlbl = new JLabel();
  private JTextField txtPais = new JTextField();
  private JLabel nombrepaistitulartxt = new JLabel();
  private JTextField txtNombreProvincia = new JTextField();
  private JLabel nombrepaistitulartxt1 = new JLabel();
  private JTextField txtNombreMunicipio = new JTextField();
  private JLabel nombrepaistitulartxt2 = new JLabel();
  private JTextField txtReferencia = new JTextField();
  private JLabel referencialbl = new JLabel();

  private JLabel distritocargolbl1 = new JLabel();
  private JTextField txtdistrito = new JTextField();
  /*String[] data = {"0001", "0002"};
  private JList padronlst = new JList(data);*/
  //private JList padronlst = new JList();
  DefaultListModel padronlst = new DefaultListModel();
  JList padronModel = new JList(padronlst);
  
  private JLabel jLabel2 = new JLabel();
  private ListModel listModel1 = new DefaultListModel();
  private JTextField txtLetra2 = new JTextField();
  private JTextField txtClaveGrupo = new JTextField();
  private JTextField txtCoefPropiedad = new JTextField();
  private JTextField txtPersonalidad = new JTextField();
  private JScrollPane jScrollPane1 = new JScrollPane();
  
  public GeopistaConsultarCatastroPanel()
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

/* public static void main(String[] args)
  {
    JFrame frame1 = new JFrame("Consultar Datos Catastro");
    GeopistaConsultarCatastroPanel geopistaConsultarDatos = new GeopistaConsultarCatastroPanel();
    frame1.getContentPane().add(geopistaConsultarDatos);
  //  frame1.setSize(676,800);
    frame1.setSize(676,550);
    frame1.setVisible(true);
    frame1.setLocation(150, 90);
  }*/
  
  private void jbInit() throws Exception
  {
    this.setLayout(null);
    this.setSize(new Dimension(700,600));

    jPanel6.setLayout(null);
    txtDireccion.setBounds(new Rectangle(165, 200, 205, 20));
    kmparlbl2.setText("Kilómetro:");
    kmparlbl2.setBounds(new Rectangle(245, 160, 65, 35));
    bloqueparlbl3.setText("Bloque:");
    bloqueparlbl3.setBounds(new Rectangle(145, 160, 60, 35));
    txtBloque.setBounds(new Rectangle(195, 165, 35, 20));
    txtKilometro.setBounds(new Rectangle(310, 165, 65, 20));
    dirnoestrucparlbl2.setText("Dirección No Estructurada:");
    dirnoestrucparlbl2.setBounds(new Rectangle(5, 195, 170, 35));
    letra2parlbl2.setText("Segunda Letra:");
    letra2parlbl2.setBounds(new Rectangle(5, 160, 95, 35));
    numero2parlbl3.setText("Segundo Número:");
    numero2parlbl3.setBounds(new Rectangle(255, 125, 100, 35));
    txtNumero2.setBounds(new Rectangle(350, 130, 35, 20));
    letra2cargotxt.setText("A");
    letra2cargotxt.setBounds(new Rectangle(565, 85, 25, 20));
    letra1parlbl2.setText("Primera Letra:");
    letra1parlbl2.setBounds(new Rectangle(145, 125, 80, 35));
    txtLetra1.setBounds(new Rectangle(225, 130, 25, 20));
    numero1cargolbl.setText("Primer Número:");
    numero1cargolbl.setBounds(new Rectangle(5, 125, 100, 35));
    txtNumero1.setBounds(new Rectangle(100, 130, 35, 20));
    codigopostalcargolbl.setText("Código Postal:");
    codigopostalcargolbl.setBounds(new Rectangle(5, 85, 90, 35));
    txtCP.setBounds(new Rectangle(85, 90, 50, 20));
    nombreviacargolbl.setText("Nombre Vía:");
    nombreviacargolbl.setBounds(new Rectangle(5, 50, 115, 35));
    txtNombre.setBounds(new Rectangle(90, 55, 230, 20));
    tipoviacargolbl.setText("Tipo de Vía:");
    tipoviacargolbl.setBounds(new Rectangle(160, 85, 60, 35));
    txtTipo.setBounds(new Rectangle(225, 90, 45, 20));
    viapublicacargolbl.setText("Código Vía Pública:");
    viapublicacargolbl.setBounds(new Rectangle(200, 15, 110, 40));
    txtCodigo.setBounds(new Rectangle(320, 25, 50, 20));
    txtEntidad.setBounds(new Rectangle(135, 25, 35, 20));
    entmenorcargolbl.setText("Código Entidad Menor:");
    entmenorcargolbl.setBounds(new Rectangle(5, 15, 125, 40));
    jPanel112.setLayout(null);
    jPanel112.setSize(new Dimension(645, 250));

    participacioncargolbl.setText("Coeficiente Paticipación:");
    participacioncargolbl.setBounds(new Rectangle(5, 95, 130, 40));
    txtparticipacioncargo.setBounds(new Rectangle(130, 105, 60, 20));
    fijocargolbl.setText("Nº Fijo Bien Inmueble:");
    fijocargolbl.setBounds(new Rectangle(5, 125, 145, 40));
    txtnumerofijo.setBounds(new Rectangle(130, 135, 95, 20));
    cargocargolbl.setText("Nº Cargo:");
    cargocargolbl.setBounds(new Rectangle(5, 60, 85, 40));
    txtcargo.setBounds(new Rectangle(85, 70, 50, 20));
    txtcontrol1cargo.setBounds(new Rectangle(275, 70, 25, 20));
    controlcargolbl.setText("Caracteres Control:");
    controlcargolbl.setBounds(new Rectangle(155, 60, 120, 40));
    txtcontrol2cargo.setBounds(new Rectangle(305, 70, 25, 20));
    txtPuerta.setBounds(new Rectangle(305, 235, 40, 20));
    puertaconslbl1.setText("Puerta:");
    puertaconslbl1.setBounds(new Rectangle(240, 230, 70, 35));
    txtPlanta.setBounds(new Rectangle(180, 235, 35, 20));
    plantaconslbl1.setText("Planta:");
    plantaconslbl1.setBounds(new Rectangle(125, 230, 70, 35));
    txtEscalera.setBounds(new Rectangle(70, 235, 40, 20));
    escaleralbl1.setText("Escalera:");
    escaleralbl1.setBounds(new Rectangle(5, 230, 70, 35));
    jPanel20.setLayout(null);

    numero2parlbl4.setText("Nº Última Notificación:");
    numero2parlbl4.setBounds(new Rectangle(5, 160, 125, 35));
    formacalculoparlbl1.setText("Año Notificación:");
    formacalculoparlbl1.setBounds(new Rectangle(135, 125, 105, 35));
    txtAnnoNotificacion.setBounds(new Rectangle(235, 130, 40, 20));
    aprobacionparlbl1.setText("Año Revisión:");
    aprobacionparlbl1.setBounds(new Rectangle(5, 125, 105, 35));
    txtAnnoRevision.setBounds(new Rectangle(90, 130, 35, 20));
    supconsparlbl2.setText("Valor Catastral:");
    supconsparlbl2.setBounds(new Rectangle(175, 15, 95, 40));
    txtValorCatastral.setBounds(new Rectangle(270, 25, 95, 20));
    txtValorCatastral.setPreferredSize(new Dimension(53, 20));
    txtValorCatastral.setSize(new Dimension(95, 20));
    txtannovalor.setBounds(new Rectangle(115, 25, 45, 20));
    supsolarlbl2.setText("Año Valor Catastral:");
    supsolarlbl2.setBounds(new Rectangle(5, 15, 105, 40));
    txtValorSuelo.setBounds(new Rectangle(285, 60, 95, 20));
    txtValorSuelo.setPreferredSize(new Dimension(53, 20));
    txtValorSuelo.setSize(new Dimension(95, 20));
    supconsparlbl3.setText("Valor Suelo:");
    supconsparlbl3.setBounds(new Rectangle(220, 50, 65, 40));
    txtBaseLiquidable.setBounds(new Rectangle(255, 95, 95, 20));
    txtBaseLiquidable.setPreferredSize(new Dimension(53, 20));
    txtBaseLiquidable.setSize(new Dimension(95, 20));
    supconsparlbl4.setText("Base Liquidable:");
    supconsparlbl4.setBounds(new Rectangle(160, 85, 90, 40));
    txtValorCons.setBounds(new Rectangle(115, 60, 95, 20));
    txtValorCons.setPreferredSize(new Dimension(53, 20));
    txtValorCons.setSize(new Dimension(95, 20));
    valorconstruccioncargolbl.setText("Valor  Construcción:");
    valorconstruccioncargolbl.setBounds(new Rectangle(5, 50, 105, 40));
    claveusocargotxt.setText("A");
    claveusocargotxt.setBounds(new Rectangle(570, 60, 45, 20));
    tipoviacargolbl1.setText("Clave Grupo:");
    tipoviacargolbl1.setBounds(new Rectangle(5, 90, 100, 35));
    txtNotificacion.setBounds(new Rectangle(130, 165, 95, 20));
    txtNotificacion.setPreferredSize(new Dimension(53, 20));
    txtNotificacion.setSize(new Dimension(95, 20));
    coeficientepropiedadtxt.setText("77777.77");
    coeficientepropiedadtxt.setBounds(new Rectangle(555, 120, 65, 20));
    coeficientepropiedadtxt.setSize(new Dimension(65, 20));
    supcubiertaparlbl7.setText("Coef. Propiedad:");
    supcubiertaparlbl7.setBounds(new Rectangle(5, 230, 125, 35));
    txtSuperficieSuelo.setBounds(new Rectangle(320, 200, 65, 20));
    txtSuperficieSuelo.setPreferredSize(new Dimension(53, 20));
    txtSuperficieSuelo.setSize(new Dimension(65, 20));
    supconsparlbl5.setText("Superficie Suelo:");
    supconsparlbl5.setBounds(new Rectangle(235, 190, 85, 40));
    txtSuperficieCons.setBounds(new Rectangle(165, 200, 55, 20));
    supsolarlbl3.setText("Sup. Elementos Constructivos:");
    supsolarlbl3.setBounds(new Rectangle(5, 190, 155, 40));
    jPanel22.setLayout(null);

    escaleralbl2.setText("Escalera:");
    escaleralbl2.setBounds(new Rectangle(275, 205, 55, 35));
    txtEscaleraTitular.setBounds(new Rectangle(340, 205, 40, 20));
    plantaconslbl2.setText("Planta:");
    plantaconslbl2.setBounds(new Rectangle(5, 240, 40, 35));
    txtPlantaTitular.setBounds(new Rectangle(45, 245, 35, 20));
    puertaconslbl2.setText("Puerta:");
    puertaconslbl2.setBounds(new Rectangle(90, 240, 40, 35));
    txtPuertaTitular.setBounds(new Rectangle(135, 245, 40, 20));
    txtDirecciontitular.setBounds(new Rectangle(135, 210, 125, 20));
    kmparlbl3.setText("Kilómetro:");
    kmparlbl3.setBounds(new Rectangle(235, 170, 65, 35));
    bloqueparlbl4.setText("Bloque:");
    bloqueparlbl4.setBounds(new Rectangle(130, 170, 60, 35));
    txtBloquetitular.setBounds(new Rectangle(175, 175, 35, 20));
    txtKilometrotitular.setBounds(new Rectangle(295, 175, 45, 20));
    dirnoestrucparlbl3.setText("Direccion No Estructurada:");
    dirnoestrucparlbl3.setBounds(new Rectangle(5, 205, 145, 35));
    letra2parlbl3.setText("Segunda Letra:");
    letra2parlbl3.setBounds(new Rectangle(5, 170, 95, 35));
    numero2parlbl6.setText("Segundo Número:");
    numero2parlbl6.setBounds(new Rectangle(250, 135, 95, 35));
    txtNumero2titular.setBounds(new Rectangle(340, 140, 35, 20));
    txtLetra2titular.setBounds(new Rectangle(90, 175, 25, 20));
    letra1parlbl3.setText("Primera Letra:");
    letra1parlbl3.setBounds(new Rectangle(135, 135, 80, 35));
    txtLetra1titular.setBounds(new Rectangle(210, 140, 25, 20));
    numero1cargolbl1.setText("Primer Número:");
    numero1cargolbl1.setBounds(new Rectangle(5, 135, 85, 35));
    txtNumero1titular.setBounds(new Rectangle(85, 140, 35, 20));
    codigopostalcargolbl1.setText("Código Postal:");
    codigopostalcargolbl1.setBounds(new Rectangle(255, 100, 80, 35));
    txtCPTitular.setBounds(new Rectangle(325, 105, 50, 20));
    nombreviacargolbl1.setText("Nombre Vía:");
    nombreviacargolbl1.setBounds(new Rectangle(5, 100, 75, 35));
    txtNombreviatitular.setBounds(new Rectangle(75, 105, 170, 20));
    tipoviacargolbl3.setText("Tipo de Vía:");
    tipoviacargolbl3.setBounds(new Rectangle(200, 70, 65, 35));
    txtTipoViaTitular.setBounds(new Rectangle(275, 75, 45, 20));
    viapublicacargolbl1.setText("Código Vía Pública:");
    viapublicacargolbl1.setBounds(new Rectangle(5, 70, 110, 40));
    txtCodviatitular.setBounds(new Rectangle(125, 75, 50, 20));
    txtMunicipioINE.setBounds(new Rectangle(320, 45, 45, 20));
    entmenorcargolbl1.setText("Código Municipio INE:");
    entmenorcargolbl1.setBounds(new Rectangle(200, 35, 145, 40));
    jPanel111.setLayout(null);

    superficiesubparlbl1.setText("Nombre y Apellidos:");
    superficiesubparlbl1.setBounds(new Rectangle(5, 85, 140, 35));
    txtTitular.setBounds(new Rectangle(5, 110, 350, 40));
    txtTitular.setSize(new Dimension(325, 40));
    personalidadtitularlbl.setText("Personalidad:");
    personalidadtitularlbl.setBounds(new Rectangle(5, 45, 95, 40));
    txtNif.setBounds(new Rectangle(105, 25, 145, 20));
    niftitularlbl.setText("NIF Titular:");
    niftitularlbl.setBounds(new Rectangle(5, 15, 80, 40));
    txtLetraNif.setBounds(new Rectangle(260, 25, 25, 20));
    txtProvincia.setBounds(new Rectangle(130, 45, 45, 20));
    tipoviacargolbl5.setText("Código Provincia INE:");
    tipoviacargolbl5.setBounds(new Rectangle(5, 40, 115, 35));
    txtMunicipioDGC.setBounds(new Rectangle(320, 15, 50, 20));
    viapublicacargolbl2.setText("Código Municipio DGC:");
    viapublicacargolbl2.setBounds(new Rectangle(200, 5, 120, 40));
    txtDelegacion.setBounds(new Rectangle(130, 15, 45, 20));
    entmenorcargolbl2.setText("Código Delegación MEH:");
    entmenorcargolbl2.setBounds(new Rectangle(5, 5, 145, 40));
    txtApartado.setBounds(new Rectangle(280, 247, 50, 20));
    apartadocorreostitularlbl.setText("Apartado Correos:");
    apartadocorreostitularlbl.setBounds(new Rectangle(180, 240, 95, 35));
    txtPais.setBounds(new Rectangle(90, 280, 210, 20));
    txtPais.setSize(new Dimension(210, 20));
    nombrepaistitulartxt.setText("Nombre País:");
    nombrepaistitulartxt.setBounds(new Rectangle(5, 275, 95, 35));
    txtNombreProvincia.setBounds(new Rectangle(120, 310, 210, 20));
    txtNombreProvincia.setSize(new Dimension(210, 20));
    nombrepaistitulartxt1.setText("Nombre Provincia:");
    nombrepaistitulartxt1.setBounds(new Rectangle(5, 305, 115, 35));
    txtNombreMunicipio.setBounds(new Rectangle(120, 342, 210, 20));
    txtNombreMunicipio.setSize(new Dimension(210, 20));
    nombrepaistitulartxt2.setText("Nombre Municipio:");
    nombrepaistitulartxt2.setBounds(new Rectangle(5, 335, 115, 35));
    txtReferencia.setText("0671810UN9107S");
    txtReferencia.setBounds(new Rectangle(205, 25, 190, 20));
    referencialbl.setText("Referencia Catastral:");
    referencialbl.setBounds(new Rectangle(205, 5, 130, 25));
    distritocargolbl1.setText("Distrito Municipal:");
    distritocargolbl1.setBounds(new Rectangle(5, 20, 105, 40));
    txtdistrito.setBounds(new Rectangle(130, 30, 40, 20));
    jLabel2.setText("Cargos:");
    jLabel2.setBounds(new Rectangle(25, 10, 95, 15));

    jTabbedPane1.setBounds(new Rectangle(0, 80, 595, 300));
    jPanel111.add(txtPersonalidad, null);
    jPanel111.add(txtLetraNif, null);
    jPanel111.add(superficiesubparlbl1, null);
    jPanel111.add(txtTitular, null);
    jPanel111.add(personalidadtitularlbl, null);
    jPanel111.add(txtNif, null);
    jPanel111.add(niftitularlbl, null);
    jPanel22.add(nombrepaistitulartxt2, null);
    jPanel22.add(txtNombreMunicipio, null);
    jPanel22.add(nombrepaistitulartxt1, null);
    jPanel22.add(txtNombreProvincia, null);
    jPanel22.add(nombrepaistitulartxt, null);
    jPanel22.add(txtPais, null);
    jPanel22.add(apartadocorreostitularlbl, null);
    jPanel22.add(txtApartado, null);
    jPanel22.add(entmenorcargolbl2, null);
    jPanel22.add(txtDelegacion, null);
    jPanel22.add(viapublicacargolbl2, null);
    jPanel22.add(txtMunicipioDGC, null);
    jPanel22.add(tipoviacargolbl5, null);
    jPanel22.add(txtProvincia, null);
    jPanel22.add(escaleralbl2, null);
    jPanel22.add(txtEscaleraTitular, null);
    jPanel22.add(plantaconslbl2, null);
    jPanel22.add(txtPlantaTitular, null);
    jPanel22.add(puertaconslbl2, null);
    jPanel22.add(txtPuertaTitular, null);
    jPanel22.add(txtDirecciontitular, null);
    jPanel22.add(kmparlbl3, null);
    jPanel22.add(bloqueparlbl4, null);
    jPanel22.add(txtBloquetitular, null);
    jPanel22.add(txtKilometrotitular, null);
    jPanel22.add(dirnoestrucparlbl3, null);
    jPanel22.add(letra2parlbl3, null);
    jPanel22.add(numero2parlbl6, null);
    jPanel22.add(txtNumero2titular, null);
    jPanel22.add(txtLetra2titular, null);
    jPanel22.add(letra1parlbl3, null);
    jPanel22.add(txtLetra1titular, null);
    jPanel22.add(numero1cargolbl1, null);
    jPanel22.add(txtNumero1titular, null);
    jPanel22.add(codigopostalcargolbl1, null);
    jPanel22.add(txtCPTitular, null);
    jPanel22.add(nombreviacargolbl1, null);
    jPanel22.add(txtNombreviatitular, null);
    jPanel22.add(tipoviacargolbl3, null);
    jPanel22.add(txtTipoViaTitular, null);
    jPanel22.add(viapublicacargolbl1, null);
    jPanel22.add(txtCodviatitular, null);
    jPanel22.add(txtMunicipioINE, null);
    jPanel22.add(entmenorcargolbl1, null);
    jPanel20.add(txtCoefPropiedad, null);
    jPanel20.add(txtClaveGrupo, null);
    jPanel20.add(supsolarlbl3, null);
    jPanel20.add(txtSuperficieCons, null);
    jPanel20.add(supconsparlbl5, null);
    jPanel20.add(txtSuperficieSuelo, null);
    jPanel20.add(supcubiertaparlbl7, null);
    jPanel20.add(coeficientepropiedadtxt, null);
    jPanel20.add(txtNotificacion, null);
    jPanel20.add(tipoviacargolbl1, null);
    jPanel20.add(claveusocargotxt, null);
    jPanel20.add(valorconstruccioncargolbl, null);
    jPanel20.add(txtValorCons, null);
    jPanel20.add(supconsparlbl4, null);
    jPanel20.add(txtBaseLiquidable, null);
    jPanel20.add(supconsparlbl3, null);
    jPanel20.add(txtValorSuelo, null);
    jPanel20.add(numero2parlbl4, null);
    jPanel20.add(formacalculoparlbl1, null);
    jPanel20.add(txtAnnoNotificacion, null);
    jPanel20.add(aprobacionparlbl1, null);
    jPanel20.add(txtAnnoRevision, null);
    jPanel20.add(supconsparlbl2, null);
    jPanel20.add(txtValorCatastral, null);
    jPanel20.add(txtannovalor, null);
    jPanel20.add(supsolarlbl2, null);
    jPanel112.add(txtdistrito, null);
    jPanel112.add(distritocargolbl1, null);
    jPanel112.add(txtcontrol2cargo, null);
    jPanel112.add(controlcargolbl, null);
    jPanel112.add(txtcontrol1cargo, null);
    jPanel112.add(participacioncargolbl, null);
    jPanel112.add(txtparticipacioncargo, null);
    jPanel112.add(fijocargolbl, null);
    jPanel112.add(txtnumerofijo, null);
    jPanel112.add(cargocargolbl, null);
    jPanel112.add(txtcargo, null);
    jPanel6.add(txtLetra2, null);
    jPanel6.add(escaleralbl1, null);
    jPanel6.add(txtEscalera, null);
    jPanel6.add(plantaconslbl1, null);
    jPanel6.add(txtPlanta, null);
    jPanel6.add(puertaconslbl1, null);
    jPanel6.add(txtPuerta, null);
    jPanel6.add(txtDireccion, null);
    jPanel6.add(kmparlbl2, null);
    jPanel6.add(bloqueparlbl3, null);
    jPanel6.add(txtBloque, null);
    jPanel6.add(txtKilometro, null);
    jPanel6.add(dirnoestrucparlbl2, null);
    jPanel6.add(letra2parlbl2, null);
    jPanel6.add(numero2parlbl3, null);
    jPanel6.add(txtNumero2, null);
    jPanel6.add(letra2cargotxt, null);
    jPanel6.add(letra1parlbl2, null);
    jPanel6.add(txtLetra1, null);
    jPanel6.add(numero1cargolbl, null);
    jPanel6.add(txtNumero1, null);
    jPanel6.add(codigopostalcargolbl, null);
    jPanel6.add(txtCP, null);
    jPanel6.add(nombreviacargolbl, null);
    jPanel6.add(txtNombre, null);
    jPanel6.add(tipoviacargolbl, null);
    jPanel6.add(txtTipo, null);
    jPanel6.add(viapublicacargolbl, null);
    jPanel6.add(txtCodigo, null);
    jPanel6.add(txtEntidad, null);
    jPanel6.add(entmenorcargolbl, null);
//    Datostb.add("Parcela", parceltb);


    jTabbedPane1.addTab("Identificación", jPanel112);
    jTabbedPane1.addTab("Localización", jPanel6);
    jTabbedPane1.addTab("Titular", jPanel111);
    jTabbedPane1.addTab("Domicilio Titular", jPanel22);
    jTabbedPane1.addTab("Datos Económicos", jPanel20);



 geopistaEditor1.setBounds(new Rectangle(415, 0, 240, 480)); //310 ancho y alto
  
    geopistaEditor1.addCursorTool("Zoom In/Out", "com.vividsolutions.jump.workbench.ui.zoom.ZoomTool");
    geopistaEditor1.addCursorTool("Pan", "com.vividsolutions.jump.workbench.ui.zoom.PanTool");
    ZoomToFullExtentPlugIn zoomToFullExtentPlugIn = new ZoomToFullExtentPlugIn();
    GeopistaEditingPlugIn geopistaEditingPlugIn = new GeopistaEditingPlugIn();
    geopistaEditor1.addPlugIn(zoomToFullExtentPlugIn);
    geopistaEditor1.addCursorTool("Select tool", "com.geopista.ui.cursortool.GeopistaSelectFeaturesTool");

    geopistaEditor1.addPlugIn("com.geopista.ui.plugin.scalebar.GeopistaInstallScaleBarPlugIn");
    geopistaEditor1.addPlugIn("com.geopista.ui.snap.GeopistaInstallGridPlugIn");
    geopistaEditor1.addPlugIn("com.geopista.ui.plugin.GeopistaOptionsPlugIn");
    geopistaEditor1.addPlugIn("com.vividsolutions.jump.workbench.ui.plugin.skin.InstallSkinsPlugIn");
    geopistaEditor1.showLayerName(false);
 
    this.setLocation(150,50);
    this.setVisible(true);

    //CAMBIO DE LO DE LAS CAPAS
    ResourceBundle RB = ResourceBundle.getBundle("GeoPista");
    String directorioMsg = RB.getString("url.capas");
/*    Layer layer07 = geopistaEditor1.loadData(directorioMsg + "numeros_policia.jml","Catastro");
    layer07.setActiva(false);
    layer07.addStyle(new BasicStyle(new Color(255,255,255)));

    Layer layer08 = geopistaEditor1.loadData(directorioMsg + "tramosvia.jml","Catastro");
    layer08.setActiva(false);
    layer08.addStyle(new BasicStyle(new Color(64,64,64)));

    Layer layer09 = geopistaEditor1.loadData(directorioMsg + "vias.jml","Catastro");
    layer09.setActiva(true);
    layer09.addStyle(new BasicStyle(new Color(220,220,220)));*/

    GeopistaLayer layer01 = (GeopistaLayer) geopistaEditor1.loadData(directorioMsg + "parcelas.jml","Catastro");    
    layer01.setActiva(true);
    layer01.addStyle(new BasicStyle(new Color(255,255,128)));
    layer01.setVisible(true);

/*    Layer layer02 = geopistaEditor1.loadData(directorioMsg + "nucleos_y_diseminados.jml","Catastro");    
    layer02.setActiva(true);
    BasicStyle estilo = new BasicStyle(new Color(220,220,220));
    estilo.setAlpha(50);
    estilo.setLineColor(new Color(0,0,128));

    layer02.setVisible(false);
    layer02.addStyle((Style) estilo);*/
    

   //Layer layer03 = geopistaEditor1.loadData(directorioMsg + "seccionespostales.jml","Catastro");        
    
/*    GeopistaPostgreCon parcelas = new GeopistaPostgreCon();
    ArrayList recojoParcelas = parcelas.ListadoCargos();
    Iterator alIt = recojoParcelas.iterator();
    while (alIt.hasNext()) 
    {
      //padronlst.add(alIt.next());
      padronlst.addElement(alIt.next());
      //System.out.println(alIt.next() + " ");
    }*/
    txtCoefPropiedad.setBounds(new Rectangle(95, 235, 100, 20));
    txtClaveGrupo.setBounds(new Rectangle(80, 95, 35, 20));
    txtLetra2.setBounds(new Rectangle(100, 165, 25, 20));
    jTabbedPane1.setBounds(new Rectangle(20, 80, 390, 400));

    this.setSize(new Dimension(573, 460));
    //layer03.setActiva(true);
    //layer03.addStyle(new BasicStyle(new Color(220,32,0)));
    jScrollPane1.getViewport().add(padronModel, null);
    this.add(jScrollPane1, null);
    this.add(geopistaEditor1, null);   
    this.add(jLabel2, null);
    this.add(referencialbl, null);
    this.add(txtReferencia, null);
    this.add(jTabbedPane1, null);
    
    geopistaEditor1.addGeopistaListener(new GeopistaListener(){

      public void selectionChanged(IAbstractSelection abtractSelection)
      {
         try
         {
          ArrayList featuresCollection = (ArrayList)abtractSelection.getFeaturesWithSelectedItems();
          Iterator featuresCollectionIter = featuresCollection.iterator();
          Feature actualFeature = (Feature) featuresCollectionIter.next();
          
          referenciacatastral = actualFeature.getString("Referencia_Catastral");
          System.out.println(referenciacatastral);
          txtReferencia.setText(referenciacatastral);
          geopistaEditor1.zoomTo(actualFeature);
          GeopistaCatastroPostgreCon parcelas = new GeopistaCatastroPostgreCon();    
          ArrayList Cargos =  parcelas.Cargos(referenciacatastral);
          Iterator alIt = Cargos.iterator();
          padronlst.clear();
          jTabbedPane1.setSelectedIndex(0);
          while (alIt.hasNext()) 
          {
              padronlst.addElement(alIt.next());
          }
          }catch(Exception e)
          {
           e.printStackTrace();
          }   
       }
      
      public void featureAdded(FeatureEvent e)
      {
          System.out.println("Recibiendo en cliente evento de nueva Feature: "+e.getType());
      }

      public void featureRemoved(FeatureEvent e)
      {
          System.out.println("Recibiendo en cliente evento de borrado de Feature: "+e.getType());
      }

      public void featureModified(FeatureEvent e)
      {
        System.out.println("Recibiendo en cliente evento de Modificacion de Feature: "+e.getType());
      }

      public void featureActioned(IAbstractSelection abtractSelection)
      {
        System.out.println("Recibiendo en cliente evento de cambio de accion en feature: "+abtractSelection.getSelectedItems());
      }

    });

    
    MouseListener mouseListener = new MouseAdapter() {
       public void mouseClicked(MouseEvent e) {
        jTabbedPane1.setSelectedIndex(0);
        if (padronModel.getSelectedValue()==null)return;
        String cargo =padronModel.getSelectedValue().toString();
        String Construcc = txtReferencia.getText();
        GeopistaCatastroPostgreCon DatosId = new GeopistaCatastroPostgreCon();
        ArrayList Datos=DatosId.DatosIdentificacion(Construcc, cargo);
        Iterator alIt = Datos.iterator();
        ArrayList Controles= new ArrayList();
        Controles.add(txtdistrito);
        Controles.add(txtcargo);
        Controles.add(txtcontrol1cargo);
        Controles.add(txtcontrol2cargo);
        Controles.add(txtparticipacioncargo);
        Controles.add(txtnumerofijo);
        Iterator itControles = Controles.iterator();
        
        while (alIt.hasNext()) 
        {
            try
            {

              JComponent comp=(JComponent)itControles.next();
              Object obj=alIt.next();
               if (comp instanceof JTextField)((JTextField)comp).setText( (obj!=null)?obj.toString():"");
               if (comp instanceof JCheckBox){
                  String check = alIt.next().toString();
                  if (check == "TRUE"){
                    ((JCheckBox)comp).setSelected(true);}
                  else{
                    ((JCheckBox)comp).setSelected(false);}}
               if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
            }
          catch(Exception A)
            {
                A.printStackTrace();
            }
}
    }
    };
    padronModel.addMouseListener(mouseListener);

    ChangeListener mousepanel = new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
      	if (padronModel.getSelectedValue()==null)return;
        String cargo =padronModel.getSelectedValue().toString();
        String Construcc = txtReferencia.getText() ;
          if (jTabbedPane1.getSelectedIndex()==0) //Datos de Identificación
          {
            GeopistaCatastroPostgreCon DatosId = new GeopistaCatastroPostgreCon();
            ArrayList Datos=DatosId.DatosIdentificacion(Construcc, cargo);
            Iterator alIt = Datos.iterator();
            ArrayList Controles= new ArrayList();
            Controles.add(txtdistrito);
            Controles.add(txtcargo);
            Controles.add(txtcontrol1cargo);
            Controles.add(txtcontrol2cargo);
            Controles.add(txtparticipacioncargo);
            Controles.add(txtnumerofijo);
            Iterator itControles = Controles.iterator();
            while (alIt.hasNext()) 
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
              if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
                }
              catch(Exception A)
                {
                    A.printStackTrace();
                }
            }
        }else if (jTabbedPane1.getSelectedIndex()==1) //Datos de Localización
          {

            GeopistaCatastroPostgreCon DatosId = new GeopistaCatastroPostgreCon();
            ArrayList Datos=DatosId.DatosLocalizacion(Construcc, cargo);
            Iterator alIt = Datos.iterator();
            ArrayList Controles= new ArrayList();
            Controles.add(txtEntidad);
            Controles.add(txtCP);
            Controles.add(txtNumero1);
            Controles.add(txtLetra1);
            Controles.add(txtNumero2);
            Controles.add(txtLetra2);
            Controles.add(txtBloque);
            Controles.add(txtKilometro);
            Controles.add(txtDireccion);
            Controles.add(txtEscalera);
            Controles.add(txtPlanta);
            Controles.add(txtPuerta);
            Controles.add(txtCodigo);
            Controles.add(txtTipo);
            Controles.add(txtNombre);
            Iterator itControles = Controles.iterator();
            while (alIt.hasNext()) 
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
             if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
                }
              catch(Exception A)
                {
                    A.printStackTrace();
                }
            }
          }else if (jTabbedPane1.getSelectedIndex()==2) //Datos del Titular
          {
            
            GeopistaCatastroPostgreCon DatosId = new GeopistaCatastroPostgreCon();
            ArrayList Datos=DatosId.DatosTitular(Construcc, cargo);
            Iterator alIt = Datos.iterator();
            ArrayList Controles= new ArrayList();
            Controles.add(txtNif);
            Controles.add(txtLetraNif);
            Controles.add(txtPersonalidad);
            Controles.add(txtTitular);
            Iterator itControles = Controles.iterator();
            while (alIt.hasNext()) 
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
             if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
                }
              catch(Exception A)
                {
                    A.printStackTrace();
                }
            }
          }else if (jTabbedPane1.getSelectedIndex()==3) //Datos de Localización
          {

            GeopistaCatastroPostgreCon DatosId = new GeopistaCatastroPostgreCon();
            ArrayList Datos=DatosId.DatosLocalizacionTitular(Construcc, cargo);
            Iterator alIt = Datos.iterator();
            ArrayList Controles= new ArrayList();
            Controles.add(txtDelegacion);
            Controles.add(txtMunicipioDGC);
            Controles.add(txtProvincia);
            Controles.add(txtMunicipioINE);
            Controles.add(txtCPTitular);
            Controles.add(txtNumero1titular);
            Controles.add(txtLetra1titular);
            Controles.add(txtNumero2titular);
            Controles.add(txtLetra2titular);
            Controles.add(txtBloquetitular);
            Controles.add(txtKilometrotitular);
            Controles.add(txtDirecciontitular);
            Controles.add(txtEscaleraTitular);
            Controles.add(txtPlantaTitular);
            Controles.add(txtPuertaTitular);
            Controles.add(txtApartado);
            Controles.add(txtPais);
            Controles.add(txtNombreProvincia);
            Controles.add(txtNombreMunicipio);
            Controles.add(txtCodviatitular);
            Controles.add(txtTipoViaTitular);
            Controles.add(txtNombreviatitular);
            Iterator itControles = Controles.iterator();
            String Hola ="";
            while (alIt.hasNext()) 
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
             if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");

                }
              catch(Exception A)
                {
                    A.printStackTrace();
                }
            }
          }else if (jTabbedPane1.getSelectedIndex()==4) //Datos Económicos
          {

            GeopistaCatastroPostgreCon DatosId = new GeopistaCatastroPostgreCon();
            ArrayList Datos=DatosId.DatosEconomicos(Construcc, cargo);
            Iterator alIt = Datos.iterator();
            ArrayList Controles= new ArrayList();
            Controles.add(txtannovalor);
            Controles.add(txtValorCatastral);
            Controles.add(txtValorCons);
            Controles.add(txtValorSuelo);
            Controles.add(txtClaveGrupo);
            Controles.add(txtBaseLiquidable);
            Controles.add(txtAnnoRevision);
            Controles.add(txtAnnoNotificacion);
            Controles.add(txtNotificacion);
            Controles.add(txtSuperficieCons);
            Controles.add(txtSuperficieSuelo);
            Controles.add(txtCoefPropiedad);
            Iterator itControles = Controles.iterator();
            while (alIt.hasNext()) 
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
             if (comp instanceof JComboBox)((JComboBox)comp).setSelectedItem((obj!=null)?obj.toString():"");
                }
              catch(Exception A)
                {
                    A.printStackTrace();
                }
            }
          }
      }
    };
    txtannovalor.setEditable(false);
    txtValorCatastral.setEditable(false);
    txtValorSuelo.setEditable(false);
    txtBaseLiquidable.setEditable(false);
    txtAnnoNotificacion.setEditable(false);
    txtSuperficieSuelo.setEditable(false);
    txtCoefPropiedad.setEditable(false);
    txtSuperficieCons.setEditable(false);
    txtNotificacion.setEditable(false);
    txtClaveGrupo.setEditable(false);
    txtAnnoRevision.setEditable(false);
    txtValorCons.setEditable(false);
    txtDirecciontitular.setEditable(false);
    txtEscaleraTitular.setEditable(false);
    txtKilometrotitular.setEditable(false);
    txtNumero2titular.setEditable(false);
    txtCPTitular.setEditable(false);
    txtTipoViaTitular.setEditable(false);
    txtMunicipioINE.setEditable(false);
    txtMunicipioDGC.setEditable(false);
    txtProvincia.setEditable(false);
    txtDelegacion.setEditable(false);
    txtCodviatitular.setEditable(false);
    txtNombreviatitular.setEditable(false);
    txtLetra1titular.setEditable(false);
    txtBloquetitular.setEditable(false);
    txtLetra2titular.setEditable(false);
    txtNumero1titular.setEditable(false);
    txtPlantaTitular.setEditable(false);
    txtPuertaTitular.setEditable(false);
    txtApartado.setEditable(false);
    txtPais.setEditable(false);
    txtNombreProvincia.setEditable(false);
    txtNombreMunicipio.setEditable(false);
    txtNif.setEditable(false);
    txtPersonalidad.setEditable(false);
    txtLetraNif.setEditable(false);
    txtTitular.setEditable(false);
    txtEntidad.setEditable(false);
    txtCodigo.setEditable(false);
    txtNombre.setEditable(false);
    txtTipo.setEditable(false);
    txtLetra1.setEditable(false);
    txtNumero2.setEditable(false);
    txtKilometro.setEditable(false);
    txtBloque.setEditable(false);
    txtLetra2.setEditable(false);
    txtNumero1.setEditable(false);
    txtCP.setEditable(false);
    txtDireccion.setEditable(false);
    txtPlanta.setEditable(false);
    txtPuerta.setEditable(false);
    txtEscalera.setEditable(false);
    txtcontrol1cargo.setEditable(false);
    txtcontrol2cargo.setEditable(false);
    txtcargo.setEditable(false);
    txtparticipacioncargo.setEditable(false);
    txtnumerofijo.setEditable(false);
    txtdistrito.setEditable(false);
    
    jScrollPane1.setBounds(new Rectangle(25, 25, 165, 50));   
    txtPersonalidad.setBounds(new Rectangle(105, 55, 145, 20));
    jTabbedPane1.addChangeListener(mousepanel);
    
  }
 
  public void enteredFromLeft(Map dataMap)
  {
  
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
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
      return "1";
    }
    public void setWizardContext(WizardContext wd)
    {
    }
    public String getInstructions()
    {
     return " ";
    }

    public boolean isInputValid()
    {
      return true;
    }

    private String nextID=null;
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }


    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {

        return nextID;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }

  


} 