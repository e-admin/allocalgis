package com.geopista.app.utilidades.estructuras;

import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.ListaEstructuras;


import javax.swing.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.awt.*;

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


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 17-ago-2004
 * Time: 11:13:47
 */
public class PanelCheckBoxEstructuras extends JPanel{
    private static final String NODO="NODO";
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PanelCheckBoxEstructuras.class);
    com.geopista.protocol.ListaEstructuras lista;

    public PanelCheckBoxEstructuras (ListaEstructuras lista, String locale, int iFilas, int iColumnas)
    {
        super();
        this.lista=lista;
        Hashtable hDomainNodes=lista.getLista();
        setLayout(new java.awt.GridLayout(iFilas,iColumnas));
        logger.debug("Ponemos Filas: "+iFilas+" columnas="+iColumnas);


        for (Enumeration e=hDomainNodes.elements();e.hasMoreElements();)
        {
               DomainNode auxDomain= (DomainNode)e.nextElement();
               JCheckBox auxCheckBox= new javax.swing.JCheckBox() ;
               auxCheckBox.setText(auxDomain.getTerm(locale));
               auxCheckBox.putClientProperty(NODO,auxDomain);
               add(auxCheckBox);
        }
   }
   public void changeScreenLang(String locale)
   {
      int iTotal=this.getComponentCount();
           for (int i=0;i<iTotal;i++)
           {
                try
                {
                    Component auxComponent=this.getComponent(i);
                    if (auxComponent instanceof JCheckBox)
                    {
                         JCheckBox auxCheckBox =(JCheckBox)auxComponent;
                         DomainNode auxNode = (DomainNode)auxCheckBox.getClientProperty(NODO);
                         auxCheckBox.setText(auxNode.getTerm(locale));
                    }
                }catch(Exception e){}
            }
   }

   public void setBackground(Color color)
   {
        int iTotal=this.getComponentCount();
           for (int i=0;i<iTotal;i++)
           {
                try
                {
                    Component auxComponent=this.getComponent(i);
                    auxComponent.setBackground(color);
                }catch(Exception e){}
            }
       super.setBackground(color);
   }
   public void setFont(Font font)
   {
        int iTotal=this.getComponentCount();
           for (int i=0;i<iTotal;i++)
           {
                try
                {
                    Component auxComponent=this.getComponent(i);
                    auxComponent.setFont(font);
                }catch(Exception e){}
            }
       super.setFont(font);
   }
   public Vector getMarcados()
   {
           Vector auxVector= new Vector();
           int iTotal=this.getComponentCount();
            for (int i=0;i<iTotal;i++)
            {
                 try
                 {
                     Component auxComponent=this.getComponent(i);
                     if (auxComponent instanceof JCheckBox)
                     {
                          JCheckBox auxCheckBox =(JCheckBox)auxComponent;
                          if (auxCheckBox.isSelected())
                          {
                                DomainNode auxNode = (DomainNode)auxCheckBox.getClientProperty(NODO);
                                auxVector.add(auxNode.getIdNode());
                          }
                     }
                 }catch(Exception e){}
             }
             return auxVector;
   }
   public Vector getStringMarcados()
   {
         Vector auxVector= new Vector();
         int iTotal=this.getComponentCount();
         for (int i=0;i<iTotal;i++)
         {
              try
              {
                  Component auxComponent=this.getComponent(i);
                  if (auxComponent instanceof JCheckBox)
                  {
                              JCheckBox auxCheckBox =(JCheckBox)auxComponent;
                              if (auxCheckBox.isSelected())
                              {
                                    auxVector.add(auxCheckBox.getText());
                              }
                         }
                     }catch(Exception e){}
                 }
                 return auxVector;
       }

   public void setMarcados(Vector marcados)
   {
        int iTotal=this.getComponentCount();
        for (int i=0;i<iTotal;i++)
        {
             try
             {
                 Component auxComponent=this.getComponent(i);
                 if (auxComponent instanceof JCheckBox)
                 {
                      JCheckBox auxCheckBox =(JCheckBox)auxComponent;
                      DomainNode auxNode = (DomainNode)auxCheckBox.getClientProperty(NODO);
                      if ((marcados!=null)&&(marcados.contains(auxNode.getIdNode())))
                           auxCheckBox.setSelected(true);
                      else
                           auxCheckBox.setSelected(false);
                 }
             }catch(Exception e){}
         }
   }

    public void setEnabled(boolean b){
         int iTotal=this.getComponentCount();
         for (int i=0;i<iTotal;i++){
              try{
                  Component auxComponent=this.getComponent(i);
                  if (auxComponent instanceof JCheckBox){
                      JCheckBox auxCheckBox =(JCheckBox)auxComponent;
                      auxCheckBox.setEnabled(b);
                  }
              }catch(Exception e){}
          }
    }

}
