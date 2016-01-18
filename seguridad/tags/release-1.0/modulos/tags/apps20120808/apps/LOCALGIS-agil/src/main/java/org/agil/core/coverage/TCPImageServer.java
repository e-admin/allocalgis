package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 *  Título: TCPImageServer. Descripcion: <p>
 *
 *  Sirve datos raster via TCP/IP. </p> Copyright: Copyright (c) 2002 Empresa:
 * AGIL.
 *
 *@author
 *@created    11 de marzo de 2003
 *@version    1.0
 */

public class TCPImageServer implements Runnable {

	/**
	 *  Puerto detras del que escucha el servidor de imagenes
	 */
	private int port;

	/**
	 *  Description of the Field
	 */
	public static final int COMPONENT_SAMPLE_MODEL = 1;
	/**
	 *  Description of the Field
	 */
	public static final int SINGLE_PIXEL_SAMPLE_MODEL = 2;
	/**
	 *  Description of the Field
	 */
	public static final int MULTI_PIXEL_SAMPLE_MODEL = 4;


	/**
	 *  clase encargada de proporcionar imagenes
	 */
	//private ServidorImagenes imageServer;


	/**
	 *  Constructor. Recibe el puerto detras del que escuchara el servidor de
	 *  imagenes
	 *
	 *@param  port              Description of Parameter
	 *@param  configuracionXML  Description of Parameter
	 */
	public TCPImageServer(int port, String configuracionXML) {
		this.port = port;
		//imageServer = new ServidorImagenes();
		try {
			//imageServer.parsearFicheroCatalogo(new FileInputStream(new File(configuracionXML)));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 *  Proceso principal del servidor de imagenes. Permanece a la escucha de
	 *  peticiones, y por cada nueva que llegue creará un thread propio para
	 *  atenderla TODO cuando cambiemos a JDK1.4, ya no sera necesario crear un
	 *  thread nuevo con las operaciones I/O no bloqueantes
	 */
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				System.out.println("esperando peticiones...");
				Socket socket = serverSocket.accept();
				ProcesoServidorImagenes pServidor = new ProcesoServidorImagenes(socket);
				//pServidor.setImageServer(imageServer);
				Thread thread = new Thread(pServidor);
				thread.start();
			}
			//while
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-2);
		}
	}


	/**
	 *  Punto de entrada de la aplicacion. Se debe especificar como argumento de
	 *  linea de comando el puerto detrás del que escuchará el servidor.
	 *
	 *@param  args  The command line arguments
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Uso correcto:java org.agil.kernel.jump.TCPImageServer numeroPuerto configuracion.xml");
			System.out.println("Saliendo de la aplicacion...");
			System.exit(-2);
		}
		int puerto = Integer.parseInt(args[0]);
		String configuracionXml = args[1];
		new TCPImageServer(puerto, configuracionXml).run();

	}
}

/**
 *  Proceso dedicado que atiende las sucesivas peticiones que van llegando.
 *  Frente a la forma de trabajar convencional (por cada imagen solicitada se
 *  crea un thread, y una vez atendida el thread se destruye) este thread
 *  permanece vivo. ¿Razón? Hemos creado una arquitectura de PROXYs, es decir,
 *  cada proceso dedicado atiende a un consumidor remoto de datos. Mientras
 *  exista este consumidor, existira el thread (ahorrandonos el tiempo de
 *  crear y destruir un thread) De esta forma, en el servidor UNIX podemos
 *  tener multiples consumidores de datos raster, y en el servidor Windows
 *  2000 tendremos un thread por cada uno de ellos.
 *
 *@author     alvaro zabala
 *@created    25 de Septiembre
 */
class ProcesoServidorImagenes implements Runnable {


	/**
	 *  clase que contiene todas las imagenes ofrecidas por el servidor, y que
	 *  contiene los metodos necesarios para dibujarlas.
	 */
	// private ServidorImagenes servidor;
	/**
	 *  puesto que cada proceso es dedicado, y existe mientras exista su
	 *  consumidor de datos (y este mantenga abierto el socket) este contador nos
	 *  permite crear estadisticas del numero de peticiones atendidas
	 */
	int contadorHints = 0;

	/**
	 *  Socket que conecta el proceso servidor con el consumidor remoto de datos
	 *  raster
	 */
	private Socket socket;
	/**
	 *  canal de salida del socket
	 */
	private DataOutputStream out;
	/**
	 *  canal de entrada del socket
	 */
	private DataInputStream input;


	/**
	 *  Constructor. Recibe un socket con el consumidor de datos raster
	 *
	 *@param  socket           socket con el cliente remoto
	 *@exception  IOException  excepcion causada por problemas con la conexion
	 *      socket
	 */
	ProcesoServidorImagenes(Socket socket) throws IOException {
		this.socket = socket;
		this.input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}


