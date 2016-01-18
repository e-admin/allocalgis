/**
 * AbstractValidator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;

import java.util.Iterator;
import java.util.Vector;

import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.ILayer;

/**
 * @author juacas
 *
 */
public abstract class AbstractValidator {
	private ApplicationContext context;
	private Vector ErroneusAttributes =new Vector();
	
	/**
	 * 
	 */
	public AbstractValidator(ApplicationContext context) {
		this.context=context;
	}
	/**
	 * Valida la feature frente a las reglas implementadas en este Validator
	 * @param feature
	 * @return
	 */
	public abstract boolean validateFeature(Feature feature);
	/**
	 * Valida en el contexto de una capa
	 * @param feature
	 * @param layer
	 * @return
	 */
	public abstract boolean validateFeature(Feature feature, ILayer layer);
	protected void resetErrorCount()
	{
		ErroneusAttributes.clear();
	}
	protected void notifyError(String attname)
	{
		notifyError(attname,null,null);
	}
	protected void notifyError(String attname, String Description, Domain domain)
	{
		ErroneusAttributes.add(new ValidationError(attname,Description, domain));
	}
	public int getErrorCount()
	{
		return ErroneusAttributes.size();
	}
	public Iterator getErrorListIterator()
	{
		return ErroneusAttributes.iterator();		
	}
}

