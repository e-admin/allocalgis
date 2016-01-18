/**
 * ScreenUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ScreenUtils {

	private static Cursor prevCursor = null;
	private static boolean started = false;

	/**
	 * Muestra un cursor de espera y deshabilita los controles de la pantalla
	 */
	public static void startHourGlass(Shell shell)
	{
		if (!started) {
			prevCursor = shell.getCursor();
			Cursor c = Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT);

			shell.setCursor(c);

			shell.setEnabled(false);
			started = true;
		}
	}
	
	/**
	 * Devuelve el cursor a su estado por defecto y habilita los controles de la pantalla
	 */
	public static void stopHourGlass(Shell shell)
	{
		if (started) {
			//control.setCursor(null);
			shell.setCursor(prevCursor);

			shell.setEnabled(true);
			started = false;
		}
	}

}