	/**
	 */
	//public void setImageServer(ServidorImagenes imageServer) {
	//this.servidor = imageServer;
	//}


	/**
	 *  Proceso principal. Trabaja mientras exista una conexion con el cliente
	 *  remoto (es un servidor dedicado)
	 */

	public void run() {
		/*
		 *  System.out.println("Creando servidor dedicado...");
		 *  boolean seguir = true;
		 *  while (seguir) {
		 *  // *  nos aprovechamos del caracter bloqueante de los socket para que el proceso
		 *  // *  viva mientras exista la conexion de la clase que hace uso de el.
		 *  // *  De esta forma NOS AHORRAMOS EL GASTO DE ANDAR CREANDO Y DESTRUYENDO THREADS
		 *  try {
		 *  String peticion = input.readUTF();//aqui podiamos hacer una arquitectura de comandos
		 *  long idImagen = input.readLong();
		 *  System.out.println("Peticion:"+peticion);
		 *  if(peticion.equalsIgnoreCase("getCoverage")){
		 *  double xMin = input.readDouble();
		 *  double yMin = input.readDouble();
		 *  double xMax = input.readDouble();
		 *  double yMax = input.readDouble();
		 *  Envelope rect = new Envelope(xMin, xMax, yMin, yMax);
		 *  int width = input.readInt();
		 *  int height = input.readInt();
		 *  //y seguidamente la atendemos
		 *  System.out.println("Procesando Peticion...!!!!");
		 *  //BufferedImage solucion = servidor.procesa(rect, width, height, idImagen);
		 *  System.out.println("Peticion procesada");
		 *  if(solucion==null){//no hay datos para la zona solicitada
		 *  out.writeBoolean(false);
		 *  System.out.println("Sin datos para la zona solicitada");
		 *  }else{
		 *  out.writeBoolean(true);
		 *  enviarImagenAsJPG(solucion);
		 *  }
		 *  out.flush();
		 *  }else if(peticion.equalsIgnoreCase("getEnvelope")){
		 *  //Envelope envelope = servidor.getEnvelope(idImagen);
		 *  System.out.println("Sirviendo envelope...");
		 *  if(envelope != null){
		 *  out.writeDouble(envelope.getMinX());
		 *  out.writeDouble(envelope.getMinY());
		 *  out.writeDouble(envelope.getMaxX());
		 *  out.writeDouble(envelope.getMaxY());
		 *  System.out.println("x1="+envelope.getMinX()+" x2="+envelope.getMaxX()+" y1="+envelope.getMinY()+" y2="+envelope.getMaxY());
		 *  }else{//hasta que no se definan codigos de error, se mandan 4 zeros
		 *  out.writeDouble(0d);
		 *  out.writeDouble(0d);
		 *  out.writeDouble(0d);
		 *  out.writeDouble(0d);
		 *  System.out.println("envelope 0 , 0, 0, 0");
		 *  }
		 *  out.flush();
		 *  }
		 *  } catch (java.io.IOException io) {
		 *  System.out.println("Error en el socket!!!");
		 *  io.printStackTrace();
		 *  try {
		 *  socket.close();
		 *  input.close();
		 *  out.close();
		 *  } catch (java.io.IOException io2) {
		 *  io2.printStackTrace();
		 *  }//try
		 *  seguir = false;
		 *  }//catch
		 *  }//while
		 */
	}


	/**
	 *  Description of the Method
	 *
	 *@param  buff             Description of the Parameter
	 *@exception  IOException  Description of the Exception
	 */
	void enviarImagenAsJPG(BufferedImage buff) throws IOException {
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(buff);
		out.flush();
	}


