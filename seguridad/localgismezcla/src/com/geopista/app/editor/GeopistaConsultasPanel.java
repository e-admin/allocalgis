
/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/

package com.geopista.app.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.geopista.app.AppContext;

public class GeopistaConsultasPanel extends JPanel
{
  private JButton btnAceptar = new JButton();
  private JPanel pnlCampos = new JPanel();
  private JLabel lblNombre = new JLabel();
  private JTextField txtNombre = new JTextField();
  private JLabel lblDescripcion = new JLabel();
  private JTextField txtDescripcion = new JTextField();
  private JCheckBox chkNegar = new JCheckBox();
  private JLabel lblCampo = new JLabel();
  private JComboBox cmbCampo = new JComboBox();
  private JLabel lblOperador = new JLabel();
  private JComboBox cmbOperador = new JComboBox();
  private JLabel lblValor = new JLabel();
  private JLabel lblEnlace = new JLabel();
  private JComboBox cmbEnlace = new JComboBox();
  private JSeparator jSeparator1 = new JSeparator();
  private JLabel lblNegar = new JLabel();
  private JTextField txtValor = new JTextField();
 
  private JLabel lblTitulo = new JLabel();
  private JScrollPane scpConsultas = new JScrollPane();
  private JButton btnSeleccionar = new JButton();
  private JButton btnNueva = new JButton();
  private JButton btnEliminar = new JButton();
  private JButton btnModificar = new JButton();
  private JLabel lblCondiciones = new JLabel();
  private JButton btnMas = new JButton();
  private JButton btnElimcruz = new JButton();
  private JButton btnAnterior = new JButton();
  private JButton btnTerminar = new JButton();
  private JButton btnCancelar = new JButton();
  private DefaultListModel modeloList = new DefaultListModel();
  private JList lstConsultas = new JList(modeloList); 
  private DefaultTableModel model = new DefaultTableModel();
  private  AppContext aplicacion = (AppContext) AppContext.getApplicationContext(); 
  private JTable tblTabla = new JTable(model);

  


  public GeopistaConsultasPanel()
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
    pnlCampos.setLayout(null);
    this.setSize(new Dimension(566, 555));
    pnlCampos.setBounds(new Rectangle(10, 150, 550, 410));
    
    pnlCampos.setBorder(BorderFactory.createTitledBorder(aplicacion.getI18nString("consultas.app.editor.parametros")));
     
    model.addColumn((String)aplicacion.getI18nString("consultas.app.editor.tabla.enlace"));
    model.addColumn((String)aplicacion.getI18nString("consultas.app.editor.tabla.negado"));
    model.addColumn((String)aplicacion.getI18nString("consultas.app.editor.tabla.campo"));
    model.addColumn((String)aplicacion.getI18nString("consultas.app.editor.tabla.operador"));
    model.addColumn((String)aplicacion.getI18nString("consultas.app.editor.tabla.valor"));


     lblNombre.setText(aplicacion.getI18nString("consultas.app.editor.nombre"));

    lblNombre.setBounds(new Rectangle(15, 0, 445, 65));
//    txtNombre.setText("Consulta1");
    txtNombre.setBounds(new Rectangle(85, 35, 445, 20));
    lblDescripcion.setText(aplicacion.getI18nString("consultas.app.editor.descripcion"));
    lblDescripcion.setBounds(new Rectangle(15, 65, 100, 20));
//    txtDescripcion.setText("Descripción Consulta 1");
    txtDescripcion.setBounds(new Rectangle(100, 65, 430, 20));
   
    lblCampo.setText(aplicacion.getI18nString("consultas.app.editor.campo"));
    lblCampo.setBounds(new Rectangle(15, 130, 80, 15));
    
    lblOperador.setText(aplicacion.getI18nString("consultas.app.editor.operador"));
    lblOperador.setBounds(new Rectangle(310, 125, 90, 20));
   
