
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


package com.geopista.app.planeamiento;
import com.geopista.app.AppContext;
import com.geopista.model.GeopistaLayer;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;

import com.geopista.util.AssociationOfDomainsRenderer;
import com.geopista.util.DomainDescriptionCode;
import com.geopista.util.DomainValueAssociation;
import com.geopista.util.ListExistingDomainRenderer;
import com.geopista.util.domainEnableCheck;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class GeopistaImportarPlaneamiento02Panel extends JPanel implements WizardPanel
{
  private String dominio;
  private Connection conexion = null;
  private GeopistaValidarImportacion valImport = new GeopistaValidarImportacion();
  private ApplicationContext aplicacion = AppContext.getApplicationContext();
  private GeopistaLayer capaPlaneamiento=null;
  private JPanel pnlVentana = new JPanel();
  private JLabel lblImagen = new JLabel();
  private JLabel lblComparacion = new JLabel();
  private JLabel lblDominio = new JLabel();
  private JComboBox cmbDominio = new JComboBox();
  private JLabel lblValorNuevo = new JLabel();
  private JLabel lblValorExistente = new JLabel();
  private JScrollPane scpValorNuevo = new JScrollPane();
  private JScrollPane scpValorExistente = new JScrollPane();
  private DefaultListModel listModelNuevo = new DefaultListModel();
  private JList lstValorNuevo = new JList(listModelNuevo);
  private DefaultListModel listModelExistente  = new DefaultListModel();
  private JList lstValorExistente = new JList(listModelExistente);
  private DefaultListModel listModelIncorporaciones  = new DefaultListModel();
  private JList lstIncorporaciones = new JList(listModelIncorporaciones);
  private AssociationOfDomainsRenderer listModelRederer = new AssociationOfDomainsRenderer();
  private ListExistingDomainRenderer listExistingDomainRenderer = new ListExistingDomainRenderer();

  private JLabel lblIncorporaciones = new JLabel();
  private JScrollPane scpIncorporaciones = new JScrollPane();
  private JButton btnVincular = new JButton();
  private boolean acceso;
  private boolean continuar;

  private Blackboard blackImportar = aplicacion.getBlackboard();
  private long idDomi;
  private int idAtributo;

  private ArrayList resValExistente;
  private String cadenaDominio;


  private String campo;
  private boolean existe;
  private boolean existeLista;
  private WizardContext wizardContext;

  
  public GeopistaImportarPlaneamiento02Panel()
  {
      final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
              .getMainFrame(), null);

      progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
      progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
      progressDialog.addComponentListener(new ComponentAdapter()
          {
              public void componentShown(ComponentEvent e)
              {

                  // Wait for the dialog to appear before starting the
                  // task. Otherwise
                  // the task might possibly finish before the dialog
                  // appeared and the
                  // dialog would never close. [Jon Aquino]
                  new Thread(new Runnable()
                      {
                          public void run()
                          {
							    try
							    {
							     
							      jbInit();
							    }
							    catch(Exception e)
							    {
							      e.printStackTrace();
							    }finally
                                {
                                    progressDialog.setVisible(false);
                                }
                            }
                        }).start();
                }
            });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
  }

  private void jbInit() throws Exception
  {
    setName(aplicacion.getI18nString("importar.ambitos.planeamiento.titulo.2"));
    lstIncorporaciones.setCellRenderer(listModelRederer);
    lstValorExistente.setCellRenderer(listExistingDomainRenderer);
    lstValorExistente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lstValorNuevo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lstIncorporaciones.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    
    try 
    {
      String helpHS="ayuda.hs";
      HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
      HelpBroker hb = hs.createHelpBroker();
      hb.enableHelpKey(this,"planeamientoImportarAmbitosPlaneamiento02",hs); 
    }
    catch (Exception excp)
    {
    }      

    
    this.setLayout(null);
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setIcon(IconLoader.icon("planeamiento.png"));
    lblComparacion.setText(aplicacion.getI18nString("ComparaDominios"));
    lblComparacion.setBounds(new Rectangle(135, 10, 375, 30));
    lblComparacion.setAlignmentY((float)1.0);
    lblDominio.setText(aplicacion.getI18nString("GeopistaImportarPlaneamiento02Panel.Dominio"));
    lblDominio.setBounds(new Rectangle(135, 40, 65, 25));
    cmbDominio.setBounds(new Rectangle(195, 45, 285, 20));
    lblValorNuevo.setText(aplicacion.getI18nString("GeopistaImportarPlaneamiento02Panel.ValorNuevo"));
    lblValorNuevo.setBounds(new Rectangle(135, 80, 105, 35));
    lblValorExistente.setText(aplicacion.getI18nString("GeopistaImportarPlaneamiento02Panel.ValorExistente"));
    lblValorExistente.setBounds(new Rectangle(430, 80, 130, 30));
    scpValorNuevo.setBounds(new Rectangle(135, 110, 280, 170));
    scpValorExistente.setBounds(new Rectangle(435, 110, 285, 170));
    lblIncorporaciones.setText(aplicacion.getI18nString("GeopistaImportarPlaneamiento02Panel.ListaIncorporaciones"));
    lblIncorporaciones.setBounds(new Rectangle(140, 295, 230, 20));
    scpIncorporaciones.setBounds(new Rectangle(135, 315, 500, 195));
    btnVincular.setText(aplicacion.getI18nString("Vincular"));
    btnVincular.setBounds(new Rectangle(645, 315, 90, 25));
    btnVincular.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnVincular_actionPerformed(e);
        }
      });
    
    btnEliminar.setText(aplicacion.getI18nString("Delete"));
    btnEliminar.setBounds(new Rectangle(645, 345, 90, 25));
    btnEliminar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnEliminar_actionPerformed(e);
        }
      });

    this.add(btnEliminar, null);

    this.setSize(750,600);
    
    this.add(btnVincular, null);
    scpIncorporaciones.getViewport().add(lstIncorporaciones, null);
    this.add(scpIncorporaciones, null);
    this.add(lblIncorporaciones, null);
    scpValorExistente.getViewport().add(lstValorExistente, null);
    this.add(scpValorExistente, null);
    scpValorNuevo.getViewport().add(lstValorNuevo, null);
    this.add(scpValorNuevo, null);
    this.add(lblValorExistente, null);
    this.add(lblValorNuevo, null);
    this.add(cmbDominio, null);
    this.add(lblDominio, null);
    this.add(lblComparacion, null);
    this.add(lblImagen, null);

    cmbDominio.setBackground(Color.white);
  }
  public void enteredFromLeft(Map dataMap)
  {
      GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Planeamiento.VincularDominiosPlaneamientoMunicipal");
      acceso = aplicacion.checkPermission(geopistaPerm,"Planeamiento");
      btnVincular.setEnabled(acceso);
 
      geopistaPerm = new GeopistaPermission("Geopista.Planeamiento.EliminarDominiosPlaneamientoMunicipal");
      acceso = aplicacion.checkPermission(geopistaPerm,"Planeamiento");
      btnEliminar.setEnabled(acceso);

      conexion = (Connection) blackImportar.get("Conexion");
       

    rellenaCuadrosLista();
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        HashMap allAssociationsHashMap = new HashMap();
        Object[] allAssociations = listModelIncorporaciones.toArray();
        for(int contadorLocal = 0;contadorLocal < allAssociations.length; contadorLocal++)
        {
            DomainValueAssociation currentAssociation = (DomainValueAssociation) allAssociations[contadorLocal];
            allAssociationsHashMap.put(currentAssociation.getNewDomain(),currentAssociation.getExistingDomain().getCode());
        }
        //hay que meter el codigo de dominios no eltexto que va
        blackImportar.put("AssociationsDomains",allAssociationsHashMap);
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
      return this.getName();
    }

    public String getID()
    {
      return "2";
    }

    public String getInstructions()
    {
     return "";
    }

    public boolean isInputValid()
    {
        if(continuar)
        {
          return true;
        }
        else
        {
          return false;
        }
    }

  private String nextID="3";
  private JButton btnEliminar = new JButton();
 
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
    
     public void setWizardContext(WizardContext wd)
    {
      wizardContext = wd;
    }

  private void btnVincular_actionPerformed(ActionEvent e)
  {
  
    
      if((lstValorNuevo.getSelectedIndex()!=-1)&&(lstValorExistente.getSelectedIndex()!=-1))
      {
          DomainValueAssociation newAssociation = new DomainValueAssociation(lstValorNuevo.getSelectedValue().toString(),(DomainDescriptionCode) lstValorExistente.getSelectedValue());

          listModelIncorporaciones.addElement(newAssociation);
          listModelNuevo.remove(lstValorNuevo.getSelectedIndex());
          listModelExistente.remove(lstValorExistente.getSelectedIndex());
          compruebaTam();
          btnEliminar.setEnabled(true);
        }
     
  }
 

 private void btnEliminar_actionPerformed(ActionEvent e)
  {
     
     int[] selectedDeleteIndex = lstIncorporaciones.getSelectedIndices();
     Object[] selectedValues = lstIncorporaciones.getSelectedValues();
     
     
  for (int contadorLocal = selectedDeleteIndex.length-1; contadorLocal >= 0 ; contadorLocal--)
  {
      DomainValueAssociation currentAssociation = (DomainValueAssociation) selectedValues[contadorLocal];
      listModelExistente.addElement(currentAssociation.getExistingDomain());
      listModelNuevo.addElement(currentAssociation.getNewDomain());
      listModelIncorporaciones.remove(selectedDeleteIndex[contadorLocal]);
  }
  
  	if(listModelIncorporaciones.getSize()==0)
  	{
  	    btnEliminar.setEnabled(false);
  	}
  	compruebaTam();
  	
}
  private void compruebaTam()
  {
        if (listModelNuevo.size()==0)
        {
          continuar = true;
        }
        else
        {
          continuar = false;
        }
        wizardContext.inputChanged();
  }
  private void rellenaCuadrosLista()
  {

    listModelNuevo.removeAllElements();
    lstValorNuevo.removeAll();
    listModelExistente.removeAllElements();
    lstValorExistente.removeAll();
    lstIncorporaciones.removeAll();
    listModelIncorporaciones.removeAllElements();
    cmbDominio.removeAllItems();
    btnEliminar.setEnabled(false);
    btnVincular.setEnabled(true);
      
      idDomi = ((Integer)blackImportar.get("idDomiBlack")).intValue();
      idAtributo = ((Integer)blackImportar.get("indiceCampo")).intValue();
       
      
      if (idDomi!=0)
      {
          String nombreDom = valImport.nombreDominio(idDomi,conexion);
          cmbDominio.addItem(nombreDom);
    
          resValExistente = valImport.leerDominios(idDomi,conexion);
          Iterator alIt = resValExistente.iterator();
          
          while (alIt.hasNext())
          {
            dominio = alIt.next().toString();
            DomainDescriptionCode newAssociation = new DomainDescriptionCode(dominio,valImport.getDescriTipoDom(dominio,idDomi,conexion));
            listModelExistente.addElement(newAssociation);
          }
           
      }

      
   
        int xEx;
        capaPlaneamiento = (GeopistaLayer)blackImportar.get("CapaPlaneamiento");
        capaPlaneamiento.setActiva(true);
        capaPlaneamiento.setVisible(true);
        List coleccion = capaPlaneamiento.getFeatureCollectionWrapper().getFeatures();
          Iterator coleccionIter = coleccion.iterator();
          if (!coleccionIter.hasNext()) return;
          while (coleccionIter.hasNext())
          {
            Feature actualFeature = (Feature) coleccionIter.next();
            
          
            campo = actualFeature.getString(idAtributo).toString().trim();
          
            Iterator alIterator = resValExistente.iterator();
          
            while (alIterator.hasNext())
            {
              existe = false;
              dominio = alIterator.next().toString().trim();
              if(campo.equals(dominio))
              {
                existe = true;
                break;
              }
            }
            if (existe == false)
            {
              existeLista = false;
              for(xEx=0;xEx<=listModelNuevo.size()-1;xEx++)
              {
                lstValorNuevo.setSelectedIndex(xEx);
                
                if(campo.equals(lstValorNuevo.getSelectedValue()))
                {
                  existeLista = true;
                  break;
                }
              }
              if (existeLista == false)
              {
                listModelNuevo.addElement(campo);
              }
            }
          }
        
     
    
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}
  
  
}