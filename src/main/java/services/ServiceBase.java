package services;

import javax.persistence.EntityManager;

import utils.DBUtil;

/**
 * Class that general process relation to DB connection
 */
public class ServiceBase {

    /**
     * Instance of EntityManager
     */
    protected EntityManager em = DBUtil.createEntityManager();

    /**
     * Close the EntityManager
     */
    public void close() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
