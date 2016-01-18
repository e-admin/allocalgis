/**
 * JNCSProgressiveUpdate.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.ermapper.ecw;

/**
 * Listener para la carga progresiva de imágenes
 */
public interface JNCSProgressiveUpdate
{

    public abstract void refreshUpdate(int i, int j, double d, double d1, double d2, double d3);

    public abstract void refreshUpdate(int i, int j, int k, int l, int i1, int j1);
}
