/**
 * TileCache.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.agil.core.util.image;

import java.awt.image.DataBuffer;
import java.util.HashMap;
import java.util.WeakHashMap;



/**
 * Clase que representa una cache de Tiles de imagenes.
 * Cada tile sera representada como un objeto de la clase java.awt.image.Raster
 *
 * La cache garantiza que siempre se guardara una cantidad minima de tiles (expresada
 * en bytes), tiles que seran apuntadas con referencias normales.
 *
 * El resto de tiles que excedan de esta cantidad minima dependera de los requerimientos
 * del Garbage Collector, pues seran referenciadas con SoftReferences.
 *
 * las
   limitaciones de los objetos apuntados con referencias normales vienen expresadas
   en bytes, y no en numero de objetos, y solo puede almacenar objetos de la clase
   java.awt.image.Raster.
 *
 *
 */
public class TileCache extends WeakHashMap{

    /**Cache intermedia no volatil, de tamaño en bytes limitado*/
    private MemoryLimitedMap cache;

  /**
   * Constructor
   */
    public TileCache(long capacity) {
        super();
        cache = new MemoryLimitedMap(capacity);
    }

    public Object put(Object key,Object tile){

        //en primer lugar añade el objeto a la lista MRU
        //(donde se guardan las ultimas entradas)
        cache.put(key,(java.awt.image.Raster)tile);
        //por ultimo se añade a la cache de SoftReferences
        //(de forma que cuande se saque de la cache no volatil, todavia
        //quede una referencia Soft apuntando al objeto.
        return (java.awt.image.Raster)super.put(key, tile);
    }

    public Object get(Object key){

          //recurrimos a la cache volatil (con weakreferences)
          java.awt.image.Raster tile = (java.awt.image.Raster)super.get(key);
            if (tile != null || super.containsKey(key)) {
                //si el objeto ha sido encontrado, se reajusta la
                //lista MRU (sacando al ultimo, y añadiendo a este)
                //haciendo asi que no lo recolecte el Garbage Collector
                  cache.put(key,tile);
            }
            return tile;
    }

    class MemoryLimitedMap {
        /**memoria consumida en bytes*/
        private long memoryUsage;

        /**memoria maxima en bytes que puede almacenar*/
        private long memoryCapacity;

        /**umbral de memoria a partir del que se reestructura el hash*/
        private float memoryThreshold;

        /**numero de tiles almacenado en el hash no volatil*/
        private int numTiles;

        /**Tile mas reciente*/
        private TileCacheEntry first;

        /**Tile mas antiguo -el siguiente en ser desechado*/
        private TileCacheEntry last;

        /**
         * hashmap que garantiza que en la cache siempre hay un numero minimo de
         * objetos (apuntados con referencias normales)
         */
        private HashMap hash;

       /**
        * Constructor que recibe la capacidad maxima de la memoria no
        * volatil (en bytes). Utiliza un umbral de carga por defecto.
        * @param memoryCapacity capacidad maxima
        */
       public MemoryLimitedMap(long memoryCapacity){
            this(memoryCapacity,0.75f);
        }

        /**
         * Construcor. Recibe el tamaño maximo de la memoria garantizada y el umbral
         * maximo a partir del cual se modifica la estructura de la tabla hash
         * @param memoryCapacity
         * @param memoryThreshold
         */
        public MemoryLimitedMap(long memoryCapacity,float memoryThreshold){
            this.memoryCapacity=memoryCapacity;
            this.memoryThreshold=memoryThreshold;
            hash = new HashMap();
        }

        /**
         * Añadimos una tile a la cache de tiles
         * @param key
         * @param data
         */
        public void put(Object key,java.awt.image.Raster data){

           //primero comprobamos si ya estaba almacenado
           TileCacheEntry entry = (TileCacheEntry)hash.get(key);
           if(entry!=null){
                //ya estaba almacenado en la cache
                //luego solo hay que moverlo para que sea el ultimo en ser desechado
                moveLast(entry);
                return;
            }//if

           //no estaba en la cache, luego se ha de crear una nueva entrada para él
           //que pasa a ocupar el primer lugar en la lista MRU
           entry = new TileCacheEntry(key,data);
           entry.prev = null;
           entry.next = first;

            if(first == null && last == null){
                first = entry;
                last = entry;
            }else{
                first.prev = entry;
                first = entry;
            }
            if(!hash.containsKey(key)){
                hash.put(key,entry);
                memoryUsage += entry.getTileSize();
                numTiles++;
                 if(memoryUsage > memoryCapacity){
//                    System.out.println("liberando memoria");
                    memoryControl();
                 }

            }
        }//put

        public java.awt.image.Raster getTile(Object key){
            java.awt.image.Raster raster = null;
            if(memoryCapacity == 0L)
                return null;

            TileCacheEntry entry = (TileCacheEntry)hash.get(key);
            if(entry == null){
                return null;
            }else{
                 raster = entry.datos;
                 moveLast(entry);
            }
//            if(raster!=null){
//                System.out.println("Encontrado el tile "+key+" en la cache");
//                System.out.println("Tiles almacenados:"+numTiles);
//                System.out.println("En bytes:"+memoryCapacity);
//            }
            return raster;
        }
        /**Este metodo se encarga de vaciar la memoria de tiles para que no exceda de
         * su tamaño maximo.
         */
        public void memoryControl(){
            long topLimit = (long)((float)memoryCapacity*memoryThreshold);
            for (long l=topLimit;(memoryUsage>l) && (last != null); ) {
                TileCacheEntry entry = (TileCacheEntry)hash.get(last.key);
                if(entry != null){
                    hash.remove(last.key);
                    memoryUsage -= last.getTileSize();
                    numTiles--;
                    last = last.prev;

                    if(last != null){
                        last.next.prev = null;
                        last.next = null;
                    } else{
                        first = null;
                    }
                 }//if
                 else{
                    break;
                 }
              }//for
        }//memoryControl


        /**Mueve una entrada de la cache al final de la lista MRU
         * @param entry entrada que se quiere añadir a la lista
         */
        private void moveLast(TileCacheEntry entry){
            if(entry != first){//si no es el primero
                if(entry != last){//ni tampoco el ultimo
                    entry.prev.next = entry.next;
                    entry.next.prev = entry.prev;
                } else{
                    //es el ultimo, pero deja de serlo
                    last = entry.prev;//para serlo su anterior
                    last.next = null;
                }
                entry.prev = null;
                entry.next = first;
                first.prev = entry;
                first = entry;
            }
        }


    }//MemoryLimitedMap

    class TileCacheEntry {
        /**
         * Tile que se añadio a la cache tras esta
         * */
        TileCacheEntry next = null;
        /**
         * Tile que se añadio a la cache anteriormente a esta
         */
		TileCacheEntry prev = null;

        /*
        * Clave del objeto
        */
        Object key;
        /**
         *Tile guardada
         */
		java.awt.image.Raster datos;

		/**
         * Constructor
         * @param key
         * @param obj
         */
		public TileCacheEntry (Object key,java.awt.image.Raster obj) {
			this.datos = obj;
			this.key=key;
		}
        /**
         * Obtiene el tamaño del tile en bytes
         *
         * @return numero de bytes que ocupa la imagen
         */
         public long getTileSize(){
            DataBuffer dataBuffer = datos.getDataBuffer();
            long bytesData = (long)DataBuffer.getDataTypeSize(dataBuffer.getDataType())/8L;
            long numSamples = (long)dataBuffer.getSize();
            return bytesData*numSamples;
        }
	}

}