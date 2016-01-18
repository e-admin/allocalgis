/**
 * GeopistaPrintJPanel03.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.text.NumberFormat;
import java.util.Map;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.print.PrintService;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.GeopistaPrintPlugIn;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


public class GeopistaPrintJPanel03 extends JPanel implements WizardPanel
{
  private PageFormat pf = null;
  private PrinterJob job = null;

  AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  private Blackboard blackboard  = aplicacion.getBlackboard();
  
  WizardContext wizardContext;
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JButton jButton1 = new JButton();
  private JLabel jLabel3 = new JLabel();
  private JLabel jLabel4 = new JLabel();
  private JLabel jLabel5 = new JLabel();
  private JLabel jLabel6 = new JLabel();
  private JButton jButton2 = new JButton();
  String printerName = null;
  String orientationPage = null;
  String paperHeight = null;
  String paperWidth = null;
  private String localId = null;

  public GeopistaPrintJPanel03(String id, String nextId)
  {
    this.nextID = nextId;
    this.localId = id;
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
    job = PrinterJob.getPrinterJob();
    setName(UtilsPrintPlugin.getMessageI18N("GeopistaPrintJPanel03.Name"));
    this.setLayout(null);
    jLabel1.setText(UtilsPrintPlugin.getMessageI18N("Impresora Seleccionada"));
    jLabel1.setBounds(new Rectangle(25, 30, 125, 25));
    jLabel2.setBounds(new Rectangle(170, 30, 250, 25));
    jButton1.setText(UtilsPrintPlugin.getMessageI18N("Cambiar Impresora"));
    jButton1.setBounds(new Rectangle(25, 65, 145, 30));
    jButton1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton1_actionPerformed(e);
        }
      });
    jLabel3.setText(UtilsPrintPlugin.getMessageI18N("Tamaño del Papel"));
    jLabel3.setBounds(new Rectangle(25, 130, 95, 35));
    jLabel4.setBounds(new Rectangle(160, 130, 195, 35));
    jLabel5.setText(UtilsPrintPlugin.getMessageI18N("Orientación del Papel"));
    jLabel5.setBounds(new Rectangle(25, 170, 115, 30));
    jLabel6.setBounds(new Rectangle(160, 170, 195, 30));
    jButton2.setText("Cambiar Formato de Papel");
    jButton2.setBounds(new Rectangle(25, 205, 175, 30));
    jButton2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton2_actionPerformed(e);
        }
      });
    this.add(jButton2, null);
    this.add(jLabel6, null);
    this.add(jLabel5, null);
    this.add(jLabel4, null);
    this.add(jLabel3, null);
    this.add(jButton1, null);
    this.add(jLabel2, null);
    this.add(jLabel1, null);
    updatePrintProperties();
    
  }

  private void updatePrintProperties()
  {
     if(blackboard.get(GeopistaPrintPlugIn.IMPRIMIRPF)==null)
     {
        pf = job.defaultPage();
     }
     else
     {
       pf =  (PageFormat) blackboard.get(GeopistaPrintPlugIn.IMPRIMIRPF);
     }
    
    PrintService printService = job.getPrintService();

    //Nombre de la impresora que tenemos seleccionada por defecto
    printerName = printService.getName();
    jLabel2.setText(printerName);    
    updatePaperProperties();
  }

  private void updatePaperProperties()
  {
  	blackboard.put(GeopistaPrintPlugIn.IMPRIMIRPF,pf);
    int orientationPageInt = pf.getOrientation();
    orientationPage = UtilsPrintPlugin.getMessageI18N("Horizontal");
    if(orientationPageInt == PageFormat.PORTRAIT)
    {
      orientationPage = UtilsPrintPlugin.getMessageI18N("Vertical");
    }
    NumberFormat fm= NumberFormat.getNumberInstance();
    fm.setMaximumFractionDigits(2);
    paperHeight = fm.format(pf.getHeight()/72 * 2.54);
    paperWidth = fm.format(pf.getWidth()/72 * 2.54);    
    jLabel4.setText(orientationPage);
    jLabel6.setText(paperWidth +"cm. x "+ paperHeight+" cm.");    
  }

  

  public void enteredFromLeft(Map dataMap)
  
  {
//Iniciamos la ayuda
            try {
                String helpHS="ayuda.hs";
                HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
                HelpBroker hb = hs.createHelpBroker();
                hb.enableHelpKey(this,"planeamientoGenerarMapaPlaneamiento04",hs); 
          }catch (Exception excp){
          }                        
 //fin de la ayuda
    if(blackboard.get(GeopistaPrintPlugIn.CARACTERISTICASIMPRESION)!=null)
    {
      job = (PrinterJob) blackboard.get(GeopistaPrintPlugIn.IMPRIMIRJOB);
    }
    
  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        blackboard.put(GeopistaPrintPlugIn.IMPRIMIRJOB,job);
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
      return localId;
    }

    public String getInstructions()
    {
      return UtilsPrintPlugin.getMessageI18N("GeopistaPrintJPanel03.Instructions");
    }

    public boolean isInputValid()
    {
      return true;
    }

    public void setWizardContext(WizardContext wd)
    {
      wizardContext = wd;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */

    private String nextID = null;
    public void setNextID(String nextID)
    {
    	this.nextID=nextID;
    }
    public String getNextID()
    {
      return nextID;
    }

  private void jButton1_actionPerformed(ActionEvent e)
  {
      if(job.printDialog())
      {
        updatePrintProperties();
      }
  }

  private void jButton2_actionPerformed(ActionEvent e)
  {
      pf = job.pageDialog(pf);
      updatePaperProperties();
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}
}
