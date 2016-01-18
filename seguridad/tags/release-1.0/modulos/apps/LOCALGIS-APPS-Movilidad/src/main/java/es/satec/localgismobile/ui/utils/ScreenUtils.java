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
