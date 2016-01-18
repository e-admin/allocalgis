/**
 * ForcedAcceptPrintService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions;

import java.lang.reflect.Field;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.ServiceUIFactory;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.event.PrintServiceAttributeListener;

/**
 * ForcedAcceptPrintService is a PrintService wrapper class that allows printing to be
 * attempted even if Java thinks the 'Printer is not accepting job'.  It is recommended
 * to use this after prompting the user, so they will see that the printer is
 * reporting as offline and can choose to "force it" to try printing anyway.
 * <p/>
 * This hack gets around annoying 'Printer is not accepting job' errors in Java 5/6 that
 * don't occur in Java 1.4.  This was enough of a problem for our 1000+ users that it
 * was the sole reason we could not move the product up from Java 1.4.  Hence, the hack
 * was invented as we had the problem with users whose printers were clearly online and
 * they could print from any non-Java application.
 * <p/>
 * Turns out this hack is also useful for printing to the latest inkjet printers that
 * have chips in the cartridges and stay in a 'No ink' status  once empty.  This is
 * presumably to cause an inconvenience for cartridge refillers, but we're the ones who
 * get the support calls from users that printing is not working, so it's an inconvenience
 * to everyone except the printer manufacturer.
 * <p/>
 * Usage:
 * <code>
 *		 // Get a printer, create a print job, and try printing however you do it today
 *		 PrintService printer = getPrinter(myFormat, aset);
 *		 DocPrintJob job = printer.createPrintJob();
 *		 try {
 *			 job.print(myDoc, aset);
 *		 } catch (PrintException pe) {         // catch the PrintException
 *           // check for the annoying 'Printer is not accepting job' error.
 *			 if (pe.getMessage().indexOf("accepting job") != -1) {
 *               // recommend prompting the user at this point if they want to force it
 *               // so they'll know there may be a problem.
 *				 try {
 *				     // try printing again but ignore the not-accepting-jobs attribute
 *                   job = printer.createPrintJob();
 *					 ForcedAcceptPrintService.setupPrintJob(job); // add secret ingredient
 *					 job.print(myDoc, aset);
 *				 } catch (PrintException e) {
 *					 // ok, you really can't print
 *					 e.printStackTrace();
 *				 }
 *			 } else {
 *				 // some other printing error
 *				 pe.printStackTrace();
 *			 }
 *		 }
 * </code>
 */
public class ForcedAcceptPrintService implements PrintService {
	private final DocPrintJob thePrintJob;
	//private final PrinterJob thePrinterJob;
	private final PrintService delegate;
	private Field printServiceFieldReference = null;

	/**
	 * Tweak the DocPrintJob to think this class is it's PrintService, long enough to
	 * override the PrinterIsAcceptingJobs attribute.  If it doesn't work out or
	 * the printer really is offline then it's no worse than if this hack was not
	 * used.
	 *
	 * @param printJob the print job to affect
	 */
	public static void setupPrintJob(DocPrintJob printJob) {
		new ForcedAcceptPrintService(printJob);
	}
	
	/*public static void setupPrinterJob(PrinterJob printerJob) {
		new ForcedAcceptPrintService(printerJob);
	}*/

	/**
	 * Private constructor as this only works as a one-shot per print attempt.
	 * Use the static method above to hack a DocPrintJob, then tell it to print.
	 * The hack is gone by the time printing occurs and this instance will be
	 * garbage collected due to having no other references once the DocPrintJob
	 * is back to its original state.
	 *
	 * @param printJob the print job to affect
	 */
	private ForcedAcceptPrintService(DocPrintJob printJob) {
		this.thePrintJob = printJob;
		this.delegate = printJob.getPrintService();

		try {
			// replace the private PrintService field on the DocPrintJob instance with a reference
			// to our replacement PrintService so that we can intercept calls to getAttributes().
			// it is expected that the first thing the DocPrintJob will do is check it's PrintService's
			// PrinterIsAcceptingJobs attribute, at which point we'll force it to think it is accepting
			// jobs and restore the PrintService to the original instance to get out of the way.
			// The only real requirement is that the DocPrintJob does not cast the PrintService
			// to it's expected type until after it has checked the PrinterIsAcceptingJobs
			// attribute.
			Class pjClass = thePrintJob.getClass();
			Field[] fields = pjClass.getDeclaredFields();
			for (int i=0; i<fields.length; i++) {
				if (fields[i].getType().equals(PrintService.class)) {
					printServiceFieldReference = fields[i];
					printServiceFieldReference.setAccessible(true);
					// if the next line errors then the hack just isn't in place and printing
					// proceeds as it normally would (e.g. no side effects).
					printServiceFieldReference.set(thePrintJob, this);
					// there should be only one, so break once found
					break;
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/** Restore the DocPrintJob's PrintService to what it was originally. */
	private void restoreServiceReference() {
		try {
			// replace the private PrintService field on the DocPrintJob instance with a
			// reference to the original PrintService so that we stop intercepting calls
			// and won't cause ClassCastExceptions as printing proceeds.
			if (printServiceFieldReference != null)
				printServiceFieldReference.set(thePrintJob, delegate);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getAttribute is the one PrintService method we want to intercept to override the
	 * PrinterIsAcceptingJobs attribute.
	 */
	public PrintServiceAttribute getAttribute(Class category) {
		if (category.equals(PrinterIsAcceptingJobs.class)) {
			// once we've overridden the return value for the PrinterIsAcceptingJobs attribute we're done.
			// put the DocPrintJob's PrintService back to what it was.
			restoreServiceReference();
			return PrinterIsAcceptingJobs.ACCEPTING_JOBS;
		}
		return delegate.getAttribute(category);
	}

	// obligatory PrintService implementation

	public DocPrintJob createPrintJob() {
		return delegate.createPrintJob();
	}

	public void addPrintServiceAttributeListener(PrintServiceAttributeListener listener) {
		delegate.addPrintServiceAttributeListener(listener);
	}

	public PrintServiceAttributeSet getAttributes() {
		return delegate.getAttributes();
	}

	public Object getDefaultAttributeValue(Class category) {
		return delegate.getDefaultAttributeValue(category);
	}

	public String getName() {
		return delegate.getName();
	}

	public ServiceUIFactory getServiceUIFactory() {
		return delegate.getServiceUIFactory();
	}

	public Class[] getSupportedAttributeCategories() {
		return delegate.getSupportedAttributeCategories();
	}

	public Object getSupportedAttributeValues(Class category, DocFlavor flavor, AttributeSet attributes) {
		return delegate.getSupportedAttributeValues(category, flavor, attributes);
	}

	public DocFlavor[] getSupportedDocFlavors() {
		return delegate.getSupportedDocFlavors();
	}

	public AttributeSet getUnsupportedAttributes(DocFlavor flavor, AttributeSet attributes) {
		return delegate.getUnsupportedAttributes(flavor, attributes);
	}

	public boolean isAttributeCategorySupported(Class category) {
		return delegate.isAttributeCategorySupported(category);
	}

	public boolean isAttributeValueSupported(Attribute attrval, DocFlavor flavor, AttributeSet attributes) {
		return delegate.isAttributeValueSupported(attrval, flavor, attributes);
	}

	public boolean isDocFlavorSupported(DocFlavor flavor) {
		return delegate.isDocFlavorSupported(flavor);
	}

	public void removePrintServiceAttributeListener(PrintServiceAttributeListener listener) {
		delegate.removePrintServiceAttributeListener(listener);
	}

}