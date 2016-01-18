/**
 * TimeOutException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw.net.communications.exceptions;

/**
 *
 * User: dbenito; XSDDate: 18-mar-2004 Time: 16:52:00
 * To change this template use Options | File Templates.
 *
 * TAGS CVS
 * @author  SATEC
 * @version $Revision: 1.1 $
 *
 * Autor:$Author: satec $
 * Fecha Ultima Modificacion:$XSDDate: 2005/09/12 16:21:15 $
 * $Name:  $ ; $RCSfile: TimeOutException.java,v $ ; $Revision: 1.1 $ ; $Locker:  $
 * $State: Exp $
 */
public class TimeOutException extends Exception
{
	//Para que el CVS guarde la revisión y así decompilar la clase en producción.
	private String Version = "$Revision: 1.1 $";

	public TimeOutException(int nSeg){super("Ha saltado el TimeOut. Segundos: "+nSeg);}
}