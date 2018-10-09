package co.service;

import org.springframework.stereotype.Service;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceContext;
import javax.persistence.ParameterMode;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
public class HandleDb {

    private final Log logger = LogFactory.getLog(this.getClass());

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @PersistenceUnit
    private EntityManagerFactory emf;

    public String execute(String name, Integer iterations) {
        logger.info(em);

        logger.info(emf);

		StoredProcedureQuery query = em.createStoredProcedureQuery("random");
		query.registerStoredProcedureParameter("iter", Integer.class, ParameterMode.IN);

		query.setParameter("iter", 70);
        query.execute();

        logger.info(query.getResultList());
        
        return "Done!";
    }
}