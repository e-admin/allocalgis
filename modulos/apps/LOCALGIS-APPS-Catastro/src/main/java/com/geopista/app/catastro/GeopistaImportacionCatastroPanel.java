/**
 * GeopistaImportacionCatastroPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
//import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
public class GeopistaImportacionCatastroPanel extends JPanel implements WizardPanel
{

  private JPanel pnlVentana = new JPanel();
  private JLabel lblImportar = new JLabel();
  private JProgressBar pbProgreso = new JProgressBar ();
  private JLabel lblOperacion = new JLabel();
  private JLabel lblFin = new JLabel();
  private JButton btnDetener = new JButton();
  private JScrollPane scpInfo = new JScrollPane();
  private JTextArea txaInfo = new JTextArea();
  private JOptionPane opCuadroDialogo;
  public final static int ONE_SECOND = 1000;
  public static  Timer timer;
  public static GeopistaLongTask task;

  public String titulo;
  private JLabel lblImagen = new JLabel();


  public GeopistaImportacionCatastroPanel()
  {


    //this.titulo = titulo;

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
    ApplicationContext appcont = AppContext.getApplicationContext();
    this.setLayout(null);
    lblImportar.setText(appcont.getI18nString("Importacion")+" ... ");
    lblImportar.setBounds(new Rectangle(135, 15, 375, 25));
    task = new GeopistaLongTask();

    timer = new Timer(ONE_SECOND, new GeopistaTimerListener());

    pbProgreso.setMinimum(0);
    pbProgreso.setMaximum(task.getLengthOfTask());
    pbProgreso.setValue(0);
    pbProgreso.setStringPainted(true);
    pbProgreso.setBounds(new Rectangle(135, 70, 545, 30));

   //lblOperacion.setText("Operación realizada: Importación de planeamiento municipal");
    lblOperacion.setBounds(new Rectangle(135, 285, 545, 30));
    lblFin.setBounds(new Rectangle(200, 250, 275, 40));
    /*btnDetener.setText("Detener");
    btnDetener.setBounds(new Rectangle(645, 495, 90, 25));
    btnDetener.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          btnDetener_actionPerformed(e);
        }
      });*/
      lblImagen.setIcon(IconLoader.icon("catastro.png"));
    //startButton.setText("startButton");
    //startButton.setBounds(new Rectangle(400,530,90,25));
    //startButton.addActionListener(new ButtonListener());
    /*startButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          startButton_actionPerformed(e);
        }
      });*/
    scpInfo.setBounds(new Rectangle(135, 130, 545, 145));
    lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
    lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    this.setSize(750,600);
    this.add(lblImagen, null);
    scpInfo.getViewport().add(txaInfo, null);
    this.add(scpInfo, null);
    this.add(btnDetener, null);
    this.add(lblFin, null);
    this.add(lblOperacion, null);
    this.add(pbProgreso, null);
    this.add(lblImportar, null);
    //this.add(startButton, null);
    txaInfo.setMargin(new Insets(5,5,5,5));
    txaInfo.setEditable(false);



  }

  public void enteredFromLeft(Map dataMap)
  {
  /*   timer.stop();
     task.stop();*/
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
      return "";
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
/*Cuando pinchen en un boton:
 class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            startButton.setEnabled(false);

            task.go();
            timer.start();
        }
    }
*/
   class GeopistaTimerListener implements ActionListener {
   AppContext appcont =  new AppContext();
        public void actionPerformed(ActionEvent evt) {

            pbProgreso.setValue(task.getCurrent());
           // startButton.setEnabled(true);

            if(task.getMessage()!=null)
            {
              txaInfo.append(task.getMessage()+"\n");
            }

            txaInfo.setCaretPosition(txaInfo.getDocument().getLength());
            if (task.done()) {
                Toolkit.getDefaultToolkit().beep();
                timer.stop();
                pbProgreso.setValue(pbProgreso.getMinimum());
//                opCuadroDialogo.showMessageDialog(pnlVentana,"Proceso de importación finalizado");
                txaInfo.append(appcont.getI18nString("FinProceso"));

            }
        }
    }

class GeopistaLongTask {
    private int lengthOfTask;
    private int current = 0;
    private String statMessage;

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

 /* private void btnDetener_actionPerformed(ActionEvent e)
  {
     timer.stop();
     task.stop();
  }*/

  public static void inicializar()
  {
    task.go();
    timer.start();
  }

/* (non-Javadoc)
 * @see com.geopista.ui.wizard.WizardPanel#exiting()
 */
public void exiting()
{
    // TODO Auto-generated method stub
    
}

}