    lblValor.setText(aplicacion.getI18nString("consultas.app.editor.valor"));
    lblValor.setBounds(new Rectangle(15, 155, 55, 20));

    lblEnlace.setText(aplicacion.getI18nString("consultas.app.editor.enlace"));
    lblEnlace.setBounds(new Rectangle(15, 190, 190, 20));
    
    jSeparator1.setBounds(new Rectangle(10, 220, 520, 2));
    lblNegar.setText(aplicacion.getI18nString("consultas.app.editor.negar"));
    chkNegar.setBounds(new Rectangle(310, 155, 25, 15));
    lblNegar.setBounds(new Rectangle(335, 155, 115, 15));

  //  txtValor.setText("5");
  
    txtValor.setBounds(new Rectangle(75, 155, 205, 20));
    lblTitulo.setText(aplicacion.getI18nString("consultas.app.editor.titulo.ventana"));
    lblTitulo.setBounds(new Rectangle(20, 15, 305, 20));


   //RELLENAR LISTA
  /* String[] Elementos = {"Consulta 1", "Consulta 2", "Consulta 3", "Consulta 4"}; 
   lstConsultas = new JList(Elementos);*/

   
   ArrayList listaConsultas = GeopistaListaConsultas();
   Iterator eIter = listaConsultas.iterator();
   while (eIter.hasNext())
    {
       modeloList.addElement((String) eIter.next());
    }
     
    
  

    scpConsultas.getViewport().add(lstConsultas,null);  
    scpConsultas.setBounds(new Rectangle(20, 45, 530, 60));

    //RELLENAR COMBOS
    cmbOperador = new JComboBox();
 
    cmbOperador.addItem(" <> ");
    cmbOperador.addItem(" > ");
    cmbOperador.addItem(" < ");
    cmbOperador.addItem(" = ");
       
    cmbOperador.setBounds(new Rectangle(385, 125, 145, 20));
    cmbOperador.setBackground(new Color(255,255,255));    

    cmbCampo = new JComboBox();
    
    GeopistaConsultasPostgreCon consultasConexion = new GeopistaConsultasPostgreCon();

    ArrayList campos = consultasConexion.nombreCamposDeLayer("Provincias");

    Iterator itCampos = campos.iterator();
    //Cargamos los campos en el combo
    cmbCampo.removeAllItems();
    while (itCampos.hasNext()){
      cmbCampo.addItem((String) itCampos.next());
    }


    cmbCampo.setBounds(new Rectangle(75, 125, 205, 20));
    cmbCampo.setBackground(new Color(255,255,255));

    cmbEnlace = new JComboBox();
    cmbEnlace.addItem("");
    cmbEnlace.addItem("And");
    cmbEnlace.addItem("Or");
    
    cmbEnlace.setBackground(new Color(255,255,255));
    cmbEnlace.setBounds(new Rectangle(195, 190, 115, 20));

    btnSeleccionar.setText(aplicacion.getI18nString("consultas.app.editor.limpiar.ventana"));
    btnSeleccionar.setBounds(new Rectangle(30, 110, 140, 25));
    btnNueva.setText(aplicacion.getI18nString("consultas.app.editor.nueva"));
    btnNueva.setBounds(new Rectangle(185, 110, 95, 25));
    btnEliminar.setText(aplicacion.getI18nString("consultas.app.editor.eliminar"));
    btnEliminar.setBounds(new Rectangle(290, 110, 100, 25));
    btnModificar.setText(aplicacion.getI18nString("consultas.app.editor.modificar"));
    btnModificar.setBounds(new Rectangle(400, 110, 105, 25));
    lblCondiciones.setText(aplicacion.getI18nString("consultas.app.editor.condiciones"));
    lblCondiciones.setBounds(new Rectangle(15, 95, 120, 15));

                                  
 //     final Object[][] info ={
 //     {"AND", "No", "Longitud", "Mayor que", "5", new Integer(6)},
 //     {"OR", "Si", "Nombre", "d", "L", "6"} 
    //  {new Integer(950), "DE", "S", "CA", "N", "SO"},
    //  {new Integer(1020), "Filosofía", "Física", "Ingles C.", "L. Castellana", "Calculo"}
  //    {new Integer(1200), "DE", "S", "CA", "N", "SO"}
//      {new Integer(1230), "Filosofía", "Calculo", "Química", "Ed. Física", "Talento"}
//	  };

