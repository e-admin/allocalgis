/**
 * ModuleDependencyTreeImpl.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.exception.ModuleNotFoundException;
public class ModuleDependencyTreeImpl implements ModuleDependencyTree, ModuleRegistry
{
   
    private static final Logger log = Logger.getLogger(ModuleDependencyTreeImpl.class.toString());

    protected HashMap<Module, Module> mods = new HashMap<Module, Module>();

    public ModuleDependencyTreeImpl() {
    	super();
    }

    @Override
    public Collection<Module> getDependencies(Module mod) throws ModuleNotFoundException
    {
	Module storedMod = getModuleInstance(mod);
	return storedMod.dependsOn();
    }

    /**
     * Obtiene una instancia actualizada y lo más completa posible del módulo. Si ya existe una referencia se sustituye por la nueva instancia. Todas las
     * instancias de dependencias se sustituyen también.
     * 
     * @throws ModuleNotFoundException
     */

    @Override
    public Module getModuleInstance(Module mod) throws ModuleNotFoundException
    {
	if (!isInstalled(mod))
	    {
		throw new ModuleNotFoundException("Module not found:" + mod);
	    } else
	    {
		return register(mod);
	    }
    }

    @Override
    public Collection<Module> getModules()
    {
	return new ArrayList<Module>(this.mods.values());
    }

    @Override
    public Collection<Module> getReverseDependencies(Module mod) throws ModuleNotFoundException
    {
	Module modInstance = getModuleInstance(mod);
	return modInstance.dependents();
    }

    @Override
    public void installModule(Module mod) throws DependencyViolationException
    {
	Module instanceMod = resolve(mod);

	Collection<Module> dependencies = instanceMod.dependsOn();
	for (Module dependency : dependencies)
	    {
		Module dependencyInstance = resolve(dependency);
		if (dependencyInstance != null) // ya está registrada
		    {
			dependencyInstance.addDependent(mod);
		    }
	    }
	this.mods.put(instanceMod, instanceMod);
    }

    @Override
    public void removeModule(Module mod) throws DependencyViolationException, ModuleNotFoundException
    {
    	log.log(Level.FINER, "-------------------------EJEMPLO2");
	if (!isInstalled(mod))
	    throw new ModuleNotFoundException(mod.toString()); // TODO: definir otra excepcion para este caso o
							       // incluir el intento de borrado en caso de uso normal.
	Module instanceMod = resolve(mod);

	if (instanceMod.canBeRemovedInmediatly())
	    {
		// limpia dependencias
		Collection<Module> dependencies = instanceMod.dependsOn();
		for (ModuleReference dependency : dependencies)
		    {
			Module module = this.mods.get(dependency);
			module.removeDependent(instanceMod);
		    }
		this.mods.remove(instanceMod);
		removeModuleFromCatalog(instanceMod);
	    } else if (instanceMod.dependents().size() != 0)
	    {
		throw new DependencyViolationException("Module " + instanceMod.getName() + "(" + instanceMod.getVersion() + ") has dependents: "
			+ instanceMod.dependents());
	    } else
	    {
		throw new DependencyViolationException("Module " + instanceMod.getName() + "(" + instanceMod.getVersion()
			+ ") can't be removed. (unknown reason) ");
	    }
    }

    private void removeModuleFromCatalog(Module mod)
    {
	// TODO Auto-generated method stub

    }

    /**
     * Reemplaza todas las dependencias coincidentes con newMod por la nueva instancia
     * 
     * @param newMod
     */
    private void replaceDependencyInstances(Module newMod)
    {
	for (Module mod : getModules())
	    {
		if (mod.dependsOn().contains(newMod))
		    {
			mod.removeDependency(newMod);
			mod.addDependency(newMod);
			log.log(Level.FINER, "Instance of module:" + newMod + " replaced in dependencies of:" + mod);
		    }
	    }
    }

    @Override
    public boolean isInstalled(ModuleReference mod)
    {
	return this.mods.get(mod) != null;
    }

    /**
     * comprueba si se cumplen las dependencias
     * 
     * @throws DependencyViolationException
     */

    @Override
    public boolean canBeInstalled(Module mod)
    {
	Module instanceMod = resolve(mod);
	Collection<Module> dependencies = instanceMod.dependsOn();

	for (ModuleReference module : dependencies)
	    {
		if (findCompatibleMod(module) == null)
		    return false;
	    }
	return true;
    }

    @Override
    public boolean packCanBeInstalled(Collection<Module> pack)
    {

	// checks if all needed are in the pack
	Collection<Module> neededForPack = needToBeInstalledForPack(pack, true);
	if (!pack.containsAll(neededForPack))
	    return false;
	else
	    return true;
    }

    @Override
    public void installPack(Collection<Module> pack) throws DependencyViolationException
    {

	if (!packCanBeInstalled(pack))
	    {
		Collection<Module> needed = needToBeInstalledForPack(pack, true);
		throw new DependencyViolationException("Pack can't be installed. Needed:" + needed);
	    }
	Collection<Module> toBeInstalled = new ArrayList<Module>(pack);
	for (Module module : toBeInstalled)
	    {
		installModuleRecursivelyFromPack(module, pack);
	    }

    }

    private void installModuleRecursivelyFromPack(Module module, Collection<Module> pack) throws DependencyViolationException
    {

	if (this.isInstalled(module))
	    {
		pack.remove(module);
	    } else if (!this.canBeInstalled(module))
	    {
		Collection<Module> deps = needToBeInstalled(module, true);
		for (Module dependency : deps)
		    {
			installModuleRecursivelyFromPack(dependency, pack);
		    }

	    }
	installModule(module);
	pack.remove(module);

    }

    @Override
    public ModuleReference findCompatibleMod(ModuleReference neededModule)
    {
	Collection<Module> mods = getModules();
	return findCompatibleModule(neededModule, mods);
    }

    protected ModuleReference findCompatibleModule(ModuleReference neededModule, Collection<Module> mods)
    {
	for (Module installedMod : mods)
	    {
		if (installedMod.isCompatible(neededModule))
		    return installedMod;
	    }
	return null;
    }

    /**
     * Busca los módulos necesarios en mod no presentes en el registro descarta las dependencias que tienen alguna compatibilidad presente
     * 
     * @see #orderModulesByDependencies(List) para ordenarlos por dependencias mutuas
     */

    @Override
    public List<Module> needToBeInstalled(Module mod, boolean excludeMod)
    {
	Module instancedMod = resolve(mod);
	// copia todas las dependencias
	Collection<Module> deps = instancedMod.dependsOn();
	if (excludeMod == false) // include itself in the list of dependencies
	    {
		deps = new Vector<Module>(deps); // deps is unmodifiable
		deps.add(instancedMod);
	    }
	List<Module> needed = new Vector<Module>(deps);
	// elimina las dependencias que tienen alguna compatibilidad presente
	for (ModuleReference dependencyModule : deps)
	    {
		Collection<Module> modValues = this.getModules();
		for (Module installedModule : modValues)
		    {
			if (installedModule.isCompatible(dependencyModule))
			    {
				needed.remove(dependencyModule);
			    }
		    }
	    }

	return needed;
    }

    @Override
    public Module resolve(Module mod)
    {
	if (isInstalled(mod)) // update if needed
	    {
		Module instancedMod = register(mod);
		return instancedMod;
	    } else
	    {
		return mod;
	    }
    }

    @Override
    public List<Module> needToBeInstalledForPack(Collection<Module> pack, boolean excludePack)
    {
	Collection<Module> neededForPack = new HashSet<Module>();
	for (Module module : pack)
	    {
		neededForPack.addAll(needToBeInstalled(module, excludePack));
	    }
	List<Module> unresolved = new ArrayList<Module>(neededForPack);
	// purge dependencies provided by the packç
	if (excludePack)
	    {
		for (ModuleReference module : neededForPack)
		    {
			if (findCompatibleModule(module, pack) != null)
			    unresolved.remove(module);
		    }
	    }
	return orderModulesByDependencies(unresolved);
    }

    /**
     * Returns a List of the modules in the order needed for installation
     * 
     * @param mods
     */

    @Override
    public List<Module> orderModulesByDependencies(Collection<Module> mods)
    {
	
	ModuleDepComparator comparator= new ModuleDepComparator();
	ArrayList<Module> ordered=new ArrayList<>(mods);
	for (int i=0;i<mods.size();i++)
	    {
		boolean reset=false; // reset the loop
		Module current=ordered.get(i);
		for (int j=i+1;j<mods.size();j++)
		    {
			Module other=ordered.get(j);
			
			if (ModuleDepComparator.isDeepDependency(current, other)) // should rearrange
			    {
				ordered.remove(j);
				ordered.add(i,other); // put dependencies at front
				i++; //shifted
				reset=true; // re-start the loop
				//break;
			    }
		    }
		if (reset)
		    {
			i=-1;
		    };
	    }
	
	return ordered;
    }
    /**
     * Recoge el módulo y sus dependencias que aún no estén en la lista del resultado final.
     * @param module
     * @param ordered
     * @return
     */
    private Collection<Module> collectDependencies(Module module, List<Module> ordered,List<Module> list)
    {
	if (ordered.contains(module) || list.contains(module))
	    return Collections.emptyList();
	List<Module> collected=new ArrayList<Module>();
	collected.add(module);
	
	Collection<Module> deps = module.dependsOn();
	for(Module dep:deps)
	    {
		if (ordered.contains(dep))
		    continue;
		Collection<Module> collectedDependencies = collectDependencies(dep, ordered,collected);
		collected.removeAll(collectedDependencies);//Avoid duplicates
		collected.addAll(0,collectedDependencies);
	    }
	return collected;
    }

    @Override
    public Collection<Module> getIndependentModules()
    {
	ArrayList<Module> independent=new ArrayList<Module>();
	for(Module mod:getModules())
	    {
		if (mod.dependsOn().size()==0)
		    independent.add(mod);
	    }
	return independent;
    }


