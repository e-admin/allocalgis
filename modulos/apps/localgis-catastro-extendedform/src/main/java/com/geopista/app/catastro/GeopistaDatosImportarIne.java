/**
 * GeopistaDatosImportarIne.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

public class GeopistaDatosImportarIne 
{
  private String campo1;
  private String campo2;
  private String campo3;
  private String campo4;
  private String campo5;

/**
 * @return Returns the campo5.
 */
public String getCampo5()
{
    return campo5;
}
/**
 * @param campo5 The campo5 to set.
 */
public void setCampo5(String campo5)
{
    this.campo5 = campo5;
}
  public GeopistaDatosImportarIne()
  {
  }

  public String getCampo1()
  {
    return campo1;
  }

  public void setCampo1(String newCampo1)
  {
    campo1 = newCampo1;
  }

  public String getCampo2()
  {
    return campo2;
  }

  public void setCampo2(String newCampo2)
  {
    campo2 = newCampo2;
  }

  public String getCampo3()
  {
    return campo3;
  }

  public void setCampo3(String newCampo3)
  {
    campo3 = newCampo3;
  }

  public String getCampo4()
  {
    return campo4;
  }

  public void setCampo4(String newCampo4)
  {
    campo4 = newCampo4;
  }
}