      //JTable tblTabla = new JTable(info, columnNames);
      
     // table1.setPreferredScrollableViewportSize();
     //si pongo a 0 hace la tabla + pequeña pero muestra el scroll
     //si pongo a 1 mueve las columnas pero no muestra el scroll
      tblTabla.setAutoResizeMode(1); //no ajuste las columnas al tamaño dado y ponga el scroll
      tblTabla.setBackground(Color.white);
      tblTabla.setVisible(true);
     cmbEnlace.setSelectedIndex(-1);
     cmbOperador.setSelectedIndex(-1);
     txtValor.setText(null);
	  JScrollPane scpTabla = new JScrollPane(tblTabla);
    btnSeleccionar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnSeleccionar_actionPerformed(e);
        }
      });
    btnModificar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnModificar_actionPerformed(e);
        }
      });
    btnNueva.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnNueva_actionPerformed(e);
        }
      });

    
    btnEliminar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnEliminar_actionPerformed(e);
        }
      });
      
    cmbCampo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cmbCampo_actionPerformed(e);
        }
      });
    tblTabla.setBounds(new Rectangle(15, 260, 525, 150));
    tblTabla.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          tblTabla_mouseClicked(e);
        }
      });
    scpTabla.setBounds(new Rectangle(10, 260, 520, 105));

   // btnElimcruz.setIcon(IconLoader.icon("eliminar.gif"));
    
    btnCancelar.setText(aplicacion.getI18nString("consultas.app.editor.cancelar"));
    btnCancelar.setBounds(new Rectangle(325, 370, 95, 25));
    lstConsultas.addMouseListener(new MouseAdapter()
      {
        public void mousePressed(MouseEvent e)
        {
          lstConsultas_mousePressed(e);
        }
      });

    
    btnTerminar.setText(aplicacion.getI18nString("consultas.app.editor.terminar"));
    btnTerminar.setBounds(new Rectangle(215, 370, 95, 25));
    btnAnterior.setText(aplicacion.getI18nString("consultas.app.editor.anterior"));
    btnAnterior.setBounds(new Rectangle(105, 370, 95, 25));
    btnElimcruz.setBounds(new Rectangle(260, 225, 30, 25));
    btnElimcruz.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnElimcruz_actionPerformed(e);
        }
      });

    btnMas.setBounds(new Rectangle(210, 225, 40, 25));
    btnMas.setText("+");
    btnMas.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnMas_actionPerformed(e);
        }
      });

    pnlCampos.add(btnCancelar, null);
    pnlCampos.add(btnTerminar, null);
    pnlCampos.add(btnAnterior, null);
    pnlCampos.add(btnElimcruz, null);
    pnlCampos.add(btnMas, null);
    scpTabla.getViewport().add(tblTabla, null);
    pnlCampos.add(scpTabla, null);
    pnlCampos.add(lblCondiciones, null);
    pnlCampos.add(txtValor, null);
    pnlCampos.add(jSeparator1, null);
    pnlCampos.add(cmbEnlace, null);
    pnlCampos.add(lblEnlace, null);
    pnlCampos.add(lblValor, null);
    pnlCampos.add(cmbOperador, null);
    pnlCampos.add(lblOperador, null);
    pnlCampos.add(cmbCampo, null);
    pnlCampos.add(lblCampo, null);
    pnlCampos.add(lblDescripcion, null);
    pnlCampos.add(txtNombre, null);
    pnlCampos.add(lblNombre, null);
    pnlCampos.add(txtDescripcion, null);
    pnlCampos.add(lblNegar, null);
    pnlCampos.add(chkNegar, null);
    scpConsultas.getViewport().add(lstConsultas, null);
    this.add(btnModificar, null);
    this.add(btnEliminar, null);
    this.add(btnNueva, null);
    this.add(btnSeleccionar, null);
    this.add(scpConsultas, null);
    this.add(lblTitulo, null);
    this.add(pnlCampos, null);
    this.add(btnAceptar, null);
  }

  private void cmbCampo_actionPerformed(ActionEvent e)
  {
  }



