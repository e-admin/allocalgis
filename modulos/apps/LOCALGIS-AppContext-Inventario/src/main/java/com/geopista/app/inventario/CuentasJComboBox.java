/**
 * CuentasJComboBox.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario;

import javax.swing.JComboBox;

import com.geopista.protocol.inventario.CuentaAmortizacion;
import com.geopista.protocol.inventario.CuentaContable;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 01-ago-2006
 * Time: 15:43:29
 * To change this template use File | Settings | File Templates.
 */
public class CuentasJComboBox extends JComboBox{
    boolean conBlanco=false;

    public CuentasJComboBox (Object[] lista, java.awt.event.ActionListener accion){
        this(lista, accion, true);
    }

    public CuentasJComboBox (Object[] lista, java.awt.event.ActionListener accion, boolean conBlanco){
        super();

        this.conBlanco=conBlanco;
        if (conBlanco){
            this.addItem(new CuentaContable());

        }
        if (lista!=null){
            for (int i=0; i<lista.length;i++){
                  this.addItem(lista[i]);
            }
        }
        this.setRenderer(new com.geopista.app.inventario.CuentasComboBoxRenderer());
        if (accion!=null)
            this.addActionListener(accion);
  }
  public void setSelected(long id){
      if (id==-1){
          if (conBlanco) this.setSelectedIndex(0);
          return;
      }
      int iSize=this.getModel().getSize();
      for (int i=0; i<iSize;i++){
          Object obj= this.getModel().getElementAt(i);
          if (obj instanceof CuentaContable){
              CuentaContable cc= (CuentaContable)obj;
              if (id==cc.getId()){
                  this.getModel().setSelectedItem(cc);
                  return;
              }
          }else if (obj instanceof CuentaAmortizacion){
              CuentaAmortizacion ca= (CuentaAmortizacion)obj;
              if (id==ca.getId()){
                  this.getModel().setSelectedItem(ca);
                  return;
              }
          }
      }
      if (conBlanco) this.setSelectedIndex(0);
  }
  public Object getSelected(){
       if (getSelectedIndex()<0) return null;
       if (getSelectedIndex()==0 && conBlanco)return null;
       return getSelectedItem();
  }

    public void removeAllItems(){
        super.removeAllItems();
    }


}