	/**
	 *@param  buff             BufferedImage que se va a enviar a un objeto
	 *      remoto.
	 *@exception  IOException  Description of the Exception
	 */
	void enviarImagen(BufferedImage buff) throws IOException {
		java.awt.image.Raster raster = buff.getData();

		DataBuffer dataBuffer = raster.getDataBuffer();
		int dataBufferType = dataBuffer.getDataType();

		//1º TIPO DE DATO DEL DATABUFFER
		out.writeInt(dataBufferType);

		//2º A CONTINUACION EL ARRAY DE DATOS
		switch (dataBufferType) {
			case DataBuffer.TYPE_BYTE:
			{
				byte[] pixels = ((DataBufferByte) dataBuffer).getData();
				out.writeInt(pixels.length);
				//primero la longitud para que aloque
				out.write(pixels);
			}
				break;
//            case DataBuffer.TYPE_DOUBLE://NO CONTEMPLADO EN JDK1.3
//            {
//                double[] pixels = ((DataBufferDouble) dataBuffer).getData();
//                out.writeInt(pixels.length);
//                for (int k = 0; k < pixels.length; k++) {
//                    out.writeDouble(pixels[k]);
//                }
//                //for
//            }
//            break;

//            case DataBuffer.TYPE_FLOAT:
//            {
//                float[] pixels = ((DataBufferFloat) dataBuffer).getData();
//                out.writeInt(pixels.length);
//                for (int k = 0; k < pixels.length; k++) {
//                    out.writeFloat(pixels[k]);
//                }
//                //for
//            }
//            break;
			case DataBuffer.TYPE_INT:
			case DataBuffer.TYPE_SHORT:
			{
				int[] pixels = ((DataBufferInt) dataBuffer).getData();
				out.writeInt(pixels.length);
				for (int k = 0; k < pixels.length; k++) {
					out.writeInt(pixels[k]);
				}
				//for
			}
				break;
//			case DataBuffer.TYPE_UNDEFINED:{
//			}
//			break;
//
//			case DataBuffer.TYPE_USHORT:{
//			}
//			break;

		}
		//switch

		//3º Ahora DIMENSIONES DE LA IMAGEN Y  NUMERO DE BANDAS
		int width = raster.getWidth();
		System.out.println("w=" + width);
		out.writeInt(width);

		int height = raster.getHeight();
		System.out.println("h=" + height);
		out.writeInt(height);

		int numBands = raster.getNumBands();
		System.out.println("nBandas=" + numBands);
		out.writeInt(numBands);

		//4º Ahora enviamos datos para obtener el SampleModel de la imagen
		//(Revisar si es del tipo Component o es del tipo MultiPixelPacked o
		//SinglePixelPacked)
		SampleModel sm = raster.getSampleModel();
		if (sm instanceof ComponentSampleModel) {
			//esto no me gusta,(instanceof es costoso) pero es la unica forma
			//ver si solo se verifica al principio del servicio -pues luego no cambia-
			System.out.println("ComponentSampleModel!");
			sendComponentSampleModel((ComponentSampleModel) sm);
		}
		else if (sm instanceof SinglePixelPackedSampleModel) {
			System.out.println("SinglePixelPacked!");
			sendSinglePixelPackedModel((SinglePixelPackedSampleModel) sm);
		}
		else {
			System.out.println("SampleModel no contemplado, del tipo");
			System.out.println(sm.getClass().toString());
		}
	}


	//enviarImagen

	/**
	 *  Description of the Method
	 *
	 *@param  sm               Description of the Parameter
	 *@exception  IOException  Description of the Exception
	 */
	private void sendSinglePixelPackedModel(SinglePixelPackedSampleModel sm) throws IOException {

		out.writeInt(TCPImageServer.SINGLE_PIXEL_SAMPLE_MODEL);

		int dataType = sm.getDataType();
		out.writeInt(dataType);

		int scanLineStride = sm.getScanlineStride();
		out.writeInt(scanLineStride);

		int[] bitMasks = sm.getBitMasks();
		System.out.println("longitud de la mascara:" + bitMasks.length);

		out.writeInt(bitMasks.length);
		for (int i = 0; i < bitMasks.length; i++) {
			out.writeInt(bitMasks[i]);
		}
		out.flush();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  sm               Description of the Parameter
	 *@exception  IOException  Description of the Exception
	 */
	private void sendMultiPixelPackedModel(MultiPixelPackedSampleModel sm) throws IOException {
	}



	/**
	 *  Description of the Method
	 *
	 *@param  sm               Description of the Parameter
	 *@exception  IOException  Description of the Exception
	 */
	private void sendComponentSampleModel(ComponentSampleModel sm) throws IOException {

		out.writeInt(TCPImageServer.COMPONENT_SAMPLE_MODEL);
		int pixelStride = sm.getPixelStride();
		out.writeInt(pixelStride);

		int scanlineStride = sm.getScanlineStride();
		out.writeInt(scanlineStride);

		int[] bandsOffset = sm.getBandOffsets();
		out.writeInt(bandsOffset.length);
		//1º el tamaño
		for (int z = 0; z < bandsOffset.length; z++) {
			out.writeInt(bandsOffset[z]);
			//seguidamente los elementos del array
		}
		//Comprobamos si el SampleModel tiene bankIndices. Si sí se
		//envia un 1 y el array. Si no, se envia un 0
		int[] bankIndices = sm.getBankIndices();
		if (bankIndices == null) {
			out.writeInt(0);
			//La imagen carece de banco de indices. Así se indica al otro extremo
			//de la comunicacion
		}
		else {
			out.writeInt(1);
			//La imagen tiene el campo bankindices. Se comunica al otro extremo

			out.writeInt(bankIndices.length);
			//longitud del banco de indices

			for (int z = 0; z < bankIndices.length; z++) {
				out.writeInt(bankIndices[z]);
			}
			//for

		}
		//else

	}
	//sendComponentSampleModel

}