@Deprecated
    public List<Module> orderModulesByDependencies2(Collection<Module> mods)
    {
	ArrayList<Module> ordered = new ArrayList<Module>();

	for (Module mod : mods)
	    {
		// busca sus dependencias en la lista
		Collection<Module> deps = mod.dependsOn();
		if (deps.size() == 0)
		    {
			if (!ordered.contains(mod))
			    ordered.add(0, mod);
		    } else
		    for (Module dep : deps)
			{
			    // busca donde estoy
			    int myIndex = ordered.indexOf(mod);
			    // busca donde esta la dep
			    int si = ordered.indexOf(dep);
			    boolean depPresent = (si != -1);
			    boolean modPresent = (myIndex != -1);

			    if (!depPresent && !modPresent) // insert
				{
				    ordered.add(dep);
				    ordered.add(mod);
				} else if (!depPresent && modPresent) // dependencia nueva, inserta encima de mod
				{
				    ordered.add(myIndex, dep);
				} else if (depPresent && modPresent) // dependencia existente, comprobar que está por encima de mod
				{
				    if (si > myIndex)
					{
					    ordered.remove(myIndex);
					    ordered.add(si, mod);
					}
				} else if (depPresent && !modPresent) // register mod below dep
				{
				    if (si == ordered.size() - 1)
					ordered.add(mod);
				    else
					ordered.add(si + 1, mod);
				}
			}
	    }
	return ordered;
    }

    @Override
    public String toString()
    {

	StringBuilder sb = new StringBuilder();
	String sep = "";
	for (Module mod : this.getModules())
	    {
		sb.append(sep);
		sb.append(mod.toString());
		sep = ",";
	    }
	return sb.toString();
    }

    @Override
    public boolean isInitialized()
    {
	return this.mods.size() > 0;
    }

    @Override
    public Module register(Module mod)
    {
	Module instance = this.mods.get(mod);

	if (instance == null) // no hay referencias en el registro
	    {
		// si es dependencia no registrada hay que complementar
		// los dependientes y las dependencias
		Module uninstalledDep = this.uninstalledDeps.get(mod.toString());
		if (uninstalledDep!=null) // utiliza la instancia ya existente
		    {
			copyDependants(mod, uninstalledDep);
			complementDependencies(mod, uninstalledDep);
			complementArtifact(mod,uninstalledDep);
			mod=uninstalledDep;
			// elimina de la lista de dependencias y pasa al registro
			this.uninstalledDeps.remove(uninstalledDep.toString());
		    }
		else
		    {
			mod=resolve(mod); // busca la instancia más completa teniendo en cuenta las dependencias no registradas previas
		    }
		this.mods.put(mod, mod); // añade la instancia
		
		Collection<Module> dependencies = mod.dependsOn();
		for (Module dep : dependencies) // registra y actualiza las dependencias
		    {
			//JPC dep = resolve(dep); // la dependencia puede no estar instalada en el registro (ser dependencia de un modulo pero no registrada aún)
			dep = resolveDependencyInstances(dep);// actualiza las dependencias por las más completas incluyendo esta nueva
			mod.addDependency(dep); // actualiza la referencia por si hay que cambiarla por una previa.
			// mod.addDependency(module);// actualiza por si fuera una version previa
			// register(module); // no hay mas que hacer salvo devolver la nueva instancia
			instance = mod;
		    }
	    } else if (instance.isReference() && !mod.isReference()) // reemplaza por una instancia no referencia
	    {
		copyDependants(instance, mod);
		replaceDependencyInstances(mod);
		this.mods.remove(instance); // limpia la hashtable
		this.mods.put(mod, mod); // instala la nueva instancia completa
		instance = mod;
	    } else if (!instance.isReference() && mod.isReference()) // mod puede ser una referencia pero estar en el registro y tener dependents registrados
	    {
		copyDependants(mod, instance); // copia los dependentes ya que la instance es un Modulo completo (no referencia) pero puede no tener los
					       // dependents.
	    }

	return instance;
    }
    /**
     * Asigna la información de from.artifact si from.getArtifact() != null
     * y to NO contiene ya un artifact
     * 
     * @param from
     * @param to
     */
    private void complementArtifact(Module from, Module to)
    {
	if (to.getArtifact()==null)
	    to.setArtifact(from.getArtifact());
    }