/**
 * Obtiene las consultas del fichero del Xml que hay almacendas.
 */

public ArrayList GeopistaListaConsultas() {
    // Recuperamos el fichero de las consultas.
    ArrayList lista = new ArrayList();

    // Recorrer el xml para traer todos los nombres de las consultas.
  try {



    

    String ruta = aplicacion.getPath("ruta.fichero.consultas.alfanumericas");  

    SAXBuilder builder = new SAXBuilder(false);
    Document doc=builder.build(new File(ruta));
      // Pillamos el raiz del Xml.
       Element raiz = doc.getRootElement();

      java.util.List consultas = raiz.getChildren("consulta");
      Iterator i = consultas.iterator();
        while (i.hasNext()) 
          {
          // procesamos tosas las consultas
             Element tipo = (Element) i.next();
             lista.add((String) tipo.getAttributeValue("nombre"));
          }
      
  }catch (Exception e) {
      e.printStackTrace();
    }  
    return lista;  
    }//del Método GeopistaListaConsultas

  private void lstConsultas_mousePressed(MouseEvent e)
  {
    txtNombre.setEnabled(true);

    String ruta = aplicacion.getString("ruta.fichero.consultas.alfanumericas");  
    Element raiz=null;

    


    try {
    SAXBuilder builder = new SAXBuilder(false);
    Document doc=builder.build(new File(ruta));
      // Pillamos el raiz del Xml.
       raiz = doc.getRootElement();
    }catch(Exception ed){
      ed.printStackTrace();
    }

    //Leer el Nombre 
    txtNombre.setText((String) lstConsultas.getSelectedValue());
    //Leer la Descripcion
    txtDescripcion.setText(obtenerDescricionConsulta(raiz,txtNombre.getText()));

    //traemos en un ArrayList todos los valores de las filas que tiene la consulta.

    for (int p=tblTabla.getRowCount()-1;p>=0;p--){
          model.removeRow(p);
      }
    ArrayList afilas = GeopistaListaFilas(raiz,txtNombre.getText());

    //iterar el arraylist y poner en la tabla los elementos
    Iterator iList = afilas.iterator();
    int numerofilas=0;
    while (iList.hasNext()){
      Geopistafilas geofilas = (Geopistafilas) iList.next();
      numerofilas = tblTabla.getRowCount();
      Object[] cadena = new Object[5];
      cadena[0]=geofilas.getEnlace();
      cadena[1]=geofilas.getNegado();
      cadena[2]=geofilas.getCampo();
      cadena[3]=geofilas.getOperador();
      cadena[4]=geofilas.getValor();
      model.insertRow(numerofilas,cadena);


    }

    
  }

/**
 * Devuelve la descripción de la consulta
 */
public String obtenerDescricionConsulta(Element raiz, String nombre){
  String resultado ="";
  java.util.List consultas = raiz.getChildren("consulta");
  Iterator i = consultas.iterator();
   while (i.hasNext()) 
          {
          // procesamos tosas las consultas
             Element tipo = (Element) i.next();
             if ((tipo.getAttributeValue("nombre")).equals(nombre)){
               Element descripcion = tipo.getChild("descripcion");
               resultado = descripcion.getText();
               break;
             }
          }

  return resultado;
  
}//Del Metodo obtenerDescripcionConsulta

/**
 * Devolverá un arrayList con todas las filas de esa consulta.
 * Se usa la clase filas 
 **/ 
