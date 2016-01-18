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

package com.geopista.app.catastro;
import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaListener;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import java.util.Date;
import java.text.*;
import java.net.URL;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.SearchView;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.ui.AbstractSelection;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import com.vividsolutions.jump.util.Blackboard;

public class GeopistaFinImportacionPanel extends JPanel implements WizardPanel
{
  private ApplicationContext appcont = AppContext.getApplicationContext();
  private GeopistaLayer layer5 = null;
  private JPanel pnlVentana = new JPanel();
  private JLabel lblImportar = new JLabel();
  private JProgressBar pbProgreso = new JProgressBar ();
  private JButton btnDetener = new JButton();
  private JOptionPane opCuadroDialogo;
  public final static int ONE_SECOND = 1000;
  public static  Timer timer;
  public static GeopistaLongTask task;
  private String cadenaTexto;
  public String titulo;
  private JLabel lblImagen = new JLabel();
  private Blackboard blackImportar = new Blackboard();
  public GeopistaFinImportacionPanel()
  {  
    try
    {
          try {
                String helpHS="ayuda.hs";
                HelpSet hs = com.geopista.app.help.HelpLoader.getHelpSet(helpHS);
                HelpBroker hb = hs.createHelpBroker();
                hb.enableHelpKey(this,"catastroImportacionFinUrb02",hs); 
          }catch (Exception excp){
          }                        
      setName(appcont.getI18nString("importar.finca.urbana.titulo.2"));
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
    lblImportar.setText(appcont.getI18nString("Importacion")+" ... ");
    lblImportar.setBounds(new Rectangle(135, 20, 445, 30));
    task = new GeopistaLongTask();

    timer = new Timer(ONE_SECOND, new GeopistaTimerListener());

    pbProgreso.setMinimum(0);
    pbProgreso.setMaximum(task.getLengthOfTask());
    pbProgreso.setValue(0);
    pbProgreso.setStringPainted(true);
    pbProgreso.setBounds(new Rectangle(135, 485, 595, 25));
    lblImagen.setIcon(IconLoader.icon("catastro.png"));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    ediInfo.setContentType("text/html");
    scpEditor.setBounds(new Rectangle(135, 50, 595, 420));
    this.setSize(750,600);
    scpEditor.getViewport().add(ediInfo, null);
    this.add(scpEditor, null);
    this.add(lblImagen, null);
    this.add(btnDetener, null);
    this.add(pbProgreso, null);
    this.add(lblImportar, null);
  }

  public void enteredFromLeft(Map dataMap)
  {
    DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
    String date = (String) formatter.format(new Date());
    String cadena="";
    blackImportar = appcont.getBlackboard();
    cadena = cadena + "<p><b>Registros de parcelas: </b>" + blackImportar.get("totalParcelas") +"</p>";
    cadena = cadena + "<p><b>Registros de subparcelas: </b>" + blackImportar.get("totalSubParcelas") +"</p>";
    cadena = cadena + "<p><b>Registros de Unidades Constructivas: </b>" + blackImportar.get("totalUnidad") +"</p>";
    cadena = cadena + "<p><b>Registros de Construcciones: </b>" + blackImportar.get("totalConstruccion") +"</p>";
    cadena = cadena + "<p><b>Registros de Cargos: </b>" + blackImportar.get("totalCargo") +"</p>";
        
    cadena = cadena + "<p><b>Hora de finalización: </b>" + date +"</p>";
    ediInfo.setText(cadena);     

  }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
      timer.stop();
      task.stop();
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
      return true;
    }

  private String nextID=null;
  private JEditorPane ediInfo = new JEditorPane();
  private JScrollPane scpEditor = new JScrollPane();
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

    }

   class GeopistaTimerListener implements ActionListener {
   AppContext appcont =  new AppContext();
        public void actionPerformed(ActionEvent evt) {

            pbProgreso.setValue(task.getCurrent());
           
            if(task.getMessage()!=null)
            {
              //cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3>"+task.getMessage()+"</font></p>";
              //ediInfo.setText(cadenaTexto);
                
            }

            ediInfo.setCaretPosition(ediInfo.getDocument().getLength());
            if (task.done()) {
                Toolkit.getDefaultToolkit().beep();
                timer.stop();
                pbProgreso.setValue(pbProgreso.getMinimum());
                cadenaTexto = cadenaTexto + "<p><font face=SansSerif size=3><b>Fin del Proceso</b></font></p>";
                //ediInfo.setText(cadenaTexto);


            }
        }
    }

class GeopistaLongTask {
    private int lengthOfTask;
    private int current = 0;
    private String statMessage;
//    private List coleccion = layer5.getFeatureCollectionWrapper().getFeatures();
    GeopistaLongTask () {
      
        lengthOfTask = 1000;
    }

    void go() {
        current = 0;
        final GeopistaSwingWorker worker = new GeopistaSwingWorker() {
            public Object construct() {
                return new GeopistaActualTask();
            }
        };
    }

    int getLengthOfTask() {
        return lengthOfTask;
    }

    int getCurrent() {
        return current;
    }

    void stop() {
        current = lengthOfTask;
    }

    boolean done() {
        if (current >= lengthOfTask)
            return true;
        else
            return false;
    }

    String getMessage() {
        return statMessage;
    }

    class GeopistaActualTask {
        GeopistaActualTask () {
        AppContext appcont =  new AppContext();
            while (current < lengthOfTask) {
                try {
                    Thread.sleep(1000); //sleep for a second
                    current += Math.random() * 100; //make some progress
                    statMessage =appcont.getI18nString("Completados") + " " + current + " " +
                                  appcont.getI18nString("De") + " " + lengthOfTask + ".";
                } catch (InterruptedException e) {
                }
            }
        }
    }
}


abstract class GeopistaSwingWorker {
    private Object value;
    private Thread thread;

    public abstract Object construct();

    public void finished() {
    }

    public Object get() {
        while (true) {
            Thread t;
            synchronized (GeopistaSwingWorker.this) {
                t = thread;
                if (t == null) {
                    return value;
                }
            }
            try {
                t.join();
            }
            catch (InterruptedException e) {
            }
        }
    }


    public GeopistaSwingWorker() {
        final Runnable doFinished = new Runnable() {
           public void run() { finished(); }
        };

        Runnable doConstruct = new Runnable() {
            public void run() {
                synchronized(GeopistaSwingWorker.this) {
                    value = construct();
                    thread = null;
                }
                SwingUtilities.invokeLater(doFinished);
            }
        };

        thread = new Thread(doConstruct);
        thread.start();
    }
}

  public static void inicializar()
  {
    task.go();
//    timer.start();
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

}

