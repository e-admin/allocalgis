/**
 * GeopistaImportarAmbitos02Panel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.planeamiento;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarAmbitos02Panel extends JPanel implements WizardPanel
{
  private JPanel pnlVentana = new JPanel();
  private ApplicationContext aplicacion = AppContext.getApplicationContext();
  private GeopistaLayer layer5 = null;
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
  private String campo;
  private DefaultListModel listModelIncorporaciones  = new DefaultListModel();
  private JList lstIncorporaciones = new JList(listModelIncorporaciones);
  private  GeopistaLayer layer01;
  private JLabel lblIncorporaciones = new JLabel();
  private JScrollPane scpIncorporaciones = new JScrollPane();
  private JButton btnVincular = new JButton();
  private JOptionPane OpCuadroDialogo;
  private Connection conexion = null;
  private boolean existe;
  private int vez=0;
  public int permisoAcceso = 0;
  public boolean acceso;
  private String dominio;
  private String cadenaDominio;
  private GeopistaValidarImportacion valImport = new GeopistaValidarImportacion();
  private GeopistaEditor geopistaEditor2 = new GeopistaEditor();
  private ArrayList resValExistente;
  private ArrayList resIdValExistente;
  private String tipgest;
  private Blackboard blackImportar = new Blackboard();
  private String valorFich;
  private DefaultListModel listModelHideID  = new DefaultListModel();
  private JList lstIDHide = new JList(listModelHideID);
  private boolean continuar;
  private WizardContext wizardContext;
  private Vector vectorIncorporaciones=new Vector();
  private String vlNuevo;
  private String vlExist;
  DefaultListModel listModelNuevoHide = new DefaultListModel();
  JList lstNuevoHide = new JList(listModelNuevoHide);
  DefaultListModel listModelExistenteHide = new DefaultListModel();
  JList lstExistenteHide = new JList(listModelExistenteHide);
  DefaultListModel listModelIDVincular = new DefaultListModel();
  JList lstIdVincular = new JList(listModelIDVincular);
  private boolean existeLista;

  public GeopistaImportarAmbitos02Panel()
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
      setName(aplicacion.getI18nString("importar.ambitos.gestion.titulo.2"));
     try 
    {
      String helpHS="ayuda.hs";
      ClassLoader c1 =  this.getClass().getClassLoader();
      URL hsURL = HelpSet.findHelpSet(c1,helpHS);
      HelpSet hs = new HelpSet(null,hsURL);
      HelpBroker hb = hs.createHelpBroker();
      hb.enableHelpKey(this,"planeamientoImportarAmbitosGestion02",hs); 
    }
    catch (Exception excp)
    {
    }

 
    
    this.setLayout(null);
    lstNuevoHide.setVisible(false);
    lstExistenteHide.setVisible(false);
    lstIdVincular.setVisible(false);

    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    lblImagen.setIcon(IconLoader.icon("planeamiento.png"));

    lblComparacion.setText(aplicacion.getI18nString("ComparaDominios"));
    lblComparacion.setBounds(new Rectangle(135, 10, 375, 30));
    lblComparacion.setAlignmentY((float)1.0);

    lblDominio.setText(aplicacion.getI18nString("Dominio")+":");
    lblDominio.setBounds(new Rectangle(135, 40, 65, 25));

    cmbDominio.setBounds(new Rectangle(195, 45, 285, 20));

    lblValorNuevo.setText(aplicacion.getI18nString("ValorNuevo")+":");
    lblValorNuevo.setBounds(new Rectangle(135, 80, 105, 35));

    lblValorExistente.setText(aplicacion.getI18nString("ValorExistente")+":");
    lblValorExistente.setBounds(new Rectangle(430, 80, 130, 30));

    lstNuevoHide.setBounds(new Rectangle(505, 35, 40, 55));
    lstExistenteHide.setBounds(new Rectangle(600, 35, 40, 55));
    lstIdVincular.setBounds(555, 35, 40, 55);
    
    this.add(lstExistenteHide, null);
    this.add(lstNuevoHide, null);
    this.add(lstIdVincular,null);

    scpValorNuevo.setBounds(new Rectangle(135, 110, 280, 170));

    scpValorExistente.setBounds(new Rectangle(435, 110, 285, 170));

    lblIncorporaciones.setText(aplicacion.getI18nString("ListaIncorporaciones")+":");
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

    this.setSize(750,600);
    this.add(btnEliminar, null);
    this.add(lstIDHide, null);
    this.add(geopistaEditor2, null);
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
    String nombreDom = valImport.nombreDominio(24,conexion);
    btnEliminar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnEliminar_actionPerformed(e);
        }
      });
    btnEliminar.setBounds(new Rectangle(645, 345, 90, 25));
    btnEliminar.setText(aplicacion.getI18nString("Delete"));
    lstIDHide.setBounds(new Rectangle(655, 395, 75, 105));
    lstIDHide.setVisible(false);
    cmbDominio.addItem(nombreDom);

  }

    public void enteredFromLeft(Map dataMap)
    {
        GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Planeamiento.VincularDominiosAmbitosGestion");
        acceso = aplicacion.checkPermission(geopistaPerm,"Planeamiento");

        if (acceso)
        {
          btnVincular.setEnabled(true);
        }
        else
        {
          btnVincular.setEnabled(false); 
          JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("NoPermisos"));
          wizardContext.cancelWizard();
        }
        
        try
        {
          conexion = valImport.getDBConnection();
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
        }
        
      geopistaEditor2.getLayerManager().remove(layer5);
      listModelNuevo.removeAllElements();
      lstValorNuevo.removeAll();
      listModelExistente.removeAllElements();
      lstValorExistente.removeAll();
      listModelHideID.removeAllElements();
      lstIDHide.removeAll();
      cmbDominio.removeAllItems();
    
      int i=0;
      blackImportar = aplicacion.getBlackboard();
      String ruta =(String) blackImportar.get("rutaFicheroImportar");

      String nombreDom = valImport.nombreDominio(24,conexion);
      lstIDHide.setBounds(new Rectangle(655, 395, 75, 105));
      cmbDominio.addItem(nombreDom);
    
      resValExistente = valImport.leerDominios(24,conexion);
      Iterator alIt = resValExistente.iterator();
      while (alIt.hasNext())
      {
        dominio = alIt.next().toString();
        cadenaDominio = dominio + " (" + valImport.getDescriTipoDom(dominio,24,conexion) + ") ";
        listModelExistente.addElement(cadenaDominio);
      }
    
      resIdValExistente = valImport.leerDominiosID(24,conexion);
      Iterator alItID = resIdValExistente.iterator();
      while (alItID.hasNext())
      {
        listModelHideID.addElement(alItID.next().toString());  
      }  
      try
      {
        layer5 = (GeopistaLayer) geopistaEditor2.loadData(ruta, "Planeamiento");
        layer5.setActiva(true);
        layer5.setVisible(true);
        List coleccion = layer5.getFeatureCollectionWrapper().getFeatures();
        Iterator coleccionIter = coleccion.iterator();
        if (!coleccionIter.hasNext()) return;
        while (coleccionIter.hasNext())
        {
           Feature actualFeature = (Feature) coleccionIter.next();
           campo = actualFeature.getString(((Integer)blackImportar.get("indexAtributoDominio")).intValue()).toUpperCase().toString().trim();
         
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
             listModelNuevo.addElement(campo);
           }
         }
        
      }
      catch(Exception exc)
      {
        exc.printStackTrace();
      }
    
   }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
     int x;
     long idx;
     for(x=0;x<=listModelNuevoHide.size()-1;x++)
     {
        lstNuevoHide.setSelectedIndex(x);     
        lstIdVincular.setSelectedIndex(x);
        idx = Long.parseLong((String)lstIdVincular.getSelectedValue());
        valImport.vincularDominio(lstNuevoHide.getSelectedValue().toString(),idx,conexion);
     }
      rellenaCuadrosLista();
      
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

    private String nextID=null;
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
    if(nextID==null)
    {
      if(permisoAcceso == 1)
      {
        return null;
      }
      else
      {
        return "3";
      }
    }
    else
    {
      return nextID;
    }
     
    }
  public void setWizardContext(WizardContext wd)
  {
      wizardContext = wd;
  }
  
  private void btnVincular_actionPerformed(ActionEvent e)
  {
    if(listModelNuevo.size()!=0)
    {
      if((lstValorNuevo.getSelectedIndex()!=-1)&&(lstValorExistente.getSelectedIndex()!=-1))
      {
        String cadena;
        String cadenaExistente;
        String cadenaTotal;
        boolean ok;
     
        lstIDHide.setSelectedIndex(lstValorExistente.getSelectedIndex());
        long id = Long.parseLong((String)lstIDHide.getSelectedValue());
        cadena = lstValorNuevo.getSelectedValue().toString();
        cadenaExistente = lstValorExistente.getSelectedValue().toString();
        cadenaTotal = aplicacion.getI18nString("VincularDominio1") + " " + cadena + " " + aplicacion.getI18nString("VincularDominio2") + " " + cadenaExistente;
       
        
        int cuenta = this.listModelIncorporaciones.size();
        int i = 0;
        int numero = 0;
        String valor;
        for(i=0;i<cuenta;i++)
        {
          valor = listModelIncorporaciones.get(i).toString();
          if (valor.equals(cadenaTotal))
          {
           OpCuadroDialogo.showMessageDialog(this,aplicacion.getI18nString("DominiosVinculados"));
           numero = 1;
          }
        }
        if(numero==0)
        {
          listModelNuevoHide.addElement(lstValorNuevo.getSelectedValue().toString());
          listModelExistenteHide.addElement(lstValorExistente.getSelectedValue().toString());
          listModelIDVincular.addElement(lstIDHide.getSelectedValue());

  //        valImport.vincularDominio(lstValorNuevo.getSelectedValue().toString(), id, conexion);
          listModelIncorporaciones.addElement(cadenaTotal);
          listModelNuevo.remove(lstValorNuevo.getSelectedIndex());
          listModelExistente.remove(lstValorExistente.getSelectedIndex());
          listModelHideID.remove(lstIDHide.getSelectedIndex());
          compruebaTam();
        }
      }
      else
      {
        OpCuadroDialogo.showMessageDialog(this,aplicacion.getI18nString("InfoDominiosVacios"));
      }
    }
    else
    {
        OpCuadroDialogo.showMessageDialog(this,aplicacion.getI18nString("ValorNuevoNoExiste"));
    }
}


 private void btnEliminar_actionPerformed(ActionEvent e)
  {
  if(lstIncorporaciones.getSelectedIndex()!=-1)
  {
    lstNuevoHide.setSelectedIndex(lstIncorporaciones.getSelectedIndex());
    listModelNuevo.addElement(lstNuevoHide.getSelectedValue());

    lstExistenteHide.setSelectedIndex(lstIncorporaciones.getSelectedIndex());
    listModelExistente.addElement(lstExistenteHide.getSelectedValue());

    lstIDHide.setSelectedIndex(lstIncorporaciones.getSelectedIndex());
    listModelHideID.addElement(lstIDHide.getSelectedValue());
  
    listModelNuevoHide.remove(lstIncorporaciones.getSelectedIndex());
    listModelExistenteHide.remove(lstIncorporaciones.getSelectedIndex());
    listModelIDVincular.remove(lstIncorporaciones.getSelectedIndex());
    listModelIncorporaciones.remove(lstIncorporaciones.getSelectedIndex());
  }
  else
  {
         JOptionPane.showMessageDialog(this,aplicacion.getI18nString("SeleccValorIncorDel"));
  }
  	
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
    geopistaEditor2.getLayerManager().remove(layer5);
    listModelNuevo.removeAllElements();
    lstValorNuevo.removeAll();
    listModelExistente.removeAllElements();
    lstValorExistente.removeAll();
    listModelHideID.removeAllElements();
    lstIDHide.removeAll();
    listModelNuevoHide.removeAllElements();
    listModelExistenteHide.removeAllElements();
    listModelIDVincular.removeAllElements();
    lstIncorporaciones.removeAll();
    listModelIncorporaciones.removeAllElements();
    lstNuevoHide.removeAll();
    lstExistenteHide.removeAll();
    lstIdVincular.removeAll();
    cmbDominio.removeAllItems();
    
      int i=0;
      blackImportar = aplicacion.getBlackboard();
      String ruta =(String) blackImportar.get("rutaFicheroImportar");
      String tabla = (String) blackImportar.get("nombreTablaSelec");
     

      String nombreDom = valImport.nombreDominio(24,conexion);
      lstIDHide.setBounds(new Rectangle(655, 395, 75, 105));
      cmbDominio.addItem(nombreDom);
    
      resValExistente = valImport.leerDominios(24,conexion);
      Iterator alIt = resValExistente.iterator();
      while (alIt.hasNext())
      {
          dominio = alIt.next().toString();
          cadenaDominio = dominio + " (" + valImport.getDescriTipoDom(dominio,24,conexion) + ") ";
          listModelExistente.addElement(cadenaDominio);
      }
    
      resIdValExistente = valImport.leerDominiosID(24,conexion);
      Iterator alItID = resIdValExistente.iterator();
      while (alItID.hasNext())
      {
          listModelHideID.addElement(alItID.next().toString());  
      }  
      
      
      try
      {
        int xEx;
        layer5 = (GeopistaLayer) geopistaEditor2.loadData(ruta, "Planeamiento");
        layer5.setActiva(true);
        layer5.setVisible(true);
        List coleccion = layer5.getFeatureCollectionWrapper().getFeatures();
          Iterator coleccionIter = coleccion.iterator();
          if (!coleccionIter.hasNext()) return;
          while (coleccionIter.hasNext())
          {
            Feature actualFeature = (Feature) coleccionIter.next();
            campo = actualFeature.getString("IDtipget").toString().trim();
          
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
      catch(Exception exc)
      {
        exc.printStackTrace();
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