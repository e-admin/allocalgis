/**
 * ModuleDependencyTree.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules;

import java.util.Collection;
import java.util.List;

import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.exception.ModuleNotFoundException;

public interface ModuleDependencyTree extends ModuleRegistry
{

    /**
     * Obtiene los módulos disponibles
     * @return
     */
    public abstract Collection<Module> getModules();

    /**
     * Instala un módulo en el modelo del sistema
     *  asegurando que no se violan sus dependencias.
     * Este método debe hacer persistente el cambio en el servidor
     */
    public abstract void installModule(Module mod) throws DependencyViolationException;


    /**
     * Elimina un módulo de la información del sistema
     *  asegurando que no se violan sus dependencias.
     * Este método debe hacer persistente el cambio en el servidor
     * @throws ModuleNotFoundException 
     */
    public abstract void removeModule(Module mod) throws DependencyViolationException, ModuleNotFoundException;

  
    /**
     * Devuelve los módulos de los que depende el módulo mod
     * @param mod
     * @return
     * @throws ModuleNotFoundException 
     */
    public abstract Collection<Module> getDependencies(Module mod) throws  ModuleNotFoundException;

    /**
     * Devuelve los módulos que dependen del módulo mod
     * @param mod
     * @return 
     * @throws ModuleNotFoundException 
     */
    public abstract Collection<Module> getReverseDependencies(Module mod) throws ModuleNotFoundException;
    
    public boolean canBeInstalled(Module mod) throws ModuleNotFoundException;
    /**
     * Determina si un  paquete de modulos pueden ser instalados en conjunto
     * @param pack
     * @return
     */
    public abstract boolean packCanBeInstalled(Collection<Module> pack);
    /**
     * Returns an ordeded list of modules needed except {@link Module}s provided in the pack
     * @param pack collection of modules to be checked
     * @param excludePack if true does not include the modules already in pack
     * @return ordered list of dependencies
     */
    public abstract List<Module> needToBeInstalledForPack(Collection<Module> pack, boolean excludePack);
    /**
     * Returns an ordeded list of modules needed except {@link Module} mod
     * @param mod {@link Module} that whould be installed in the registry.
     * @param excludeMod if true does not include the module mod
     * @return ordered list of dependencies
     */
    public abstract List<Module> needToBeInstalled(Module mod, boolean excludeMod);

    public abstract Module getAndInstallInstance(Module module);
    public abstract Module getModuleInstance(Module module) throws ModuleNotFoundException;

    public abstract boolean isInstalled(ModuleReference module);

    public abstract ModuleReference findCompatibleMod(ModuleReference neededModule);
    /**
     * Devuelve true si se ha cargado la lista de modulos.
     */
    public boolean isInitialized();
    /**
     * Busca el módulo en el registro y obtiene la instancia más completa que pueda
     * (con dependencias y definición de artefactos)
     * Si mod no es una referencia y existe su referencia se actualiza el registro.
     * @see ModuleReference 
     * @param mod
     * @return
     */
    public abstract Module resolve(Module mod);

    public abstract List<Module> orderModulesByDependencies(Collection<Module> mods);
    /**
     * Busca los módulos que se pueden actualizar aunque sean compatibles con los requisitos del pack
     * @param modules
     * @return
     */
    public abstract List<Module> getCompatiblesForPack(Collection<Module> modules);
    /**
     * Busca los módulos que tienen un "UpgradePath" entre los módulos indicados
     * TODO distinguir entre compatible y actualizable
     * @param modules
     * @return
     */
    public abstract List<Module> getUpgradablesForPack(Collection<Module> modules);
    /**
     * Busca los módulos que son incompatibles con el pack indicado.
     * Incompatibilidades actuales:
     * 	<ul>
     * <li>Incrementos de version no actualizable.</li>
     * </ul>
     * Futuro: incompatibilidades explícitas de los módulos. TODO
     * @param modules
     * @return
     */
    public abstract Collection<Module> getIncompatiblesForPack(Collection<Module> modules);

    Collection<Module> findIncompatibleModules(ModuleReference neededModule, Collection<Module> mods);

    Collection<Module> findIncompatibleMods(ModuleReference neededModule);

    
    @Override
    public abstract Module register(Module mod);

    public abstract void installPack(Collection<Module> pack) throws DependencyViolationException;

    public abstract List<Module> getUpgradePath(Module mod);

    Collection<Module> getIndependentModules();

 

}