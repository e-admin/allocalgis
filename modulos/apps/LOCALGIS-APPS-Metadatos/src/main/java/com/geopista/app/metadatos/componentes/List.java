/**
 * List.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.componentes;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-jul-2004
 * Time: 15:15:55
 */
public class List extends JList{

    private DefaultListModel listModel;
    String pregunta;
    private JPopupMenu jPopupMenu;
    private java.awt.event.ActionListener accionNuevoElemento;
    public List (ResourceBundle resource, java.awt.event.ActionListener accionNuevoElemento)
    {
        super();
        initComponent(resource, null ,accionNuevoElemento);

    }
    public List (ResourceBundle resource, String sPregunta)
    {
        super();
        initComponent(resource, sPregunta ,null);

    }
    private void initComponent(ResourceBundle resource, String sPregunta,java.awt.event.ActionListener accionNuevoElemento)
    {
        this.accionNuevoElemento=accionNuevoElemento;
        listModel= new DefaultListModel();
        this.setModel(listModel);
        jPopupMenu= new JPopupMenu();
        JMenuItem jMenuNuevoElemento = new JMenuItem();
        jMenuNuevoElemento.setText(resource.getString("List.jMenuNuevoElemento"));
        if (accionNuevoElemento==null)
           jMenuNuevoElemento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoElemento();
            }
           });
        else
           jMenuNuevoElemento.addActionListener(accionNuevoElemento);

        JMenuItem jMenuBorrarElemento= new JMenuItem();
        jMenuBorrarElemento.setText(resource.getString("List.jMenuBorrarElemento"));

        jMenuBorrarElemento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeElement();
            }
        });
        jPopupMenu.add(jMenuNuevoElemento);
        jPopupMenu.add(jMenuBorrarElemento);

        this.addMouseListener (
                new MouseAdapter () {
                   public void mouseReleased( MouseEvent e ) {
                      if ( e.isPopupTrigger()) {
                          jPopupMenu.show( (JComponent)e.getSource(), e.getX(), e.getY() );
                          }
                      }
                   }
                );
        this.addKeyListener(
              new KeyAdapter(){
                 public void keyPressed(KeyEvent e)
                 {
                     if (e.getKeyCode()==KeyEvent.VK_INSERT)
                        nuevoElemento();
                     if (e.getKeyCode()==KeyEvent.VK_DELETE)
                         removeElement();
                 }

              }
        ) ;


        pregunta=sPregunta;
    }
    public void setModel(Vector vModel)
    {
        listModel=new DefaultListModel();
        if (vModel!=null)
        {
           for (Enumeration e=vModel.elements();e.hasMoreElements();)
           {
              listModel.addElement(e.nextElement());
            }
        }
        this.setModel(listModel);
    }
    public void removeElement()
    {
            int index = this.getSelectedIndex();
           if ((listModel.size()<=0)||(index<0)) return;

            listModel.remove(index);
            //this.setSelectedIndex(index);
            this.ensureIndexIsVisible(index);
    }
    public void replaceElement(Object element)
    {
        int index = this.getSelectedIndex();
        if (index == -1) return;
        try
        {
             listModel.setElementAt(element, index);
        }catch (Exception e)
        {
        }
    }
    public Object getElement()
    {
        int index = this.getSelectedIndex();
        if (index == -1) return null;
        try
        {
             return listModel.getElementAt(index);
        }catch (Exception e)
        {
            return null;
        }
    }
    public void addElement(Object element)
    {

            //User didn't type in a unique name...
            if ((element==null)||(element.toString().equals("")) || listModel.contains(element))
            {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            int index = this.getSelectedIndex();
            if (index == -1) {
                index = 0;
            } else {
                index++;
            }
            if (listModel.size()==0) index=0;
            listModel.insertElementAt(element, index);
            this.setSelectedIndex(index);
            this.ensureIndexIsVisible(index);
    }
    public void nuevoElemento()
    {
        if (accionNuevoElemento!=null)
        {
            accionNuevoElemento.actionPerformed(null);
            return;
        }
        if (this.isEnabled())
        {
            String inputValue=(String)JOptionPane.showInputDialog(this, "", pregunta, -1,
				                     null, null, null);
            addElement(inputValue);
        }
    }

    public Vector getVectorModel()
    {
        if (listModel==null) return null;
        Vector auxVector= new Vector();
        for (Enumeration e=listModel.elements();e.hasMoreElements();)
        {
             auxVector.add(e.nextElement());
        }
        return auxVector;
    }

}