public ArrayList GeopistaListaFilas(Element raiz,String nombre){
  ArrayList resultado = new ArrayList();
  java.util.List consultas = raiz.getChildren("consulta");
  Iterator i = consultas.iterator();

   while (i.hasNext()) 
          {
          // procesamos tosas las consultas
             Element tipo = (Element) i.next();
             if ((tipo.getAttributeValue("nombre")).equals(nombre)){
               Element filas = tipo.getChild("filas");
               //Encontramos las componentes de las tablas. 
               java.util.List listaFilas = filas.getChildren("fila");
               Iterator i2 = listaFilas.iterator();
               while (i2.hasNext()){
                      Element fila = (Element)i2.next();

                      Geopistafilas clasefilas = new Geopistafilas();
                      Element enlace = fila.getChild("enlace");
                      Element negado = fila.getChild("negado");
                      Element campo = fila.getChild("campo");
                      Element operador = fila.getChild("operador");
                      Element valor = fila.getChild("valor");
                      
                      clasefilas.setOrden(fila.getAttributeValue("id"));
                      clasefilas.setEnlace(enlace.getText());
                      clasefilas.setNegado(negado.getText());
                      clasefilas.setValor(valor.getText());
                      clasefilas.setOperador(operador.getText());
                      clasefilas.setCampo(campo.getText());

                      resultado.add(clasefilas);
                  

               }
               
               break;
             }
          }

  return resultado;

}//del Método GeopistaListaFilas

  private void btnMas_actionPerformed(ActionEvent e)
  {
    //Recogemos los valores que existan en los elementos de la interface 
    //añadirlo a la tabla

    
    int filas = tblTabla.getRowCount();


    Object[] cadena = new Object[5];

    if (filas==0){
      if ((cmbCampo.getSelectedIndex())==-1)
      {

       JOptionPane.showMessageDialog(this, aplicacion.getI18nString("consultas.app.editor.seleccione.un.campo")); 
      }else{
        if ((cmbOperador.getSelectedItem())==null){
            JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.seleccione.un.operador"));
        }else{
            if ((txtValor.getText()).equals("")){
              JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.introduzca.un.valor"));
            }else{
            cadena[0]="";
            if(chkNegar.isSelected()){
              cadena[1]="S";
            }else{
              cadena[1]="N";
            };
          
              cadena[2] = cmbCampo.getSelectedItem();
              cadena[3] = cmbOperador.getSelectedItem();
              cadena[4] = txtValor.getText();
              model.insertRow(filas,cadena);
              cmbEnlace.setSelectedIndex(-1);
              cmbOperador.setSelectedIndex(-1);
              txtValor.setText(null);

            }
        } 
      }
    }else{
      //Hay mas de una fila y hay que hacer comprobaciones
      if ((cmbCampo.getSelectedIndex())==-1)
      {
        JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.seleccione.un.campo")); 
      }else{
        if ((cmbOperador.getSelectedItem())==null){
            JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.seleccione.un.operador"));
        }else{
           if ((cmbEnlace.getSelectedItem())==null){
            JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.seleccione.un.enlace"));
           }else{
            if ((txtValor.getText()).equals("")){
              JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.introduzca.un.valor"));
            }else{
                 cadena[0] = cmbEnlace.getSelectedItem();
                  if(chkNegar.isSelected()){
                    cadena[1]="S";
                  }else{
                    cadena[1]="N";
                  }
              cadena[2] = cmbCampo.getSelectedItem();
              cadena[3] = cmbOperador.getSelectedItem();
              cadena[4] = txtValor.getText();
              model.insertRow(filas,cadena);
              cmbEnlace.setSelectedIndex(-1);
              cmbOperador.setSelectedIndex(-1);
              txtValor.setText(null);


            }
           }
        }
       }
      } //De las filas 
    
  }

  private void btnElimcruz_actionPerformed(ActionEvent e)
  {
    if (tblTabla.getSelectedRow()==-1){
      JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.seleccione.una.fila.borrar"));
    }else{
      
      model.removeRow(tblTabla.getSelectedRow());
      int filas = tblTabla.getRowCount();
      if (filas>0) {
          String enlace = (String) tblTabla.getValueAt(0,0);
          if (enlace!=null) {
            tblTabla.setValueAt("",0,0);
          }
          
      }
    }
  }

  private void tblTabla_mouseClicked(MouseEvent e)
  {
    //Inicializamos
    cmbCampo.setSelectedIndex(-1);
    cmbOperador.setSelectedIndex(-1);
    cmbEnlace.setSelectedIndex(-1);
    txtValor.setText(null);
    chkNegar.setSelected(false);
    //Seleccionamos la fila
    int fila =tblTabla.getSelectedRow();
    String enlace = (String) tblTabla.getValueAt(fila,0);
    String negado = (String) tblTabla.getValueAt(fila,1);
    String campo = (String) tblTabla.getValueAt(fila,2);
    String Operador = (String) tblTabla.getValueAt(fila,3);
    String valor = (String) tblTabla.getValueAt(fila,4);

    //Colocamos los valores 
    //Enlace
    for (int i=0;i<cmbEnlace.getItemCount();i++)
    {
      if (enlace.equals(cmbEnlace.getItemAt(i))){
          cmbEnlace.setSelectedIndex(i);
          break;
      }
    }

    //Campos 
    for (int i=0;i<cmbCampo.getItemCount() ;i++)
    {
      if (campo.equals(cmbCampo.getItemAt(i))){
          cmbCampo.setSelectedIndex(i);
          break;
      }
    }

    //Operador
   for (int i=0;i<cmbOperador.getItemCount() ;i++)
    {
      if (Operador.equals(cmbOperador.getItemAt(i))){
          cmbOperador.setSelectedIndex(i);
          break;
      }
    }
    // Negar condición
    if (negado.equals("S")){
      chkNegar.setSelected(true);
    }else{
      chkNegar.setSelected(false);
      };
    // Valor
      txtValor.setText(valor);
  }

  private void btnEliminar_actionPerformed(ActionEvent e)
  {
    if (lstConsultas.getSelectedIndex()==-1){
       JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.seleccione.una.consulta.borrar"));
      
    }else{
        //Eliminamos la entrada de la lista
        String consulta = (String) lstConsultas.getSelectedValue();
        modeloList.removeElementAt(lstConsultas.getSelectedIndex());
        //Borramos la entrada del fichero
        borrarEntradaConsultasXml(consulta);
        txtNombre.setText(null);
        txtDescripcion.setText(null);
        
    }
  }

