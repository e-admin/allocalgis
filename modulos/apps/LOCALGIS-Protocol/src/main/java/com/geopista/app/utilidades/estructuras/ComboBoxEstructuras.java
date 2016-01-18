/**
 * ComboBoxEstructuras.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades.estructuras;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComboBox;

import com.geopista.protocol.administrador.dominios.DomainNode;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 26-jul-2004
 * Time: 13:17:09
 */
public class ComboBoxEstructuras extends JComboBox {

    boolean conBlanco=false;

    /** inicio */
    public ComboBoxEstructuras (boolean blanco){
        this(new com.geopista.protocol.ListaEstructuras(), null, "es_ES", blanco);
    }

    /**fin*/

    public ComboBoxEstructuras (com.geopista.protocol.ListaEstructuras lista, java.awt.event.ActionListener accion, String locale)
    {
        this(lista, accion,locale, true);
    }
    public ComboBoxEstructuras (com.geopista.protocol.ListaEstructuras lista, java.awt.event.ActionListener accion, String locale, boolean conBlanco)
    {
        super();

        this.conBlanco=conBlanco;
        Vector vDomainNodes=lista.getListaSorted(locale);
        if (conBlanco){
            this.addItem(new DomainNode());

        }
        if (vDomainNodes!=null)
        {
            for (Enumeration e=vDomainNodes.elements();e.hasMoreElements();)
            {
                   DomainNode auxDomain= (DomainNode)e.nextElement();
                    this.addItem(auxDomain);
            }
        }
        this.setRenderer(new com.geopista.app.utilidades.estructuras.RendererEstructuras(locale));
        if (accion!=null)
            this.addActionListener(accion);
  }
  public void setSelected(String idDomainNode)
  {
      if (idDomainNode==null)
      {
          if (conBlanco) this.setSelectedIndex(0);
          return;
      }
      int iSize=this.getModel().getSize();
      for (int i=0; i<iSize;i++)
      {
          DomainNode auxDomain=(DomainNode)this.getModel().getElementAt(i);
          if (idDomainNode.equals(auxDomain.getIdNode()))
          {
              this.getModel().setSelectedItem(auxDomain);
              return;
          }
      }
      if (conBlanco) this.setSelectedIndex(0);
  }
  public void setSelectedPatron(String patron)
  {
      if (patron==null)
      {
          if (conBlanco) this.setSelectedIndex(0);
          return;
      }
      int iSize=this.getModel().getSize();
      for (int i=0; i<iSize;i++)
      {
          DomainNode auxDomain=(DomainNode)this.getModel().getElementAt(i);
          if (patron.equalsIgnoreCase(auxDomain.getPatron()))
          {
              this.getModel().setSelectedItem(auxDomain);
              return;
          }
      }
      if (conBlanco) this.setSelectedIndex(0);
  }
  public String getSelectedPatron()
  {
      if (getSelectedIndex()<0) return null;
      if (getSelectedIndex()==0 && conBlanco) return null;
      DomainNode domain=(DomainNode)getSelectedItem();
      return domain.getPatron();
  }
  public String getSelectedId()
  {
       if (getSelectedIndex()<0) return null;
       if (getSelectedIndex()==0 && conBlanco)return null;
       DomainNode domain=(DomainNode)getSelectedItem();
       return domain.getIdNode();

  }
    public void removeAllItems(){
        super.removeAllItems();
    }

    public void setEstructuras(com.geopista.protocol.ListaEstructuras lista, java.awt.event.ActionListener accion, String locale, boolean conBlanco){
        this.conBlanco=conBlanco;
        Vector vDomainNodes=lista.getListaSorted(locale);
        if (conBlanco)
            this.addItem(new DomainNode ());
        if (vDomainNodes!=null){
            for (Enumeration e=vDomainNodes.elements();e.hasMoreElements();){
                   DomainNode auxDomain= (DomainNode)e.nextElement();
                    this.addItem(auxDomain);
            }
        }
        this.setRenderer(new com.geopista.app.utilidades.estructuras.RendererEstructuras(locale));
        if (accion!=null)
            this.addActionListener(accion);

    }

}
