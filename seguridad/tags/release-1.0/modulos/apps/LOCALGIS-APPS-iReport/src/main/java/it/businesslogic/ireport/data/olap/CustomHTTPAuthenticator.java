/*
 * Copyright (C) 2005-2007 JasperSoft http://www.jaspersoft.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 *
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  USA  02111-1307
 *
 *
 * CustomHTTPAuthenticator.java
 *
 * Created on February 14, 2007, 4:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.businesslogic.ireport.data.olap;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;

/**
 *
 * @author gtoffoli
 */
public class CustomHTTPAuthenticator extends Authenticator {
    
    private String username = "";
    private String password = "";
    
    public CustomHTTPAuthenticator(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    
    
    protected PasswordAuthentication getPasswordAuthentication() {
            // Get information about the request
            String promptString = getRequestingPrompt();
            String hostname = getRequestingHost();
            InetAddress ipaddr = getRequestingSite();
            int port = getRequestingPort();
    // Return the information
            return new PasswordAuthentication(username, password.toCharArray());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