/**
 * Método que elimina una entrada de una consulta en el fichero de consultas xml
 * @param String Consulta, nombre de la consulta
 */
public void borrarEntradaConsultasXml(String consulta){
   String ruta = aplicacion.getString("ruta.fichero.consultas.alfanumericas");  

  try {
    SAXBuilder builder = new SAXBuilder(false);
    Document doc=builder.build(new File(ruta));
    Element raiz = doc.getRootElement();
    //sacar los hijos de la raiz.
    java.util.List  listaConsultas = raiz.getChildren("consulta");
    Iterator it = listaConsultas.iterator();

    while (it.hasNext()){
      Element consultaNombre = (Element) it.next();
      if (consulta.equals(consultaNombre.getAttributeValue("nombre"))){
          raiz.removeContent(consultaNombre);
          //Borrar las filas de la tabla
          String tmp = String.valueOf(tblTabla.getRowCount());
          //JOptionPane.showMessageDialog(this,tmp);
          for (int a=tblTabla.getRowCount()-1;a>=0;a--){
              model.removeRow(a);
          }
          //Borrar los campos
          cmbEnlace.setSelectedIndex(-1);
          cmbOperador.setSelectedIndex(-1);
          cmbCampo.setSelectedIndex(-1);
          txtValor.setText(null);
          chkNegar.setSelected(false);
          break;
      }
      
    }

    XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
    FileOutputStream file = new FileOutputStream(ruta);
    out.output(doc,file);

  }catch(Exception e){
      e.printStackTrace();
  }
  
}//Del método de BorrarConsulta en XML

