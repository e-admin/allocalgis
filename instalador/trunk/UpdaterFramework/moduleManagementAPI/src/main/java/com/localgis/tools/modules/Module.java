/**
 * Module.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules;

import java.util.Collection;
import java.util.List;


public interface Module extends ModuleReference {
	/**
	 * Artifact binario que sirve de raiz para distribuir el módulo. 
	 * @return
	 */
	public Artifact getArtifact();
	public void setArtifact(Artifact artifact);
	/**
	 * Devuelve los módulos de los que depende
	 * @return
	 */
	public Collection<Module> dependsOn();
	/**
	 * Devuelve los módulos que dependen de este módulo
	 * @return
	 */
	public Collection<Module> dependents();
	/**
	 * Comprueba si puede eliminarse directamente teniendo en cuenta la lista de dependencias
	 * y si éstas se pueden borrar inmediatamente
	 * @return
	 */
	public boolean canBeRemovedInmediatly();
	/**
	 * Comprueba si puede eliminarse tras limpiar las dependencias existentes
	 * @mod el modulo que está interesado en ser eliminado tambien. Se usa para rastrear el arbol de dependencias.
	 * @return
	 */
	public boolean canBeRemovedPotentially(Collection<Module>removableMods);
	/**
	 * Elimina la dependencia inversa de un módulo
	 * (mod ya no depende de this)
	 * @param mod
	 */
	
	public void removeDependent(ModuleReference mod);
	public void addDependent(Module mod);
	/**
	 * Elimina la dependencia  de un módulo
	 * (this ya no depende de mod)
	 * @param mod
	 */
	public void removeDependency(ModuleReference mod);
	/**
	 * Informa que se sabe que el modulo no tiene dependencias.. (Inicializa un Set vacio de dependencias)
	 */
	public void hasNoDependencies();
	public void addDependency(Module mod);
	/**
	 * Comprueba reglas de compatibilidad teniendo en cuenta las versiones
	 * 
	 * @param mod dependencia que se necesita.
	 * @return true si this es un módulo equivalente a mod.(Misma major.minor version con igual o posterior veriones incrementales)
	 */
	public boolean isCompatible(ModuleReference mod);
	/**
	 * Comprueba si la definición está completa.
	 * @return true si falta información necesaria como el Artifact o una especificación de dependencias
	 */
	public boolean isReference();
	public String getDescription();
	public void setDescription(String description);
	void setUpgrades(Module upgradableFrom);
	Module getUpgradableMod();
	/**
	 * Ordered list of upgrade operations needed to traverse the history of upgrades
	 * @return list ordered. First upgrade first.
	 */
	public List<Module> getUpgradePath();

	
}
