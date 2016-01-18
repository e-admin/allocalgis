/**
 * DXFCanvas.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */



package  org.agil.dxf;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.PrintJob;
import java.util.Vector;

import org.agil.dxf.math.Matrix4D;
import org.agil.dxf.math.Point3D;
import org.agil.dxf.math.Quaternion;
import org.agil.dxf.math.Vector3D;
import org.agil.dxf.reader.DxfException;
import org.agil.dxf.reader.DxfFile;
import org.agil.dxf.reader.DxfLAYER;



/**
 *  This class is the heart of the viewer. It manages a drawing area which shows the
 *  converted model and gives some manipulation gimmicks. At the moment it is possible
 *  to
 *  <ul>
 *  <li> ROTATE the model by dragging the mouse in the drawing area </li>
 *  <li> TRANSLATE the model by dragging the mouse in the drawing area while pressing SHIFT </li>
 *  <li> SCALE the model by dragging the mouse in Y direction while pressing CTRL </li>
 *  <li> CHANGE the view distance by dragging the mouse in X direction while pressing CTRL </li>
 *  </ul>
 *  Further this class manages a choice to control the model view while moving the model
 *  and a list to handle the visibility of the layers of the model.
 *  <p>
 *  <b>Version History:<b>
 *  <p>
 *  From 1.00 to 1.03 quaternions were introduced to handle rotations.
 *
 *
 *  @version  1.03
 *
 *
 *
 * Modificaciones Alvaro Zabala:
 *
 * Se han retocado bastante los metodos de IMPRESION
 * (print, printAll, paint) para conseguir una impresion decente trabajando con JDK 1.1.8
 *
 *
 *
 */
public class DXFCanvas extends Canvas
{
	/** different drawing modes when moved */
	public static final int NO_REDUCE = 0, BBOX = 1, LESS = 2, ALL_TYPES = 3;

	/** lines to draw, when reduced */
	private static final int NR_LINES = 1500;

	/** if BB calculation should only use visible entities */
	private static final boolean BB_USES_ONLY_VISIBLE = true;

	/** locations for coord system */
	private static final int SOUTH = 1, NORTH = 2, WEST = 4, EAST = 8;
	public static final int CS_NW = NORTH | WEST;
	public static final int CS_NE = NORTH | EAST;
	public static final int CS_SE = SOUTH | EAST;
	public static final int CS_SW = SOUTH | WEST;

	/* coord system constants */
	private static final int COORD_SIZE = 80;

	/* size of the small CS */
	private static final int COORD_OFFSET = 5;

	/* offset of the small CS */
	private static final double COORD_NO_SHOW = 0.999f;

	/* when no to show coord letter */
	private static final int COORD_LINE_PERCENT = 70;

	/* relativ length of coord line */
	private static final Color COORD_COLOR = new Color(128, 0, 0);
	private static final Font[] COORD_FONT = new Font[] {
		new Font("SansSerif", Font.PLAIN, 9), new Font("SansSerif", Font.PLAIN,
				10), new Font("SansSerif", Font.PLAIN, 11)
	};
	/** the model to draw (not private due to 1.1 problems with inner classes)*/
	public DrawAble model;

	/** last pos of mouse */
	public int prevX = -1,prevY = -1;

	/** active transformation */
	private Matrix4D trafo = new Matrix4D(),
	//rot   = new Matrix4D(), /* rotation */
	/** view mat */
	view = new Matrix4D();

	/** translation */
	private Matrix4D trans;

	/** bounding box */
	private Point3D max = new Point3D(-1.0e30f, -1.0e30f, -1.0e30f),
	min = new Point3D(1.0e30f, 1.0e30f, 1.0e30f);


	/** model diameter:Diametro del modelo cargado (longitud de la diagonal mayor del bounding box del
	 * modelo dxf cargado */
	private double diameter;

	/** scaling factor */
	private double scale;

	/** distance of viewer */
	private double dist;

	/** false until initialized */
	private boolean isInitialized = false;

	/**to see whether panel size has changed */
	private Dimension lastSize = new Dimension(-1, -1);

	/** reduced resolution model */
	private DrawAble reduced;

	/** drawed bounding box */
	private DrawAble bBox;

	/** type of reduced model */
	public int reduceType = NO_REDUCE;
	private int actualReduce = NO_REDUCE;

	/** nr of lines in this model */
	private int nrLines = 0;

	/** what to show when reduced view */
	private int whichReduceType = BBOX;

	/** where we are displayed in */
	private DxfFile dxf;

	/** layer 0 is default in DXF */
	private DxfLAYER layer0 = null;

