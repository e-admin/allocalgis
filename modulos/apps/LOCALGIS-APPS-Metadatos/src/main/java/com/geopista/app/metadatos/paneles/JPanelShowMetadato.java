/**
 * JPanelShowMetadato.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.metadatos.componentes.ArbolEntry;
import com.geopista.app.metadatos.componentes.TreeRendererMetadatos;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.app.metadatos.init.Constantes;
import com.geopista.app.metadatos.xml.MD_MetadataXML;
import com.geopista.app.metadatos.xml.XMLTranslationInfo;
import com.geopista.app.metadatos.xml.XMLTranslator;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.metadatos.MD_Metadata;
import com.geopista.protocol.metadatos.OperacionesMetadatos;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-ago-2004
 * Time: 12:17:22
 */
public class JPanelShowMetadato extends JPanelPrintable implements TreeSelectionListener, Printable {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelShowMetadato.class);
    private JFrame framePadre;
    private ResourceBundle messages;
    MD_Metadata metadato;
    boolean bprimera=true;
    private static Image cabecera= cargarCabecera();

    public JPanelShowMetadato (ResourceBundle messages, JFrame framePadre) {
        super();
        this.framePadre=framePadre;
        this.messages=messages;
        initComponents(messages);
        changeScreenLang(messages);
    }
    private void initComponents(ResourceBundle messages)
    {


            while (!Estructuras.isCargada())
            {
                if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
                logger.debug("Esperando a que se terminen de cargar las estructuras");
                try
                {
                    Thread.sleep(1000);
                }catch(Exception e){}
            }


            logger.debug("Inicio pantalla principal");

            metadatoJTabbedPane = new javax.swing.JTabbedPane();
            jPanelMetadato = new JPanelMetadato(messages,framePadre);
            jPanelIdentificacion = new JPanelIdentificacion(messages,framePadre);
            jPanelCalidad = new JPanelCalidad(messages,framePadre);
            jPanelDistribucion = new JPanelDistribucion(messages,framePadre);
            jPanelReferencia = new JPanelReferencia(messages,framePadre);
            jLabelFileIdentifier = new JLabel();
            jLabelFileIdentifier.setFont(new java.awt.Font("Arial", Font.BOLD, 12));
            jLabelFileIdentifier.setForeground(new Color(0,128,192));
            setLayout(new java.awt.BorderLayout());


            metadatoJTabbedPane.setFont(new java.awt.Font("Arial", 0, 10));

            logger.debug("JPanelMetadato");
            metadatoJTabbedPane.addTab(messages.getString("CMetadatos.jPanelMetadato"), jPanelMetadato);
            logger.debug("JPanelIdentificacion");
            metadatoJTabbedPane.addTab(messages.getString("CMetadatos.jPanelIdentificacion"), jPanelIdentificacion);
            logger.debug("JPanelReferencia");
            metadatoJTabbedPane.addTab(messages.getString("CMetadatos.jPanelReferencia"), jPanelReferencia);
            logger.debug("JPanelCalidad");
            metadatoJTabbedPane.addTab(messages.getString("CMetadatos.jPanelCalidad"), jPanelCalidad);
            logger.debug("JPanelDistribucion");
            metadatoJTabbedPane.addTab(messages.getString("CMetadatos.jPanelDistribucion"), jPanelDistribucion);

            metadatoJTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
            JPanel jPanelTotal = new JPanel();
            jPanelTotal.setLayout(new BorderLayout());
            jPanelTotal.add(metadatoJTabbedPane,java.awt.BorderLayout.CENTER);
            jPanelTotal.add(jLabelFileIdentifier,java.awt.BorderLayout.SOUTH);
            add(jPanelTotal,java.awt.BorderLayout.CENTER);



            jTreeMetadatos=new JTree(initArbol());
            jTreeMetadatos.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            jTreeMetadatos.setCellRenderer(new TreeRendererMetadatos());
            jTreeMetadatos.addTreeSelectionListener(this);
            jTreeMetadatos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            jScrollPaneArbol= new JScrollPane(jTreeMetadatos);
            jScrollPaneArbol.setMinimumSize(new Dimension(200,-1));
            jScrollPaneArbol.setPreferredSize(new Dimension(200,-1));
            add(jScrollPaneArbol, java.awt.BorderLayout.WEST);
            logger.debug("Fin de cracion pantalla");

    }

    private static  Image cargarCabecera()
    {
         if (cabecera!=null) return cabecera;
         URL urlpathgsImagen=null;
         try
         {
                urlpathgsImagen= new URL("file://"+System.getProperty("user.dir", ".")+File.separator+"img"+File.separator+"cabecera.gif");
                return Toolkit.getDefaultToolkit().getImage(urlpathgsImagen);

         }catch(Exception e)
         {
             logger.debug("Error al cargar la cabecera de impresión: "+urlpathgsImagen);
         }
         return null;

    }

    public void changeScreenLang(ResourceBundle messages) {
         this.messages=messages;

         jPanelMetadato.changeScreenLang(messages);
         jPanelIdentificacion.changeScreenLang(messages);
         jPanelCalidad.changeScreenLang(messages);
         jPanelReferencia.changeScreenLang(messages);
         jPanelDistribucion.changeScreenLang(messages);
         if (metadato!=null)
           jLabelFileIdentifier.setText(" "+messages.getString("CMetadatos.jLabelIdentificador")+" "+
                   (metadato.getFileidentifier()!=null?metadato.getFileidentifier():""));
         else
           jLabelFileIdentifier.setText(" "+messages.getString("CMetadatos.jLabelIdentificador"));

      }
      private void guardarCambios() throws Exception
      {
          if (metadato==null) metadato = new MD_Metadata();
          metadato.setLanguage_id(jPanelMetadato.getIdLanguage());
          metadato.setCharacterset(jPanelMetadato.getCharacterset());
          metadato.setDatestamp(jPanelMetadato.getDatestamp());
          metadato.setMetadatastandardname(jPanelMetadato.getStandardname());
          metadato.setMetadatastandardversion(jPanelMetadato.getStandardversion());
          metadato.setResponsibleParty(jPanelMetadato.getResponsibleParty());
          metadato.setRolecode_id(jPanelMetadato.getRolecode_id());
          metadato.setScopecode_id(jPanelMetadato.getScopecode_id());
          metadato.setId_capa(jPanelMetadato.getId_capa());
          metadato.setIdentificacion(jPanelIdentificacion.getIdentificacion());
          metadato.setFormatos(jPanelDistribucion.getFormatos());
          metadato.setOnlineresources(jPanelDistribucion.getRecursos());
          metadato.setCalidad(jPanelCalidad.getCalidad());
          metadato.setReference(jPanelReferencia.getReferencias());
      }

      public boolean save()
      {
           if (!com.geopista.security.SecurityManager.isLogged())
           {
               JOptionPane optionPane= new JOptionPane(messages.getString("JPanelShowMetadato.mensaje.nologin"),JOptionPane.ERROR_MESSAGE);
               JDialog dialog =optionPane.createDialog(this,"");
               dialog.show();
               return false;
           }
           try
           {
               if (!validar())return false;
               guardarCambios();
               CResultadoOperacion result=null;
               String sMensaje="";
               try
               {
                   if (metadato.getFileidentifier()==null)
                        sMensaje=messages.getString("JPanelShowMetadato.mensaje.metadatoinsertado");
                   else
                        sMensaje=messages.getString("JPanelShowMetadato.mensaje.metadatoactualizado");

                   result=(new OperacionesMetadatos(Constantes.url)).salvarMetadato(metadato);
                   logger.debug("ID del MD_DataIdentification salvado:"+(metadato.getIdentificacion()!=null?metadato.getIdentificacion().getIdentification_id():"No tengo identificacion"));
            }catch(Exception e)
              {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.error("Exception al grabar en base de datos un nuevo metadato: " + sw.toString());
                    result= new CResultadoOperacion(false,e.getMessage());
              }
              if (result.getResultado())
              {
                 try
                 {
                     logger.debug("ID del MD_DataIdentification que va a ser salvado:"+(metadato.getIdentificacion()!=null?metadato.getIdentificacion().getIdentification_id():"No tengo identificacion"));
                     metadato= (MD_Metadata)result.getVector().elementAt(0);
                     jPanelMetadato.setContacto(metadato.getResponsibleParty());
                     jPanelIdentificacion.setIdentificacion(metadato.getIdentificacion());
                     jPanelCalidad.setCalidad(metadato.getCalidad());
                     logger.debug("ID del MD_DataIdentification salvado:"+(metadato.getIdentificacion()!=null?metadato.getIdentificacion().getIdentification_id():"No tengo identificacion"));
                 }
                 catch(Exception e)
                 {logger.error("Error al coger el objeto metadato: "+e.toString());}

                 logger.debug("Identificador del metadato insertado:"+metadato.getFileidentifier());
                 JOptionPane optionPane= new JOptionPane(sMensaje,JOptionPane.INFORMATION_MESSAGE);
                 JDialog dialog =optionPane.createDialog(this,"");
                 dialog.show();
                 if (metadato!=null)
                    jLabelFileIdentifier.setText(" "+messages.getString("CMetadatos.jLabelIdentificador")+" "+
                            (metadato.getFileidentifier()!=null?metadato.getFileidentifier():""));
                else
                    jLabelFileIdentifier.setText(" "+messages.getString("CMetadatos.jLabelIdentificador"));
                return true;
              }
              else
              {
                   JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                   JDialog dialog =optionPane.createDialog(this,"");
                   dialog.show();
                   return false;
              }


           }catch(Exception ex)
           {
               java.io.StringWriter sw=new java.io.StringWriter();
               java.io.PrintWriter pw=new java.io.PrintWriter(sw);
               ex.printStackTrace(pw);
               logger.error("Excepcion al añadir el metadato: "+sw.toString());
               JOptionPane optionPane= new JOptionPane(ex,JOptionPane.ERROR_MESSAGE);
               JDialog dialog =optionPane.createDialog(this,"");
               dialog.show();

           }
           return false;
     }
     public void validarAccion()
    {
        if (validar())
        {
                JOptionPane optionPane= new JOptionPane(messages.getString("CMetadatos.mensaje.valido"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
        }
    }
    public boolean validar()
    {
        if (!jPanelMetadato.validar())
        {
                JOptionPane optionPane= new JOptionPane(messages.getString(jPanelMetadato.getErrorValidacion()),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return false;
        }
        if (!jPanelIdentificacion.validar())
        {
                JOptionPane optionPane= new JOptionPane(messages.getString(jPanelIdentificacion.getErrorValidacion()),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return false;
        }
        return true;
    }
    public boolean save2File(File f)
    {
          try
          {
              if (!validar()) return false;
              guardarCambios();
              BufferedWriter writer = new BufferedWriter(new FileWriter(f));
              StringWriter sw = new StringWriter();
              Marshaller.marshal(metadato, sw);
              writer.write(sw.toString());
              writer.flush();
              writer.close();
              logger.debug("Estructura del metadato salvada correctamente en el fichero: "+f.getPath());
              JOptionPane optionPane= new JOptionPane(messages.getString("JPanelShowMetadato.mensaje.save2file")+f.getPath(),JOptionPane.INFORMATION_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"");
              dialog.show();

              return true;

          }catch(Exception ex)
          {
              logger.error("Error al salvar el fichero: "+ f.getPath()+". Excepcion: "+ex.toString());
              JOptionPane optionPane= new JOptionPane(ex.getMessage(),JOptionPane.ERROR_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"");
              dialog.show();
              return false;
          }
     }

    public boolean exportar(File f)
    {
          try
          {
              if (!validar()) return false;
              guardarCambios();
              XMLTranslator traductor= new  XMLTranslator(
                                            new XMLTranslationInfo(f.getPath(),false));
              traductor.cargar();
              traductor.save(new MD_MetadataXML(metadato));

              logger.debug("Estructura del metadato exportada correctamente en el fichero: "+f.getPath());
              JOptionPane optionPane= new JOptionPane(messages.getString("JPanelShowMetadato.mensaje.save2file")+f.getPath(),JOptionPane.INFORMATION_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"");
              dialog.show();

              return true;

          }catch(Exception ex)
          {
              StringWriter sw = new StringWriter();
              PrintWriter pw = new PrintWriter(sw);
              ex.printStackTrace(pw);
              logger.error("Error al exportar el fichero: "+ f.getPath()+". Excepcion: "+sw.toString());
              JOptionPane optionPane= new JOptionPane(ex.getMessage()!=null?ex.getMessage():ex.toString(),JOptionPane.ERROR_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"");
              dialog.show();
              return false;
          }
     }
     public boolean importar(File f)
    {
          try
          {
                XMLTranslator traductor= new  XMLTranslator(
                   new XMLTranslationInfo(f.getPath(),false));
               traductor.parsear();
               metadato=new MD_MetadataXML().load(traductor);
               return load(metadato);
          }catch(Exception ex)
          {
              StringWriter sw = new StringWriter();
              PrintWriter pw = new PrintWriter(sw);
              ex.printStackTrace(pw);
              logger.error("Error al importar el fichero: "+ f.getPath()+". Excepcion: "+sw.toString());
              JOptionPane optionPane= new JOptionPane(ex.getMessage()!=null?ex.getMessage():ex.toString(),JOptionPane.ERROR_MESSAGE);
              JDialog dialog =optionPane.createDialog(this,"");
              dialog.show();
              return false;
          }
     }







     public boolean loadFile(File f)
     {
         return loadFile(f,this);
     }


     public boolean loadFile(File f, JPanel propietario)
     {
         try
         {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String sLine=reader.readLine();
            StringBuffer sb=new StringBuffer("");
            while (sLine != null)
            {
                sb.append(sLine);
                sLine=reader.readLine();
            }
            if (sb.length() ==0)
            {
                  logger.warn("Fichero "+f.getPath()+ " vacio");
                  JOptionPane optionPane= new JOptionPane(messages.getString("JPanelShowMetadato.mensaje.loadvacio")+f.getPath(),JOptionPane.INFORMATION_MESSAGE);
                  JDialog dialog =optionPane.createDialog(propietario,"");
                  dialog.show();
                  return false;
            }
            boolean bCargarFichero=true;
            try
            {
	            metadato=(MD_Metadata) Unmarshaller.unmarshal(MD_Metadata.class,new StringReader(sb.toString()));
            }catch(Exception ex)
            {
                logger.error("Error al cargar el fichero: "+ f.getPath()+". Excepcion: "+ex.toString());
                bCargarFichero=false;
            }
            reader.close();
            if (metadato==null||!bCargarFichero)
            {
                JOptionPane optionPane= new JOptionPane(messages.getString("JPanelShowMetadoto.mensaje.formatonovalido"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(propietario,"");
                dialog.show();
                return false;
            }
            return load(metadato);
        }catch(Exception ex)
        {
            logger.error("Error al cargar el fichero: "+ f.getPath()+". Excepcion: "+ex.toString());
            JOptionPane optionPane= new JOptionPane(ex.getMessage(),JOptionPane.INFORMATION_MESSAGE);
            JDialog dialog =optionPane.createDialog(propietario,"");
            dialog.show();
            return false;
        }

    }
     public boolean delete(MD_Metadata metadato)
     {
         if (metadato==null) return false;
         int n = JOptionPane.showOptionDialog(this,
                 messages.getString("JPanelShowMetadato.mensaje.eliminar")+" "+
                                    (metadato.getIdentificacion()!=null&&
                                    metadato.getIdentificacion().getCitacion()!=null?
                                    metadato.getIdentificacion().getCitacion().getTitle()+"?":"?"),
                                                       "",
                                                       JOptionPane.YES_NO_OPTION,
                                                       JOptionPane.QUESTION_MESSAGE,null,null,null);
         if (n==JOptionPane.NO_OPTION)
                  return false;
         else
         {
             return deleteMetadato(metadato.getFileidentifier());
         }
     }
     public boolean delete(String sIdMetadato)
     {
         MD_Metadata metadato = new MD_Metadata();
         metadato.setFileidentifier(sIdMetadato);
         return delete(metadato);
     }
     public boolean delete()
     {
         return delete(this.metadato);
     }

     private boolean deleteMetadato(String sFileIdentifier)
     {
         if (com.geopista.security.SecurityManager.isLogged())
         {
             try
             {
                CResultadoOperacion result= new OperacionesMetadatos(Constantes.url).deleteMetadata(sFileIdentifier);
                if (result.getResultado())
                {
                      MD_Metadata metadata = new MD_Metadata();
                      return load(metadata);
                }
                else
                {

                    logger.error("Error al borrar el metadato de la base de datos."+sFileIdentifier+". Descripcion: "+result.getDescripcion());
                    JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog =optionPane.createDialog(this,"");
                    dialog.show();
                    return false;
                }
             }
             catch(Exception ex)
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                logger.error("Error al borrar el metadato de la base de datos."+sFileIdentifier+". Excepcion: "+sw.toString());
                JOptionPane optionPane= new JOptionPane(ex.getMessage(),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return false;
            }

         }
         return true;
     }
     public boolean load(String sFileIdentifier)
     {
         if (com.geopista.security.SecurityManager.isLogged())
         {
             try
             {
                CResultadoOperacion result= new OperacionesMetadatos(Constantes.url).getMetadato(sFileIdentifier);
                if (result.getResultado())
                {
                      if (result.getVector()!=null&&result.getVector().size()>0)
                      {
                          MD_Metadata metadata = (MD_Metadata)result.getVector().elementAt(0);
                          return load(metadata);
                      }
                      else
                      {
                          logger.error("Valor obtenido no el lógico no esta relleno el Metadato");
                          return false;
                      }
                }
                else
                {
                    logger.error("Error al obtener el metadato de la base de datos."+sFileIdentifier+". Descripcion: "+result.getDescripcion());
                    JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog =optionPane.createDialog(this,"");
                    dialog.show();
                    return false;
                }
             }
             catch(Exception ex)
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                logger.error("Error al obtener el metadato de la base de datos."+sFileIdentifier+". Excepcion: "+sw.toString());
                JOptionPane optionPane= new JOptionPane(ex.getMessage(),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return false;
            }

         }
         return false;
     }
     public boolean load(MD_Metadata metadato)
     {
         if (metadato==null) return false;
         this.metadato=metadato;
         jPanelMetadato.load(metadato);
         jPanelIdentificacion.load(metadato.getIdentificacion());
         jPanelCalidad.load(metadato.getCalidad());
         jPanelDistribucion.load(metadato);
         jPanelReferencia.load(metadato);
         jLabelFileIdentifier.setText(" "+messages.getString("CMetadatos.jLabelIdentificador")+" "+
                  (metadato.getFileidentifier()!=null?metadato.getFileidentifier():""));

         return true;
     }
     public void showArbol()
     {
        jScrollPaneArbol.setVisible(true);
        this.repaint();
        this.invalidate();
        this.validate();
     }
     public void hideArbol()
     {
        jScrollPaneArbol.setVisible(false);
        this.repaint();
        this.invalidate();
        this.validate();
     }

    public void valueChanged(TreeSelectionEvent e) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTreeMetadatos.getLastSelectedPathComponent();
         if (node == null) return;
         Object nodeInfo = node.getUserObject();
         if (nodeInfo instanceof ArbolEntry)
         {
            ArbolEntry entrada =((ArbolEntry)nodeInfo);
            metadatoJTabbedPane.setSelectedIndex(entrada.getCarpeta());
            if (entrada.getComponente()!=null)
               entrada.getComponente().requestFocus();
         }

    }
    public DefaultMutableTreeNode initArbol()
    {
                DefaultMutableTreeNode root = new DefaultMutableTreeNode(quita2puntos(messages.getString("CMetadatos.title")));
                DefaultMutableTreeNode child11 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelMetadato.jLabelIdioma")),TreeRendererMetadatos.CONDICIONAL,0,jPanelMetadato.jComboBoxIdioma));
                root.add(child11);
                DefaultMutableTreeNode child12 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelMetadato.jLabelFecha")),TreeRendererMetadatos.OBLIGATORIO,0,jPanelMetadato.jCalendarButtonFecha));
                root.add(child12);
                DefaultMutableTreeNode child13 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelMetadato.jLabelStdName")),TreeRendererMetadatos.OPTATIVO,0,jPanelMetadato.jTextFieldStdName));
                root.add(child13);
                DefaultMutableTreeNode child14 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelMetadato.jLabelVersion")),TreeRendererMetadatos.OPTATIVO,0,jPanelMetadato.jTextFieldVersion));
                root.add(child14);
                DefaultMutableTreeNode child15 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelMetadato.jLabelCodificacion")),(TreeRendererMetadatos.CONDICIONAL),0,jPanelMetadato.jComboBoxCodificacion));
                root.add(child15);
                DefaultMutableTreeNode child16 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelMetadato.jPanelContacto")),(TreeRendererMetadatos.OBLIGATORIO),0,jPanelMetadato.jTextFieldContacto));
                root.add(child16);
                DefaultMutableTreeNode child161 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelMetadato.jLabelRol")),(TreeRendererMetadatos.OBLIGATORIO),0,jPanelMetadato.jComboBoxRol));
                root.add(child161);
                DefaultMutableTreeNode child17 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelMetadato.jLabelCapa")),(TreeRendererMetadatos.OPTATIVO),0,jPanelMetadato.jComboBoxRol));
                root.add(child17);
                DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.title")),(TreeRendererMetadatos.OBLIGATORIO),1));
                root.add(child2);
                DefaultMutableTreeNode child21 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jLabelCitacion")),(TreeRendererMetadatos.OBLIGATORIO),1,jPanelIdentificacion.jButtonCitacion));
                child2.add(child21);
                DefaultMutableTreeNode child211 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("ShowCitacion.jLabelTitulo")),(TreeRendererMetadatos.OBLIGATORIO),1,jPanelIdentificacion.jButtonCitacion));
                child21.add(child211);
                DefaultMutableTreeNode child212 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("ShowCitacion.jLabelFechas")),(TreeRendererMetadatos.OBLIGATORIO),1,jPanelIdentificacion.jButtonCitacion));
                child21.add(child212);
                DefaultMutableTreeNode child22 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jLabelResumen")),(TreeRendererMetadatos.OBLIGATORIO),1,jPanelIdentificacion.jTextPanelResumen));
                child2.add(child22);
                DefaultMutableTreeNode child23 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jLabelProposito")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jTextPanelProposito));
                child2.add(child23);
                DefaultMutableTreeNode child24 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jPanelIdiomas")),(TreeRendererMetadatos.OBLIGATORIO),1,jPanelIdentificacion.jPanelIdiomas));
                child2.add(child24);
                DefaultMutableTreeNode child25 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jLabelCaracteres")),(TreeRendererMetadatos.CONDICIONAL),1,jPanelIdentificacion.jComboBoxCaracteres));
                child2.add(child25);
                DefaultMutableTreeNode child26 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jPanelContacto")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jButtonContacto));
                child2.add(child26);
                DefaultMutableTreeNode child27 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jLabelRol")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jComboBoxRol));
                child2.add(child27);
                DefaultMutableTreeNode child28 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jPanelRepresentacion")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jPanelRepresentacion));
                child2.add(child28);
                DefaultMutableTreeNode child29 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jPanelCategoria")),(TreeRendererMetadatos.CONDICIONAL),1,jPanelIdentificacion.jPanelCategoria));
                child2.add(child29);
                DefaultMutableTreeNode child210 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jPanelExtension")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jPanelExtension));
                child2.add(child210);
                DefaultMutableTreeNode child2101 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelExtension.jLabelDescripcion")),(TreeRendererMetadatos.CONDICIONAL),1,jPanelIdentificacion.jPanelExtension));
                child210.add(child2101);
                DefaultMutableTreeNode child2102 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelExtension.jPanelVertical")),(TreeRendererMetadatos.CONDICIONAL),1,jPanelIdentificacion.jPanelExtension));
                child210.add(child2102);
                DefaultMutableTreeNode child2103 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelExtension.jPanelCoordenadas")),(TreeRendererMetadatos.CONDICIONAL),1,jPanelIdentificacion.jPanelExtension));
                child210.add(child2103);
                DefaultMutableTreeNode child2111 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelExtension.jLabelEscala")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jPanelExtension));
                child2.add(child2111);
                DefaultMutableTreeNode child2112 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jPanelGraficos")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jPanelGraficos));
                child2.add(child2112);
                DefaultMutableTreeNode child2113 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelIdentificacion.jPanelRestricciones")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jPanelGraficos));
                child2.add(child2113);
                DefaultMutableTreeNode child21131 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelRestricciones.jLabelLimitaciones")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jPanelGraficos));
                child2113.add(child21131);
                DefaultMutableTreeNode child21132 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelRestricciones.title")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jPanelGraficos));
                child2113.add(child21132);
                DefaultMutableTreeNode child211321 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelRestricciones.jLabelAcceso")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jPanelGraficos));
                child21132.add(child211321);
                DefaultMutableTreeNode child211322 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelRestricciones.jLabelRUso")),(TreeRendererMetadatos.OPTATIVO),1,jPanelIdentificacion.jPanelGraficos));
                child21132.add(child211322);
                DefaultMutableTreeNode child211323 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelRestricciones.jLabelOtras")),(TreeRendererMetadatos.CONDICIONAL),1,jPanelIdentificacion.jPanelGraficos));
                child21132.add(child211323);
                DefaultMutableTreeNode child30 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("CMetadatos.jPanelReferencia")),(TreeRendererMetadatos.OPTATIVO),2));
                root.add(child30);
                DefaultMutableTreeNode child3 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("CMetadatos.jPanelCalidad")),(TreeRendererMetadatos.OPTATIVO),3));
                root.add(child3);
                DefaultMutableTreeNode child31 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jPanelLinage")),(TreeRendererMetadatos.CONDICIONAL),3));
                child3.add(child31);
                DefaultMutableTreeNode child311 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jLabelDes")),(TreeRendererMetadatos.OPTATIVO),3,jPanelCalidad.jTextPaneDes));
                child31.add(child311);
                DefaultMutableTreeNode child312 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jLabelFuentes")),(TreeRendererMetadatos.OPTATIVO),3,jPanelCalidad.jTextPaneFuentes));
                child31.add(child312);
                DefaultMutableTreeNode child313 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jLabelPasos")),(TreeRendererMetadatos.OPTATIVO),3,jPanelCalidad.jTextPanePasos));
                child31.add(child313);
                DefaultMutableTreeNode child32 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jPaneInformes")),(TreeRendererMetadatos.CONDICIONAL),3));
                child3.add(child32);
                DefaultMutableTreeNode child321 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jLabelTipoInforme")),(TreeRendererMetadatos.OBLIGATORIO),3,jPanelCalidad.jComboBoxTipoInforme));
                child32.add(child321);
                DefaultMutableTreeNode child322 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jLabelNombre")),(TreeRendererMetadatos.OBLIGATORIO),3,jPanelCalidad.jTextPaneNombre));
                child32.add(child322);
                DefaultMutableTreeNode child323 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jLabelValor")),(TreeRendererMetadatos.OBLIGATORIO),3,jPanelCalidad.jTextPaneValor));
                child32.add(child323);
                DefaultMutableTreeNode child324 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jLabelEspecificacion")),(TreeRendererMetadatos.OBLIGATORIO),3,jPanelCalidad.jButtonEspe));
                child32.add(child324);
                DefaultMutableTreeNode child325 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelCalidad.jLabelExplicacion")),(TreeRendererMetadatos.OBLIGATORIO),3,jPanelCalidad.jTextPaneExplicacion));
                child32.add(child325);
                DefaultMutableTreeNode child4 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("CMetadatos.jPanelDistribucion")),(TreeRendererMetadatos.OPTATIVO),4));
                root.add(child4);
                DefaultMutableTreeNode child41 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelDistribucion.jLabelFormato")),(TreeRendererMetadatos.OPTATIVO),4,jPanelDistribucion.jListFormato));
                child4.add(child41);
                DefaultMutableTreeNode child411 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("ShowFormatoDistribucion.jLabelFormatoNombre")),(TreeRendererMetadatos.OBLIGATORIO),4,jPanelDistribucion.jListFormato));
                child41.add(child411);
                DefaultMutableTreeNode child412 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("ShowFormatoDistribucion.jLabelFormatoVersion")),(TreeRendererMetadatos.OBLIGATORIO),4,jPanelDistribucion.jListFormato));
                child41.add(child412);
                DefaultMutableTreeNode child42 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("JPanelDistribucion.jLabelRecursosEnLinea")),(TreeRendererMetadatos.OPTATIVO),4,jPanelDistribucion.jListRecursosEnLinea));
                child4.add(child42);
                DefaultMutableTreeNode child421 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("ShowRecursosEnLinea.jLabelEnlace")),(TreeRendererMetadatos.OBLIGATORIO),4,jPanelDistribucion.jListRecursosEnLinea));
                child42.add(child421);
                DefaultMutableTreeNode child422 = new DefaultMutableTreeNode(new ArbolEntry(quita2puntos(messages.getString("ShowRecursosEnLinea.jLabelFuncion")),(TreeRendererMetadatos.OPTATIVO),4,jPanelDistribucion.jListRecursosEnLinea));
                child42.add(child422);
        return root;
    }
    public String quita2puntos(String cadena)
    {
        if (cadena==null || cadena.indexOf(":")<0) return cadena;
        cadena=cadena.substring(0,cadena.indexOf(":"));
        return cadena;
    }

      public void printPanel()
    {
          PrinterJob printerJob=PrinterJob.getPrinterJob();
          //Imprimo en formato A$
          PageFormat format = new PageFormat();
          format.setOrientation(PageFormat.PORTRAIT);
          Paper paper = format.getPaper();
          paper.setSize(587, 842); // Svarer til A4
          paper.setImageableArea(44, 52, 500, 738);
          format.setPaper(paper);
          //Obtengo un vector con los graficos a imprimir
          //printPanel(null, format, -1);
          int paginas = 3;
          logger.warn("Se van a imprimir: "+paginas+" pagina/s");
          //Relleno los gráficos
          Book book = new Book();
          book.append(this,format,paginas);

          printerJob.setPageable(book);
          boolean doPrint=printerJob.printDialog();
          if (doPrint)
          {
              try{ printerJob.print();}catch (Exception e){
                  logger.error("Excepcion al imprimir "+e.toString());
             }
          }
       }
        public int printPanel(Graphics g, PageFormat format, int pageIndex)
       {
            Graphics2D g2d = (Graphics2D)g;
            init();
            if (g2d!=null)g2d.translate(format.getImageableX(), format.getImageableY());//System.getProperty("user.dir", ".")+File.separator+
            try
            {
                 g2d.drawImage(cabecera,0,0, new Double(format.getImageableWidth()).intValue(),cabecera.getHeight(this),this);
                 g2d.translate(0,cabecera.getHeight(this)+5);
                 setAltura(cabecera.getHeight(this)+5);
            }catch(Exception e)
            {}
            if (jPanelMetadato!=null)
            {
                try
                {
                   jPanelMetadato.printPanel(g,format,pageIndex);
                   jPanelIdentificacion.printPanel(g,format,pageIndex);
                   jPanelReferencia.printPanel(g,format,pageIndex);
                   jPanelCalidad.printPanel(g,format,pageIndex);
                   jPanelDistribucion.printPanel(g,format,pageIndex);

                }catch(Exception e){
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    logger.error("Error al imprimir los datos:"+sw.toString());
                }
            }
          if (pageIndex>getPaginasDocumento())
              return Printable.NO_SUCH_PAGE;
          return Printable.PAGE_EXISTS;
       }
        public int print(Graphics g, PageFormat format, int pageIndex) {

               return printPanel(g, format, pageIndex);

          }



    private JPanelIdentificacion jPanelIdentificacion;
    private JPanelCalidad jPanelCalidad;
    private JPanelDistribucion jPanelDistribucion;
    private JPanelMetadato jPanelMetadato;
    private JPanelReferencia jPanelReferencia;
    private javax.swing.JTabbedPane metadatoJTabbedPane;
    private javax.swing.JTree jTreeMetadatos;
    private javax.swing.JScrollPane jScrollPaneArbol;
    private javax.swing.JLabel jLabelFileIdentifier;


}