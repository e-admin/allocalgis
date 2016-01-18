/**
 * ClientTest.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