	/** offscreen image for double buffering */
	private Image offScreenImg = null;

	/**Guarda una referencia al contenedor*/
	private Component parent;

	 /** for backward, forward buttons*/
	private Vector trafoStack = new Vector();
	/** cuurrent position in trafoStack*/
	private int stackPos = -1;
	/** last initial view*/
	private char initView;
	/** change trafo when painting?*/
	public boolean doPushTrafo = false;
	/** swap black and white?*/
	public boolean swapBW = false;
	/** position of coord system */
	private int coordPos = SOUTH | WEST;
	/** show coord system? */
	private boolean showCoord = false;
	/** is the image changed*/
	private boolean viewChanged = false;
	/** starting vector on track ball for rotation*/
	public Vector3D startVec;
	/** quaternion describing actual view's rotation   */
	private Quaternion rotQuat;
	/** last quaternion while rotating*/
	private Quaternion oldQuat;
	/** radius of track ball (changes with window size)*/
	private double radius;
	/** are we just rotating? (used for drawing circle)*/
	public boolean rotating = false;

	/**--- some mouse cursors ---*/
	public static Cursor defaultCursor = Cursor.getDefaultCursor();
	public static Cursor scaleCursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	public static Cursor moveCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
	public static Cursor rotateCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	public static Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

	/** --- the DXF default color ---*/
	private static short whiteColor = (short)7;



	// RRI
	/**flag que indica que un repintado (paing(g)) se esta haciendo al imprimir, por lo que se
	 * tomaran unas consideraciones especiales (fondo blanco,dimensiones del papel y no del
	 * control, etc)
	 */
	private boolean printing = false;

	/**Dimensiones del papel para la impresion. Son variables globales porque en algunos
	 * casos la impresion se hace pasando el graphics de la impresora al metodo paint(g), metodo
	 * que no se puede sobreescribir
	 */
	private int printHeight, printWidth;

	/**Esto es un guarreo. Indica al evento de repintado (paint(g)) que el graphics que se le pasa
	no es el de una impresora, sino el de un componente. De esta forma conseguimos eliminar el
	pixelado en la impresion
	*/
	private boolean printingComponent = true;


	 //Algunas opciones para debugging

	/** calculate faces*/
	public static final boolean calcFaces = false;
	/** calculate lighting*/
	public static final boolean calcLight = false;
	/** show debugging?*/
	public static final int debugLevel = 0;


	/**
	 *  Create with a DxfViewer as a parent (in application) when there is
	 *  no file loaded.
	 *  @param  parent   the DxfViewer parent
	 */
	public DXFCanvas (Component parent) {
		this(null, null, parent);
	}

	/**
	 *  Create with no DxfViewer as a parent (in Applet).
	 *  @param  inModel  the model to show
	 *  @param  dxf      for references
	 */
	public DXFCanvas (DrawAble inModel, DxfFile dxf) {
		this(inModel, dxf, null);
	}

	/**
	 *  Create with a DxfViewer as a parent (in application) when there is
	 *  no file loaded.
	 *  @param  inModel  the model to show
	 *  @param  dxf      for references
	 *  @param  parent   the DxfViewer parent
	 */
	public DXFCanvas (DrawAble inModel, DxfFile dxf, Component parent) {
		model = inModel;
		this.dxf = dxf;
		this.parent = parent;
		setBackground(Color.black);
		if (model != null) {
			initialize();
		}
	}


	/**
	 *  Do the initialization.
	 *  Calculate bounding box coordinates, create visible bounding box,
	 *  reset the view.
	 */
	private void initialize () {
		/* bounding box */
		max = new Point3D(-1.0e30f, -1.0e30f, -1.0e30f);
		min = new Point3D(1.0e30f, 1.0e30f, 1.0e30f);
		nrLines = model.calcBB(min, max, (Matrix4D)null, (DxfLAYER)null, BB_USES_ONLY_VISIBLE);

		/* --- make a reduced version of model --- */
		createBBox();
		resetTrafos('z');

		/* --- get layer0 --- */
		layer0 = dxf.getLayer("0");
	}