/**
 * Copia las dependencias desde from  NO EXISTENTES en to
 * @param from
 * @param to
 */
    private void complementDependencies(Module from, Module to)
    {
	if (from.dependsOn()!=null)
	    {
		to.hasNoDependencies(); //inicializa la lista
		for(Module dep:from.dependsOn())
		    if (!to.dependsOn().contains(dep))
			to.addDependency(dep);
	    }
    }

    HashMap<String,Module> uninstalledDeps= new HashMap<String,Module>();
    /**
     * Compatibility criteria: implicit upgradable rules: incrementalVersions are assumed upgradable ?
     */
    private boolean implicitlyUpgradable=false;
    public boolean isImplicitlyUpgradable()
    {
        return this.implicitlyUpgradable;
    }

    public void setImplicitlyUpgradable(boolean implicitlyUpgradable)
    {
        this.implicitlyUpgradable = implicitlyUpgradable;
    }

    /**
     * actualiza las dependencias devolviendo las más completa incluyendo la información de 
     * esta nueva newDep
     * 
     * @param dep
     */
    private Module resolveDependencyInstances(Module newDep)
    {
	Module resolvedDep=null;
	// Elige prioritariamente la instancia a utilizar
	if (isInstalled(newDep))
	    {
		try
		    {
			resolvedDep = getModuleInstance(newDep);
		    } catch (ModuleNotFoundException e)
		    {
			// ya se ha evitado la condición no puede ocurrir
			throw new RuntimeException(e);
		    }
	    }
	else if (this.uninstalledDeps.containsKey(newDep.toString()))
	    {
		resolvedDep = this.uninstalledDeps.get(newDep.toString());
	    }
	else
	    {
		this.uninstalledDeps.put(newDep.toString(), newDep); //registra la dependencia para acumular dependientes y otros datos
		resolvedDep=newDep;
	    }
	
	// actualiza dependientes previamente computados en la nueva dependencia
	// se supone que resolvedDep acumula la lista más completa
	copyDependants(newDep, resolvedDep);
	
	return resolvedDep;
	
//	for (Module mod:this.getModules())
//	    {
//		
//		if (mod.dependsOn().contains(newDep))
//		    {
//			Module resolved=null;
//			for (Module dep:mod.dependsOn())
//			    {
//				if (dep.equals(newDep)) // Elige la mejor alternativa e instalala
//				    {
//					
//					
//					if (isInstalled(dep))
//					    {
//						 resolved = this.resolve(dep);
//					    }
//					else
//					    {
//						if (newDep.isReference())
//						    {
//							resolved= dep;
//						    }
//						else
//						if (!newDep.isReference())
//						    {
//							copyDependants(dep, newDep);
//							resolved=newDep;
//						    }
//					    } // best instance
//					
//					break;
//				    }// search if
//			    }// search loop
//			mod.addDependency(resolved);
//		    }
//	    }// all mods loop
	
    }
