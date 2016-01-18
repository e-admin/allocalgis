package com.geopista.ui.plugin.sadim.ws;

public interface WSOtrosOrganismosImplService extends javax.xml.rpc.Service {
	public String getWSOtrosOrganismosImplPortAddress();

	public WSOtrosOrganismos getWSOtrosOrganismosImplPort()
			throws javax.xml.rpc.ServiceException;

	public WSOtrosOrganismos getWSOtrosOrganismosImplPort(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