	/**
	 *  Reset the view so the model is viewed from the given axis.
	 *  @param axis <code>'x'</code>: view y-z-plane,
	 *              <code>'y'</code>: view x-z-plane,
	 *              <code>'z'</code> and others: view x-y-plane
	 */
	public void resetTrafos (char axis) {

		/* --- set/reset trafos --- */
		scale = 0f;
		lastSize.width = lastSize.height = -1;
		//    rot.identity();

		switch (axis) {
			case 'x':case 'X':
				initView = 'x';
				rotQuat = new Quaternion(-0.5f, -0.5f, -0.5f, 0.5f);
				break;
			case 'y':case 'Y':
				initView = 'y';
				rotQuat = new Quaternion(-0.70710678f, 0.0f, 0.0f, 0.70710678f);
				break;
			case 'z':case 'Z':default:
				initView = 'z';
				rotQuat = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
				break;
		}

		//diameter = max.minus(min).length();
		//dist = diameter/1.73f;  // sqrt(3) Estos eran los valores originales considerando
		//el caso 3D. Anado estas modificaciones puesto que ya solo usamos 2 dimensiones (alvaro)

		diameter = (double)Math.sqrt(((max.x - min.x)*(max.x - min.x)) + ((max.y
				- min.y)*(max.y - min.y)));
		dist = diameter/2f;

		calcViewMat();
		// Quitamos la vista en Z cambiando la matriz de transformacion
		//    trans = new Matrix4D(
		//           1, 0, 0, -(min.x+max.x)/2,
		//			 0, 1, 0, -(min.y+max.y)/2,
		//			 0, 0, 1, -(min.z+max.z)/2,
		//			 0, 0, 0, 1);

		trans = new Matrix4D(1, 0, 0, -(min.x + max.x)/2,
							 0, 1, 0, -(min.y + max.y)/2,
							 0, 0, 0, 0,
							 0, 0, 0, 1);

		/* --- empty stack --- */
		trafoStack.removeAllElements();
		stackPos = -1;
		doPushTrafo = true;
	}

	/**
	 *  Reset everything to a new model.
	 *  @param  model   drawable model
	 *  @param  dxf     DXF file for references
	 */
	public void setModel (DrawAble model, DxfFile dxf) {
		if (model != null && dxf != null) {
			this.model = model;
			this.dxf = dxf;
			initialize();
			updateView();
		}
	}

	/**Recibe el path de un dxfFile, y trata de cargarlo.
	 * Se utilizara desde aplicaciones independientes.
	 * Al tratar de crear el DxfFile a partir del path, se puede
	 * lanzar una excepcion del tipo DxfException
	 *
	 * @param dxfFilePath Path del fichero dxf que queremos cargar.
	 * @throws DxfException Excepcion propia del package DxfViewer.
	 */
	public void draw (String dxfFilePath) throws DxfException {
		DxfFile dxfFile = new DxfFile(dxfFilePath);
		draw(dxfFile);
	}

	/**Permite dibujar un DxfFile  ya existente en el canvas,
	 * Este metodo debera ser empleado desde APLICACIONES INDEPENDIENTES, pues
	 * internamente accede a ficheros de Fonts, lo que desde algunos navegadores
	 * lanzaria excepciones si no se solicitan privilegios de seguridad.
	 *
	 * @param dxfFile Objeto de la clase DxfFile que contiene las entidades a dibujar.
	 */
	public void draw (DxfFile dxfFile) {
		// convert DXF file into DrawAble
		DrawableConverter converter = new DrawableConverter();
		converter.loadDefaultFont();
		draw(dxfFile, converter);
	}

