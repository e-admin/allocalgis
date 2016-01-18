/**
 * DAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.business.dao;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.locks.ReentrantLock;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: gusjer
 * Date: 03-nov-2008
 * Time: 15:06:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class DAO {

    private static SqlMapClient sqlMapper = null;
    private static final ReentrantLock lock = new ReentrantLock();

    protected DAO() {
        lock.lock();
        try {
            if (sqlMapper == null) {
                Reader reader = Resources.getResourceAsReader("/sqlMaps/sqlMapConfig.xml");
                sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader);
                reader.close();

            }
        } catch (IOException e) {
            throw new RuntimeException("Something bad happened while building the SqlMapClient instance." + e, e);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public SqlMapClient getSqlMapClientTemplate() {

        return sqlMapper;
    }

}
