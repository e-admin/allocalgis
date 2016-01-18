package com.geopista.server.administradorCartografia;

import com.geopista.feature.*;

//import com.geopista.protocol.administrador.dominios.*;

import java.util.HashMap;
import java.util.Iterator;

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