/**
 * Devuelve un valor verdadero si el nombre de la consulta ya existe.
 * @param Stirng nombre, nombre de la consulta
 * @return boolean True si existe o false caso contrario
 */
public boolean existeNombreConsulta(String nombre){
  boolean resultado = false;
  for (int i=0;i<modeloList.getSize()-1;i++)
  {
    if (modeloList.getElementAt(i).equals(nombre)){
      resultado=true;
      break;
    }
  }
  return resultado;
}

  private void btnNueva_actionPerformed(ActionEvent e)
  {
    //Comprobación de campos obligatorios
  
    if((txtNombre.getText()).equals("")){
      JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.nombre.consulta"));
    }else{
      //Comprobamos que la descripción no sea nula
       if ((txtDescripcion.getText()).equals("")){
        JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.descripcion.consulta"));
       }else{
         //obtenemos el numero de filas de la tabla
         if (model.getRowCount()==0){
           JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.criterios"));
         }else{
            //Comprobamos que no exista el nombre de la consulta
           if (existeNombreConsulta(txtNombre.getText())){
           JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.consulta.duplicada"));
           }else{
            //Creamos una entrada en la lista
            modeloList.addElement((String) txtNombre.getText());
            //Creamos entrada en el fichero Xml.
            crearEntradaConsulta(txtNombre.getText());
            txtNombre.setText(null);
            txtDescripcion.setText(null);
            cmbCampo.setSelectedIndex(-1);
            for (int p=tblTabla.getRowCount()-1;p>=0;p--){
              model.removeRow(p);
            }

           }
         }
       }
    }
  }

/**
 * Clase que crea una entrada nueva en el fichero XML
 * @param String nombreConsulta,
 */