	/**Permite dibujar un DxfFile  ya existente en el canvas.
	 * Este metodo es el que se debe emplear cuando se utilice el componente
	 * desde APPLETS, pues recibe como parametro un DrawableConverter en el que
	 * ya se habran cargado los ficheros de fuentes DXF con anterioridad, previa solicitud de
	 * los permisos apropiados al applet.
	 *
	 * @param dxfFile Objeto de la clase DxfFile con las entidades graficas a dibujar
	 * @param converter Objeto de la clase DrawableConverter que ya debe tener cargadas las fuentes por
	 * defecto, previa solicitud de los privilegios al navegador. Al solicitar los privilegios de
	 * forma externa, evitamos incluir referencias a librerias externas en esta clase
	 */
	public void draw (DxfFile dxfFile, DrawableConverter converter) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		dxfFile.convert(converter);
		// DrawAble representa el modelo dibujable: hacemos una
		//conversion DxfFile---->DrawAble
		DrawAble model = converter.getModel();
		setModel(model, dxfFile);
		setCursor(Cursor.getDefaultCursor());
	}

	/**
	 *  Swap black and white colors when invert is true.
	 *  DXF entities cannot be black because the background is black.
	 *  Here this can be changed by swapping black and white.
	 *  @param  invert  do the swaping?
	 */
	public void swapBlackAndWhite (boolean invert) {
		if (swapBW != invert) {
			swapBW = invert;
			updateView();
		}
	}

	/**
	 *  Create a normalized vector from mouse coords.
	 *  @param  x   mouse x
	 *  @param  y   mouse y
	 *  @return  normalized vector simulating track ball point
	 */
	private Vector3D createSphereVector (int x, int y) {
		Dimension size = getSize();
		radius = 0.3f*(double)Math.sqrt((double)(size.width*size.width + size.height*size.height));
		Vector3D mouse = new Vector3D((x - size.width/2)/radius, (size.height/2
				- y)/radius, 0.0f);
		double mag = mouse.x*mouse.x + mouse.y*mouse.y;
		if (mag > 1.0f) {
			mouse.scale(1.0f/(double)Math.sqrt(mag));
		}
		else {
			mouse.z = (double)Math.sqrt(1.0f - mag);
		}
		return  mouse;
	}

	/**
	 *  Return the  number of lines the model has.
	 *  This number is calculated when determining the bounding box so
	 *  if hidden layers don't count for the bounding box it's possibly
	 *  incorrect.
	 *  @return  number of lines forming the model
	 */
	public int getNrLines () {
		return  nrLines;
	}


	/**Hace un zoom proporcional al desplazamiento existente entre los dos
	 * puntos proporcionados. Se usa desde el evento mouseDragged.
	 * @param x Coordenada x del segundo punto
	 * @param y Coordenada y del segundo punto
	 * @param prevX Coordenada x del primer punto
	 * @param prevY Coordenada y del primer punto
	 */
	public void doZoom (int x, int y, int prevX, int prevY) {
		setZoomCursor();
		int dx = x - prevX;
		int dy = y - prevY;
		scale *= (double)Math.pow(1.01, (double)dx);
		dist -= dy*(Math.abs(dist) + diameter)/500.0f;
		calcViewMat();
	}

	/**Desplaza el modelo DXF en funcion de los dos puntos proporcionados.
	 * Es utilizado desde el evento mouseDrag, pero puede ser utilizado
	 * desde cualquier otro metodo.
	 * @param x Coordenada x del segundo punto
	 * @param y Coordenada y del segundo punto
	 * @param prevX Coordenada x del primer punto
	 * @param prevY Coordenada y del primer punto
	 */
	public void doPan (int x, int y, int prevX, int prevY) {
		setPanCursor();
		Vector3D d = new Vector3D((x - prevX)*diameter/scale, (prevY - y)*diameter/scale,
				0);
		Matrix4D rotInv = rotQuat.getMatrix();
		rotInv.transponize();
		trans.translate(rotInv.mult(d));
	}

	/**Pone el cursor del raton al cursor de zoom
	 */
	private void setZoomCursor () {
		if (getCursor() != scaleCursor) {
			setCursor(scaleCursor);             //setZoomCursor()
			rotating = false;
		}
	}

	/**Pone el cursor del raton al cursor de pan
	 */
	private void setPanCursor () {
		if (getCursor() != moveCursor) {
			setCursor(moveCursor);
			rotating = false;
		}
	}


	/**
	 *  Update method overloaded to prevent it from clearing the screen.
	 *  We use double buffering instead.
	 *  @param   g  the graphics context
	 */
	public void update (Graphics g) {
		if (model == null) {
			super.update(g);
		}
		else {
			paint(g);
		}
	}

	/**
	 *  Add a new trafo to the trafo stack.
	 */
	private void pushTrafo () {
		doPushTrafo = false;
		if (stackPos < trafoStack.size()) {
			for (int i = trafoStack.size() - 1; i > stackPos; i--) {
				trafoStack.removeElementAt(i);
			}
		}
		trafoStack.addElement(new TrafoChunk(scale, dist, rotQuat, trans));
		stackPos++;
	}

	/**
	 *  Update the view. Call this internally instead of repaint.
	 */
	public void updateView () {
		// calling this means that the model/view has changed
		viewChanged = true;
		repaint();
	}

	public void prePaint()
	{
	}


	/**
	 *  Overloaded paint method. Uses double buffering.
	 *  Paints the model and the coordinate system.
	 *  @param   g  the graphics context
	 */
	public void paint (Graphics g) {
		if (model != null) {
			prePaint();

			/* --- initialize layer 0 (every DXF file should have one) --- */
			if (layer0 == null) {
				layer0 = dxf.getLayer("0");
			}


			// --- if trafo is reset o si estamos imprimiendo o se hace un resize()---
			if (!lastSize.equals(getSize()) || printing) {
				Dimension size = getSize();

				if (size.width <= 0 || size.height <= 0) {
					//Si el componente todavia no tiene tamano

					// happens in early startup on some machines (ie IE)
					invalidate();
					repaint();
					return;// just do nothing
				}

				// RRI
				if (printing)
					calcViewMat(printWidth, printHeight);
				else
					calcViewMat();
				viewChanged = true;
			}


			if (!viewChanged) {
				// --- this is an external redraw event (i.e. by exposure) ---
				// --- just copy the offscreen image if we have one ---
				if (offScreenImg != null) {
					// --- copy offscreen image on the screen ---
					g.drawImage(offScreenImg, 0, 0, this);
				}

			}else {
				viewChanged = false;

				// --- calculate active trafo ---
				trafo = view.mult(rotQuat.getMatrix().mult(trans));

				if (doPushTrafo) {
					pushTrafo();
				}

				if (offScreenImg != null) {
					Color bgcolor;
					DxfColorModel dxfcolors;

					if (swapBW || printing) {
						// black & white inverted   Fondo blanco
						bgcolor = Color.white;
						dxfcolors = DxfColorModel.getInvertColors();
					}else {
						// black & white as usual   Fondo negro
						bgcolor = Color.black;
						dxfcolors = DxfColorModel.getNormalColors();
					}


					//Preparamos la offscreen imagen del DXFCanvas para dibujar en ella
					Graphics offScreenGC = offScreenImg.getGraphics();
					offScreenGC.setColor(bgcolor);
					offScreenGC.fillRect(0, 0, lastSize.width, lastSize.height);
					offScreenGC.setClip(0, 0, lastSize.width, lastSize.height);

					//Dibujamos el modelo en el offscreen Image
					switch (reduceType) {
						case NO_REDUCE:
							model.draw(offScreenGC, trafo, layer0, dxfcolors,
									whiteColor);
							break;
						case BBOX:
							bBox.draw(offScreenGC, trafo, null, dxfcolors,
									whiteColor);
							break;
						case LESS:
							model.draw(offScreenGC, trafo, layer0, dxfcolors,
									whiteColor, 0, nrLines/NR_LINES);
							break;
					}

					if (printing) {

						//Si estamos imprimiendo, ademas de dibujar en el offscreen Image
						//del DXFCanvas habra que dibujar en el graphics de la impresora
						model.draw(g, trafo, layer0, dxfcolors, whiteColor);

					}

					if (printingComponent) {

						//Creo que estamos haciendo una tonteria:
						//printingComponent siempre valdra true para un preview, y false
						//para un graphics de impresora. Por tanto, con el preview
						//dibujamos dos veces(model.draw(g) y g.drawImage

						g.drawImage(offScreenImg, 0, 0, this);
					}

					printingComponent = true; //Nos aseguramos de que una vez dibujado por impresora,
					//se ejecuta el codigo anterior


					/*RRI
					 if (showCoord)
					 {
					 drawCoordSystem(offScreenGC, dxfcolors.getColor(whiteColor), bgcolor);
					 }
					 if (rotating)
					 {
					 Dimension size = getSize();
					 int diameter = (int)(2*radius);
					 offScreenGC.setColor(COORD_COLOR);
					 offScreenGC.drawOval((size.width-diameter)/2, (size.height-diameter)/2, diameter, diameter);
					 }
					 */
				}else {
				/* There are some redraw events in the very beginning with
				 * size (0,0) causing offScreenImg to be null. It's ok to do nothing.
				 */
				}
			}
		}
	}



	/**
	 *  Draw the little coord system in the corner.
	 *  @param  g      graphics context
	 *  @param  fg     foreground color
	 *  @param  bg     background color
	 */
	private void drawCoordSystem (Graphics g, Color fg, Color bg) {
		Dimension size = getSize();
		int midx = (coordPos & EAST) != 0 ? size.width - COORD_SIZE/2 - COORD_OFFSET
				- 1 : COORD_SIZE/2 + COORD_OFFSET;
		int midy = (coordPos & SOUTH) != 0 ? size.height - COORD_SIZE/2 - COORD_OFFSET
				- 1 : COORD_SIZE/2 + COORD_OFFSET;
		if (true) {
			// Workaround for Java 1.2
			// see g.translate() at functions end!
			g = g.create();
			g.translate(midx, midy);
			g.setClip(-COORD_SIZE/2, -COORD_SIZE/2, COORD_SIZE, COORD_SIZE);
		}
		else {
			g.translate(midx, midy);
		}
		g.setColor(bg);
		g.fillRect(-COORD_SIZE/2, -COORD_SIZE/2, COORD_SIZE, COORD_SIZE);
		Matrix4D rot = rotQuat.getMatrix();
		Vector3D x = new Vector3D(1.0f, 0.0f, 0.0f);
		Vector3D y = new Vector3D(0.0f, 1.0f, 0.0f);
		Vector3D z = new Vector3D(0.0f, 0.0f, 1.0f);
		rot.transform(x);
		rot.transform(y);
		rot.transform(z);
		// draw back letters
		g.setColor(fg);
		if (x.z < -0.001) {
			drawCoordLetter(g, 'x', x);
		}
		if (y.z < -0.001) {
			drawCoordLetter(g, 'y', y);
		}
		if (z.z < -0.001) {
			drawCoordLetter(g, 'z', z);
		}
		// draw cs
		g.setColor(COORD_COLOR);
		drawCoordLine(g, x);
		drawCoordLine(g, y);
		drawCoordLine(g, z);
		// draw front letters
		g.setColor(fg);
		if (x.z >= -0.001) {
			drawCoordLetter(g, 'x', x);
		}
		if (y.z >= -0.001) {
			drawCoordLetter(g, 'y', y);
		}
		if (z.z >= -0.001) {
			drawCoordLetter(g, 'z', z);
		}
		if (false) {
			// not needed with workaround for JDK 1.2
			// compare above!
			g.translate(-midx, -midy);
		}
	}

	/**
	 *  Draw the letter for a coordinate of the coordinate system.
	 *  @param  g       graphics conext
	 *  @param  letter  letter to draw
	 *  @param  vector  transformed coord vector
	 */
	private void drawCoordLetter (Graphics g, char letter, Vector3D vector) {
		int fontnr = (int)Math.floor(((vector.z + COORD_NO_SHOW)/(2*COORD_NO_SHOW))*COORD_FONT.length);
		if (fontnr >= 0 && fontnr < COORD_FONT.length) {
			g.setFont(COORD_FONT[fontnr]);
			// draw string
			int cx = (int)(vector.x*(COORD_LINE_PERCENT + 100)/2*COORD_SIZE/2)/100;
			int cy = -(int)(vector.y*(COORD_LINE_PERCENT + 100)/2*COORD_SIZE/2)/100;
			FontMetrics fm = g.getFontMetrics();
			int len = fm.charWidth(letter);
			g.drawChars(new char[] {
				letter
			}, 0, 1, cx - len/2, cy + fm.getHeight()/2 - fm.getMaxDescent());
		}
	}

	/**
	 *  Draw one line of the coord  system.
	 *  @param g     graphics conext
	 *  @param v     vector to show
	 */
	private void drawCoordLine (Graphics g, Vector3D v) {
		int px = (int)(v.x*COORD_LINE_PERCENT*COORD_SIZE/2)/100;
		int py = -(int)(v.y*COORD_LINE_PERCENT*COORD_SIZE/2)/100;
		if (px != 0 || py != 0) {
			g.drawLine(-px, -py, px, py);
		}
	}

	/**
	 *  Is the coord system shown?
	 *  @return the answer
	 */
	public boolean getShowCoord () {
		return  showCoord;
	}

	/**
	 *  Set the show status of the coord system.
	 *  @param  onOff  show state
	 */
	public void setShowCoord (boolean onOff) {
		if (onOff != showCoord) {
			showCoord = onOff;
			updateView();
		}
	}

	/**
	 *  Get the position of the coord system.
	 *  @return position (CS_NE, CS_N, CS_SE, CS_SW)
	 */
	public int getCoordPosition () {
		return  coordPos;
	}

	/**
	 *  Set the position of the coord system.
	 *  @param pos  one of CS_NE, CS_N, CS_SE, CS_SW
	 */
	public void setCoordPos (int pos) {
		switch (pos) {
			case CS_NE:case CS_NW:case CS_SE:case CS_SW:
				if (coordPos != pos) {
					coordPos = pos;
					if (showCoord) {
						updateView();
					}
				}
		}
	}

	/**
	 *  Calculate the view transformation matrix.
	 *  (Usando el tamaño del componente. Usado para repaint)
	 */
	private void calcViewMat () {
		Dimension newSize = getSize();
		calcViewMat(newSize.width, newSize.height);
	}

	/**
	 *  Calculate the view transformation matrix.
	 *  (Usando un tamaño especificado. Usado para imprimir)
	 */
	private void calcViewMat (int w, int h) {

		//Dimension newSize = getSize();

		if ((lastSize.width != w || lastSize.height != h) || printing) {

			// panel size changed
			if (w <= 0) {
				// oops! happens on some JVMs
				w = 10;
			}
			if (h <= 0) {
				// oops! happens on some JVMs
				h = 10;
			}

			/* --- calc new scaling --- */
			double lastScale;

			if (scale != 0f) {
				lastScale = (lastSize.width < lastSize.height) ? lastSize.width :
						lastSize.height;
			}
			else {
				scale = lastScale = 1.0f;
			}

			lastSize.height = h;
			lastSize.width = w;
			double newScale = (lastSize.width < lastSize.height) ? lastSize.width :
					lastSize.height;

			scale *= newScale/lastScale;
			offScreenImg = createImage(lastSize.width, lastSize.height);
		}

		view.identity();
		view.translate(lastSize.width/2, lastSize.height/2, 0);
		view.multBy(new Matrix4D(scale,      0,   0, 0,
									 0, -scale,   0, 0,
									 0,      0, -32, 0,
									 0,      0,  -1, dist));
		view.translate(0, 0, -diameter/2);
	}

	/**
	 * Calcula una matriz de transformacion local, es decir, realiza calculos
	 * analogos a los del metodo calcViewMat(w,h) pero no afecta a la propiedad
	 * view, sino que devuelve una nueva transformacion para centrar el modelo dxf en un graphics.
	 *
	 * @param w Ancho del graphics donde se quiere centrar el modelo a dibujar
	 * @param h Alto del graphics donde se quiere centrar el modelo a dibujar
	 * @param lRotQuat Matriz de rotacion
	 * @param lDiameter Longitud de la diagonal mayor del bounding box
	 * @param lDist Distancia a la que se situara el "observador" (distancia de vision)
	 * @param lTrans Traslacion
	 * @return Matriz de transformacion que nos permitira centrar el modelo en un graphics
	 */
	private Matrix4D calcLocalViewMat (int w, int h,Quaternion lRotQuat, double lDiameter, double lDist, Matrix4D lTrans) {

		Matrix4D lTrafo;//Solucion


		double newScale = (w < h) ? w : h;
		Matrix4D lView = new Matrix4D();
		lView.identity();
		lView.translate(w/2, h/2, 0);
		lView.multBy(new Matrix4D(newScale, 0, 0, 0, 0, -newScale, 0, 0, 0, 0,
				-32, 0, 0, 0, -1, lDist));
		lView.translate(0, 0, -lDiameter/2);


		lTrafo  = lView.mult(lRotQuat.getMatrix().mult(lTrans));

		return  lTrafo;

	}

	/**
	 *  Create bbox of the model for faster drawing while moving the model.
	 */
	private void createBBox () {
		DrawLines lines = new DrawLines(4);
		lines.addPoint(min.x, min.y, min.z);
		lines.addPoint(max.x, min.y, min.z);
		lines.addPoint(max.x, max.y, min.z);
		lines.addPoint(min.x, max.y, min.z);
		lines.close();
		bBox = lines.extrude(max.z - min.z);                    // no need to reinvent the wheel
	}

	/**
	 * put your documentation comment here
	 * @param type
	 */
	public void setReduce (int type) {
		actualReduce = type;
	}

	/**
	 *  Get the state of the reduce choice. The choice is not handled consistently
	 *  with the layer list because it is handled here instead of the callback type
	 *  handling used for the list.
	 */
	public int getReduce () {
		return  actualReduce;
	}

	/**
	 *  Print the content of the draw area.
	 *  @param  job  print job
	 */
	public void print (PrintJob job) {
		if (model != null) {
			Dimension pageSize = job.getPageDimension();
			Matrix4D v = new Matrix4D();        // local view matrix
			double newscale, lastscale, myscale;
			if (scale != 0f) {
				lastscale = (lastSize.width < lastSize.height) ? lastSize.width :
						lastSize.height;
			}
			else {
				myscale = lastscale = 1.0f;
			}
			newscale = (pageSize.width < pageSize.height) ? pageSize.width :
					pageSize.height;
			myscale = scale*newscale/lastscale;
			v.translate(pageSize.width/2, pageSize.height/2, 0);
			v.multBy(new Matrix4D(myscale, 0, 0, 0, 0, -myscale, 0, 0, 0, 0,
					-32, 0, 0, 0, -1, dist));
			//v.translate(0, 0, -diameter/2); A ver si es de esto
			Matrix4D tmat = v.mult(rotQuat.getMatrix().mult(trans));
			// --- do the print ---
			Graphics pg = job.getGraphics();
			model.draw(pg, tmat, layer0, DxfColorModel.getInvertColors(), whiteColor);
			pg.dispose();
		}       //if model
	}

	/**
	 *  Go backward in the view stack.
	 */
	public synchronized void goBackward () {
		if (model != null && stackPos > 0) {
			TrafoChunk chunk = (TrafoChunk)trafoStack.elementAt(--stackPos);
			//	System.out.println("Showing view "+stackPos);
			scale = chunk.scale;
			dist = chunk.dist;
			rotQuat = new Quaternion(chunk.rot);
			trans = new Matrix4D(chunk.trans);
			calcViewMat();
			updateView();
		}
	}

	/**
	 *  Go backward in the view stack.
	 */
	public synchronized void goForward () {
		if (model != null && stackPos < trafoStack.size() - 1) {
			TrafoChunk chunk = (TrafoChunk)trafoStack.elementAt(++stackPos);
			//	System.out.println("Showing view "+stackPos);
			scale = chunk.scale;
			dist = chunk.dist;
			rotQuat = new Quaternion(chunk.rot);
			trans = new Matrix4D(chunk.trans);
			calcViewMat();
			updateView();
		}
	}

	/**
	 *  Reset the view stack.
	 *  @param axis <code>'x'</code>: view y-z-plane<br>
	 *              <code>'y'</code>: view x-z-plane<br>
	 *              <code>'z'</code> and others: view x-y-plane
	 */
	public synchronized void resetViewStack (char axis) {
		if (model != null) {
			resetTrafos(axis);
			updateView();
		}
	}

	/**
	 *  Get the minimal point (from bounding box).
	 *  @return minimal point
	 */
	public Point3D getMinPoint () {
		return  new Point3D(min);
	}

	/**
	 *  Get the maximal point (from bounding box).
	 *  @return maximal point
	 */
	public Point3D getMaxPoint () {
		return  new Point3D(max);
	}

	/**
	 * Activa los flags de impresion antes
	 * de volcar el contenido del DXFCanvas sobre
	 * el graphics de la impresora.
	 * Solo se utiliza para la impresion.
	 *
	 * @param   g  Graphics en el que se pinta.
	 * @param   w  Anprintcho del repaint.
	 * @param   h  Alto del repaint.
	 * @param   printingComponent Nos indica si se vuelca sobre un componente
	 *          o sobre una impresora el contenido del DXFCanvas.
	 */
	public void print (Graphics g, int w, int h, boolean printingComponent) {
		printWidth = w;
		printHeight = h;
		printing = true;
		this.printingComponent = printingComponent;
		paint(g);
		printing = false;
	}





	/**
	 *  Este metodo se utiliza para imprimir todo el dibujo
	 *  @param g  Graphics de la impresora
	 *  @param width Ancho del papel
	 *  @param height Alto del papel
	 */
	public void printAll (Graphics g, int width, int height) {
		if (model != null) {

			//Las transformaciones que hacemos son las mismas que las efectuadas
			//por los metodos calcViewMat(w,h) y paint(g), pero empleando variables
			//locales para que no afecte al dibujo del modelo
			Quaternion localRotQuat = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);

			double localDiameter = (double)Math.sqrt(((max.x - min.x)*(max.x -
					min.x)) + ((max.y - min.y)*(max.y - min.y)));

			Matrix4D localTrans = new Matrix4D(1, 0, 0, -(min.x + max.x)/2,
					0, 1, 0, -(min.y + max.y)/2, 0, 0, 0, 0, 0, 0, 0, 1);
			//			paint(g);

			Matrix4D lTrafo =calcLocalViewMat(width, height,localRotQuat,
							localDiameter,localDiameter/3f, localTrans);

			Color bgcolor = Color.white;
			DxfColorModel dxfcolors = DxfColorModel.getInvertColors();

			g.setClip(0, 0, width+1,height+1);
			g.setColor(bgcolor);
			g.fillRect(0, 0, width,height);


			model.draw(g, lTrafo, layer0, dxfcolors, whiteColor);

		} //if model
	}

	/**
	 *  Inner class just to hold all transformation related data as an unit.
	 */
	class TrafoChunk {
		double scale;
		double dist;
		Quaternion rot;
		Matrix4D trans;

		/**
		 *  Create new chunk.
		 *  @param  sc   scaling
		 *  @param  d    distance
		 *  @param  r    rotation quaternion
		 *  @param  t    translation matrix
		 */
		TrafoChunk (double sc, double d, Quaternion r, Matrix4D t) {
			scale = sc;
			dist = d;
			rot = new Quaternion(r);
			trans = new Matrix4D(t);
		}
	}
}



