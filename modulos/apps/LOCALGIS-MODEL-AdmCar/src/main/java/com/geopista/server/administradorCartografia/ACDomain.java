/**
 * ACDomain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;


import java.io.Serializable;
import java.util.Iterator;

import com.geopista.feature.AutoFieldDomain;
import com.geopista.feature.BooleanDomain;
import com.geopista.feature.CodeBookDomain;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.DateDomain;
import com.geopista.feature.NumberDomain;
import com.geopista.feature.StringDomain;
import com.geopista.feature.TreeDomain;

/** Datos de un dominio para el interfaz con el Administrador de Cartografia */
public class ACDomain implements Serializable, IACDomain{
    int id;
    String name;
    DomainNode domainNode;

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDomain#getId()
	 */
    @Override
	public int getId() {
        return id;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDomain#setId(int)
	 */
    @Override
	public void setId(int id) {
        this.id = id;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDomain#getName()
	 */
    @Override
	public String getName() {
        return name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDomain#setName(java.lang.String)
	 */
    @Override
	public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDomain#getDomainNode()
	 */
	public DomainNode getDomainNode() {
        return domainNode;
    }

    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDomain#setDomainNode(com.geopista.server.administradorCartografia.DomainNode)
	 */
	public void setDomainNode(DomainNode domainNode) {
        this.domainNode = domainNode;
    }


    /* (non-Javadoc)
	 * @see com.geopista.server.administradorCartografia.IACDomain#convert(java.lang.String)
	 */
    @Override
	public com.geopista.feature.Domain convert(String sLocale){
        com.geopista.feature.Domain dRet=null;
        if (this.domainNode!=null)
            switch (this.domainNode.getType()){
                case com.geopista.feature.Domain.CODEBOOK:
                    //dRet=newCodeBookDomain(this.name,this.domainNode.getTerm(sLocale),this.domainNode.getlHijos(),sLocale);
                    dRet=newCodeBookDomain(this.name,this.domainNode.getTerm(sLocale),this.domainNode.getlHijos(),sLocale,this.domainNode.getPatron());
                    break;
                case com.geopista.feature.Domain.PATTERN:
                    dRet=newStringDomain(this.domainNode.getPatron(),this.domainNode.getTerm(sLocale));
                    break;
                case com.geopista.feature.Domain.DATE:
                    dRet=newDateDomain(this.domainNode.getPatron(),this.domainNode.getTerm(sLocale));
                    break;
                case com.geopista.feature.Domain.NUMBER:
                    dRet=newNumberDomain(this.domainNode.getPatron(),this.domainNode.getTerm(sLocale));
                    break;
                case com.geopista.feature.Domain.TREE:
                    dRet=newTreeDomain(this.name,this.domainNode,sLocale);
                    break;
                case com.geopista.feature.Domain.BOOLEAN:
                    dRet=newBooleanDomain(this.name,this.domainNode.getTerm(sLocale));
                    break;
                case com.geopista.feature.Domain.AUTO:
                    dRet=newAutoFieldDomain(this.getDomainNode().getPatron(),this.domainNode.getTerm(sLocale));
                    break;
            }
        return dRet;
    }

    private static BooleanDomain newBooleanDomain(String sName,String sDescription){
        BooleanDomain bdRet=new BooleanDomain(sName,sDescription);
        return bdRet;
    }
    private static AutoFieldDomain newAutoFieldDomain(String sName,String sDescription){
        AutoFieldDomain atRet=new AutoFieldDomain(sName,sDescription);
        return atRet;
    }

    private static StringDomain newStringDomain(String sPattern,String sDescription){
        StringDomain sdRet=new StringDomain(sPattern,sDescription);
        return sdRet;
    }

    private static CodeBookDomain newCodeBookDomain(String sName,String sDescription,ListaDomainNode ldnEntries, String sLocale,String patron){
        CodeBookDomain cbdRet=new CodeBookDomain(sName,sDescription);
        cbdRet.setPattern(patron);
        DomainNode node=null;
        String sPattern;
        String sTerm;
        for (Iterator it=ldnEntries.gethDom().values().iterator();it.hasNext();){
            node=(DomainNode)it.next();
            cbdRet.addChild((CodedEntryDomain)newCodedEntryDomain(node.getPatron(),node.getTerm(sLocale)));
        }
        return cbdRet;
    }

    private static CodedEntryDomain newCodedEntryDomain(String sPattern, String sDescription){
        CodedEntryDomain cedRet=new CodedEntryDomain(sPattern,sDescription);
        return cedRet;
    }

    private static DateDomain newDateDomain(String sPattern, String sDescription){
        DateDomain ddRet=new DateDomain(sPattern,sDescription);
        return ddRet;
    }

    private static NumberDomain newNumberDomain(String sPattern, String sDescription){
        NumberDomain ndRet=new NumberDomain(sPattern,sDescription);
        return ndRet;
    }


    private static TreeDomain newTreeDomain(String sName,DomainNode dnRoot,String sLocale){
        return (TreeDomain)addTreeNode(null,dnRoot,sLocale);
    }

    private static com.geopista.feature.Domain addTreeNode(com.geopista.feature.Domain dRoot,DomainNode childNode,String sLocale){
        String sName=childNode.getTerm(sLocale);
        com.geopista.feature.Domain newDom=null;
        switch (childNode.getType()){
            case com.geopista.feature.Domain.TREE:
                newDom=new TreeDomain(sName,sName);
                break;
            case com.geopista.feature.Domain.PATTERN:
                newDom=newStringDomain(childNode.getPatron(),sName);
                break;
            case com.geopista.feature.Domain.DATE:
                newDom=newDateDomain(childNode.getPatron(),sName);
                break;
            case com.geopista.feature.Domain.NUMBER:
                newDom=newNumberDomain(childNode.getPatron(),sName);
                break;
            case com.geopista.feature.Domain.CODEDENTRY:
                newDom=newCodedEntryDomain(childNode.getPatron(),sName);
                break;
            case com.geopista.feature.Domain.BOOLEAN:
                newDom=newBooleanDomain(sName,sName);
                break;
        }
        if (dRoot!=null)
            dRoot.addChild(newDom);
        for (Iterator it=childNode.getlHijos().gethDom().values().iterator();it.hasNext();){
            DomainNode node=(DomainNode)it.next();
            addTreeNode(newDom,node,sLocale);
        }
        return newDom;
    }
}