public void crearEntradaConsulta(String nombreConsulta){


    String ruta = aplicacion.getString("ruta.fichero.consultas.alfanumericas");  

  try {
    SAXBuilder builder = new SAXBuilder(false);
    Document doc=builder.build(new File(ruta));
    Element raiz = doc.getRootElement();
    //Vamos recuperando valores.
    Element consultaElemento = new Element("consulta");
    consultaElemento.setAttribute("nombre",nombreConsulta);
    //Descripción
    Element descripcion = new Element("descripcion");
    descripcion.setText(txtDescripcion.getText());
    consultaElemento.addContent(descripcion);
    
    Element filas = new Element("filas");
    String Sql="";
    //Filas de las tablas.
    for(int i=0;i<tblTabla.getRowCount();i++){
      Element fila = new Element("fila");
       fila.setAttribute("id",String.valueOf(i));
      Element enlace = new Element("enlace");
      enlace.setText((String)tblTabla.getValueAt(i,0));
      Element negado = new Element("negado");
      negado.setText((String)tblTabla.getValueAt(i,1));
      Element campo = new Element("campo");
      campo.setText((String)tblTabla.getValueAt(i,2));
      Element operador = new Element("operador");
     
      operador.setText((String)tblTabla.getValueAt(i,3));
      Element valor = new Element("valor");
      valor.setText((String)tblTabla.getValueAt(i,4));
      fila.addContent(enlace);
      fila.addContent(negado);
      fila.addContent(campo);
      fila.addContent(operador);
      fila.addContent(valor);
      //Evaluamos el enlace 
      String p1=""; // Enlace
      String p2=""; // Negado
      String p3=""; //Operador
      
      if ((enlace.getText()).equals("")){
          p1="";
      }else{
        if ((enlace.getText()).equals("Or")){
          p1=" OR ";
        }else{
          p1=" AND ";
        }
      }  
      if ((negado.getText()).equals("S")){
        p2=" NOT "; 
      } else{
        p2 ="";}
      //Operador 
      if ((operador.getText()).equals(" <> ")){
        p3=" <> ";
      }else{
        if ((operador.getText()).equals(" > ")){
          p3=" >= ";
        }else{
          if ((operador.getText()).equals(" < ")){
            p3 = " <= ";
          }else{
            p3 =" = ";
          }
        }
      }
      Sql = Sql + p1 + p2 + campo.getText() + p3 + valor.getText();
      filas.addContent(fila);
    }
    consultaElemento.addContent(filas);
    Element sentenciaSql = new Element("sql");
    CDATA datosCdata  = new CDATA(Sql);
    sentenciaSql.addContent(datosCdata);
    consultaElemento.addContent(sentenciaSql);
    raiz.addContent(consultaElemento);

    //Creamos el data
    
    XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
    FileOutputStream file = new FileOutputStream(ruta);
    out.output(doc,file);

  }catch(Exception e){
      e.printStackTrace();
  }
  
}//

  private void btnModificar_actionPerformed(ActionEvent e)
  {
    //Modificar es borrar una entrada y crearla de nuevo
    if (lstConsultas.getSelectedIndex()==-1){
       JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.seleccione.consulta.modificar"));
      
    }else{
        //Eliminamos la entrada de la lista
        if (tblTabla.getRowCount()!=0){
            String consulta = (String) lstConsultas.getSelectedValue();
            txtNombre.setEnabled(false);
            //Borramos la entrada del fichero
            borrarEntradaFilasConsultasXml(consulta);
            crearEntradaConsulta(consulta);

            txtNombre.setText(null);
            txtDescripcion.setText(null);
            cmbCampo.setSelectedIndex(-1);
            for (int p=tblTabla.getRowCount()-1;p>=0;p--){
              model.removeRow(p);
            }
            
        }else{
            JOptionPane.showMessageDialog(this,aplicacion.getI18nString("consultas.app.editor.criterios"));
        }

    }
  }//del Método


/**
 * Borra entrada de las filas en el fichero de consultas 
 * @param String consulta, Nombre de la consulta
 */
  public void borrarEntradaFilasConsultasXml(String consulta){
    String ruta = aplicacion.getString("ruta.fichero.consultas.alfanumericas");  
  try {
    SAXBuilder builder = new SAXBuilder(false);
    Document doc=builder.build(new File(ruta));
    Element raiz = doc.getRootElement();
    //sacar los hijos de la raiz.
    java.util.List  listaConsultas = raiz.getChildren("consulta");
    Iterator it = listaConsultas.iterator();

    while (it.hasNext()){
      Element consultaNombre = (Element) it.next();
      if (consulta.equals(consultaNombre.getAttributeValue("nombre"))){
          raiz.removeContent(consultaNombre);
          //Borrar las filas de la tabla
          cmbEnlace.setSelectedIndex(-1);
          cmbOperador.setSelectedIndex(-1);
          cmbCampo.setSelectedIndex(-1);
          txtValor.setText(null);
          chkNegar.setSelected(false);
          break;
      }
      
    }

    XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
    FileOutputStream file = new FileOutputStream(ruta);
    out.output(doc,file);

  }catch(Exception e){
      e.printStackTrace();
  }
  
}//Del método de BorrarConsulta en XML

  private void btnSeleccionar_actionPerformed(ActionEvent e)
  {
          lstConsultas.setSelectedIndex(-1);
          txtNombre.setText(null);
          txtDescripcion.setText(null);
          for(int h=tblTabla.getRowCount()-1;h>=0;h--){
            model.removeRow(h);
          }

          cmbEnlace.setSelectedIndex(-1);
          cmbOperador.setSelectedIndex(-1);
          cmbCampo.setSelectedIndex(-1);
          txtValor.setText(null);
          chkNegar.setSelected(false);
  
  }

} //De la clase 

 
