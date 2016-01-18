/**
 * SearchDataBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.utils;

import org.eclipse.swt.graphics.Color;

/**Clase que va a almacenar los datos que se van a seleccionar a la hora
 * de realizar la busqueda**/
public class SearchDataBean {
	
	private String capa=null;
	private String atributo=null;
	private String operador=null;
	private String valor=null;
	private String strokeWidth=null;
	private Color fill=null;
	private Color stroke=null;
	private boolean ejecutando= false;
	
	public SearchDataBean(String Capa, String Atributo, String Operador, String Valor, String strokeWidth, Color fill, Color stroke, boolean eje){
		this.capa=Capa;
		this.atributo=Atributo;
		this.operador=Operador;
		this.valor=Valor;
		this.strokeWidth=strokeWidth;
		this.fill=fill;
		this.stroke=stroke;
		this.ejecutando=eje;
	}
	public void setCapa(String cap){
		this.capa=cap;
	}
	public void setstrokeWidth(String str){
		this.strokeWidth=str;
	}
	public void setAtributo(String atri){
		this.atributo=atri;
	}
	public void setOperador(String ope){
		this.operador=ope;
	}
	public void setValor(String val){
		this.valor=val;
	}
	public void setFill(Color fil){
		this.fill=fil;
	}
	public void setStroke(Color strok){
		this.stroke=strok;
	}
	public void setEjecutado(boolean eje){
		this.ejecutando=eje;
	}
	
	public boolean getEjecutado(){
		return this.ejecutando;
	}
	public String getstrokeWidth(){
		return this.strokeWidth;
	}
	public String getCapa(){
		return this.capa;
	}
	public String getAtributo(){
		return this.atributo;
	}
	public String getOperador(){
		return this.operador;
	}
	public String  getValor(){
		return this.valor;
	}
	public Color getStroke(){
		return this.stroke;
	}
	public Color getFill(){
		return this.fill;
	}
}
