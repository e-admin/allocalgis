
package com.geopista.ui.plugin.sadim.ws;


import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;


public class ClientTest extends TestCase
{

	public void testWSIneSadei() throws ServiceException, RemoteException
	{
		WSOtrosOrganismosImplServiceLocator wsOtrosOrganismosServiceLocator=new WSOtrosOrganismosImplServiceLocator();

		WSOtrosOrganismos otrosOrganismos=wsOtrosOrganismosServiceLocator.getWSOtrosOrganismosImplPort();

		String resultado=otrosOrganismos.obtenerInformacionIneSadei("33","1",2012);
		
		System.out.println("\n***********************************************************\n"+resultado);
	}

	public void testWSEducacion() throws ServiceException, RemoteException
	{
		WSOtrosOrganismosImplServiceLocator wsOtrosOrganismosServiceLocator=new WSOtrosOrganismosImplServiceLocator();

		WSOtrosOrganismos otrosOrganismos=wsOtrosOrganismosServiceLocator.getWSOtrosOrganismosImplPort();

		String resultado=otrosOrganismos.obtenerInformacionEducacion("33","1",2012);
		
		System.out.println("\n***********************************************************\n"+resultado);
	}

	public void testWSTurismo() throws ServiceException, RemoteException
	{
		WSOtrosOrganismosImplServiceLocator wsOtrosOrganismosServiceLocator=new WSOtrosOrganismosImplServiceLocator();

		WSOtrosOrganismos otrosOrganismos=wsOtrosOrganismosServiceLocator.getWSOtrosOrganismosImplPort();

		String resultado=otrosOrganismos.obtenerInformacionTurismo("33","1",2012);
		
		System.out.println("\n***********************************************************\n"+resultado);
	}

	
	public void testWSCentroSanitario() throws ServiceException, RemoteException
	{
		WSOtrosOrganismosImplServiceLocator wsOtrosOrganismosServiceLocator=new WSOtrosOrganismosImplServiceLocator();

		WSOtrosOrganismos otrosOrganismos=wsOtrosOrganismosServiceLocator.getWSOtrosOrganismosImplPort();

		String resultado=otrosOrganismos.obtenerInformacionCentroSanitario("33","1",2012);
		
		System.out.println("\n***********************************************************\n"+resultado);
	}

	
	public void testWSCentroAsistencial() throws ServiceException, RemoteException
	{
		WSOtrosOrganismosImplServiceLocator wsOtrosOrganismosServiceLocator=new WSOtrosOrganismosImplServiceLocator();

		WSOtrosOrganismos otrosOrganismos=wsOtrosOrganismosServiceLocator.getWSOtrosOrganismosImplPort();

		String resultado=otrosOrganismos.obtenerInformacionCentroAsistencial("33","1",2012);
		
		System.out.println("\n***********************************************************\n"+resultado);
	}
	
}
