/**
 * ModuleImpl.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.localgis.tools.modules.Artifact;
import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleReference;
import com.localgis.tools.modules.Version;

public class ModuleImpl implements Module
{
	Version version;
	String name;
	String description;
	HashMap<String,Module> dependencies=null; // null means the module is not initialized
	HashMap<String,Module> dependents=new HashMap<String, Module>();
	Module upgrades; // only one path for upgrades
	
	private Artifact artifact;
	/**
	 * LocalGISModule
	 * @param name
	 * @param version
	 */
	public ModuleImpl(String name, String version)
	{
	this(name, new VersionImpl(version));	
	}
	public ModuleImpl(String name, Version version)
	{
		this.name=name;
		this.version=version;
	}
	
	
	@Override
	public void addDependency(Module mod) {
	    	if (this.dependencies==null)
	    	    hasNoDependencies();
		this.dependencies.put(mod.getName(), mod);
	}

	
	@Override
	public void addDependent(Module mod) {
		this.dependents.put(mod.getName(), mod);
	}

	
	@Override
	public boolean canBeRemovedInmediatly() {
		return dependents().size()==0;
	}
	
	@Override
	public boolean canBeRemovedPotentially(Collection<Module> removableMods)
	{
	
		if (canBeRemovedInmediatly())
			{
			removableMods.add(this);
			return true;
			}
		// comprueba si este nodo se puede eliminar teniendo en cuenta la lista
			
		Collection<Module> dependents=dependents();
		
		
		/**
		 * Si todos los módulos dependentes se pueden borrar potencialmente
		 * éste tambien.
		 */
		for (Module module : dependents) {
			if (module.canBeRemovedPotentially(removableMods)==false)
				return false;
			else
				removableMods.add(module);
		}
		
		
		return true;
	}

	
	@Override
	public Collection<Module> dependents() {
		
		return new ArrayList<Module>(this.dependents.values());
	}

	
	@Override
	public Collection<Module> dependsOn() {
	    if (this.dependencies==null)
		    return Collections.emptyList();
		else
		    return new ArrayList<Module>(this.dependencies.values());
	}

	
	@Override
	public String getName() {
		return this.name;
	}

	
	@Override
	public Version getVersion() {
		
		return this.version;
	}

	
	@Override
	public void removeDependency(ModuleReference mod)
	{
	    if (this.dependencies!=null)
		this.dependencies.remove(mod);
	}

	
	@Override
	public void removeDependent(ModuleReference mod) {
		this.dependents.remove(mod);
	}
	/**
	 * Implementa la condición de compatibilidad entre módulos
	 * 
	 */
	
	@Override
	public boolean isCompatible(ModuleReference mod) {
		Version modVersion=mod.getVersion();
		if (mod==null || !getName().equals(mod.getName()))
				return false;
		if (modVersion.getMajorVersion()==getVersion().getMajorVersion() &&
				modVersion.getMinorVersion() == getVersion().getMinorVersion() &&
				modVersion.getIncrementalVersion() >= getVersion().getIncrementalVersion()
				)
			return true;
		else
			return false;
	}
	
	@Override
	public Artifact getArtifact()
	{
	   return this.artifact;
	}
	
	@Override
	public void setArtifact(Artifact artifact)
	{
	    this.artifact=artifact;
	}
	
	@Override
	public boolean isReference()
	{
	    return getArtifact()==null || this.dependencies==null;
	}
	
	@Override
	public Module getUpgradableMod()
	{
	    return this.upgrades;
	}
	
	@Override
	public void setUpgrades(Module upgradableFrom)
	{
	    this.upgrades = upgradableFrom;
	}
	
	@Override
	public boolean equals(Object obj) {
		ModuleReference mod=(ModuleReference)obj;
		if (mod!=null && this.getName().equals(mod.getName()) && this.getVersion().equals(mod.getVersion()))
			return true;
		else 
			return false;
	}
	
	@Override
	public String getDescription() {
		return this.description!=null?this.description:"";
	}
	
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int hashCode() {
		
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return getName()+":"+getVersion();
	}
	
	@Override
	public List<Module> getUpgradePath()
	{
	    List<Module> path=new ArrayList<Module>();
	    collectUpgrades(path,this);
	    Collections.reverse(path);
	    return path;
	}
	/**
	 * Recursively collect upgrades
	 * @param path
	 * @param upgrades2
	 */
	private void collectUpgrades(List<Module> path, Module mod)
	{
	   path.add(mod);
	   Module upgradableMod = mod.getUpgradableMod();
	   if (upgradableMod!=null)
		collectUpgrades(path, upgradableMod);
	}
	@Override
	public void hasNoDependencies()
	{
	    this.dependencies=new HashMap<String, Module>();
	}
}
