package org.agil.core.jump.coverage;

import java.awt.Image;
import java.awt.RenderingHints;

import org.agil.core.coverage.Coverage;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.AbstractLayerable;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;

/**
 *  Layer -del modelo de clases de JUMP- encargada de dibujar origenes de
 *  datos raster.
 *
 *@author    alvaro zabala 29-sep-2003
 */
public class CoverageLayer extends AbstractLayerable implements Cloneable {

	/**
	 *  algoritmo de interpolacion empleado
	 */
	private int interpolation = NEAREST_NEIGHBOUR;
	/**
	 *  Coverage que proporciona los datos -en forma de imagen- a la capa para
	 *  que ésta los dibuje.
	 */
	private Coverage coverage;
	/**
	 *  referencia que permite guardar informacion de sesion (obligada al heredar
	 *  de Layer)
	 */
	private Blackboard blackboard;
	/**
	 *  transparencia. puede tomar un valor de 0 a 255
	 */
	private int alpha = 200;

	/**
	 *  Constante que indica que utiliza el algoritmo del vecino mas proximo para
	 *  el relleno.
	 */
	public static final int NEAREST_NEIGHBOUR = 0;
	/**
	 *  Constante que indica que utiliza el algoritmo de interpolacion bilineal
	 *  para el relleno.
	 */
	public static final int BILINEAR = 1;
	/**
	 *  Constante que indica que utiliza el algoritmo de interpolacion bicubica
	 *  para el relleno.
	 */
	public static final int BICUBIC = 2;

	//75 % opaco (hacer configurable)

public CoverageLayer()
{
	blackboard = new Blackboard();
}
	/**
	 *  Constructor
	 *
	 *@param  coverage      origen de datos raster
	 *@param  layerName     nombre del layer
	 *@param  layerManager  gestor de layers
	 */
	public CoverageLayer(Coverage coverage, String layerName, LayerManager layerManager) {
		super(layerName, layerManager);
		setCoverage(coverage);
		blackboard = new Blackboard();
	}


	/**
	 *  Fija el grado de transparencia (de 0 a 255)
	 *
	 *@param  i  valor del canal alpha
	 */
	public void setAlpha(int i) {
		alpha = i;
	}


	/**
	 *  Fija el tipo de interpolacion
	 *
	 *@param  i  constante entera que indica el tipo de interpolacion utilizado
	 */
	public void setInterpolation(int i) {
		interpolation = i;
	}
	
	/**
	 * Devuelve la cobertura asociada
	 * @return cobertura asociada
	 */
	public Coverage getCoverage(){
		return coverage;
	}


	/**
	 *  Devuelve un objeto para guardar informacion de estado (Blackboard)
	 *
	 *@return    The Blackboard value
	 *@see       com.vividsolutions.jump.workbench.model.Layerable#getBlackboard()
	 */
	public Blackboard getBlackboard() {
		return blackboard;
	}


	/**
	 *  Devuelve el grado de transparencia (de 0 a 255)
	 *
	 *@return
	 */
	public int getAlpha() {
		return alpha;
	}


	/**
	 *  devuelve la constante entera que indica el tipo de interpolacion empleado
	 *
	 *@return    constante que indica el tipo de interpolacion
	 */
	public int getInterpolation() {
		return interpolation;
	}


	/**
	 *  Devuelve el RenderingHints (propiedad AWT) que se encargará de utilizar
	 *  el algoritmo de interpolacion especificado a la hora de dibujar imagenes
	 *  raster.
	 *
	 *@return    renderingHints clase que sirve para especificar propiedades de
	 *      dibujo de Java2D
	 */
	public RenderingHints getRenderingHints() {
		RenderingHints solucion;
		switch (interpolation) {
			case BILINEAR:
				solucion = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				break;
			case BICUBIC:
				solucion = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				break;
			default:
				solucion = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
				break;
		}
		return solucion;
	}


	/**
	 *  Gets the Name attribute of the CoverageLayer object
	 *
	 *@return    The Name value
	 */
	public String getName() {
		return super.getName();
	}


	/**
	 *  Devuelve el envelope de la Coverage asociada.
	 *
	 *@return    envelope
	 */
	public Envelope getEnvelope() {
		return coverage.getEnvelope();
	}


	/**
	 *  Devuelve una imagen del origen de datos para la zona del puerto de vision
	 *  del LayerViewPanel.
	 *
	 *@param  panel  panel de dibujo de la aplicacion principal.
	 *@return        imagen del origen de datos asociado para el puerto de
	 *      vision.
	 */
	public Image createImage(LayerViewPanel panel) {
		Image solucion = null;
		Envelope envelope = panel.getViewport().getEnvelopeInModelCoordinates();
		int width = panel.getWidth();
		int height = panel.getHeight();
		solucion = coverage.getBufferedImage(width, height, envelope);
		return solucion;
	}


	/**
	 *  Converts to a String representation of the object.
	 *
	 *@return    A string representation of the object.
	 */
	public String toString() {
		return getName();
	}


	/**
	 *  Creates an exact copy of this object.
	 *
	 *@return                                 Description of the Returned Value
	 *@exception  CloneNotSupportedException  Description of Exception
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public void setCoverage(Coverage coverage)
	{
		this.coverage = coverage;
	}
}
