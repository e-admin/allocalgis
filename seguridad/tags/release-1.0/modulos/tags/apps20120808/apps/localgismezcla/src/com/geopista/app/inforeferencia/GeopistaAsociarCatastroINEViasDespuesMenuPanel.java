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

package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.geopista.app.AppContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * GeopistaEnlazarPoliciasViasPanel Panel que carga enlaza el numero de policias
 * con las vias
 */

public class GeopistaAsociarCatastroINEViasDespuesMenuPanel extends JPanel implements WizardPanel
{
    ApplicationContext aplicacion = AppContext.getApplicationContext();

    Blackboard blackboard = aplicacion.getBlackboard();

    private static Connection con = null;
    
    public String myID;

    public String siguiente;

    private JLabel lblOpcion = new JLabel();
    private JLabel lblDescripcion = new JLabel();
    private JLabel lblDescripcion1 = new JLabel();
    private JLabel lblDescripcion2 = new JLabel();
    private JLabel lblDescripcion3 = new JLabel();
    private JLabel lblDescripcion4 = new JLabel();
    private JLabel lblDescripcion5 = new JLabel();
    private JLabel lblDescripcion6 = new JLabel();
    private JLabel lblDescripcion7 = new JLabel();
    private JLabel lblImagen = new JLabel();
    
    private String nextID = "5";

    private WizardContext wizardContext;

    public GeopistaAsociarCatastroINEViasDespuesMenuPanel(){
    	jbInit();
    }

    private void jbInit(){
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter(){
        	public void componentShown(ComponentEvent e){
        		new Thread(new Runnable(){
        			public void run(){
        				try{
        					setName(aplicacion.getI18nString("importar.asistente.callejero.titulo.5"));
        					setSize(new Dimension(537, 390));
        					setLayout(null);
        					setBounds(new Rectangle(10, 10, 750, 600));
        					lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
                            lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                            lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        					
                            int altura = 120;
        					lblDescripcion.setBounds(new Rectangle(200, altura, 400, 20));
                            lblDescripcion.setText(aplicacion.getI18nString("importar.asistente.callejero.descripcion"));
                            lblDescripcion1.setBounds(new Rectangle(210, altura+20, 400, 20));
                            lblDescripcion1.setText(aplicacion.getI18nString("importar.asistente.callejero.descripcion1"));
                            lblDescripcion2.setBounds(new Rectangle(210, altura+40, 400, 20));
                            lblDescripcion2.setText(aplicacion.getI18nString("importar.asistente.callejero.descripcion2"));
                            lblDescripcion3.setBounds(new Rectangle(210, altura+60, 400, 20));
                            lblDescripcion3.setText(aplicacion.getI18nString("importar.asistente.callejero.descripcion3"));
                            lblDescripcion4.setBounds(new Rectangle(200, altura+100, 400, 20));
                            lblDescripcion4.setText(aplicacion.getI18nString("importar.asistente.callejero.descripcion4"));
                            lblDescripcion5.setBounds(new Rectangle(210, altura+120, 400, 20));
                            lblDescripcion5.setText(aplicacion.getI18nString("importar.asistente.callejero.descripcion5"));
                            lblDescripcion6.setBounds(new Rectangle(210, altura+140, 400, 20));
                            lblDescripcion6.setText(aplicacion.getI18nString("importar.asistente.callejero.descripcion6"));
                            lblDescripcion7.setBounds(new Rectangle(200, altura+180, 400, 20));
                            lblDescripcion7.setText(aplicacion.getI18nString("importar.asistente.callejero.descripcion7"));
                            
        					lblOpcion.setBounds(new Rectangle(135, 15, 595, 25));
        					add(lblOpcion, null);
        					add(lblImagen, null);
        					add(lblDescripcion, null);
        					add(lblDescripcion1, null);
        					add(lblDescripcion2, null);
        					add(lblDescripcion3, null);
        					add(lblDescripcion4, null);
        					add(lblDescripcion5, null);
        					add(lblDescripcion6, null);
        					add(lblDescripcion7, null);
        				} catch (Exception e){

        				} finally{
        					progressDialog.setVisible(false);
        				}
        			}
        		}).start();
        	}
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }

    public void enteredFromLeft(Map dataMap){
        wizardContext.previousEnabled(false);
        try{
            if (con == null){
                con = aplicacion.getConnection();
                con.setAutoCommit(false);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("NoConexionBaseDatos"));
            wizardContext.cancelWizard();
            return;
        }
    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception{
    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
    public void add(InputChangedListener listener){

    }

    public void remove(InputChangedListener listener){

    }

    public String getTitle(){
        return this.getName();
    }

    public String getID(){
        return "1";
    }

    public String getInstructions(){
        return "";
    }

    public boolean isInputValid(){
        return true;
    }

    public void setNextID(String nextID){
        this.nextID = nextID;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID(){

        return nextID;

    }

    public void setWizardContext(WizardContext wd){
        this.wizardContext = wd;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting(){
        
        // TODO Auto-generated method stub
        
    }

}
