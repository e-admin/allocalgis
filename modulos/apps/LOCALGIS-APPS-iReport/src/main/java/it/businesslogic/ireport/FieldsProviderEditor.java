/**
 * FieldsProviderEditor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * FieldsProviderEditor.java
 *
 * Created on December 7, 2006, 8:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport;


/**
 *
 * @author gtoffoli
 */
public interface FieldsProviderEditor {
    
       /**
        * the method can be not executed in the AWT Event Dispatcher Thread.
        * Subsequent calls of this method can be placed. I suggest to catch a Throwable to
        * clean up if the thread is interrupted.
        */
        public void queryChanged(String newQuery);
}
