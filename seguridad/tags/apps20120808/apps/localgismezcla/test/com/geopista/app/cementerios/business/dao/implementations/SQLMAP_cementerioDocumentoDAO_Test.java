package com.geopista.app.cementerios.business.dao.implementations;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.geopista.app.cementerios.business.dao.interfaces.DocumentoDAO;
import com.geopista.app.cementerios.business.dao.interfaces.mapDAO;
import com.geopista.app.cementerios.business.vo.Documento;
import com.geopista.protocol.cementerios.ElemCementerioBean;
import com.geopista.protocol.document.DocumentBean;

public class SQLMAP_cementerioDocumentoDAO_Test {
	
	
	mapDAO mapDAO;
	DocumentoDAO documentDAO;
	
	public SQLMAP_cementerioDocumentoDAO_Test() {
		mapDAO = new mapDAOImpl();
		documentDAO = new  DocumentoDAOImpl();
	}

	@Test
	public void testSelectAttached() {
		assertTrue(true);
	}

	@Test
	public void testSelectAttachedCementerio() throws SQLException {
		
    	int idElemCementerio = 2;
    	String superpatron = "1";
    	String patron = "2";
        
        
        List listaDocumentos = null;
//		listaDocumentos = mapDAO.selectAttachedCementerio(idElemCementerio);
        listaDocumentos = documentDAO.selectAttachedCementerio(idElemCementerio, superpatron, patron);
        DocumentBean document= null;
        for (int i = 0; i < listaDocumentos.size(); i++) {
			Documento documentoVo = (Documento) listaDocumentos.get(i);
			System.out.println(documentoVo.getComentario());
        }
		System.out.println("FINN");
	}

//	@Test
//	public void testSelectdocumento() throws SQLException {
//		
//        
//		List listaDocumentos = documentDAO.selectdocumento();
//        DocumentBean document= null;
//        for (int i = 0; i < listaDocumentos.size(); i++) {
//			Documento documentoVo = (Documento) listaDocumentos.get(i);
//			System.out.println(documentoVo.getComentario());
//        }
//		System.out.println("FINN");
//	}
//
//	@Test
//	public void testSelectdifunto() throws SQLException {
//		List listaDocumentos = mapDAO.selectDifunto();
//        DocumentBean document= null;
//        if (listaDocumentos.size()> 0){
//        	System.out.println("okiii");
//        }
//		System.out.println("FINN");
//	}
//	
//	@Test
//	public void testSelectanexo() throws SQLException {
//		List listaDocumentos = mapDAO.selectanexo();
//        DocumentBean document= null;
//        for (int i = 0; i < listaDocumentos.size(); i++) {
//			Documento documentoVo = (Documento) listaDocumentos.get(i);
//			System.out.println(documentoVo.getComentario());
//        }
//		System.out.println("FINN");
//	}
	
}
