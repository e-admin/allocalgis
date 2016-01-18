package es.satec.localgismobile.fw.image.ico;

import java.util.Vector;

/**
 * Clase que almacena la lista de entradas de directorio presente
 * en el icono. Cada una de las entradas de directorio contiene
 * información sobre cada una de las imágenes presentes en el icono
 * @author jpolo
 *
 */
public class IcoDirectoryList {
	/**
	 * Tipo de icono
	 */
	protected int type;
	/**
	 * Número de entradas de directorio
	 */
	protected int numberOfEntries; // idCount en MSDN
	/**
	 * Vector con cada una de las entradas de directorio
	 */
	protected Vector directoryEntries;
	
	public IcoDirectoryList() {
		directoryEntries = new Vector();
	}
	public int getNumberOfEntries() {
		return numberOfEntries;
	}
	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Vector getDirectoryEntries() {
		return directoryEntries;
	}
	public void setDirectoryEntries(Vector directoryEntries) {
		this.directoryEntries = directoryEntries;
	}	
	
	public IcoDirectoryEntry getDirectoryEntry(int index){
		IcoDirectoryEntry directoryEntry = (IcoDirectoryEntry) directoryEntries.elementAt(index);
		return directoryEntry;
	}
	
}
