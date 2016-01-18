/*
 * Created on 20-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * @author hgarcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro;



import java.awt.Dimension;
import java.awt.FlowLayout;


import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import javax.swing.event.ListSelectionListener;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;


import com.geopista.app.AppContext;
import com.geopista.ui.autoforms.FeatureFieldPanel;
import com.geopista.ui.dialogs.ExtendedPanelsDialog;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;



public class ListadoHabitantesPanel extends  JPanel implements FeatureExtendedPanel {
    
    public FeatureDialogHome fdh;
    
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard Identificadores = app.getBlackboard();
    
    private JLabel lblDescripcion = new JLabel();
    private DefaultListModel modelo = new DefaultListModel();
    private JList jListHabitantes = new JList(modelo);
    private JScrollPane scrollableList = new JScrollPane(jListHabitantes);
    private ArrayList identifs= new ArrayList();
    private JRadioButton rdbOrdenApellidos = new JRadioButton();
    private JRadioButton rdbOrdenDni = new JRadioButton();
    private ButtonGroup bgOrden= new ButtonGroup();
    private String opcion="";
    private PadronHabitantesPostgre consultaBD = new PadronHabitantesPostgre();
    int align = FlowLayout.LEFT;   
    private JPanel jPanelRadio = new JPanel(new FlowLayout(align));
    private boolean panelesCargados=false;
    
    public ListadoHabitantesPanel(FeatureDialogHome fd)
    {
        try
        {
            fdh = fd; 
            jbInit();
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    private void jbInit() throws Exception
    {
        this.setName(app.getI18nString("listado.panel.titulo"));
        this.setLayout(null);
        this.setSize(new Dimension(600, 550));
        this.setBounds(new java.awt.Rectangle(5,10,636,493)); 
        
        
        lblDescripcion.setText(app.getI18nString("listado.panel.descripcion"));
        lblDescripcion.setBounds(new Rectangle(10, 25, 400, 15));
        
        scrollableList.setBounds(new Rectangle(20, 50, 450, 280));           
        jListHabitantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        jPanelRadio.setBounds(20, 350, 400, 50);
        
        rdbOrdenApellidos.setText(app.getI18nString("listado.panel.orden.apellidos"));
        rdbOrdenApellidos.setActionCommand("apellidos");
        
        rdbOrdenDni.setText(app.getI18nString("listado.panel.orden.dni"));
        rdbOrdenDni.setActionCommand("dni");
        
        
        rdbOrdenApellidos.addActionListener(new ActionListener()
                {
            public void actionPerformed(ActionEvent e)
            {
                radiobutton_actionPerformed(e);
            }
                });
        
        rdbOrdenDni.addActionListener(new ActionListener()
                {
            public void actionPerformed(ActionEvent e)
            {
                radiobutton_actionPerformed(e);
            }
                });
        
        
        bgOrden.add(rdbOrdenApellidos);
        bgOrden.add(rdbOrdenDni);
        
        ButtonModel model = rdbOrdenApellidos.getModel();
        bgOrden.setSelected(model, true);
        rdbOrdenApellidos.doClick();
        
        
        int indice=Integer.parseInt(Identificadores.get("ID_Habitante").toString());
        if (indice!=0){
            jListHabitantes.setSelectedIndex(indice);
        }
        
        jListHabitantes.addListSelectionListener(new ListSelectionListener(){
            
            public void valueChanged(ListSelectionEvent evt) {              
                jListHabitantes_valueChanged(evt);     
            }
        }
        ); 
        
        
        jPanelRadio.setVisible(true);
        jPanelRadio.add(rdbOrdenApellidos, null);
        jPanelRadio.add(rdbOrdenDni, null);
        
        
        this.add(scrollableList, null);
        this.add(lblDescripcion, null);
        this.add(jPanelRadio, null);
        
    } 
    
    public void enter()
    {      
        panelesCargados=true;
        
        if (fdh instanceof FeatureDialog)
        {
            FeatureFieldPanel fp = ((FeatureDialog)fdh).get_fieldPanel();         
            fp.setPreferredSize(new Dimension(500, 420));                  
            ((FeatureDialog)fdh).set_fieldPanel(fp);
            ((FeatureDialog)fdh).pack();          
            
        }      
    }
    
    private void jListHabitantes_valueChanged(ListSelectionEvent evt)
    {  
        //En cuanto el usuario suelta el raton, getValueIsAdjusting es false
        if (!evt.getValueIsAdjusting()) 
        {
            JList list = (JList)evt.getSource();
            int index = list.getSelectedIndex();
            
            if (fdh instanceof FeatureDialog)
            {
                ((FeatureDialog)fdh).setPanelEnabled(HabitantePanel.class, true);
                ((FeatureDialog)fdh).setPanelEnabled(DomicilioPanel.class, true);
            }
            
            if (index>=0){
                Identificadores.put("ID_Habitante", identifs.get(index));
            }
        }
    }
    
    public void exit()
    {   
    }
    
    private void radiobutton_actionPerformed(ActionEvent e)
    {        
        jListHabitantes.setSelectedValue(null, true);
        
        opcion= e.getActionCommand();
        modelo.removeAllElements();      
        
        ArrayList[] Datos = consultaBD.getListaHabitantes(Integer.parseInt(Identificadores.get("ID_Policia").toString()), opcion);
        
        if (Datos==null)return;
        identifs = Datos[0];
        Iterator alDat = Datos[1].iterator();
        
        int n=0;        
        while (alDat.hasNext()) 
        {            
            modelo.add(n, alDat.next()); 
            n++;            
        } 
        
        if (panelesCargados && fdh instanceof FeatureDialog)
        {          
            ((FeatureDialog)fdh).setPanelEnabled(HabitantePanel.class, false);
            ((FeatureDialog)fdh).setPanelEnabled(DomicilioPanel.class, false);
        }
        
    }
    
}