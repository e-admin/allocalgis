/*
 * TemplateStyle.java
 * 
 * Created on Aug 21, 2007, 4:05:48 PM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/*
 * Copyright (C) 2006 JasperSoft http://www.jaspersoft.com
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
 */

package it.businesslogic.ireport;

/**
 *
 * @author gtoffoli
 */
public class TemplateStyle extends Style {

    private JasperTemplate jasperTemplate = null;

    public JasperTemplate getJasperTemplate() {
        return jasperTemplate;
    }

    public void setJasperTemplate(JasperTemplate jasperTemplate) {
        this.jasperTemplate = jasperTemplate;
    }
    
    public TemplateStyle() {
        super();
    }
    
    public TemplateStyle(Style s1) {
        super(s1);
    }

}
