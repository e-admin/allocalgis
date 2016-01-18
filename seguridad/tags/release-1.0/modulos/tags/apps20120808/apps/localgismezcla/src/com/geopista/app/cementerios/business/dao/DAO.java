package com.geopista.app.cementerios.business.dao;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.locks.ReentrantLock;

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
                Reader reader = Resources.getResourceAsReader("sqlMaps/sqlMapConfig.xml");
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
