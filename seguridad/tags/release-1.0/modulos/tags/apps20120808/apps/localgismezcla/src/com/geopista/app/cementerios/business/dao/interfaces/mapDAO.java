package com.geopista.app.cementerios.business.dao.interfaces;

import java.util.List;



public interface mapDAO {
	
	List selectAttachedCementerio (Integer idelemcementerio);
	
	List selectdocumento();
	
	List selectanexo();

	public List selectDifunto();
}