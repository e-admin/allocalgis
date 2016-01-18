/**
 * ListaApp.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

import java.util.Hashtable;


/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 16-mar-2012
 * Time: 10:13:59
 */
public class ListaApp {
    private Hashtable hApps;
    public  ListaApp()
    {
          this.hApps = new Hashtable();
    }
    public void add(App app) {
        this.hApps.put(app.getId(),app);
    }

    public App get(String sIdApp)
    {
        return (App)this.hApps.get(sIdApp);
    }

    public Hashtable gethApps() {
        return hApps;
    }
}

