/**
 * ScreenComponent.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.geopista.app.AppContext;


public class ScreenComponent extends JPanel
{           
    private JPanel buttonPanel = new JPanel();
    private JPanel fillerPanel = new JPanel();
    private JPanel centerPanel = new JPanel();    
    private Border buttonBorder = null;
    private Border centerBorder = null;    
    private JButton btnSalir = new JButton();
    
    //private JButton nextButton = new JButton();
    //private JButton backButton = new JButton(); 
    
    static final int FINISHED = 0;
    
    Component comp;
    
    private ArrayList actionListeners = new ArrayList();
    
    
    
    public ScreenComponent()
    {
        super();
        try
        {
            jbInit();
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
//      appContext.getMainFrame().addWindowListener(new WindowAdapter() {
//      public void windowClosing(WindowEvent e)
//      {
//      cancel();
//      }
//      });
    }
    
    
    public ScreenComponent(boolean isDoubleBuffered)
    {
        super(isDoubleBuffered);
        // TODO Auto-generated constructor stub
    }
    
    public ScreenComponent(LayoutManager layout)
    {
        super(layout);
        // TODO Auto-generated constructor stub
    }
    
    public ScreenComponent(LayoutManager layout, boolean isDoubleBuffered)
    {
        super(layout, isDoubleBuffered);
    }
    
    private void cancel() {
        setVisible(false);
        fire(this,ScreenComponent.FINISHED,"finished");        
    }
    
    private void jbInit() throws Exception 
    {        
        centerBorder = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(
                Color.white, new Color(148, 145, 140)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0));        
        centerPanel.setBorder(centerBorder);        
        
        btnSalir.setText(AppContext.getApplicationContext().getI18nString("Exit"));
        //btnSalir.setPreferredSize(new Dimension(150,25));
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnSalir_actionPerformed(e);
            }
        });        
        
        buttonBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        buttonPanel.setLayout(new GridBagLayout());    
        buttonPanel.setBorder(buttonBorder);
        buttonPanel.add(btnSalir,
                new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 20, 0, 0), 0, 0));
        buttonPanel.add(fillerPanel,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));       
        
        
        this.setLayout(new BorderLayout());
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        //this.setPreferredSize(new Dimension(ModuloCatastralFrame.DIM_X, ModuloCatastralFrame.DIM_Y-50));
        
    }
    
    public void setFillerPanel(JPanel fill)
    {
        buttonPanel.removeAll();
        fillerPanel = fill;
        this.remove(buttonPanel);
        buttonBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        buttonPanel.setLayout(new GridBagLayout());    
        buttonPanel.setBorder(buttonBorder);
        
        buttonPanel.add(fillerPanel,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));   
        buttonPanel.add(btnSalir,
                new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 20, 0, 0), 0, 0));
        
        this.add(buttonPanel, BorderLayout.SOUTH);
        //this.setPreferredSize(new Dimension(ModuloCatastralFrame.DIM_X, ModuloCatastralFrame.DIM_Y-50));
        
    }
    
    void btnSalir_actionPerformed(ActionEvent e) {
        cancel();
    }
    public void addComponent(Component c)
    {
    	comp=c;
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(c, BorderLayout.CENTER);
        centerBorder = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(
                Color.white, new Color(148, 145, 140)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0));        
        
        centerPanel.setBorder(centerBorder);//BorderFactory.createEtchedBorder());       
    }
    
    public void removeComponent(Component c)
    {
        centerPanel.remove(c);
    }
    
    
    public Component getComponentFromPanel()
    {
        return comp;
    }
    
    /**
     * Anota un ActionListener para los eventos de acción definidos en el ScreenComponent
     * ActionEvent:
     *          source: el WizardComponent que genera el evento.
     *          id puede ser ScreenComponent.FINISHED
     *          command: puede ser "finished"
     * @param listener
     */
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }
    
    public void remove(ActionListener listener) {
        actionListeners.remove(listener);
    }
    
    void fire(Object source, int id, String command) {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener listener = (ActionListener) i.next();
            listener.actionPerformed(new ActionEvent(source, id, command));
        }
    }
    public JButton getBtnSalir()
    {
        return btnSalir;
    }
}
