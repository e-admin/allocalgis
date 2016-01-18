/**
 * GeopistaDomains.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.util.HashMap;
//import com.geopista.protocol.administrador.dominios.*;

public class GeopistaDomains {
    static HashMap hmMunicipios=new HashMap();
    public static final int DEFAULT_MUNICIPIO=0;

    public static void clear(){
        hmMunicipios.clear();
    }

    public static void addDomains(int idMunicipio, ListaDomain lDomains){
        hmMunicipios.put(new Integer(idMunicipio),lDomains);
    }

    public static ACDomain getDomain(int iMunicipio,int iDomain) throws Exception{
        Domain domainData=null;
        ACDomain dRet=null;
        
        if(hmMunicipios.size()==0) AdministradorCartografiaServlet.reloadDomains();
        
        ListaDomain ldMunicipio=(ListaDomain) hmMunicipios.get(new Integer(iMunicipio));
        if (ldMunicipio!=null){
            domainData=ldMunicipio.get(String.valueOf(iDomain));
        }
        if (domainData==null){
            ldMunicipio=(ListaDomain) hmMunicipios.get(new Integer(DEFAULT_MUNICIPIO));
            if (ldMunicipio!=null)
                domainData=ldMunicipio.get(String.valueOf(iDomain));
        }
        if (domainData!=null){
            dRet=new ACDomain();
            dRet.setId(iDomain);
            dRet.setName(domainData.getName());
            dRet.setDomainNode(domainData.getListaNodes().getFirst());
        }
        return dRet;
    }


}