/**
 * 
 * @param from
 * @param to
 */
    private void copyDependants(Module from, Module to)
    {
	Collection<Module> dependents = from.dependents();

	for (Module dependent : dependents) // preserva las dependencias inversas puesto que se calculan en tiempo de ejecución para
					    // este contexto
	    {
		Module dep = resolve(dependent);
		to.addDependent(dep);
	    }

    }

    @Override
    public Module getAndInstallInstance(Module module)
    {
	try
	    {
		Module instance = getModuleInstance(module);
		return instance;
	    } catch (ModuleNotFoundException e)
	    {
		register(module);
		return module;
	    }
    }

    @Override
    public List<Module> getUpgradablesForPack(Collection<Module> modules)
    {
	// Search explicit upgradepath
	ArrayList<Module> upgradables = new ArrayList<Module>();
	
	for (Module module : modules)
	    {
		List<Module> path = getUpgradePath(module);

		if (path.size() > 0)
		    {
			// get existent module first in the chain of upgrades
			Module existentMod = path.get(0).getUpgradableMod();
			Collection<Module> dependents = existentMod.dependents();
//			if (dependents.size() == 1) // TODO check this condition
//			    {
//				upgradables.add(module);
//			    } else
			    {
				// TODO analize deeply concurrent upgrades
				// if all the dependents are in the pack (or in the registry) we will assume upgradability
				int numDependents = dependents.size();
				ArrayList<Module> availableMods = new ArrayList<Module>(getModules());
				availableMods.addAll(modules);
				
				dependents.retainAll(availableMods);
				if (dependents.size() == numDependents)
				    upgradables.add(module);
			    }
		    }
	    }

	// Compatibility set: implicit upgradable rules: incrementalVersions are upgradables
	if (this.implicitlyUpgradable)
	    {
		List<Module> compatibles = getCompatiblesForPack(modules);// TODO sacar del bucle esta linea
		compatibles.removeAll(modules); // remove from compatibility list the modules themself
		upgradables.addAll(compatibles);
	    }
	return upgradables;
    }

    /**
     * Return and ordered list of the upgradepath to upgrade the Module mod to its version
     * The list doesn't include already installed modules.
     * @param to
     * @return upgrade-order-ordered list
     */

    @Override
    public List<Module> getUpgradePath(Module mod)
    {
	List<Module> upgradepath;

	// state of the registry
	upgradepath = mod.getUpgradePath();// metamodel description
	// look for any of the modules in the registry
	if (upgradepath.size() > 1)
	    for (int index = 0; index < upgradepath.size(); index++)
		{
		    // truncate the list from the existent module
		    Module upgradable = upgradepath.get(index);
		    if (isInstalled(upgradable))
			{
			    return upgradepath.subList(index + 1, upgradepath.size());
			}
		}
	return Collections.emptyList();
    }

    @Override
    public List<Module> getCompatiblesForPack(Collection<Module> modules)
    {
	List<Module> compatibles = new Vector<Module>();
	for (Module module : modules)
	    {
		Module compatible = (Module) findCompatibleMod(module);
		if (compatible != null) // compatible pero actualizable
		    compatibles.add(compatible);
	    }
	return compatibles;
    }

    @Override
    public Collection<Module> getIncompatiblesForPack(Collection<Module> modules)
    {
	Set<Module> incompatibles = new HashSet<Module>();
	for (Module module : modules)
	    {
		Collection<Module> incompatibleMods = findIncompatibleMods(module);
		incompatibles.addAll(incompatibleMods);
	    }
	return incompatibles;
    }

    @Override
    public Collection<Module> findIncompatibleMods(ModuleReference neededModule)
    {
	Collection<Module> mods = getModules();
	return findIncompatibleModules(neededModule, mods);
    }

    @Override
    public Collection<Module> findIncompatibleModules(ModuleReference neededModule, Collection<Module> mods)
    {
	Collection<Module> incompatibles = new HashSet<Module>();

	for (Module installedMod : mods)
	    {
		if (installedMod.getName().equals(neededModule.getName()) && !installedMod.isCompatible(neededModule))
		    incompatibles.add(installedMod);
	    }
	return incompatibles;
    }

}