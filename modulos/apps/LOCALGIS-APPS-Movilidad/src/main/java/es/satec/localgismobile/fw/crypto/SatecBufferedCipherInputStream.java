/**
 * SatecBufferedCipherInputStream.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CryptoException;

/**
 * Stream de entrada con capacidad de cifrado
 * @author jpolo
 *
 */
public class SatecBufferedCipherInputStream extends InputStream {
	
	/**
	 * ByteArrayOutputStream utilizado para volcar el contenido del stream de entrada
	 * en memoria
	 */ 
	private ByteArrayOutputStream byteArrayOut;
	/**
	 * ByteArrayInputStream utilizado para almacenar el contenido del stream de entrada
	 * cifrado
	 */
	private ByteArrayInputStream cipheredByteArrayInput;
	/**
	 * Array de bytes que contiene el stream de entrada cifrado
	 */
	private byte cipheredBytes[];
	int offset;
	int markPosition;
	
	/**
	 * Construye un nuevo stream de entrada cifrado
	 * @param in Stream de entrada que se desea cifrar
	 * @param cipher Objeto encargado de realizar el cifrado. Debe estar correctamente inicializado
	 * @throws IOException Si se produce algun error al acceder al stream de entrada
	 */
	SatecBufferedCipherInputStream(InputStream in, BufferedBlockCipher cipher) throws IOException{
		super();
		ByteArrayOutputStream decodedBytes = new ByteArrayOutputStream();
		//this.in = in;
		this.byteArrayOut = new ByteArrayOutputStream();
		
		//this.cipher = cipher;
		this.offset = 0;
		this.markPosition = 0;
		int count;
		
		while ((count = in.available()) != 0){
			byte availableBytes[] = new byte[count];
			in.read(availableBytes);
			byteArrayOut.write(availableBytes, 0 , count);
		}
		
		byte originalBytes[] = byteArrayOut.toByteArray();
		
		int predictedOutputSize = cipher.getOutputSize(originalBytes.length);
		cipheredBytes = new byte[predictedOutputSize];		
		int outputLen = cipher.processBytes(originalBytes, 0, originalBytes.length, cipheredBytes, 0);
		if (outputLen > 0)
			decodedBytes.write(cipheredBytes, 0, outputLen);
		
		try {
			outputLen = cipher.doFinal(cipheredBytes, 0);
			if (outputLen > 0)
				decodedBytes.write(cipheredBytes, 0, outputLen);
		} catch (CryptoException e) {
			throw new IOException("Error al cifrar el contenido del flujo de entrada");
		}
		
		cipheredBytes = decodedBytes.toByteArray();
		
		this.cipheredByteArrayInput = new ByteArrayInputStream(cipheredBytes);
		in.close();
	}
	
	/**
	 * Indica el número de bytes disponibles en el stream
	 */
	public int available() throws IOException {
		//return cipheredBytes.length - offset;
		return this.cipheredByteArrayInput.available();
	}

	/**
	 * Cierra el stream
	 */
	public void close() throws IOException {
		super.close();
		this.cipheredByteArrayInput.close();
	}

	/**
	 * Indica si el stream soporta posicionamiento mediante marcas
	 */
	public boolean markSupported() {
		//return true;
		return this.cipheredByteArrayInput.markSupported();
	}

	/**
	 * Lee un conjunto de bytes del stream de entrada
	 * @param b Array de bytes donde almacenar los datos leidos
	 * @param off Offset desde el que empezar a leer
	 * @param len Número de bytes que se desean leer
	 * @return Número de bytes leídos
	 */
	public int read(byte[] b, int off, int len) throws IOException {
//		int numBytes = len;
//		int available = this.available() - off;
//		if (numBytes > available)
//			numBytes = available;
//		
//		int startOffset = this.offset + off;
//		
//		for (int i = 0; i < numBytes; i++)
//			b[i] = cipheredBytes[startOffset + i];
//		
//		this.offset += numBytes + off;
//		
//		return numBytes;
		return this.cipheredByteArrayInput.read(b, off, len);
	}

	/**
	 * Lee un byte del stream de entrada
	 * @param b Array de bytes donde almacenar el dato leído
	 * @return Número de bytes leidos
	 * @throws IOException Si se produce un error al realizar la lectura
	 */
	public int read(byte[] b) throws IOException {
//		int numBytes = b.length;
//		int available = this.available();
//		if (numBytes > available)
//			numBytes = available;
//		
//		int startOffset = this.offset;
//		
//		for (int i = 0; i < numBytes; i++)
//			b[i] = cipheredBytes[startOffset + i];
//		
//		this.offset += numBytes;
//		
//		return numBytes;
		return this.cipheredByteArrayInput.read(b);
	}

	/**
	 * Posiciona el puntero de lectura al comienzo
	 */
	public synchronized void reset() throws IOException {
		//this.offset = markPosition;
		this.cipheredByteArrayInput.reset();
	}
	
	/**
	 * Marca el punto a partir del cual se comienza a leer el stream
	 * @param markPosition Posición de la marca
	 */
	public synchronized void mark(int markPosition) {
		//this.markPosition = markPosition;
		this.cipheredByteArrayInput.mark(markPosition);
	}

	/**
	 * Salta n bytes del stream de entrada
	 * @param n Número de bytes a saltar
	 * @return Número de bytes saltados
	 * @throws IOException Si se produce algún error al acceder al 
	 * stream de entrada
	 */
	public long skip(long n) throws IOException {
//		long available = this.available();
//		long skipped = n;
//		if (n > available)
//			skipped = available;
//		offset += skipped;
//		
//		return skipped;
		
		return this.cipheredByteArrayInput.skip(n);
	}

	/**
	 * Lee un byte del stream de entrada
	 * @throws IOException Si se produce algún error al acceder al 
	 * stream de entrada
	 * @return Número de bytes leido
	 */
	public int read() throws IOException {
//		int startOffset = this.offset;
//		int readByte = cipheredBytes[startOffset] & 0xFF;
//		this.offset += 1;
//		return readByte;
		
		return this.cipheredByteArrayInput.read();
	}
}
