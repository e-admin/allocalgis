/**
 * ListaPane.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.panels;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.filter.CampoFiltro;
import com.geopista.app.filter.ListaRendererEIEL;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 02-ago-2006
 * Time: 13:58:34
 * To change this template use File | Settings | File Templates.
 */
public class ListaPane extends JScrollPane{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList jList;
    private Collection coleccion;
    private Object selected;
    private ArrayList actionListeners;
    private String locale;

    public ListaPane(Collection collection){
        init(collection);
    }

    public ListaPane(Collection collection, String locale){
        this.locale= locale;
        init(collection);
    }

    public void init(Collection collection){
        jList= new JList();
        actionListeners= new ArrayList();

        coleccion= collection;
        setLayout(new ScrollPaneLayout());
        jList.setCellRenderer(new ListaRendererEIEL(locale));
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = jList.getSelectionModel();
        actualizarModelo();
        rowSM.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                select();
                fireActionPerformed();
            }
        });
        setViewportView(jList);
    }

    public void renombrar(String title){
        setBorder(new TitledBorder(title));

    }

    /**
     * Actualiza la lista cada vez que se modifica algo
     */
    public void actualizarModelo(){
        DefaultListModel listModel = new DefaultListModel();

        if (coleccion != null){
            for(Iterator iterator= coleccion.iterator(); iterator.hasNext();)
                listModel.addElement(iterator.next());
        }
        jList.setModel(listModel);
    }

    /**
     * Actualiza la lista cada vez que se opera sobre ellos
     */
    public void update(Object obj){
        if (coleccion == null || obj == null) return;
        Vector newObjects= new Vector();
        for (Iterator it=coleccion.iterator(); it.hasNext();){
            Object leido= it.next();
            if (leido instanceof CampoFiltro){
                CampoFiltro aux= (CampoFiltro)leido;
                if (aux.equals((CampoFiltro)obj))
                    newObjects.add((CampoFiltro)obj);
                else
                    newObjects.add(aux);

            }

        }
        coleccion= newObjects;
        actualizarModelo();
    }

    public void clearSeleccion(){
        jList.clearSelection();
        selected= null;
    }


    /**
     * Método para seleccionar un elemento de la lista
     */
    public void seleccionar(Object obj){
        if (obj == null)return;
        ListModel auxList= jList.getModel();
        for (int i=0;i<auxList.getSize();i++){
            if (auxList.getElementAt(i) instanceof CampoFiltro){
                CampoFiltro aux= (CampoFiltro)auxList.getElementAt(i);
                if (aux.equals((CampoFiltro)obj)){
                    jList.setSelectedIndex(i);
                    return;
                }

            }

        }
    }
    /**
     * Método para borrar de la lista los elementos que han sido eliminados por el usuario
     */
    public void borrar(){
        if (selected==null) return;
        coleccion.remove(selected);
        selected= null;
        actualizarModelo();
    }

    /**
     * Método para añadir a la lista el elemento agregado por el usuario
     */
    public void add(Object obj, boolean b){
        if(obj == null) return;
        if (coleccion == null) coleccion= new Vector();
        coleccion.add(obj);
        actualizarModelo();
        if (b) seleccionar(obj);
    }

    public void add(Object obj){
        add(obj, true);
    }
    public void addOnlyIfNotExist(Object obj, boolean b){
        if (!exist(obj))
            add(obj, b);
    }


    /**
     * Método que indica el elemento seleccionado
     */
    private void select(){
        int selectedRow = jList.getMinSelectionIndex();
        if(selectedRow < 0) return;
        ListModel auxList = jList.getModel();
        if (auxList.getElementAt(selectedRow) instanceof CampoFiltro){
            selected= (CampoFiltro)auxList.getElementAt(selectedRow);
        }
    }

    public Object getSelected(){
        return selected;
    }

    public void setSelected(Object obj){
        this.selected= obj;
    }

    public Collection getCollection(){
        return coleccion;
    }

    public void setCollection(Collection c){
        this.coleccion= c;
    }

    public boolean exist(Object obj){
        for (int i=0; i<jList.getModel().getSize();i++){
            if (obj instanceof CampoFiltro){
                if (((CampoFiltro)jList.getModel().getElementAt(i)).getTabla().equalsIgnoreCase(((CampoFiltro)obj).getTabla()) &&
                    ((CampoFiltro)jList.getModel().getElementAt(i)).getNombre().equalsIgnoreCase(((CampoFiltro)obj).getNombre())) {
                    if (((CampoFiltro)jList.getModel().getElementAt(i)).getOperador().equalsIgnoreCase(((CampoFiltro)obj).getOperador()))
                        return true;
                }
            }
        }
        return false;
    }

    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }


}
