/**
 * ExtentManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 20-oct-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print.elements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;

/**
 * Gestor de la extensión preferida de los mapas.
 * Permite almacenar una colección de extensiones para generar series de mapas.
 * Independiza los encuadres de lo que se está viendo en el LayerViewPanel principal.
 * @author juacas
 *
 */
public class ExtentManager
{
	// Origen por defecto de encuadres
	private ILayerViewPanel	layerViewPanel;
	// Lista de encuadres
	private Vector extents	= new Vector();
	// Lista de parametros enviados
	private Map parameters;
	
	private Envelope currentenvelope;
	private int	currentExtentIndex=0;
	

	public Map getParameters() {
		return parameters;
	}
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Constructor que genera un Extentmanager enlazado con un mapa
	 */
	public ExtentManager(ILayerViewPanel lvp)
	{
		this.layerViewPanel = lvp;
		this.parameters = new HashMap();
	}
	
	public int getExtentCount()
	{
		return extents.size()==0?1:extents.size();
	}
	/**
	 * Genera los extents a partir de los envelope de las features pasadas
	 * @param fc
	 */
	public void setExtentsFromFeatures(List lstFeatures)
	{
		extents	= new Vector();
		for (Iterator iter = lstFeatures.iterator(); iter.hasNext();)
		{
			Feature element = (Feature) iter.next();
			addExtentFromFeature(element);
		}
	}
	/**
	 * Añade un envelope nuevo al conjunto a partir de la feature
	 * @param element
	 */
	public void addExtentFromFeature(Feature element)
	{
		Envelope env = element.getGeometry().getEnvelopeInternal();
		extents.add(env);
	}
	/**
	 * Devuelve el extent actual teniendo en cuenta la configuración de
	 * extentType
	 * 
	 * @param extentType
	 *            puede ser FULLEXTENT, CURRENTEXTENT o un número indicando
	 *            escala concreta 1:N
	 * @return
	 */
	public Envelope getEnvelope(int extentType, MapFrame mapFrame)
	{

		if (extentType == MapFrame.CURRENT_EXTENT)
		{

			Envelope mapEnvelope = getCurrentReferenceEnvelope();
			return mapEnvelope;

		}
		else if (extentType == MapFrame.FULL_EXTENT)
		{

			return layerViewPanel.getViewport().fullExtent();

		}
		else
			/**
			 * Escala especificada en el entero extentType
			 */
		{
			PrintLayoutFrame parent = mapFrame.getParent();
			double newScale = extentType;// extentType;
			double pageWidth = parent.getPageFormat().getWidth(); // picas
			double pageHeight = parent.getPageFormat().getHeight(); // picas
			double px_cm = ((pageWidth) / 72 * 2.54);
			double py_cm = ((pageHeight) / 72 * 2.54);

			double mapWidth = mapFrame.getGraphicElementsForPrint().getWidth()
					* px_cm / parent.getPage().getHauteur();

			double mapHeight = mapFrame.getGraphicElementsForPrint().getHeight()
					* py_cm / parent.getPage().getLargeur();

			double envelopeWidth = newScale * mapWidth / 100; // en metros
			double envelopeHeight = newScale * mapHeight / 100;
			// obtiene el centro del encuadre
			Envelope oldenvelope = getCurrentReferenceEnvelope();
			double envelopeCenterX = (oldenvelope.getMaxX() + oldenvelope.getMinX()) / 2;
			double envelopeCenterY = (oldenvelope.getMaxY() + oldenvelope.getMinY()) / 2;
			// Calcula encuadre de la escala adecuada
			Envelope envelope = new Envelope(envelopeCenterX - (envelopeWidth / 2),
					envelopeCenterX + (envelopeWidth / 2), envelopeCenterY
					+ (envelopeHeight / 2), envelopeCenterY
					- (envelopeHeight / 2));

			// double px_cm = ((printAreaWidth - 2*margen)/72 * 2.54);
			// double py_cm = ((printAreaHeight - 2*margen)/72 * 2.54);
			//
			// double envelopeWidth = newScale*px_cm/100; // en metros
			// double envelopeHeight = newScale*py_cm/100; // en metros
			// Envelope originalEnvelope =
			// layerViewPanel.getViewport().getEnvelopeInModelCoordinates();
			// double envelopeCenterX =
			// originalEnvelope.getMinX()+(originalEnvelope.getMaxX()-originalEnvelope.getMinX())/2;
			// double envelopeCenterY =
			// originalEnvelope.getMinY()+(originalEnvelope.getMaxY()-originalEnvelope.getMinY())/2;
			//		      
			// //
			// envelope = new
			// Envelope(envelopeCenterX-(envelopeWidth/2),envelopeCenterX+(envelopeWidth/2),
			// envelopeCenterY+(envelopeHeight/2),envelopeCenterY-(envelopeHeight/2));
			return envelope;
		}
	}

	/**
	 * Current envelope may be:
	 * 
	 * a) current fixed envelope in extent list
	 * b) current dinamic envelope in viewport 
	 * @return
	 */
	private Envelope getCurrentReferenceEnvelope()
	{
		if (extents.size()==0)
			this.currentenvelope = layerViewPanel.getViewport().getEnvelopeInModelCoordinates();
		return currentenvelope;
	}
	/**
	 * selects current page if exists
	 * @param i
	 */
	public void setCurrentExtent(int i )
	{
		if (extents.size()==0 && i==0) return;
		currentenvelope=(Envelope) extents.get(i);
		currentExtentIndex=i;
	}
	public int getCurrentExtent()
	{
		return currentExtentIndex;
	}


}
